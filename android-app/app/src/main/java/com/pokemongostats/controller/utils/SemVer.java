package com.pokemongostats.controller.utils;

public class SemVer implements Comparable<SemVer> {
	int major;
	int minor;
	int patch;

	public SemVer(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public static SemVer parse(String versionString) {
		if (versionString == null
			|| versionString.isEmpty()) { return new SemVer(0, 0, 0); }
		versionString = versionString.trim().replace("v", "");
		String[] versionParts = versionString.split("\\.");
		int size = versionParts.length;
		int majorVersion = size >= 0 ? Integer.parseInt(versionParts[0]) : 0;
		int minorVersion = size >= 1 ? Integer.parseInt(versionParts[1]) : 0;
		int patchVersion = size >= 2 ? Integer.parseInt(versionParts[2]) : 0;
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
		if (this == other) { return true; }
		if (!(other instanceof SemVer)) { return false; }
		return compareTo((SemVer) other) == 0;
	}

	@Override
	public String toString() {
		return String.format("v%d.%d.%d", major, minor, patch);
	}
}
