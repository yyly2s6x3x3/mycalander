package com.example.clander;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 日历显示activity
 * 
 *
 */
public class CalendarActivity extends Activity {

	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView topText = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private Bundle bd=null;//发送参数
	private Bundle bun=null;//接收参数
	private String ruzhuTime;
	private String lidianTime;
	private String state="";
	private int startX=0,gvFlag=0;
//	private HorizontalScrollView weatherListView;
	private WeatherAdapter weatherAdapter;
	private ArrayList<Mode> weatherModes=new ArrayList<Mode>();
	
	
//	private HorizontalCircleRollingView hcrv1;
	
	public CalendarActivity() {

		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		
		bd=new Bundle();//out
		bun=getIntent().getExtras();//in
		
		
		  if(bun!=null&&bun.getString("state").equals("ruzhu"))
          {
          	state=bun.getString("state");
          }else if(bun!=null&&bun.getString("state").equals("lidian")){
          	
          	state=bun.getString("state");
          }
		
//		bd=new Bundle();
        /*calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        addGridView();
        gridView.setAdapter(calV);
        
		topText = (TextView) findViewById(R.id.tv_month);
		addTextToTopTextView(topText);*/
		MyCalandarView calandarView=(MyCalandarView) findViewById(R.id.calandarview);

		//weatherListView=(HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		
		weatherModes.add(new Mode("1","晴天"));
		weatherModes.add(new Mode("2","雨天"));
		weatherModes.add(new Mode("3","运动会"));
		weatherModes.add(new Mode("4","大考试")); 
		
		//weatherAdapter=new WeatherAdapter(this, weatherModes);
		//weatherListView.setAdapter(weatherAdapter);
		
		//mCycleScrollView = ((CycleScrollView<PackageInfo>) this.findViewById(R.id.cycle_scroll_view));  
		  
        /** 
         * Get APP list and sort by update time. 
         */  
//        List<PackageInfo> list = this.getPackageManager()  
//                .getInstalledPackages(0);  
  
//        mAdapter = new AppCycleScrollAdapter(list, mCycleScrollView, this); 
		//setModeData(weatherModes);
		//init(); 
//		hcrv1=(HorizontalCircleRollingView) findViewById(R.id.hcrv_weather);
//		hcrv1.setItemLayoutId(R.layout.weather_item);
//		hcrv1.setFirstIndex(1);
//		hcrv1.setList(weatherModes);
	}
	

	//添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	//添加gridview
	private void addGridView() {
		
		gridView =(GridView)findViewById(R.id.gridview);

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
	                  calV.setSelectionPosition(position);
	                  
	                  calV.notifyDataSetChanged();
	                  Toast.makeText(CalendarActivity.this, scheduleYear+"-"+scheduleMonth+"-"+scheduleDay, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(CalendarActivity.this, "不能选择下一个月了", Toast.LENGTH_SHORT).show();
						return true;
					}
					addGridView();   //添加一个gridView
					jumpMonth++;     //下一个月
					
					calV = new CalendarAdapter(CalendarActivity.this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
			        gridView.setAdapter(calV);
			        addTextToTopTextView(topText);
			        gvFlag++;
			
					return true;
				} else if (startX - event.getX() < -120) {
		            //向右滑动
					if(gvFlag==-1){
						Toast.makeText(CalendarActivity.this, "不能选择上一个月了", Toast.LENGTH_SHORT).show();
						return true;
					}
					addGridView();   //添加一个gridView
					jumpMonth--;     //上一个月
					
					calV = new CalendarAdapter(CalendarActivity.this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
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
	
	//TODO
	//水平滑动控件
	private HorizontalScrollView horizontalScrollView;
	private LinearLayout linearLayout;
	// 滚动条的宽度
	private int hsv_width;
	// 总共有多少个view
	private int child_count;
	// 每一个view的宽度
	private int child_width;
	// 预计显示在屏幕上的view的个数
	private int child_show_count;
	// 一开始居中选中的view
	private int child_start;
	
	private TextView texeview;
	private ImageView imageView;
	
	private List<Mode> datas;
	private TextView teView;

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}*/

	/**
	 * 初始化值  设置选择模式
	 */
	private void setModeData(List<Mode> datas) {
		this.datas = datas;
	}
	
	/**
	 * 得到选择的模式id
	 */
	
	/** * 初始化控件及变量 */
	private void init() {
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		
		
		child_count = datas.size(); 
		child_show_count = 5;
		child_start = 2;

		int mlu=0;
		if(datas.size()>1&datas.size()<=child_show_count){
			mlu=child_show_count/datas.size()+1;
			child_count=mlu*datas.size();
		}
		List<Mode> sigModes=datas;
		for(int i=0;i<mlu;i++){
			datas.addAll(sigModes);
		}
	}

	/** * 给滚动控件添加view，只有重复两个列表才能实现循环滚动 */
	private void initData() {
		for (int i = 0; i < child_count; i++) {
			/*TextView textView = new TextView(this);
			textView.setLayoutParams(new ViewGroup.LayoutParams(child_width,
					ViewGroup.LayoutParams.MATCH_PARENT));
			textView.setText("" + (i + 1));
			textView.setGravity(Gravity.CENTER);
			linearLayout.addView(textView);*/
			
			View view = getLayoutInflater().inflate(R.layout.weather_item, null);
			view.setLayoutParams(new ViewGroup.LayoutParams(child_width, 
					ViewGroup.LayoutParams.MATCH_PARENT));
			teView = (TextView) view.findViewById(R.id.tvtext);
			teView.setText(datas.get(i).getModename());
			linearLayout.addView(view);
		}
		  
		for (int i = 0; i < child_count; i++) {
			/*TextView textView = new TextView(this);
			textView.setLayoutParams(new ViewGroup.LayoutParams(child_width,
					ViewGroup.LayoutParams.MATCH_PARENT));
			textView.setText("" + (i + 1));
			textView.setGravity(Gravity.CENTER);
			linearLayout.addView(textView);*/
			
			View view = getLayoutInflater().inflate(R.layout.weather_item, null);
			view.setLayoutParams(new ViewGroup.LayoutParams(child_width, 
					ViewGroup.LayoutParams.MATCH_PARENT));
			teView = (TextView) view.findViewById(R.id.tvtext);
			teView.setText(datas.get(i).getModename());
			linearLayout.addView(view);
		}
	}

	/** * 实现滚动的循环处理，及停止触摸时的处理 */
	private void initHsvTouch() {
		horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
			private int pre_item;
			private int selectPosition;
			private String modeNameString;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean flag = false;
				int x = horizontalScrollView.getScrollX();
				int current_item = (x + hsv_width / 2) / child_width + 1;
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					flag = false;
					if (x <= child_width) {
						horizontalScrollView.scrollBy(child_width * child_count, 0);
						current_item += child_count;
					} else if (x >= (child_width * child_count * 2 - hsv_width - child_width)) {
						horizontalScrollView.scrollBy(-child_width* child_count, 0);
						current_item -= child_count;
					}
					break;
				case MotionEvent.ACTION_UP:
					flag = true;
					horizontalScrollView.smoothScrollTo(child_width* current_item - child_width / 2 - hsv_width / 2,
							horizontalScrollView.getScrollY());
					selectPosition = current_item % child_count;
					
					Toast.makeText(CalendarActivity.this,
							"" + selectPosition,
							Toast.LENGTH_LONG).show();
					 
					
					break;
				}
				if (pre_item == 0) {
					isChecked(current_item, true);
				} else if (pre_item != current_item) {
					isChecked(pre_item, false);
					isChecked(current_item, true);
				}
				pre_item = current_item;
				return flag;
			}
		});
	}

	/**
	 * * 设置指定位置的状态 * *
	 * 
	 * @param item
	 *            *
	 * @param isChecked
	 * */
	@SuppressLint("ResourceAsColor") private void isChecked(int item, boolean isChecked) {
		RelativeLayout mlLayout = (RelativeLayout) linearLayout.getChildAt(item-1);
		
		texeview = (TextView) mlLayout.findViewById(R.id.tvtext);
		imageView = (ImageView) mlLayout.findViewById(R.id.iv_dot);
			
		
		if (isChecked) {
			texeview.setTextColor(Color.rgb(255, 136, 22));
			texeview.setTextSize(18);
			imageView.setImageResource(R.drawable.icon_dot_orange);
		} else {
			texeview.setTextColor(Color.BLACK);
			texeview.setTextSize(16);
			imageView.setImageResource(R.drawable.andr_news_picdots);
		}
	} 

	/** * 刚开始进入界面时的初始选中项的处理 */
	private void initStart() {
		final ViewTreeObserver observer = horizontalScrollView
				.getViewTreeObserver();
		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				observer.removeOnPreDrawListener(this);
				int child_start_item = child_start;
				if ((child_start * child_width - child_width / 2 - hsv_width / 2) <= child_width) {
					child_start_item += child_count;
				}
				horizontalScrollView.scrollTo(child_width * child_start_item
						- child_width / 2 - hsv_width / 2,
						horizontalScrollView.getScrollY());
				isChecked(child_start_item, true);
				return false;
			}
		});
	}

//	/** * 只有到了这个方法才能获取控件的尺寸 */
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		super.onWindowFocusChanged(hasFocus);
//		hsv_width = horizontalScrollView.getWidth();
//		int child_width_temp = hsv_width / child_show_count;
//		if (child_width_temp % 2 != 0) {
//			child_width_temp++;
//		}
//		child_width = child_width_temp;
//		initData();
//		initHsvTouch();
//		initStart();
//	}

	
}