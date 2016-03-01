package com.listview;

//import com.asset.Asset_Config;
//import com.asset.Asset_RepairHistory;
//import com.asset.Asset_RepairHistory_item;
//import com.asset.Asset_Type;
//import com.guide.Activity_TaskGuide;
//import com.guide.Activity_TaskGuideType;
//import com.person.AvDataDictionary;
//import com.task.Activity_SceneCheck;
//import com.task.Activity_repairRecord;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import ui.Activity_TowerList;

public class CHScrollView extends HorizontalScrollView {
	
	
	public ScrollChange mscrollChange = null;
	
	public interface ScrollChange {
		public void	onScrollChanged(int l, int t, int oldl, int oldt);
	}
	
	public void registerScrollChange(ScrollChange networkFlipCallback) {
		// TODO Auto-generated method stub
		mscrollChange = networkFlipCallback;
	}



	Activity_TowerList ractivity;
//	Activity_TaskGuide guidactivity;
//	Activity_TaskGuideType guidetype;
//	Asset_Config asset_Config;
//	Activity_SceneCheck activity_SceneCheck;
//	AvDataDictionary activity_DataDictionary;
//	Asset_RepairHistory_item asset_RepairHistory_item;
//	Asset_RepairHistory asset_RepairHistory;
//	Asset_Type asset_Type;
	public CHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getActivity(context);
		}

	
	public CHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getActivity(context);
	}

	public CHScrollView(Context context) {
		super(context);
		getActivity(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//进行触摸赋值
//		if(tactivity!=null){
//			tactivity.mTouchView = this;
//		}else
			if(ractivity!=null){
			ractivity.mTouchView = this;
		}

		return super.onTouchEvent(ev);
	}
	
//如果这个没有，则只会滚动当前行
	private void getActivity(Context context){

		 if(context instanceof Activity_TowerList){
			ractivity= (Activity_TowerList) context;
		}

	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//当当前的CHSCrollView被触摸时，滑动其它
		
		if(mscrollChange!=null){
			mscrollChange.onScrollChanged(l, t, oldl, oldt);
		}
		else if(ractivity!=null){
			ractivity.onScrollChanged(l, t, oldl, oldt);
		}

		else{
			super.onScrollChanged(l, t, oldl, oldt);
		}

	}
}
