/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class TableLabelFieldView extends LinearLayout {

	private TextView mLabel;
	private TextView mField;

	public TableLabelFieldView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public TableLabelFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initializeViews(context, attrs);
	}

	public TableLabelFieldView(Context context, AttributeSet attrs,
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
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.TableLabelField, 0, 0);
			try {
				labelText = typedArray
						.getString(R.styleable.TableLabelField_labelText);
				labelStyle = typedArray.getResourceId(
						R.styleable.TableLabelField_labelBackground,
						labelStyle);
				labelWeight = typedArray
						.getFloat(R.styleable.TableLabelField_labelWeight, 1f);

				fieldStyle = typedArray.getResourceId(
						R.styleable.TableLabelField_fieldBackground,
						fieldStyle);
				fieldWeight = (labelWeight < 1)
						? 1f - labelWeight
						: labelWeight;
			} finally {
				typedArray.recycle();
			}
		}

		inflate(getContext(), R.layout.view_table_labelfield, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		// label
		mLabel = (TextView) findViewById(R.id.table_label);
		mLabel.setText(labelText);
		mLabel.setTextAppearance(context, labelStyle);
		mLabel.setLayoutParams(
				new LayoutParams(0, LayoutParams.WRAP_CONTENT, labelWeight));

		// field
		mField = (TextView) findViewById(R.id.table_field);
		mField.setTextAppearance(context, fieldStyle);
		mField.setLayoutParams(
				new LayoutParams(0, LayoutParams.WRAP_CONTENT, fieldWeight));
	}

	/**
	 * @return
	 */
	public CharSequence getLabelText() {
		return mLabel.getText();
	}

	/**
	 * @param text
	 */
	public void setLabelText(CharSequence text) {
		mLabel.setText(text);
	}

	/**
	 * @return
	 */
	public CharSequence getFieldText() {
		return mField.getText();
	}

	/**
	 * @param text
	 */
	public void setFieldText(CharSequence text) {
		mField.setText(text);
	}

	/**
	 * @param watcher
	 */
	public void addTextChangedListener(final TextWatcher watcher) {
		mField.addTextChangedListener(watcher);
	}
}
