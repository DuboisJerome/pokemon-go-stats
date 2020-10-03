package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.controller.utils.StringUtils;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;

/**
 * Created by Zapagon on 08/04/2017.
 * Parcelable for PkmnDescFilterInfo
 */
public class PclbPkmnDescFilterInfo extends PkmnDescFilterInfo implements Parcelable {

    public static final Creator<PclbPkmnDescFilterInfo> CREATOR = new Creator<PclbPkmnDescFilterInfo>() {
        @Override
        public PclbPkmnDescFilterInfo createFromParcel(Parcel source) {
            return new PclbPkmnDescFilterInfo(source);
        }

        @Override
        public PclbPkmnDescFilterInfo[] newArray(int size) {
            return new PclbPkmnDescFilterInfo[size];
        }
    };

    public PclbPkmnDescFilterInfo(PkmnDescFilterInfo filter) {
        if(filter != null){
            setName(filter.getName());
            setType1(filter.getType1());
            setType2(filter.getType2());
        }
    }

    private PclbPkmnDescFilterInfo(Parcel in) {
        setName(in.readString());
        String typeStr = in.readString();
        Type type = StringUtils.isNotEmpty(typeStr) ?
            Type.valueOf(typeStr) : null;
        setType1(type);
        typeStr = in.readString();
        type = StringUtils.isNotEmpty(typeStr) ?
                Type.valueOf(typeStr) : null;
        setType2(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getType1() == null ? "" : getType1().name());
        parcel.writeString(getType2() == null ? "" : getType2().name());
    }
}
