package ui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbg.yan01.R;

public class SettingAdapter extends BaseAdapter{
	
	SettingActivity setactivity;
	LayoutInflater inflater;
	String[] settinginfo = new String[] { "用户管理",  "退出程序" ,  "同步更新"};
	
	public SettingAdapter(SettingActivity msetactivity){
		setactivity=msetactivity;
		inflater= setactivity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return settinginfo.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return settinginfo[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View cview, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		cview = inflater.inflate(R.layout.setting_row, null);
		TextView txt_settinginfoName =(TextView) cview.findViewById(R.id.txt_settinginfoName);
		txt_settinginfoName.setText(settinginfo[position]);
		ImageView  txt_settingarrow=(ImageView) cview.findViewById(R.id.txt_settingarrow);
		if(position==2){
			txt_settingarrow.setVisibility(View.GONE);
		}else{
			txt_settingarrow.setVisibility(View.VISIBLE);
		}
		
//		cview.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(position==0){//用戶管理
//					
//				}
//			}
//		});
		
		return cview;
	}

}
