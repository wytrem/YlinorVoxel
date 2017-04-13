package com.ylinor.server;

public final class Version {
    private final int major, minor, build;
    private final String versionString;

    public Version(int major, int minor) {
        this.major = major;
        this.minor = minor;
        this.build = 0;

        this.versionString = String.format("%d.%d", major, minor);
    }

    public Version(int major, int minor, int build) {
        this.major = major;
        this.minor = minor;
        this.build = build;

        this.versionString = String.format("%d.%d.%d", major, minor, build);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getBuild() {
        return build;
    }

    @Override
    public String toString() {
        return versionString;
    }
}
