/**
 *
 */
package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.model.bean.Team;
import com.pokemongostats.model.bean.Trainer;

/**
 * @author Zapagon
 */
public class PclbTrainer extends Trainer implements Parcelable {

    public static final Parcelable.Creator<PclbTrainer> CREATOR = new Parcelable.Creator<PclbTrainer>() {
        @Override
        public PclbTrainer createFromParcel(Parcel source) {
            return new PclbTrainer(source);
        }

        @Override
        public PclbTrainer[] newArray(int size) {
            return new PclbTrainer[size];
        }
    };
    /**
     *
     */
    private static final long serialVersionUID = -8637527109795740518L;

    public PclbTrainer(Trainer t) {
        if (t != null) {
            setId(t.getId());
            setLevel(t.getLevel());
            setName(t.getName());
            setTeam(t.getTeam());
        }
    }

    public PclbTrainer(Parcel in) {
        setId(in.readLong());
        setLevel(in.readInt());
        setName(in.readString());
        setTeam(Team.valueOfIgnoreCase(in.readString()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeInt(getLevel());
        dest.writeString(getName());
        dest.writeString(getTeam().name());
    }

}
