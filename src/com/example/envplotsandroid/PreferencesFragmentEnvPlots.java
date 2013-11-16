package com.example.envplotsandroid;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class PreferencesFragmentEnvPlots extends PreferenceFragment {
	//NumberPickerPreference dependentWidget;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //setPreferenceScreen(createPreferenceHierarchy());
        //dependentWidget.setDependency("auto_update_checkbox_preference");
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
