package com.example.envplotsandroid;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class PreferencesFragmentEnvPlots extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	//NumberPickerPreference dependentWidget;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        EditTextPreference serverAddressPref = (EditTextPreference)findPreference("server_address_preference");
        serverAddressPref.setSummary(
        		sp.getString("server_address_preference", 
        				getString(R.string.preference_server_address_default)));
        //setPreferenceScreen(createPreferenceHierarchy());
        //dependentWidget.setDependency("auto_update_checkbox_preference");
    }
    
    @Override
	public void onResume() {
    	super.onResume();
    	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    	// refresh the probe select screen upon resuming.
    	String prefScreenKey = "probe_select_screen_preference";
		PreferenceScreen ps = (PreferenceScreen)findPreference(prefScreenKey);
		SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
		ps.setSummary(sp.getString(prefScreenKey, "None"));
    }
    
    @Override
	public void onPause() {
    	super.onPause();
    	getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { 
    	Preference pref = findPreference(key);
    	if (pref instanceof EditTextPreference) {
    		EditTextPreference etp = (EditTextPreference)pref;
    		pref.setSummary(etp.getText());
    	}
    }
    
    /** DEPRECATED; WAS TESTING
     *  It proved we can use code to add preferences too.
     *  **/
    /*
    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(getActivity());
        
        // Probe preferences
        PreferenceCategory probePrefCat = new PreferenceCategory(getActivity());
        probePrefCat.setTitle(R.string.preferences_probe_title);
        root.addPreference(probePrefCat);
        // Probe select sub-screen
        PreferenceScreen probeSelectPrefScreen = getPreferenceManager().createPreferenceScreen(getActivity());
        probeSelectPrefScreen.setKey("probe_select_screen_preference");
        probeSelectPrefScreen.setTitle(R.string.preferences_probe_select_screen_title);
        probeSelectPrefScreen.setSummary(R.string.preferences_probe_select_screen_summary);
        probePrefCat.addPreference(probeSelectPrefScreen);
        
        // Plot Range preference
        PreferenceCategory plotRangePrefCat = new PreferenceCategory(getActivity());
        plotRangePrefCat.setTitle(R.string.preference_plot_range_title);
        root.addPreference(plotRangePrefCat);
        // range NumberPicker preference
        NumberPickerPreference rangeNumberPickerPref = new NumberPickerPreference(getActivity());
        rangeNumberPickerPref.setKey("range_number_picker_preference");
        rangeNumberPickerPref.setTitle(R.string.preference_range_picker_title);
        plotRangePrefCat.addPreference(rangeNumberPickerPref);
        
        // Update preference category
        PreferenceCategory updatePrefCat = new PreferenceCategory(getActivity());
        updatePrefCat.setTitle(R.string.preference_update_title);
        root.addPreference(updatePrefCat);   
        // Auto Update Checkbox Preference
        CheckBoxPreference autoUpdate = new CheckBoxPreference(getActivity());
        autoUpdate.setKey("auto_update_checkbox_preference");
        autoUpdate.setTitle(R.string.preference_auto_update_checkbox_title);
        autoUpdate.setSummary(R.string.preference_auto_update_checkbox_summary);
        updatePrefCat.addPreference(autoUpdate);
        // Auto update interval NumberPicker preference
        NumberPickerPreference autoUpdateIntervalNumberPickerPref = new NumberPickerPreference(getActivity());
        autoUpdateIntervalNumberPickerPref.setKey("auto_update_interval_number_picker_preference");
        autoUpdateIntervalNumberPickerPref.setTitle(R.string.preference_auto_update_picker_title);
        updatePrefCat.addPreference(autoUpdateIntervalNumberPickerPref);
        dependentWidget = autoUpdateIntervalNumberPickerPref;

        return root;
    }
    */
}
