package com.pokemongostats.model.bean;

import androidx.annotation.NonNull;

import java.util.Locale;

public class SemVer implements Comparable<SemVer> {
    final int major;
    final int minor;
    final int patch;

    public SemVer(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static SemVer parse(String versionString) {
        if (versionString == null
                || versionString.isEmpty()) {
            return new SemVer(0, 0, 0);
        }
        versionString = versionString.trim().replace("v", "");
        String[] versionParts = versionString.split("\\.");
        int majorVersion = Integer.parseInt(versionParts[0]);
        int minorVersion = Integer.parseInt(versionParts[1]);
        int patchVersion = Integer.parseInt(versionParts[2]);
        return new SemVer(majorVersion, minorVersion, patchVersion);
    }

    @Override
    public int compareTo(SemVer other) {
        int result = major - other.major;
        if (result == 0) {
            result = minor - other.minor;
            if (result == 0) {
                result = patch - other.patch;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SemVer)) {
            return false;
        }
        return compareTo((SemVer) other) == 0;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "v%d.%d.%d", major, minor, patch);
    }
}
