package com.pokemongostats.view.commons;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.adapter.TypeRecyclerViewAdapter;
import com.pokemongostats.view.viewholder.LstTypeViewHolder;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import lombok.Getter;

/**
 * @author Zapagon
 */
public class ChooseTypeView extends RecyclerView {

	private Consumer<Type> mCallbackType;

	@Getter
	private final Set<Type> lstCurrentType = new HashSet<>();

	public ChooseTypeView(Context context) {
		super(context);
		initializeViews(null);
	}

	public ChooseTypeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeViews(attrs);
	}

	public ChooseTypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(attrs);
	}

	private void initializeViews(AttributeSet attrs) {
		Context ctx = getContext();
		setLayoutManager(new GridLayoutManager(ctx, 3));
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		TypeRecyclerViewAdapter adapter = new TypeRecyclerViewAdapter(false) {
			@NonNull
			@Override
			public LstTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				LstTypeViewHolder l = super.onCreateViewHolder(parent, viewType);
				int heightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, getResources().getDisplayMetrics());
				l.getBinding().idType.setHeight(heightPx);
				return l;
			}
		};
		setAdapter(adapter);
		adapter.addBinderView("SELECT_TYPE", (v, type) -> {
			v.setEnabled(!isTypeSelected(type));
			v.setOnClickListener(v1 -> {
				boolean isAlreadySelected = isTypeSelected(type);
				v1.setEnabled(true);
				if (!isAlreadySelected && this.mCallbackType != null) {
					this.mCallbackType.accept(type);
				}
			});
		});
	}

	public void setCallbackType(Consumer<Type> mCallbackType) {
		this.mCallbackType = mCallbackType;
	}

	public void addCurrentType(Type currentType) {
		if (currentType != null) {
			this.lstCurrentType.add(currentType);
		}
	}

	public void removeCurrentType(Type currentType) {
		if (currentType != null) {
			this.lstCurrentType.remove(currentType);
		}
	}

	public void clearCurrentType() {
		this.lstCurrentType.clear();
	}

	public boolean isTypeSelected(Type t) {
		return this.lstCurrentType.contains(t);
	}

}