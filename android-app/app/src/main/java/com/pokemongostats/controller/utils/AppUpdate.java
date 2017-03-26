package com.pokemongostats.controller.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class AppUpdate implements Parcelable {

    public static final int UPDATE_AVAILABLE = 1234;
    public static final int UP_TO_DATE = 1235;
    public static final int ERROR = 1236;
    public static final Parcelable.Creator<AppUpdate> CREATOR = new Parcelable.Creator<AppUpdate>() {
        @Override
        public AppUpdate createFromParcel(Parcel in) {
            return new AppUpdate(in);
        }

        @Override
        public AppUpdate[] newArray(int size) {
            return new AppUpdate[size];
        }
    };
    private String assetUrl;
    private String version;
    private String changelog;
    private int status;

    public AppUpdate(String assetUrl, String version, String changelog,
                     int status) {
        this.assetUrl = assetUrl;
        this.version = version;
        this.changelog = changelog;
        this.status = status;
    }

    private AppUpdate(Parcel in) {
        assetUrl = in.readString();
        version = in.readString();
        changelog = in.readString();
        status = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(assetUrl);
        out.writeString(version);
        out.writeString(changelog);
        out.writeInt(status);
    }

    /**
     * @return the assetUrl
     */
    public String getAssetUrl() {
        return assetUrl;
    }

    /**
     * @param assetUrl the assetUrl to set
     */
    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the changelog
     */
    public String getChangelog() {
        return changelog;
    }

    /**
     * @param changelog the changelog to set
     */
    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
