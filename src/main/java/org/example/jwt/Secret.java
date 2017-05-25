package org.example.jwt;

public class Secret {

    public static byte[] getKey() {
        return "secret".getBytes();
    }
}
