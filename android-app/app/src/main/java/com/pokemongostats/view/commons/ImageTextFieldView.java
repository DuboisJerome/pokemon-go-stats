package com.pokemongostats.view.commons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pokemongostats.R;

/**
 * @author Zapagon
 */
public class ImageTextFieldView extends LinearLayout {

    private ImageView img;
    private TextView field;

    public ImageTextFieldView(Context context) {
        super(context);
        initializeViews(context, null);
    }

    public ImageTextFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public ImageTextFieldView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        String labelText = "";
        int resImg = R.drawable.pokeball_close;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.ImageTextFieldView, 0, 0);
            try {
                resImg = typedArray
                        .getResourceId(R.styleable.ImageTextFieldView_img, resImg);
            } finally {
                typedArray.recycle();
            }
        }

        inflate(getContext(), R.layout.view_img_text, this);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.HORIZONTAL);

        // img
        img = findViewById(R.id.img);
        img.setImageResource(resImg);
        //img.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,1));

        // field
        field = findViewById(R.id.text);
        field.setText(labelText);
        //field.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,1));
    }

    public TextView getField() {
        return this.field;
    }

    /**
     * @param field
     */
    public void setField(final TextView field) {
        if (field == null) {
            return;
        }
        removeView(field);

        this.setGravity(Gravity.CENTER);
        //this.addView(field,new LayoutParams(0, LayoutParams.WRAP_CONTENT,1));

        this.field = field;
    }

    /**
     * @param text
     */
    public void setFieldText(final CharSequence text) {
        field.setText(text);
    }

    public void setImg(final ImageView img) {
        if (img == null) {
            return;
        }
        removeView(img);

        this.setGravity(Gravity.CENTER);
        //this.addView(img, new LayoutParams(0, LayoutParams.WRAP_CONTENT,1));

        this.img = img;
    }
}
