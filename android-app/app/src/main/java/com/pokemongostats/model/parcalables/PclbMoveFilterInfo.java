package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.controller.utils.StringUtils;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;

/**
 * Created by Zapagon on 08/04/2017.
 * Parcelable for MoveFilterInfo
 */
public class PclbMoveFilterInfo extends MoveFilterInfo implements Parcelable {

    public static final Parcelable.Creator<PclbMoveFilterInfo> CREATOR = new Parcelable.Creator<>() {
	    @Override
	    public PclbMoveFilterInfo createFromParcel(Parcel source) {
		    return new PclbMoveFilterInfo(source);
	    }

	    @Override
	    public PclbMoveFilterInfo[] newArray(int size) {
		    return new PclbMoveFilterInfo[size];
	    }
    };

    public PclbMoveFilterInfo(MoveFilterInfo filter) {
        if (filter != null) {
            setName(filter.getName());
            setType(filter.getType());
        }
    }

    private PclbMoveFilterInfo(Parcel in) {
        setName(in.readString());
        String typeStr = in.readString();
        Type type = StringUtils.isNotEmpty(typeStr) ?
                Type.valueOf(typeStr) : null;
        setType(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getType() == null ? "" : getType().name());
    }
}