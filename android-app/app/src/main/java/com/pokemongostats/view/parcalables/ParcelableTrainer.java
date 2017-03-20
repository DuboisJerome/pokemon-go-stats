/**
 * 
 */
package com.pokemongostats.view.parcalables;

import com.pokemongostats.model.bean.Team;
import com.pokemongostats.model.bean.Trainer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Zapagon
 *
 */
public class ParcelableTrainer extends Trainer implements Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8637527109795740518L;

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
	public static final Parcelable.Creator<ParcelableTrainer> CREATOR = new Parcelable.Creator<ParcelableTrainer>() {
		@Override
		public ParcelableTrainer createFromParcel(Parcel source) {
			return new ParcelableTrainer(source);
		}

		@Override
		public ParcelableTrainer[] newArray(int size) {
			return new ParcelableTrainer[size];
		}
	};

	public ParcelableTrainer(Trainer t) {
		if (t != null) {
			setId(t.getId());
			setLevel(t.getLevel());
			setName(t.getName());
			setTeam(t.getTeam());
		}
	}

	public ParcelableTrainer(Parcel in) {
		setId(in.readLong());
		setLevel(in.readInt());
		setName(in.readString());
		setTeam(Team.valueOfIgnoreCase(in.readString()));
	}

}