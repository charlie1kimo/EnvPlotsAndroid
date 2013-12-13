package com.example.envplotsandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class SelectProbeActivity extends Activity {
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expandableListView;
	public List<String> probeGroups;
	public HashMap<String, List<String>> probeGroupChild;
	private ProgressDialog pDialog;
	// JSON related:
	private JSONParser jParser = new JSONParser();
	private final String JSON_SUCCESS = "success";
	private final String JSON_ERROR_MESSAGE = "message";
	private final String JSON_DATA = "data";
	private final String JSON_STATION_NAME = "station";
	private final String JSON_ID = "id";
	private final String JSON_ALIAS = "alias";
	// urls:
	private String url_read_stations;
	private String url_read_aliases_in_station;
	private String url_read_sensor_aliases;
	private String url_read_sensors;
	// data storage
	private HashMap<String, Integer> stations_map;
	private HashMap<String, String> aliases_map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_probe);
		setupActionBar();		// Show the Up button in the action bar.	
		
		// initialize data storage
		stations_map = new HashMap<String, Integer>();
		aliases_map = new HashMap<String, String>();
		
		// initialize the expandible list and items containers
		probeGroups = new ArrayList<String>();
		probeGroupChild = new HashMap<String, List<String>>();
		
		// setup the url for reading from the server
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String server_url = sharedPref.getString("server_address_preference", getString(R.string.preference_server_address_default));
		url_read_stations = server_url + "android_connect/read_stations.php";
		url_read_aliases_in_station = server_url + "android_connect/read_aliases_in_station.php";
		url_read_sensor_aliases = server_url + "android_connect/read_sensor_aliases.php";
		url_read_sensors = server_url + "android_connect/read_sensors.php";
		
		// get the listView
		expandableListView = (ExpandableListView)this.findViewById(R.id.expandable_list_view_select_probe_activity);
		
		// prepare the list data
		new LoadAllProbes().execute();
		
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
		/*
		expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			// show a little toast notification when group collapsed.
			@Override
			public void onGroupCollapse(int groupPos) {
				Toast.makeText(getApplicationContext(), 
						"Group \""+probeGroups.get(groupPos)+"\" closed",
						Toast.LENGTH_SHORT).show();
			}
		});
		*/
		// ListView on Child click listener
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			// setup action when click on the child
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPos, int childPos, long id) {
				String txtChild = probeGroupChild.get(probeGroups.get(groupPos)).get(childPos);
				Toast.makeText(getApplicationContext(), "Selected "+txtChild, Toast.LENGTH_SHORT).show();
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				SharedPreferences.Editor spEditor = sp.edit();
				spEditor.putString("probe_select_screen_preference", txtChild);
				spEditor.putString("probe_id_select_screen_preference", aliases_map.get(txtChild));
				spEditor.commit();
				SelectProbeActivity.this.finish();
				return true;
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
	 * Background async task to load all groups and probes alias
	 */
	private class LoadAllProbes extends AsyncTask<String, String, String> {
		
		// Show the progress dialog before the background thread
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SelectProbeActivity.this);
			pDialog.setMessage("Loading Probes. Please Wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			// setup the station name and ID map...
			// building parameters
			List<NameValuePair> params;
			JSONObject jsonStations;
			JSONObject jsonAliasesInStation;
			try {
				params = new ArrayList<NameValuePair>();
				jsonStations = jParser.makeHttpRequest(url_read_stations, "GET", params);
			// debug message for JSON response
				Log.d("JSON_Stations", jsonStations.toString());
			}
			catch (HttpHostConnectException e) {
				Toast.makeText(getApplicationContext(), "Connection Error. Offline Mode", Toast.LENGTH_SHORT).show();
				prepareListData();
				return null;
			}
			catch (Exception e) {
				e.printStackTrace();
				prepareListData();
				return null;
			}
			
			try {
				int success = jsonStations.getInt(JSON_SUCCESS);
				if (success == 1) {
					JSONArray stationsArray = jsonStations.getJSONArray(JSON_DATA);
					for (int i = 0; i < stationsArray.length(); i++) {
						JSONObject station = stationsArray.getJSONObject(i);
						String name = station.getString(JSON_STATION_NAME);
						Integer id = Integer.valueOf(station.getInt(JSON_ID));
						stations_map.put(name, id);
						probeGroups.add(name);
					}
				}
				else {
					// popup an error windows or something.
					Log.w("JSON_WARNING", jsonStations.getString(JSON_ERROR_MESSAGE));
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Connection Error. Offline Mode", Toast.LENGTH_SHORT).show();
				prepareListData();
				return null;
			}
			
			// setup the aliases in stations
			for (String key : stations_map.keySet()) {
				List<String> aliases = new ArrayList<String>();
				Integer key_id = stations_map.get(key);
				try {
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("stationId", key_id.toString()));
					jsonAliasesInStation = jParser.makeHttpRequest(url_read_aliases_in_station, "GET", params);
				// 	debug message for JSON response:
					Log.d("JSON_AliasesInStation", key+": "+jsonAliasesInStation.toString());
				}
				catch (HttpHostConnectException e) {
					Toast.makeText(getApplicationContext(), "Connection Error. Offline Mode", Toast.LENGTH_SHORT).show();
					prepareListData();
					return null;
				}
				
				try {
					int success = jsonAliasesInStation.getInt(JSON_SUCCESS);
					if (success == 1) {
						JSONArray aliasesArray = jsonAliasesInStation.getJSONArray(JSON_DATA);
						for (int i = 0; i < aliasesArray.length(); i++) {
							JSONObject jobj = aliasesArray.getJSONObject(i);
							String id = jobj.getString(JSON_ID);
							String alias = jobj.getString(JSON_ALIAS);
							aliases_map.put(alias, id);
							aliases.add(alias);
						}
					}
					else {
						Log.w("JSON_WARNING", "processing '"+key+"' ERROR!\n"+
								jsonAliasesInStation.getString(JSON_ERROR_MESSAGE));
					}					
				}
				catch (JSONException e) {
					e.printStackTrace();
					break;
				}
				// add the aliases list to the groups.
				probeGroupChild.put(key, aliases);
			}
			
			return null;
		}
		
		// dismiss the dialog after the loading is done
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			listAdapter = new ProbeGroupExpandableListAdapter(SelectProbeActivity.this, probeGroups, probeGroupChild);
			// setting list adapter
			expandableListView.setAdapter(listAdapter);
		}
		
		/**
		 * FALL BACK DEFAULT LIST:
		 * Prepare the group list data for selecting
		 */
		private void prepareListData() {			
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
}
