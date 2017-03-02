/**
 * 
 */
package com.pokemongostats.view.parcalables;

import com.pokemongostats.model.bean.Type;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Zapagon
 *
 */
public class PclbType implements Parcelable {

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
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type == null ? "" : type.name());
	}

	public static final Parcelable.Creator<PclbType> CREATOR = new Parcelable.Creator<PclbType>() {
		@Override
		public PclbType createFromParcel(Parcel source) {
			return new PclbType(source);
		}

		@Override
		public PclbType[] newArray(int size) {
			return new PclbType[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
