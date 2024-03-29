package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 */
public class PclbType implements Parcelable {

	public static final Parcelable.Creator<PclbType> CREATOR = new Parcelable.Creator<>() {
		@Override
		public PclbType createFromParcel(Parcel source) {
			return new PclbType(source);
		}

		@Override
		public PclbType[] newArray(int size) {
			return new PclbType[size];
		}
	};
	private Type type;

	public PclbType(Type t) {
		this.type = t;
	}

	private PclbType(Parcel in) {
		this.type = Type.valueOfIgnoreCase(in.readString());
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.type == null ? "" : this.type.name());
	}

	@Override
	public int describeContents() {
		return 0;
	}

}