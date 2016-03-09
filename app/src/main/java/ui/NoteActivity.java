package ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baseDao.Areas;
import com.baseDao.AreasDao;
import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;

public class NoteActivity extends Activity implements OnClickListener {
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e("state", "onStart");
		loadData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("state", "onResume");
		loadData();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private TextView txtUserName;// 记录当前登录用户
	private String setting_userName = "setting_userName";// 共享文件名
	private String shared_userName = "shared_userName";// 共享文件存储变量
	// private ImageButton ibgBtnDelAll;// 删除当前用户全部信息
	public ListView lvTitle;// 显示当前用户的记事ID、日期、标题，单击某一行查看详细信息，长按可修改和删除该行信息
	private ImageButton ibgBtnExit;
	public MyAdapter mAdapter;
	SparseArray<Areas> mData,mallDatas;
	AreasDao notesDao;
	public int Id = 0;
	public int curLevel = 0;

	TextView menu_btn_new, menu_btn_setting;
	EditText et_search;
	TextView main_title;
	ImageView img_search;
	private PopupWindow popupwindow;
	PagerAdapter mPagerAdapter;
	View view1, view2, view3;
	private ViewPager mTabPager;
	TextView mTab1,mTab2,mTab3;
	ListView notesortList;
	public static NoteActivity noteActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题

		setContentView(R.layout.activity_main);
		noteActivity=this;
		//谈软键盘
		final InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

		MyApplication myApplication = (MyApplication) getApplication();
		SqlHelper helper=myApplication.getSqlHelper();
		notesDao = new AreasDao(helper);
		// 新建笔记or文件夹
//		menu_btn_new = (TextView) findViewById(R.id.menu_btn_new);
//		menu_btn_setting = (TextView) findViewById(R.id.menu_btn_setting);
//		et_search= (EditText) findViewById(R.id.et_search);
//		main_title = (TextView) findViewById(R.id.main_title);
//		img_search = (ImageView) findViewById(R.id.img_search);
//
//		mTabPager = (ViewPager) findViewById(R.id.tpager);
//		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
//
//		mTab1 = (TextView) findViewById(R.id.menu_btn_notes);
//		mTab2 = (TextView) findViewById(R.id.menu_btn_sort);
//		mTab3 = (TextView) findViewById(R.id.menu_btn_search);
		
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//搜索
				main_title.setVisibility(View.GONE);
				et_search.setVisibility(View.VISIBLE);
				et_search.setFocusable(true);
				et_search.setFocusableInTouchMode(true);
                et_search.requestFocus();
                et_search.requestFocusFromTouch();
				img_search.setVisibility(View.VISIBLE);
				//谈软键盘
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
			}
		});

	img_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//搜索查询
			String note_search=	et_search.getText().toString();
			
			mData=notesDao.queryToList("name like ?", new String[] {"%"+note_search+"%" });//模糊查询
			
			lvTitle.setAdapter(mAdapter);
			
			}
		});
				
				
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.activity_menu_layout, null);
		view2 = mLi.inflate(R.layout.acitivity_classification, null);
//		view3 = mLi.inflate(R.layout.activity_menu_layout, null);
		
		LinearLayout linre = (LinearLayout) view1
				.findViewById(R.id.content_layout);
		LayoutInflater inflater = getLayoutInflater();
//		View convertView = inflater.inflate(R.layout.notebook, null);
//		linre.addView(convertView);
//		lvTitle = (ListView) view1.findViewById(R.id.note_lvTitle);

		mAdapter = new MyAdapter(getApplicationContext());
		lvTitle.setAdapter(mAdapter);
		
		RelativeLayout sortcontent_layout= (RelativeLayout) view2
				.findViewById(R.id.sortcontent_layout);
		notesortList= (ListView) view2
				.findViewById(R.id.notesortList);

		getView(0);
		// 每个页面的view数据
		final List<View> views = new ArrayList<View>();
		if (view1 != null) {
			views.add(view1);
		}
		if (view2 != null) {
			views.add(view2);
		}
		// 填充ViewPager的数据适配器
		mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);

		Bundle extras = getIntent().getExtras();
		SharedPreferences settings = getSharedPreferences(setting_userName, 0);// 共享当前登陆用户

		
		mData = notesDao.queryToList("parentid=?", new String[] { 0 + "" });

		if (mData == null) {
			mData = new SparseArray<Areas>();
		}
		if (extras != null) {
			settings.edit()
					.putString(shared_userName, extras.getString("uname"))
					.commit();
		} else {
			// setAlertDialog("警告","获取不到当前用户名");
		}
		settings.getString(shared_userName, "");
		Log.e(setting_userName, settings.getString(shared_userName, ""));
		menu_btn_new.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹菜单
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					return;
				} else {
					initmPopupWindowView();
					popupwindow.showAsDropDown(v, 0, 5);
				}

			}
		});

		menu_btn_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转至设置界面
				Intent int_set = new Intent(NoteActivity.this,
						SettingActivity.class);
				startActivity(int_set);

			}
		});

		
		// 长按标题弹出修改信息活动窗体
		lvTitle.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Areas dataSet = mData.get(position);
				if (dataSet.area.equals("types")) {
					// 弹出删除修改目录对话框
					Builder builder = new Builder(NoteActivity.this);
					builder.setTitle("选择您的操作");
					builder.show();
				} else {
					// 弹出删除备忘录对话框
					Builder builder = new Builder(NoteActivity.this);
					builder.setTitle("温馨提示：");
					builder.setMessage("确实要删除当前备忘录么？");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									try {
//									String userName= IndexActivity.sharedPreferences_userInfo.getString("userName", "");

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
									notesDao.delete(dataSet);
									dialog.dismiss();
									loadData();
								}
							});
					builder.show();
				}
				return false;
			}
		});
		lvTitle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Areas dataSet = mData.get(position);

//				if (dataSet.types.equals("types")) {
//					Id = dataSet.id;
//					curLevel += 1;
//					loadData();
//
//				} else if (dataSet.types.equals("note")) {
//
//					intent.putExtra("edit", 1);// 0添加、1修改
//					intent.putExtra("Id", dataSet.id);
//					intent.putExtra("level", curLevel);
//					startActivity(intent);
//				}

			}

		});

	}

	View getView(int i) {
		View viewd = null;
		switch (i) {
		case 0:
			mData = notesDao.queryToList("parentid=?", new String[] { 0 + "" });
			mAdapter = new MyAdapter(getApplicationContext());
			lvTitle.setAdapter(mAdapter);
			break;
		case 1:

			break;

		case 2:
			break;
		case 3:

			break;
		}
		return viewd;

	}

	/*
	 * 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			getView(arg0);
			Animation animation = null;
			switch (arg0) {
			case 0:
				getView(0);
				break;
			case 1:
				getView(1);
				break;
			case 2:

				break;
			case 3:

				break;

			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
			
			main_title.setVisibility(View.VISIBLE);
			et_search.setVisibility(View.GONE);
			img_search.setVisibility(View.GONE);

		}
	};

	// 删除当前用户的全部信息
	class DelAllListener implements OnClickListener {
		public void onClick(View v) {
			if (lvTitle.getCount() == 0) {
				setAlertDialog("温馨提示！", "当前用户没有可删除的信息！");
			} else {
				setDialogActivityDelete("警告！", "确认删除当前用户的全部信息吗？");
			}
		}
	}

	// 退出应用程序
	class ExitListener implements OnClickListener {
		public void onClick(View v) {
			setDialogActivityExit("温馨提示！", "您确认退出程序吗？");
		}
	}

	public void initmPopupWindowView() {
		
		Display display = getWindowManager().getDefaultDisplay();  
	    int width = display.getWidth();  
		int height = display.getHeight();

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item,
				null, false);
		
		LinearLayout linear_pop=(LinearLayout)customView. findViewById(R.id.linear_pop);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);  
        params.width = width/5*2; 
        params.height = width/10*3;  
        linear_pop.setLayoutParams(params);  
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupwindow = new PopupWindow(customView, width/5*2, width/10*3);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
		popupwindow.setAnimationStyle(R.style.AnimationFade);
		// 自定义view添加触摸事件
//		customView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//				if (popupwindow != null && popupwindow.isShowing()) {
//					popupwindow.dismiss();
//					popupwindow = null;
//				}
//				return false;
//			}
//		});

		/** 在这里可以实现自定义视图的功能 */
		TextView txt_addfolder = (TextView) customView
				.findViewById(R.id.txt_addfolder);
		TextView txt_addfile = (TextView) customView
				.findViewById(R.id.txt_addfile);
		txt_addfolder.setOnClickListener(this);
		txt_addfile.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.txt_addfolder: // 新增文件夹

			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				popupwindow = null;
			}

			final EditText dirext = new EditText(NoteActivity.this);

			Builder aa = new Builder(NoteActivity.this);
			aa.setTitle("请输入文件夹名称");
			aa.setIcon(android.R.drawable.ic_dialog_info);
			aa.setView(dirext);
			aa.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			aa.setNegativeButton("取消", null);
			aa.setCancelable(true);
			aa.show();
			break;
		case R.id.txt_addfile: // 新增文件

			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				popupwindow = null;
			}


			break;
		default:
			break;
		}

	}

	// 弹出警告对话框
	public void setAlertDialog(String title, String message) {
		Builder builder = new Builder(NoteActivity.this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.show();
	}

	// 判断是否删除当前用户全部信息
	public void setDialogActivityDelete(String title, String message) {
		// Button button=new Button(Register.this);
		Builder builder = new Builder(NoteActivity.this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

//				SQLiteDatabase db = IndexActivity.helper.getWritableDatabase();
//				db.delete("user", "name=?", new String[] { txtUserName
//						.getText().toString().trim() });
//				db.close();
//				setAlertDialog("温馨提示！", "删除当前用户全部信息成功！");
//				onCreate(null);

			}

		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();

	}

	public void loadData() {
		mData = notesDao.queryToList("parentid=? ", new String[] { Id + "" });
		if (mData == null) {
			mData = new SparseArray<Areas>();
		}
		mAdapter.notifyDataSetChanged();
	}

	// 自定义弹出对话框并选择是否退出程序
	public void setDialogActivityExit(String title, String message) {
		// Button button=new Button(Register.this);
		Builder builder = new Builder(NoteActivity.this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// dialog.dismiss();
				NoteActivity.this.finish();
			}

		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();

	}

	boolean quit = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Do something.
			if (curLevel > 0) {
				curLevel -= 1;
				return true;
			} else if (curLevel == 0) {
				if (quit) {
					android.os.Process.killProcess(android.os.Process.myPid());
				} else {

					Toast.makeText(NoteActivity.this, "再点一下将退出程序",
							Toast.LENGTH_SHORT).show();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							quit = false;
						}
					}, 5 * 1000);
					quit = true;
				}
				return true;
			}
		}
		return true;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = inflater.inflate(R.layout.main_list_row, null, false);
			ImageView img = (ImageView) convertView.findViewById(R.id.ico);// 用于显示图片
			TextView tv = (TextView) convertView.findViewById(R.id.context);// 显示文字
			final Areas dataSet = mData.get(position);
			tv.setText(dataSet.area);
			if (dataSet.area.equals("types")) {
				img.setImageResource(R.drawable.main_allnote);
			} else if (dataSet.area.equals("note")) {
				img.setImageResource(R.drawable.main_newnote);
			}
			return convertView;

		}

		private int selectItem = -1;

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		@Override
		public int getCount() {
			if(mData!=null){
				return mData.size();
			}else{
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	public void deleteNote(int id) {
		SparseArray<Areas> notes_sa = new SparseArray<Areas>();
		notes_sa = notesDao.queryToList("parentid=?", new String[] { id + "" });
		if (notes_sa == null) {
			return;
		}
		for (int i = 0; i < notes_sa.size(); i++) {
			Areas note = notes_sa.get(i);
//			if (note.types.equals("types")) {
//				deleteNote(note.id);
//			}
			notesDao.delete(note);
		}

	}

}
