/**
 * 
 */
package com.pokemongostats.view.expandables;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.rows.TypeRowView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public class TypeExpandable extends CustomExpandableList<Type> {

	public TypeExpandable(Context context) {
		super(context);
	}

	public TypeExpandable(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public TypeExpandable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public View buildView(Type p) {
		return TypeRowView.create(getContext(), p);
	}

	@Override
	public int compare(Type t1, Type t2) {
		if (t1 == null && t2 == null) {
			return 0;
		}
		if (t2 == null) {
			return 1;
		}
		if (t1 == null) {
			return -1;
		}

		String nameT1 = t1.name();
		String nameT2 = t2.name();

		if (nameT1 == null && nameT2 == null) {
			return 0;
		}
		if (nameT2 == null) {
			return 1;
		}
		if (nameT1 == null) {
			return -1;
		}

		return nameT1.compareTo(nameT2);
	}
}
