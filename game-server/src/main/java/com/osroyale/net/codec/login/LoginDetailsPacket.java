package com.osroyale.net.codec.login;

import com.osroyale.net.codec.IsaacCipher;

public final class LoginDetailsPacket {

    private final String UUID;
    private final String macAddress;
    private final String username;
    private final String password;
    private final IsaacCipher encryptor;
    private final IsaacCipher decryptor;

    public LoginDetailsPacket(String UUID, String macAddress, String username, String password,
                              IsaacCipher encryptor, IsaacCipher decryptor) {
        this.UUID = UUID;
        this.macAddress = macAddress;
        this.username = username.trim();
        this.password = password;
        this.encryptor = encryptor;
        this.decryptor = decryptor;
    }

    public String getUUID() {
        return UUID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public IsaacCipher getEncryptor() {
        return encryptor;
    }

    public IsaacCipher getDecryptor() {
        return decryptor;
    }

    public String getMacAddress() {
        return macAddress;
    }

}
