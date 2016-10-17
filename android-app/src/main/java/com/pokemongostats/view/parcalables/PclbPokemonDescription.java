/**
 * 
 */
package com.pokemongostats.view.parcalables;

import java.util.ArrayList;
import java.util.List;

import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.model.bean.Type;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Zapagon
 *
 */
@SuppressWarnings("serial")
public class PclbPokemonDescription extends PokemonDescription
		implements
			Parcelable {

	public PclbPokemonDescription(PokemonDescription p) {
		setId(p.getId());
		setDescription(p.getDescription());
		setEvolutionIds(p.getEvolutionIds());
		setFamily(p.getFamily());
		setName(p.getName());
		setPokedexNum(p.getPokedexNum());
		setType1(p.getType1());
		setType2(p.getType2());
	}

	private PclbPokemonDescription(Parcel in) {
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
	}

	public static final Parcelable.Creator<PclbPokemonDescription> CREATOR = new Parcelable.Creator<PclbPokemonDescription>() {
		@Override
		public PclbPokemonDescription createFromParcel(Parcel source) {
			return new PclbPokemonDescription(source);
		}

		@Override
		public PclbPokemonDescription[] newArray(int size) {
			return new PclbPokemonDescription[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
