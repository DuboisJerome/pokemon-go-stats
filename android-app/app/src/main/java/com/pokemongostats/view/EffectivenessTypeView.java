package com.pokemongostats.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.pokemongostats.databinding.ViewEffectivenessTypeBinding;
import com.pokemongostats.view.adapter.TypeRecyclerViewAdapter;
import com.pokemongostats.view.listitem.TypeRecyclerView;

public class EffectivenessTypeView extends LinearLayout {

	private final ViewEffectivenessTypeBinding binding;

	public EffectivenessTypeView(Context context, double eff) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		this.binding = ViewEffectivenessTypeBinding.inflate(LayoutInflater.from(getContext()), this, true);
		this.binding.setEff(eff);
	}

	public EffectivenessTypeView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		this.binding = ViewEffectivenessTypeBinding.inflate(LayoutInflater.from(getContext()), this, true);
		this.binding.setEff(1);
	}

	public EffectivenessTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setOrientation(LinearLayout.VERTICAL);
		this.binding = ViewEffectivenessTypeBinding.inflate(LayoutInflater.from(getContext()), this, true);
		this.binding.setEff(1);
	}

	public void setNbColonnesType(int nb) {
		getTypeRecyclerView().setNbColonnes(nb);
	}

	public void setAdapter(TypeRecyclerViewAdapter adapter) {
		getTypeRecyclerView().setAdapter(adapter);
	}

	public TypeRecyclerView getTypeRecyclerView() {
		return this.binding.types;
	}
}