package com.pokemongostats.model.parcalables;

import android.os.Parcel;
import android.os.Parcelable;

import com.pokemongostats.controller.utils.PkmnTags;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.Type;

import java.util.Set;

/**
 * @author Zapagon
 */
public class PclbPkmnDesc extends PkmnDesc
		implements
		Parcelable {

	public static final Parcelable.Creator<PclbPkmnDesc> CREATOR = new Parcelable.Creator<>() {
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
	private static final long serialVersionUID = -1221928294608213678L;

	public PclbPkmnDesc(PkmnDesc p) {
		if (p != null) {
			setId(p.getId());
			// FIXME setName(p.getName());
			setForm(p.getForm());
			setPokedexNum(p.getPokedexNum());
			setType1(p.getType1());
			setType2(p.getType2());
			setStamina(p.getStamina());
			setAttack(p.getAttack());
			setDefense(p.getDefense());
			setPhysicalAttack(p.getPhysicalAttack());
			setPhysicalDefense(p.getPhysicalDefense());
			setSpecialAttack(p.getSpecialAttack());
			setSpecialDefense(p.getSpecialDefense());
			setPv(p.getPv());
			setSpeed(p.getSpeed());
			setKmsPerCandy(p.getKmsPerCandy());
			setKmsPerEgg(p.getKmsPerEgg());
			setTags(p.isLegendaire() ? PkmnTags.LEGENDAIRE : "");
		}
	}

	private PclbPkmnDesc(Parcel in) {
		setId(in.readLong());
		// FIXME setName(in.readString());
		setForm(in.readString());
		setPokedexNum(in.readLong());
		setType1(Type.valueOfIgnoreCase(in.readString()));
		setType2(Type.valueOfIgnoreCase(in.readString()));
		setStamina(in.readInt());
		setAttack(in.readInt());
		setDefense(in.readInt());
		setPhysicalAttack(in.readInt());
		setPhysicalDefense(in.readInt());
		setSpecialAttack(in.readInt());
		setSpecialDefense(in.readInt());
		setPv(in.readInt());
		setSpeed(in.readInt());
		setKmsPerCandy(in.readDouble());
		setKmsPerEgg(in.readDouble());
		setTags(Set.of(in.readString().split(",")));
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(getId());
		// FIXME dest.writeString(getName());
		dest.writeString(getForm());
		dest.writeLong(getId());
		dest.writeString(getType1().name());
		dest.writeString(getType2() == null ? "" : getType2().name());
		dest.writeInt(getStamina());
		dest.writeInt(getAttack());
		dest.writeInt(getDefense());
		dest.writeInt(getPhysicalAttack());
		dest.writeInt(getPhysicalDefense());
		dest.writeInt(getSpecialAttack());
		dest.writeInt(getSpecialDefense());
		dest.writeInt(getPv());
		dest.writeInt(getSpeed());
		dest.writeDouble(getKmsPerCandy());
		dest.writeDouble(getKmsPerEgg());
		dest.writeString(String.join(",", getTags()));
	}

	@Override
	public int describeContents() {
		return 0;
	}

}