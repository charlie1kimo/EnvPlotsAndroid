package com.example.envplotsandroid;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPickerPreference extends DialogPreference {
	private final int MAX = 60;
	private final int MIN = 1;
	private final int DEFAULT = 3;
	private int number = DEFAULT;
	private NumberPicker np;
	
	// Constructors
	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setPositiveButtonText(R.string.OK);
		this.setNegativeButtonText(R.string.CANCEL);
	}
	
	// dialog initializes View with NumberPicker widget
	@Override
	protected View onCreateDialogView() {
		np = new NumberPicker(getContext());
		return np;
	}
	
	// set the NumberPicker dialog limit
	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		
		np.setMaxValue(MAX);
		np.setMinValue(MIN);
		np.setValue(number);
	}
	
	// save the preference value when dialog closed 
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		
		number = np.getValue();
		if (positiveResult) {
			persistInt(number);
			setSummary(getSummary());
		}
	}
	
	// provide the initial values
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {	// restore existing state
			number = getPersistedInt(DEFAULT);
		}
		else {							// set the default state from the XML attribute
			number = (Integer)defaultValue;
			persistInt(number);
		}
		setSummary(getSummary());
	}
	
	// provide the summary for the widget
	@Override
	public CharSequence getSummary() {
		return (CharSequence)Integer.toString(number);
	}

}
