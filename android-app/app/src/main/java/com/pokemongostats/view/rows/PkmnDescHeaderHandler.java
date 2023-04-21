package com.pokemongostats.view.rows;

import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.comparators.PkmnDescComparators;
import com.pokemongostats.view.adapter.PkmnDescAdapter;

import java.util.Comparator;

/**
 * @author Zapagon
 */
public class PkmnDescHeaderHandler {

	public void onClickName(View v, PkmnDescAdapter a) {
		onClick(v, a, PkmnDescComparators.getComparatorByName());
	}

	public void onClickImg(View v, PkmnDescAdapter a) {
		onClick(v, a, PkmnDescComparators.getComparatorById());
	}

	public void onClickAttack(View v, PkmnDescAdapter a) {
		onClick(v, a, PkmnDescComparators.getComparatorByBaseAttack());
	}

	public void onClickDefense(View v, PkmnDescAdapter a) {
		onClick(v, a, PkmnDescComparators.getComparatorByBaseDefense());
	}

	public void onClickStamina(View v, PkmnDescAdapter a) {
		onClick(v, a, PkmnDescComparators.getComparatorByBaseStamina());
	}

	public void onClickCPMax(View v, PkmnDescAdapter a) {
		onClick(v, a, PkmnDescComparators.getComparatorByMaxCp());
	}

	private void onClick(View v, PkmnDescAdapter a, Comparator<PkmnDesc> c) {
		if (a == null) return;

		if (a.getComparator() != null && a.getComparator().equals(c)) {
			c = c.reversed();
		} else {
			LinearLayoutCompat l = (LinearLayoutCompat) v.getParent();
			for (int i = 0; i < l.getChildCount(); ++i) {
				View child = l.getChildAt(i);
				if (v != child) {
					child.setSelected(false);
				}
			}
		}
		a.sort(c);
		v.setSelected(true);
	}

}