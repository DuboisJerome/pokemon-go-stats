package com.pokemongostats.view.commons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.pokemongostats.R;
import com.pokemongostats.databinding.ViewHeaderDropdownBinding;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Zapagon
 */
public class CustomExpandableView extends LinearLayout implements Observable {

	private TextView titleTextView;
	private final List<View> listExpandableView = new ArrayList<>();
	private boolean isExpand = false;
	private boolean keepExpand = false;
	private Drawable icClosed, icOpened;
	private final OnClickListener onClickExpandListener = v -> {
		if (this.isExpand && !this.keepExpand) {
			retract();
		} else {
			expand();
		}
	};

	public CustomExpandableView(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public CustomExpandableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeViews(context, attrs);
	}

	public CustomExpandableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	public void setTitle(String str){
		((TextView)findViewById(R.id.title)).setText(str);
	}

	private void initializeViews(Context context, AttributeSet attrs) {

		int titleStyle = -1;
		String title = "";
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					new int[]{R.attr.title, R.attr.titleStyle}, 0, 0);
			try {
				title = typedArray.getString(R.styleable.CustomExpandableView_title);
				title = title == null ? "" : title;
				titleStyle = typedArray.getResourceId(R.styleable.CustomExpandableView_titleStyle, android.R.style.TextAppearance_DeviceDefault);

			} finally {
				typedArray.recycle();
			}
		}
		setOrientation(VERTICAL);

		ViewHeaderDropdownBinding headerBinding = ViewHeaderDropdownBinding.inflate(LayoutInflater.from(getContext()), this, true);


		this.icClosed = ContextCompat.getDrawable(getContext(), R.drawable.pokeball_close);
		assert this.icClosed != null;
		this.icClosed.setBounds(0, 0, 12 * 4, 12 * 4);

		this.icOpened = ContextCompat.getDrawable(getContext(), R.drawable.pokeball_open);
		assert this.icOpened != null;
		this.icOpened.setBounds(0, 0, 12 * 4, 16 * 4);

		// title text view
		this.titleTextView = headerBinding.title;
		this.titleTextView.setText(title);
		this.titleTextView.setTextAppearance(titleStyle);
		this.titleTextView.setOnClickListener(this.onClickExpandListener);
		this.titleTextView.setCompoundDrawables(this.icClosed, null, this.icClosed,
				null);

	}

	public void addExpandableView(View v) {
		if (v == null) {
			return;
		}
		this.listExpandableView.add(v);
		v.setVisibility(this.isExpand || this.keepExpand ? VISIBLE : GONE);
	}

	/**
	 * @param keepExpand the keepExpand to set
	 */
	public void setKeepExpand(boolean keepExpand) {
		this.keepExpand = keepExpand;
		if (keepExpand) {
			this.titleTextView.setCompoundDrawables(null, null, null, null);
		}
	}

	public void retract() {
		if (!this.isExpand || this.keepExpand) {
			return;
		}
		this.isExpand = false;
		this.titleTextView.setCompoundDrawables(this.icClosed, null, this.icClosed,
				null);
		for (View v : this.listExpandableView) {
			if (v != this.titleTextView) {
				v.setVisibility(GONE);
			}
		}
		notifyObservers();
	}

	public void expand() {
		if (this.isExpand) {
			return;
		}
		this.isExpand = true;
		this.titleTextView.setCompoundDrawables(this.icOpened, null, this.icOpened,
				null);
		for (View v : this.listExpandableView) {
			if (v != this.titleTextView) {
				v.setVisibility(VISIBLE);
			}
		}
		notifyObservers();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		// begin boilerplate code that allows parent classes to save state
		Parcelable superState = super.onSaveInstanceState();

		CustomExpandableSavedState savedState = new CustomExpandableSavedState(
				superState);
		// end

		savedState.isExpand = this.isExpand;
		savedState.keepExpand = this.keepExpand;

		return savedState;
	}

	// Save/Restore State

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// begin boilerplate code so parent classes can restore state
		if (!(state instanceof CustomExpandableSavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		CustomExpandableSavedState savedState = (CustomExpandableSavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		// end

		boolean isExpandSaved = savedState.isExpand;
		if (isExpandSaved) {
			this.expand();
		} else {
			this.retract();
		}
		this.setKeepExpand(savedState.keepExpand);
	}

	public boolean isExpand() {
		return this.isExpand;
	}

	private final List<Observer> observers = new ArrayList<>();

	@Override
	public List<Observer> getObservers() {
		return this.observers;
	}

	protected static class CustomExpandableSavedState extends BaseSavedState {
		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<CustomExpandableSavedState> CREATOR = new Parcelable.Creator<CustomExpandableSavedState>() {
			@Override
			public CustomExpandableSavedState createFromParcel(Parcel in) {
				return new CustomExpandableSavedState(in);
			}

			@Override
			public CustomExpandableSavedState[] newArray(int size) {
				return new CustomExpandableSavedState[size];
			}
		};
		private boolean isExpand;
		private boolean keepExpand;

		CustomExpandableSavedState(Parcelable superState) {
			super(superState);
		}

		protected CustomExpandableSavedState(Parcel in) {
			super(in);
			this.isExpand = in.readByte() != 0;
			this.keepExpand = in.readByte() != 0;
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeByte((byte) (this.isExpand ? 1 : 0));
			out.writeByte((byte) (this.keepExpand ? 1 : 0));
		}
	}

}