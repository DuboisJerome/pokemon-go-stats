package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 */
@SuppressWarnings("serial")
public class PclbMove extends Move implements Parcelable {

    public static final Parcelable.Creator<PclbMove> CREATOR = new Parcelable.Creator<PclbMove>() {
        @Override
        public PclbMove createFromParcel(Parcel source) {
            PclbMove p = new PclbMove(source);
            return (p.getId() <= 0) ? null : p;
        }

        @Override
        public PclbMove[] newArray(int size) {
            return new PclbMove[size];
        }
    };

    public PclbMove(Move m) {
        this.setCriticalChance(m.getCriticalChance());
        this.setDuration(m.getDuration());
        this.setEnergyDelta(m.getEnergyDelta());
        this.setId(m.getId());
        this.setMoveType(m.getMoveType());
        this.setName(m.getName());
        this.setPower(m.getPower());
        this.setStaminaLossScalar(m.getStaminaLossScalar());
        this.setType(m.getType());
    }

    private PclbMove(Parcel in) {
        this.setCriticalChance(in.readDouble());
        this.setDuration(in.readInt());
        this.setEnergyDelta(in.readInt());
        this.setId(in.readLong());
        this.setMoveType(MoveType.valueOfIgnoreCase(in.readString()));
        this.setName(in.readString());
        this.setPower(in.readInt());
        this.setStaminaLossScalar(in.readDouble());
        this.setType(Type.valueOfIgnoreCase(in.readString()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(getCriticalChance());
        dest.writeInt(getDuration());
        dest.writeInt(getEnergyDelta());
        dest.writeLong(getId());
        dest.writeString(getMoveType() == null ? "" : getMoveType().name());
        dest.writeString(getName());
        dest.writeInt(getPower());
        dest.writeDouble(getStaminaLossScalar());
        dest.writeString(getType() == null ? "" : getType().name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
