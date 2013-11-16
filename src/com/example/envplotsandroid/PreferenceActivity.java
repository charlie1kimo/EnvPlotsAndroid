package com.example.envplotsandroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PreferenceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                                .beginTransaction();
        PreferencesFragmentEnvPlots frag = new PreferencesFragmentEnvPlots();
        mFragmentTransaction.replace(android.R.id.content, frag);
        mFragmentTransaction.commit();
    }
}
