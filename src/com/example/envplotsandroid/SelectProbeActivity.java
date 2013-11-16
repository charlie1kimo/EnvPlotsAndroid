package com.example.envplotsandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class SelectProbeActivity extends Activity {
	ExpandableListAdapter listAdapter;
	ExpandableListView expandableListView;
	List<String> probeGroups;
	HashMap<String, List<String>> probeGroupChild;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_probe);
		setupActionBar();		// Show the Up button in the action bar.
		
		// get the listView
		expandableListView = (ExpandableListView)this.findViewById(R.id.expandable_list_view_select_probe_activity);
		
		// prepare the list data
		prepareListData();
		listAdapter = new ProbeGroupExpandableListAdapter(this, probeGroups, probeGroupChild);
		
		// setting list adapter
		expandableListView.setAdapter(listAdapter);
		// ListView Group click listener
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPos, long id) {
				// we are not doing anything here with the click on the group.
				return false;
			}
		});
		// ListView Group expanded listener
		expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			// show a little toast notification when group expanded.
			@Override
			public void onGroupExpand(int groupPos) {
				Toast.makeText(getApplicationContext(), 
						"Group \""+probeGroups.get(groupPos)+"\" opened",
						Toast.LENGTH_SHORT).show();
			}
		});
		// ListView Group collapsed listener
		expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			// show a little toast notification when group collapsed.
			@Override
			public void onGroupCollapse(int groupPos) {
				Toast.makeText(getApplicationContext(), 
						"Group \""+probeGroups.get(groupPos)+"\" closed",
						Toast.LENGTH_SHORT).show();
			}
		});
		// ListView on Child click listener
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			// setup action when click on the child
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPos, int childPos, long id) {
				String txtChild = probeGroupChild.get(probeGroups.get(groupPos)).get(childPos);
				Toast.makeText(getApplicationContext(), "Selected "+txtChild, Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Prepare the group list data for selecting
	 */
	private void prepareListData() {
		probeGroups = new ArrayList<String>();
		probeGroupChild = new HashMap<String, List<String>>();
		
		// testing expandable list code:
		probeGroups.add("Group1");
		probeGroups.add("Group2");
		List<String> child1 = new ArrayList<String>();
		child1.add("group 1 child 1");
		child1.add("group 1 child 2");
		child1.add("group 1 child 3");
		List<String> child2 = new ArrayList<String>();
		child2.add("group 2 child 1");
		child2.add("group 2 child 2");
		child2.add("group 2 child 3");
		probeGroupChild.put(probeGroups.get(0), child1);
		probeGroupChild.put(probeGroups.get(1), child2);
	}
}
