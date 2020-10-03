/**
 *
 */
package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.model.bean.MoveCombination;

/**
 * @author Zapagon
 */
public class PclbMoveComb extends MoveCombination implements Parcelable {

    public static final Creator<PclbMoveComb> CREATOR = new Creator<PclbMoveComb>() {
        @Override
        public PclbMoveComb createFromParcel(Parcel source) {
            PclbMoveComb p = new PclbMoveComb(source);
            return p;
        }

        @Override
        public PclbMoveComb[] newArray(int size) {
            return new PclbMoveComb[size];
        }
    };

    public PclbMoveComb(MoveCombination m) {
        this.setPkmnDesc(m.getPkmnDesc());
        this.setQuickMove(m.getQuickMove());
        this.setChargeMove(m.getChargeMove());
        this.setAttPPS(m.getAttPPS());
        this.setDefPPS(m.getDefPPS());
    }

    private PclbMoveComb(Parcel in) {
        PclbPkmnDesc pclbPkmnDesc = in.readParcelable(PclbPkmnDesc.class.getClassLoader());
        this.setPkmnDesc(pclbPkmnDesc);
        PclbMove pclbQuickMove = in.readParcelable(PclbMove.class.getClassLoader());
        this.setQuickMove(pclbQuickMove);
        PclbMove pclbChargeMove = in.readParcelable(PclbMove.class.getClassLoader());
        this.setChargeMove(pclbChargeMove);
        this.setAttPPS(in.readDouble());
        this.setDefPPS(in.readDouble());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new PclbPkmnDesc(getPkmnDesc()), 0);
        dest.writeParcelable(new PclbMove(getQuickMove()), 0);
        dest.writeParcelable(new PclbMove(getChargeMove()), 0);
        dest.writeDouble(getAttPPS());
        dest.writeDouble(getDefPPS());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
