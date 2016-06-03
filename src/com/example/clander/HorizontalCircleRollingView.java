package com.example.clander;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HorizontalCircleRollingView extends LinearLayout{
	
	private List<Mode> datas=new ArrayList<Mode>();
	private List<Mode> sigModes;
	private HorizontalScrollView horizontalScrollView;
	private LinearLayout linearLayout;
	private int itemLayoutId=0;
	// 滚动条的宽度
	private int hsv_width;
	// 总共有多少个view
	private int child_count;
	// 每一个view的宽度
	private int child_width;
	// 预计显示在屏幕上的view的个数
	private int child_show_count;
	// 一开始居中选中的view
	private int child_start=2;
		
	private TextView texeview;
	private ImageView imageView;
		
	private TextView teView;
	
	private int index=0;
	private int dataSize=0;
	
	public HorizontalCircleRollingView(Context context) {
		this(context,null);
	}

	public HorizontalCircleRollingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	@SuppressLint("NewApi")
	public HorizontalCircleRollingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		View view=View.inflate(getContext(), R.layout.circlerolling_view, this);
		horizontalScrollView=(HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
	}
	
	public void setFirstIndex(int child_start){
		this.child_start=child_start+1;
		index=child_start+1;
	}
	
	public int getSelectedIndex(){
		int selectIndex=index;
		if(selectIndex>0){
			selectIndex=selectIndex-1;
		}else{
			selectIndex=dataSize-1;
		}
		return selectIndex;
	}
	
	public void setList(List<Mode> datas){
		this.datas=datas;
		dataSize=datas.size();
		child_count = datas.size(); 
		child_show_count = 5;

		int mlu=0;
		if(datas.size()>1&datas.size()<=child_show_count){
			mlu=child_show_count/datas.size()+1;
			child_count=mlu*datas.size();
		}
		sigModes=datas;
		for(int i=0;i<mlu;i++){
			datas.addAll(sigModes);
		}
		Toast.makeText(getContext(), ""+dataSize, Toast.LENGTH_SHORT).show();
	}
	
	public void setItemLayoutId(int itemLayoutId){
		this.itemLayoutId=itemLayoutId;
	}
	
	/** * 给滚动控件添加view，只有重复两个列表才能实现循环滚动 */
	private void initData() {
		if(itemLayoutId==0){
			itemLayoutId=R.layout.weather_item;
		}
		for (int i = 0; i < child_count; i++) {
			View view = View.inflate(getContext(),itemLayoutId, null);
			view.setLayoutParams(new ViewGroup.LayoutParams(child_width,ViewGroup.LayoutParams.MATCH_PARENT));
			teView = (TextView) view.findViewById(R.id.tvtext);
			teView.setText(datas.get(i).getModename());
			linearLayout.addView(view);
		}
		  
		for (int i = 0; i < child_count; i++) {
			View view = View.inflate(getContext(),itemLayoutId, null);
			view.setLayoutParams(new ViewGroup.LayoutParams(child_width, ViewGroup.LayoutParams.MATCH_PARENT));
			teView = (TextView) view.findViewById(R.id.tvtext);
			teView.setText(datas.get(i).getModename());
			linearLayout.addView(view);
		}
	}

	/** * 实现滚动的循环处理，及停止触摸时的处理 */
	private void initHsvTouch() {
		horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
			private int pre_item;
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
					index=pre_item%dataSize;
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
	@SuppressLint("ResourceAsColor") 
	private void isChecked(int item, boolean isChecked) {
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
	   
	private void setExceptSelected(int item) {
		for(int i=0;i<linearLayout.getChildCount();i++){
			if(i!=item-1){
				RelativeLayout mlLayout = (RelativeLayout) linearLayout.getChildAt(i);
				
				texeview = (TextView) mlLayout.findViewById(R.id.tvtext);
				imageView = (ImageView) mlLayout.findViewById(R.id.iv_dot);
				texeview.setTextColor(Color.BLACK);
				texeview.setTextSize(16);
				imageView.setImageResource(R.drawable.andr_news_picdots);
			}
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
				setExceptSelected(child_start_item);
				return false;
			}
		});
	}

	/** * 只有到了这个方法才能获取控件的尺寸 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		hsv_width = horizontalScrollView.getWidth();
		int child_width_temp = hsv_width / child_show_count;
		if (child_width_temp % 2 != 0) {
			child_width_temp++;
		}
		child_width = child_width_temp;
		initData();
		initHsvTouch();
		initStart();
	}
	
}
