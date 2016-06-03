package com.example.clander;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyCalandarView extends LinearLayout {

	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private View view=null;
	private TextView topText = null;
	private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private int startX=0,gvFlag=0,selposition=-1;

	public MyCalandarView(Context context) {
		this(context, null);
	}

	public MyCalandarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public MyCalandarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
		view = View.inflate(getContext(), R.layout.calandar_view, this);
		calV = new CalendarAdapter(getContext(), getResources(), jumpMonth, jumpYear,year_c, month_c, day_c);
		addGridView();
		gridView.setAdapter(calV);

		topText = (TextView) view.findViewById(R.id.tv_month);
		addTextToTopTextView(topText);
	}
	
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	private void addGridView() {
		
		gridView =(GridView)view.findViewById(R.id.gridview);

		gridView.setOnTouchListener(touchListener);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            

			//gridView中的每一个item的点击事件
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				  int startPosition = calV.getStartPositon();
				  
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position+7  && position <= endPosition-7){
					  scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];
					  scheduleYear = calV.getShowYear();
	                  scheduleMonth = calV.getShowMonth();
	                  calV.setSelect(scheduleYear, scheduleMonth, position);
	                  selposition=position;
	                  calV.notifyDataSetChanged();
	                  Toast.makeText(getContext(), scheduleYear+"-"+scheduleMonth+"-"+scheduleDay, Toast.LENGTH_SHORT).show();
	                }
				  }
		});
	}
	
	private String scheduleDay;
	private String scheduleYear;
	private String scheduleMonth;
	/**
	 * 得到选择的日期
	 */
	public String getSelectData(){
		return  scheduleYear+"-"+scheduleMonth+"-"+scheduleDay;
	}

	private OnTouchListener touchListener=new OnTouchListener() {
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX=(int) event.getX();
			break;
		case MotionEvent.ACTION_UP:
			if (startX - event.getX() > 120) {
	            //像左滑动
				if(gvFlag==1){
					Toast.makeText(getContext(), "不能选择下一个月了", Toast.LENGTH_SHORT).show();
					return true;
				}
				addGridView();   //添加一个gridView
				jumpMonth++;     //下一个月
				
				calV = new CalendarAdapter(getContext(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
				calV.setSelect(scheduleYear, scheduleMonth, selposition);
		        gridView.setAdapter(calV);
		        addTextToTopTextView(topText);
		        gvFlag++;
		
				return true;
			} else if (startX - event.getX() < -120) {
	            //向右滑动
				if(gvFlag==-1){
					Toast.makeText(getContext(), "不能选择上一个月了", Toast.LENGTH_SHORT).show();
					return true;
				}
				addGridView();   //添加一个gridView
				jumpMonth--;     //上一个月
				
				calV = new CalendarAdapter(getContext(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
				calV.setSelect(scheduleYear, scheduleMonth, selposition);
		        gridView.setAdapter(calV);
		        gvFlag--;
		        addTextToTopTextView(topText);

				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
};

}
