/**
 *
 */
package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;

import java.util.ArrayList;
import java.util.List;

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
            setEvolutionIds(p.getEvolutionIds());
            setFamily(p.getFamily());
            setName(p.getName());
            setPokedexNum(p.getPokedexNum());
            setType1(p.getType1());
            setType2(p.getType2());
            setBaseAttack(p.getBaseAttack());
            setBaseDefense(p.getBaseDefense());
            setBaseStamina(p.getBaseStamina());
            setKmsPerCandy(p.getKmsPerCandy());
            setKmsPerEgg(p.getKmsPerEgg());
            setMoveIds(p.getMoveIds());
        }
    }

    private PclbPkmnDesc(Parcel in) {
        setId(in.readLong());
        setDescription(in.readString());
        long[] arrayId = in.createLongArray();
        List<Long> evolutionsIds = new ArrayList<Long>();
        for (int i = 0; i < arrayId.length; ++i) {
            evolutionsIds.add(arrayId[i]);
        }
        setEvolutionIds(evolutionsIds);
        setFamily(in.readString());
        setName(in.readString());
        setPokedexNum(in.readLong());
        setType1(Type.valueOfIgnoreCase(in.readString()));
        setType2(Type.valueOfIgnoreCase(in.readString()));
        setBaseAttack(in.readDouble());
        setBaseDefense(in.readDouble());
        setBaseStamina(in.readDouble());
        setKmsPerCandy(in.readDouble());
        setKmsPerEgg(in.readDouble());
        arrayId = in.createLongArray();
        List<Long> movesIds = new ArrayList<Long>();
        for (int i = 0; i < arrayId.length; ++i) {
            movesIds.add(arrayId[i]);
        }
        setMoveIds(movesIds);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getDescription());
        dest.writeArray(getEvolutionIds() == null
                ? new Long[0]
                : getEvolutionIds().toArray());
        dest.writeString(getFamily());
        dest.writeString(getName());
        dest.writeLong(getId());
        dest.writeString(getType1().name());
        dest.writeString(getType2() == null ? "" : getType2().name());
        dest.writeDouble(getBaseAttack());
        dest.writeDouble(getBaseDefense());
        dest.writeDouble(getBaseStamina());
        dest.writeDouble(getKmsPerCandy());
        dest.writeDouble(getKmsPerEgg());
        dest.writeArray(
                getMoveIds() == null ? new Long[0] : getMoveIds().toArray());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
