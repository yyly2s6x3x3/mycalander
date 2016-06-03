package com.example.clander;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeatherAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<Mode> modes=new ArrayList<Mode>();
	
	public WeatherAdapter(Context context,ArrayList<Mode> modes){
		this.context=context;
		this.modes=modes;
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView=View.inflate(context, R.layout.weather_item, null);
		TextView textView=(TextView) convertView.findViewById(R.id.tvtext);
		textView.setText(modes.get(position%modes.size()).getModename());
		return convertView;
	}

}
