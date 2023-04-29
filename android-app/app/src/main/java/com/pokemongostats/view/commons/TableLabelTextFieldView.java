package com.pokemongostats.view.commons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class TableLabelTextFieldView extends LinearLayout {

	private TextView mLabel;
	private TextView mField;

	public TableLabelTextFieldView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public TableLabelTextFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeViews(context, attrs);
	}

	public TableLabelTextFieldView(Context context, AttributeSet attrs,
	                               int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {
		String labelText = "";
		int labelStyle = R.style.TableLabelTextViewStyle;
		int fieldStyle = R.style.TableFieldTextViewStyle;
		float labelWeight = 1f;
		float fieldWeight = 1f;
		if (attrs != null) {
			@SuppressLint("CustomViewStyleable")
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.TableLabelFieldView, 0, 0);
			try {
				labelText = typedArray
						.getString(R.styleable.TableLabelFieldView_labelText);
				labelStyle = typedArray.getResourceId(
						R.styleable.TableLabelFieldView_labelBackground,
						labelStyle);
				labelWeight = typedArray
						.getFloat(R.styleable.TableLabelFieldView_labelWeight, 1f);

				fieldStyle = typedArray.getResourceId(
						R.styleable.TableLabelFieldView_fieldBackground,
						fieldStyle);
				fieldWeight = (labelWeight < 1)
						? 1f - labelWeight
						: labelWeight;
			} finally {
				typedArray.recycle();
			}
		}

		inflate(getContext(), R.layout.view_table_labeltextfield, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setOrientation(LinearLayout.HORIZONTAL);

		// label
		this.mLabel = findViewById(R.id.table_label);
		this.mLabel.setText(labelText);
		this.mLabel.setTextAppearance(labelStyle);
		this.mLabel.setLayoutParams(
				new LayoutParams(0, LayoutParams.WRAP_CONTENT, labelWeight));

		// field
		this.mField = findViewById(R.id.table_field);
		this.mField.setTextAppearance(fieldStyle);
		this.mField.setLayoutParams(
				new LayoutParams(0, LayoutParams.WRAP_CONTENT, fieldWeight));
		//mField.setHeight(mLabel.getHeight());
	}

	public CharSequence getLabelText() {
		return this.mLabel.getText();
	}

	public void setLabelText(CharSequence text) {
		this.mLabel.setText(text);
		//mField.setHeight(mLabel.getHeight());
	}

	public CharSequence getFieldText() {
		return this.mField.getText();
	}

	public void setFieldText(CharSequence text) {
		this.mField.setText(text);
	}

	public void addTextChangedListener(TextWatcher watcher) {
		this.mField.addTextChangedListener(watcher);
	}

	public void setField(TextView field) {
		ViewGroup.LayoutParams params = null;
		if (this.mField != null) {
			params = this.mField.getLayoutParams();
			removeView(this.mField);
		}
		this.mField = field;
		if (params != null) {
			this.mField.setLayoutParams(params);
		}
		addView(this.mField);
	}
}