package com.pokemongostats.view.viewholder.pokedexdata;

import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.card.MaterialCardView;
import com.pokemongostats.R;
import com.pokemongostats.model.bean.pokedexdata.IPokedexDataItem;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemCreate;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemDelete;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemUpdate;

import fr.commons.generique.model.db.IObjetBdd;
import fr.commons.generique.ui.AbstractGeneriqueViewHolder;

public abstract class AbstractLstPokedexDataViewHolder<T extends IObjetBdd> extends AbstractGeneriqueViewHolder<IPokedexDataItem<T>> {

	private final View root;

	public AbstractLstPokedexDataViewHolder(ViewDataBinding binding) {
		super(binding.getRoot());
		this.root = binding.getRoot();
	}

	@Override
	protected final void bind(IPokedexDataItem<T> item) {
		if (item instanceof PokedexDataItemCreate<T> create) {
			bindCreate(create.getData());
			setTypeData(R.drawable.add_24, getColor(R.color.insertData));
		} else if (item instanceof PokedexDataItemUpdate<T> update) {
			bindUpdate(update.getOldData(), update.getNewData());
			setTypeData(R.drawable.update_24, getColor(R.color.updateData));
		} else if (item instanceof PokedexDataItemDelete<T> delete) {
			bindDelete(delete.getData());
			setTypeData(R.drawable.delete_24, getColor(R.color.deleteData));
		}
	}

	@ColorInt
	private int getColor(@ColorRes int res) {
		return this.root.getContext().getColor(res);
	}

	public abstract MaterialCardView getCardView();

	protected abstract void bindCreate(T data);

	protected abstract void bindUpdate(T oldData, T newData);

	protected abstract void bindDelete(T data);

	protected abstract void setTypeData(@DrawableRes int drawable, @ColorInt int color);
}