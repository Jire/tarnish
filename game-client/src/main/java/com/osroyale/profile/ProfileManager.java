package com.osroyale.profile;

import com.osroyale.Client;
import com.osroyale.Utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.runelite.client.RuneLite.PROFILES_DIR;
import static net.runelite.client.RuneLite.RUNELITE_DIR;

public class ProfileManager {

    public static final int MAX_PROFILES = 3;
    public static final List<Profile> profiles = Collections.synchronizedList(new ArrayList<>(MAX_PROFILES));

    static {
        for (int i = 0; i < MAX_PROFILES; i++) {
            profiles.add(new Profile());
        }
    }

    public static void add(Profile newProfile) {
        int index = 0;
        Profile existingProfile = null;
        for (int i = 0; i < MAX_PROFILES; i++) {
            Profile profile = profiles.get(i);
            if (newProfile.getUsername().equalsIgnoreCase(profile.getUsername())) {
                existingProfile = profile;
                index = i;
                break;
            }

            // replace first empty slot.
            if (profile.emptySlot()) {
                profiles.remove(i);
                profiles.add(i, newProfile);
                break;
            }
        }

        if (existingProfile == null) {
            if (profiles.size() < MAX_PROFILES) {
                profiles.add(newProfile);
            }
        } else {
            profiles.remove(index);
            profiles.add(index, newProfile);
        }
        save();
    }

    public static void delete(Profile profile) {
        profiles.remove(profile);
        // add back empty slot
        profiles.add(new Profile());
        save();
    }

    public static void save() {
        File file = new File(Utility.findcachedir() + File.separator + "profiles.dat");
        if (!file.exists()) {
            System.err.println("profiles.dat doesn't exist in " + file.getAbsolutePath());
            return;
        }
        try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            output.write(profiles.size());
            for (Profile profile : profiles) {
                output.writeUTF(profile.getUsername());
                output.writeUTF(profile.getPassword());

                output.writeByte(profile.getGender());
                output.writeByte(profile.getEquipment().length);
                for (int i = 0; i < profile.getEquipment().length; i++) {
                    output.writeShort(profile.getEquipment()[i]);
                }
                output.writeByte(profile.getRecolours().length);
                for (int i = 0; i < profile.getRecolours().length; i++) {
                    output.writeShort(profile.getRecolours()[i]);
                }
                if(!profile.getUsername().isEmpty() && Client.loggedIn) {
                    saveImage(createImageFromPixels(profile.convertModelToSprite(profile.getModel()).getPixels(), 50, 50), PROFILES_DIR + "/" + profile.getUsername() + ".png");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() throws IOException {
        File file = new File(Utility.findcachedir() + File.separator + "profiles.dat");
        if (!file.exists()) {
            if (!file.createNewFile()) {
                System.err.println("could not create profiles.dat in " + file.getAbsolutePath());
                return;
            }
        }
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            int totalProfiles = input.read();
            for (int i = 0; i < totalProfiles; i++) {
                String username = input.readUTF();
                String password = input.readUTF();

                int gender = input.readByte();
                int[] equipment = new int[input.readByte()];
                for (int j = 0; j < equipment.length; j++) {
                    equipment[j] = input.readShort();
                }
                int[] recolours = new int[input.readByte()];
                for (int j = 0; j < recolours.length; j++) {
                    recolours[j] = input.readShort();
                }
                profiles.add(i, new Profile(username, password, gender, equipment, recolours));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates a BufferedImage from an array of pixels.
     *
     * @param pixels The array of pixels representing the image.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return The created BufferedImage.
     */
    public static BufferedImage createImageFromPixels(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Create a WritableRaster to set the pixel data
        int[] dataBuffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(pixels, 0, dataBuffer, 0, pixels.length);

        return image;
    }

    /**
     * Saves a BufferedImage to the specified file path as a PNG image.
     *
     * @param image    The BufferedImage to save.
     * @param filePath The file path where the image will be saved.
     */
    public static void saveImage(BufferedImage image, String filePath) {
        try {
            File outputFile = new File(filePath);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }
}
