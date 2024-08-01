package com.osroyale;

import static com.osroyale.AnnouncementManager.FINISH;
import static com.osroyale.AnnouncementManager.STALL_TIME;

public class Announcement {
    private Client client;
    private String title;
    private String context;
    private int firstColor;
    private int secondColor;
    private int firstLinePosition;
    private int secondLinePosition;
    private int opacity;
    private long firstTime;
    private long secondTime;

    Announcement(Client client, String title, String context, int firstColor, int secondColor) {
        this.client = client;
        this.title = title;
        this.context = context;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.opacity = 0;
        this.firstTime = 0;
        this.secondTime = 0;
        this.firstLinePosition = Client.canvasWidth - (!Client.instance.isResized() ? 253 : 155);
        this.secondLinePosition = client.newSmallFont.getTextWidth(context);
    }

    public void update() {
        if (firstLinePosition <= 255) {
            client.announcement.state = FINISH;
        }

        int width = Client.canvasWidth - (!Client.instance.isResized() ? 253 : 155);
        int delta = 4 * (1 << (int) (width / (765.0 - (!Client.instance.isResized() ? 253 : 155))));

        if (firstLinePosition < width / 2.0) {
            if (opacity - delta >= 0) {
                opacity -= delta;
            }
        } else {
            if (opacity + delta <= 255) {
                opacity += delta;
            }
        }

        if (System.currentTimeMillis() - firstTime >= STALL_TIME) {
            firstLinePosition -= delta;
        }

        if (System.currentTimeMillis() - secondTime >= STALL_TIME) {
            secondLinePosition += delta;
        }

        if (firstTime == 0) {
            int center = (int) ((width + client.newFancyFont.getTextWidth(title)) / 2.0);
            if (firstLinePosition >= center - delta && firstLinePosition <= center + delta) {
                firstTime = System.currentTimeMillis();
            }
        }

        if (secondTime == 0) {
            int center = (int) ((width + client.newRegularFont.getTextWidth(context)) / 2.0);
            if (secondLinePosition >= center - delta && secondLinePosition <= center + delta) {
                secondTime = System.currentTimeMillis();
            }
        }
    }

    public Client getClient() {
        return client;
    }

    String getTitle() {
        return title;
    }

    String getContext() {
        return context;
    }

    int getFirstColor() {
        return firstColor;
    }

    int getSecondColor() {
        return secondColor;
    }

    int getFirstLinePosition() {
        return firstLinePosition;
    }

    int getSecondLinePosition() {
        return secondLinePosition;
    }
}
