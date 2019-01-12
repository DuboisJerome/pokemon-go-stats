package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
/**
 * @author Zapagon
 */
@SuppressWarnings("serial")
public class PclbPkmnDesc extends PkmnDesc
        implements
        Parcelable {

    public static final Parcelable.Creator<PclbPkmnDesc> CREATOR = new Parcelable.Creator<PclbPkmnDesc>() {
        @Override
        public PclbPkmnDesc createFromParcel(Parcel source) {
            PclbPkmnDesc p = new PclbPkmnDesc(source);
            return (p.getId() <= 0) ? null : p;
        }

        @Override
        public PclbPkmnDesc[] newArray(int size) {
            return new PclbPkmnDesc[size];
        }
    };

    public PclbPkmnDesc(PkmnDesc p) {
        if (p != null) {
            setId(p.getId());
            setDescription(p.getDescription());
            setFamily(p.getFamily());
            setName(p.getName());
            setPokedexNum(p.getPokedexNum());
            setType1(p.getType1());
            setType2(p.getType2());
            setPhysicalAttack(p.getPhysicalAttack());
            setPhysicalDefense(p.getPhysicalDefense());
            setSpecialAttack(p.getSpecialAttack());
            setSpecialDefense(p.getSpecialDefense());
            setPv(p.getPv());
            setSpeed(p.getSpeed());
            setKmsPerCandy(p.getKmsPerCandy());
            setKmsPerEgg(p.getKmsPerEgg());
        }
    }

    private PclbPkmnDesc(Parcel in) {
        setId(in.readLong());
        setDescription(in.readString());
        setFamily(in.readString());
        setName(in.readString());
        setPokedexNum(in.readLong());
        setType1(Type.valueOfIgnoreCase(in.readString()));
        setType2(Type.valueOfIgnoreCase(in.readString()));
        setPhysicalAttack(in.readInt());
        setPhysicalDefense(in.readInt());
        setSpecialAttack(in.readInt());
        setSpecialDefense(in.readInt());
        setPv(in.readInt());
        setSpeed(in.readInt());
        setKmsPerCandy(in.readDouble());
        setKmsPerEgg(in.readDouble());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getDescription());
        dest.writeString(getFamily());
        dest.writeString(getName());
        dest.writeLong(getId());
        dest.writeString(getType1().name());
        dest.writeString(getType2() == null ? "" : getType2().name());
        dest.writeInt(getPhysicalAttack());
        dest.writeInt(getPhysicalDefense());
        dest.writeInt(getSpecialAttack());
        dest.writeInt(getSpecialDefense());
        dest.writeInt(getPv());
        dest.writeInt(getSpeed());
        dest.writeDouble(getKmsPerCandy());
        dest.writeDouble(getKmsPerEgg());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
