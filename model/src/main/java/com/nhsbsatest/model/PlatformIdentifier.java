package com.nhsbsatest.model;

import java.util.Objects;
import java.util.UUID;

public class PlatformIdentifier {
    private String platformIdentifier;

    public static PlatformIdentifier valueOf(String platformIdentifier) {
        return new PlatformIdentifier(UUID.fromString(platformIdentifier).toString());
    }

    public static PlatformIdentifier create() {
        return new PlatformIdentifier(UUID.randomUUID().toString());
    }

    public static PlatformIdentifier create(String hash) {
        return new PlatformIdentifier(UUID.nameUUIDFromBytes(hash.getBytes()).toString());
    }

    private PlatformIdentifier(String platformIdentifier) {
        this.platformIdentifier = platformIdentifier;
    }

    @Override
    public String toString() {
        return platformIdentifier;
    }

    private PlatformIdentifier() {}

    public String getPlatformIdentifier() {
        return platformIdentifier;
    }

    public void setPlatformIdentifier(String platformIdentifier) {
        this.platformIdentifier = UUID.fromString(platformIdentifier).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformIdentifier that = (PlatformIdentifier) o;
        return Objects.equals(platformIdentifier, that.platformIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(platformIdentifier);
    }
}