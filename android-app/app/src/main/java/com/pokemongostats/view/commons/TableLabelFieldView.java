package com.pokemongostats.view.commons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class TableLabelFieldView extends LinearLayout {

	private TextView mLabel;
	private View mField;
	private float fieldWeight = 1f;

	public TableLabelFieldView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public TableLabelFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
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
		float labelWeight = 1f;
		this.fieldWeight = 1f;
		if (attrs != null) {
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

				this.fieldWeight = (labelWeight < 1)
						? 1f - labelWeight
						: labelWeight;
			} finally {
				typedArray.recycle();
			}
		}

		inflate(getContext(), R.layout.view_table_labelfield, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setOrientation(LinearLayout.HORIZONTAL);

		// label
		this.mLabel = findViewById(R.id.table_label);
		this.mLabel.setText(labelText);
		this.mLabel.setTextAppearance(labelStyle);
		this.mLabel.setLayoutParams(
				new LayoutParams(0, LayoutParams.WRAP_CONTENT, labelWeight));
	}

	public CharSequence getLabelText() {
		return this.mLabel.getText();
	}

	public void setLabelText(CharSequence text) {
		this.mLabel.setText(text);
	}

	public void setField(View field) {
		if (field == null) {
			return;
		}
		if (this.mField != null) {
			removeView(this.mField);
		}
		this.setGravity(Gravity.CENTER);
		this.addView(field,
				new LayoutParams(0, LayoutParams.WRAP_CONTENT, this.fieldWeight));

		this.mField = field;
	}
}