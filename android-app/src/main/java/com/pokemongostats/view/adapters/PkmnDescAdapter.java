/**
 * 
 */
package com.pokemongostats.view.adapters;

import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.commons.OnClickItemListener;
import com.pokemongostats.view.listeners.HasPkmnDescSelectable;
import com.pokemongostats.view.listeners.SelectedVisitor;
import com.pokemongostats.view.rows.PkmnDescRowView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

/**
 * @author Zapagon
 *
 */
public class PkmnDescAdapter extends ItemAdapter<PokemonDescription>
		implements
			Filterable,
			HasPkmnDescSelectable {

	private SelectedVisitor<PokemonDescription> mCallbackPkmnDesc;

	private boolean isBaseAttVisible;
	private boolean isBaseDefVisible;
	private boolean isBaseStaminaVisible;
	private boolean isMaxCPVisible;

	public PkmnDescAdapter(Context context) {
		super(context);
	}

	/**
	 * @return the isBaseAttVisible
	 */
	public boolean isBaseAttVisible() {
		return isBaseAttVisible;
	}

	/**
	 * @param isBaseAttVisible
	 *            the isBaseAttVisible to set
	 */
	public void setBaseAttVisible(boolean isBaseAttVisible) {
		this.isBaseAttVisible = isBaseAttVisible;
	}

	/**
	 * @return the isBaseDefVisible
	 */
	public boolean isBaseDefVisible() {
		return isBaseDefVisible;
	}

	/**
	 * @param isBaseDefVisible
	 *            the isBaseDefVisible to set
	 */
	public void setBaseDefVisible(boolean isBaseDefVisible) {
		this.isBaseDefVisible = isBaseDefVisible;
	}

	/**
	 * @return the isBaseStaminaVisible
	 */
	public boolean isBaseStaminaVisible() {
		return isBaseStaminaVisible;
	}

	/**
	 * @param isBaseStaminaVisible
	 *            the isBaseStaminaVisible to set
	 */
	public void setBaseStaminaVisible(boolean isBaseStaminaVisible) {
		this.isBaseStaminaVisible = isBaseStaminaVisible;
	}

	/**
	 * @return the isMaxCPVisible
	 */
	public boolean isMaxCPVisible() {
		return isMaxCPVisible;
	}

	/**
	 * @param isMaxCPVisible
	 *            the isMaxCPVisible to set
	 */
	public void setMaxCPVisible(boolean isMaxCPVisible) {
		this.isMaxCPVisible = isMaxCPVisible;
	}

	/**
	 * Change text view color & text from Trainer at position
	 * 
	 * @param textView
	 * @param position
	 * @return textView
	 */
	@Override
	protected View createViewAtPosition(int position, View v,
			ViewGroup parent) {
		PokemonDescription p = getItem(position);
		if (p == null) { return v; }

		final PkmnDescRowView view;
		if (v == null || !(v instanceof PkmnDescRowView)) {
			view = new PkmnDescRowView(getContext());
			view.setPkmnDesc(p);
			view.update();
		} else {
			view = (PkmnDescRowView) v;
			if (!p.equals(view.getPkmnDesc())) {
				view.setPkmnDesc(p);
				if (mCallbackPkmnDesc != null) {
					view.setOnClickListener(
							new OnClickItemListener<PokemonDescription>(
									mCallbackPkmnDesc, p));
				}
				view.update();
			}
		}
		view.setBaseAttVisible(isBaseAttVisible);
		view.setBaseDefVisible(isBaseDefVisible);
		view.setBaseStaminaVisible(isBaseStaminaVisible);
		view.setMaxCPVisible(isMaxCPVisible);
		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptSelectedVisitorPkmnDesc(
			SelectedVisitor<PokemonDescription> visitor) {
		this.mCallbackPkmnDesc = visitor;
	}

	@Override
	protected String itemToString(PokemonDescription item) {
		return (item == null) ? "" : item.getName();
	}
}