package com.example.envplotsandroid;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ProbeGroupExpandableListAdapter extends BaseExpandableListAdapter {
	private Context _context;
	private List<String> _listDataHeader;
	private HashMap<String, List<String>> _listDataChild;
	
	public ProbeGroupExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listDataChild;
	}

	@Override
	public Object getChild(int groupPos, int childPos) {
		return this._listDataChild.get(this._listDataHeader.get(groupPos)).get(childPos);
	}

	@Override
	public long getChildId(int groupPos, int childPos) {
		return childPos;
	}

	@Override
	public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView,
			ViewGroup parent) {
		final String childText = (String) getChild(groupPos, childPos);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.expandable_list_item_select_probe, null);
		}
		
		TextView txtListChild = (TextView) convertView.findViewById(R.id.text_view_expandable_list_item);
		txtListChild.setText(childText);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPos) {
		return this._listDataChild.get(this._listDataHeader.get(groupPos)).size();
	}

	@Override
	public Object getGroup(int groupPos) {
		return this._listDataHeader.get(groupPos);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPos) {
		return groupPos;
	}

	@Override
	public View getGroupView(int groupPos, boolean isExpanded, View convertView, ViewGroup parent) {
		final String headerTitle = (String) getGroup(groupPos);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.expandable_list_group_select_probe, null);
		}
		
		TextView txtListHeader = (TextView) convertView.findViewById(R.id.text_view_expandable_list_header);
		txtListHeader.setTypeface(null, Typeface.BOLD);
		txtListHeader.setText(headerTitle);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPos, int childPos) {
		// TODO Auto-generated method stub
		return true;
	}

}
