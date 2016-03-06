package ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.baseDao.Ganta;
import com.baseDao.GantaDao;
import com.google.gson.reflect.TypeToken;
import com.lbg.yan01.R;
import com.listview.CHScrollView;
import com.objs.TowerInfo;

public class Activity_TowerList extends Activity {

	Button add_new;
	ImageButton b_back;
	SharedPreferences sp;
	private int selectItem = -1;
	int[] headIds = new int[] { R.id.txt_1201, R.id.txt_1202, R.id.txt_1203,
			R.id.txt_1204, R.id.txt_1205, R.id.txt_1206, R.id.txt_1207,
			R.id.txt_1208, R.id.txt_1209, R.id.txt_1210, R.id.txt_1211,
			R.id.txt_1212, R.id.txt_1213, R.id.txt_1214 };
	String[] headers = new String[] { "维修单号", "工单状态", "设备编号", "资产名称", "规格型号",
			"用途", "申请部门", "申请人", "维修类别", "详细描述", "发布日期", "发布人", "维修人员",
			"计划维修日期" };
	// 方便测试，直接写的public
	Button btn_add, btn_cailu;
	DataAdapter adapter;
	public HorizontalScrollView mTouchView;
	// 装入所有的HScrollView
	protected List<CHScrollView> mHScrollViews = new ArrayList<CHScrollView>();
	TowerInfo TowerInfo;
	private LayoutInflater mInflater;

	LinearLayout repairhead;
	private ListView mListView;
	int id;
	GantaDao gantaDao;
	SparseArray<Ganta> areaslist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题（尽量在前面设置）
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Bundle bundle = this.getIntent().getExtras();
        /* 获取Bundle中的数据，注意类型和key */
		if (bundle != null) {
			id = bundle.getInt("id");
			if(id==0){
//				showMsg("aaa");
			}else{
//				showMsg("aaa");
			}
		}

		setContentView(R.layout.lay_tower_list);

		TextView title_text = (TextView) findViewById(R.id.title_text);

		title_text.setText("塔杆列表");
		ImageButton b_back = (ImageButton) findViewById(R.id.b_back);
		b_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_cailu = (Button) findViewById(R.id.btn_cailu);

		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (selectItem == -1) {
					new AlertDialog.Builder(Activity_TowerList.this)
							.setTitle("温馨提示")
							.setMessage("请选择一行!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											return;
										}
									}).create().show();
					return;
				}
				String cityname = TowerInfo.rows.get(selectItem).cityname;
				String assetCode = TowerInfo.rows.get(selectItem).assetCode;
				String content = TowerInfo.rows.get(selectItem).content;
				String assetName = TowerInfo.rows.get(selectItem).assetName;
				Bundle bundle = new Bundle();
				/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
				bundle.putString("cityname", cityname);
				bundle.putString("assetCode", assetCode);
				bundle.putString("content", content);
				bundle.putString("assetName", assetName);
				bundle.putInt("maintainType",
						TowerInfo.rows.get(selectItem).maintainType);
				/* 把bundle对象assign给Intent */
//				intent.putExtras(bundle);
//				startActivityForResult(intent, 1);
				// startActivity(intent);
			}
		});

		btn_cailu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (selectItem == -1) {
					new AlertDialog.Builder(Activity_TowerList.this)
							.setTitle("温馨提示")
							.setMessage("请选择一行!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											return;
										}
									}).create().show();
					return;
				}
				Intent intent = null;
				if (TowerInfo.rows.size() > selectItem) {
					String cityname = TowerInfo.rows.get(selectItem).cityname;
					String assetCode = TowerInfo.rows.get(selectItem).assetCode;
					String assetName = TowerInfo.rows.get(selectItem).assetName;
					Bundle bundle = new Bundle();

					String content = TowerInfo.rows.get(selectItem).content;
					/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
					bundle.putString("cityname", cityname);
					bundle.putString("content", content);
					/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
					bundle.putString("assetCode", assetCode);
					bundle.putString("assetName", assetName);
					bundle.putString("department",
							TowerInfo.rows.get(selectItem).department);
					bundle.putString("issuer",
							TowerInfo.rows.get(selectItem).issuer);

					bundle.putInt("maintainType",
							TowerInfo.rows.get(selectItem).maintainType);
					/* 把bundle对象assign给Intent */
					intent.putExtras(bundle);
					// startActivity(intent);
					startActivityForResult(intent, 1);
				}

			}
		});

		for (int i = 0; i < headers.length; i++) {
			TextView tmpView = ((TextView) findViewById(headIds[i]));
			tmpView.setText(headers[i]);
		}

		repairhead = (LinearLayout) findViewById(R.id.line_head);
		CHScrollView headerScroll = (CHScrollView) findViewById(R.id.repairheader);

		headerScroll.registerScrollChange(new CHScrollView.ScrollChange() {

			@Override
			public void onScrollChanged(int l, int t, int oldl, int oldt) {
				// TODO Auto-generated method stub
				for (CHScrollView mscrollView : mHScrollViews) {
					// 防止重复滑动
					if (mTouchView != mscrollView)
						mscrollView.smoothScrollTo(l, t);
				}
			}
		});

		// item_line.setVisibility(View.GONE);
		// 添加头滑动事件
		mHScrollViews.add(headerScroll);
		mListView = (ListView) findViewById(R.id.scroll_list);

		if (TowerInfo == null) {
			// mListView.setVisibility(View.GONE);
			TowerInfo = new TowerInfo();
			 for (int i = 0; i < 30; i++) {
			 TowerInfo.Rows rows = TowerInfo.new Rows();
			 rows.cityname = "" + i;
			 rows.assetCode = "code" + i;
			 rows.assetName = "name" + i;
			 rows.maintainPerson = "weixiuren" + i;
			 rows.maintainDate = "release_date" + i;
			 rows.department = "department" + i;
			 rows.maintainType = i;
			 rows.person = "person" + i;
			 rows.assetSize = i + "";
			 rows.maintainType = i;
			 rows.content = i + "content";
			 rows.issuer = i + "issuer";
			 rows.orderState=i+"";
			 TowerInfo.rows.add(rows);
			 }
		}
		adapter = new DataAdapter();

		mListView.setAdapter(adapter);
//		downData();
		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// mListView = (ListView) this.findViewById(R.id.repair_listview);
		b_back = (ImageButton) findViewById(R.id.b_back);
		b_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳转到主界面
				finish();
			}
		});

	}
	public void geiDatas(){
		areaslist = gantaDao.queryToList("", null);
		if (areaslist == null) {
			// mListView.setVisibility(View.GONE);
			areaslist = new SparseArray<Ganta>();
			for (int i = 0; i < 15; i++) {
				Ganta rows = new Ganta();
				rows.caizhi=1;
				rows.danwei = i + "content";
				rows.pictatou=i+"";
				areaslist.put(areaslist.size(),rows);
			}
		}
	}
	Dialog mdlg;
	@SuppressLint("HandlerLeak")
	private Handler mMsgReciver = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (mdlg != null) {
					mdlg.dismiss();
					mdlg = null;
					java.lang.reflect.Type type = new TypeToken<TowerInfo>() {
					}.getType();

					List<TowerInfo.Rows> mqList = TowerInfo.rows;
					Iterator iter = mqList.iterator();
					while (iter.hasNext()) {

						TowerInfo.Rows s = (TowerInfo.Rows) iter.next();
					}
					List<String> maintainNums = new ArrayList<String>();

					for (TowerInfo.Rows element : TowerInfo.rows) {
						maintainNums.add(element.maintainNum);
					}
//					MainActivity.maintaincodes = (String[]) maintainNums
//							.toArray(new String[maintainNums.size()]);
//					adapter.notifyDataSetInvalidated();
				}
				break;
			case 2:
				if (mdlg != null) {
					mdlg.dismiss();
					mdlg = null;
					new AlertDialog.Builder(Activity_TowerList.this)
							.setTitle("温馨提示")
							.setMessage("网络超时了!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											return;
										}
									}).create().show();
				}
				break;
			}
		}
	};

	public void onScrollChanged (int l, int t, int oldl, int oldt) {
		for (CHScrollView scrollView : mHScrollViews) {
			// 防止重复滑动
			if (mTouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
	}

	public void addHViews(int position, final CHScrollView hScrollView) {
		int size = 0;
		if (!mHScrollViews.isEmpty()) {
			size = mHScrollViews.size();
			CHScrollView scrollView = mHScrollViews.get(size - 1);
			final int scrollX = scrollView.getScrollX();
			// 第一次满屏后，向下滑动，有一条数据在开始时未加入
			if (scrollX != 0) {
				mListView.post(new Runnable() {
					@Override
					public void run() {
						// 当listView刷新完成之后，把该条移动到最终位置
						hScrollView.scrollTo(scrollX, 0);
					}
				});
			}
		}
		if (size >= position) {
			mHScrollViews.add(hScrollView);
		} else {
			mHScrollViews.set(position, hScrollView);
		}
	}

	private class DataAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return TowerInfo.rows.size();
		}
		public void setSelectItem(int tselectItem) {
			selectItem = tselectItem;
		}
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lay_tower_row, null);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setSelectItem(position); // 自定义的变量，以便让adapter知道要选中哪一项
					TowerInfo.Rows tmpTowerInfo = TowerInfo.rows.get(position);
					if (tmpTowerInfo.orderState.equals("1")) {
						// holder.orderState.setText("发布");
						btn_cailu.setEnabled(false);
						btn_add.setEnabled(true);
					} else if (tmpTowerInfo.orderState.equals("2")
							|| tmpTowerInfo.orderState.equals("4")) {
						// holder.orderState.setText("维修申请  ");
						btn_cailu.setEnabled(false);
						btn_add.setEnabled(false);
					} else if (tmpTowerInfo.orderState.equals("3")) {

						btn_cailu.setEnabled(true);
						btn_add.setEnabled(false);
						// holder.orderState.setText("审批通过");
					}
					notifyDataSetChanged();// 提醒数据已经变动

					// mListView.setSelection(scrolledY);
					// mListView.scrollTo(scrolledX, scrolledY);
				}
			});
			// }
			if (position == 0 && selectItem == -1) {
				convertView.setBackgroundColor(Color.WHITE);
			} else if (position == selectItem) {
				convertView.setBackgroundColor(Color.YELLOW);
			} else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
			final ViewHolder holder = new ViewHolder();
			holder.maintainNum = (TextView) convertView
					.findViewById(R.id.maintainNum);
			// holder.maintainNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.assetCode = (TextView) convertView
					.findViewById(R.id.assetCode);
			holder.assetName = (TextView) convertView
					.findViewById(R.id.assetName);
			holder.assetSize = (TextView) convertView
					.findViewById(R.id.assetSize);
			holder.yt = (TextView) convertView.findViewById(R.id.yt);
			holder.department = (TextView) convertView
					.findViewById(R.id.department);
			holder.person = (TextView) convertView.findViewById(R.id.person);
			holder.maintainType = (TextView) convertView
					.findViewById(R.id.maintainType);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.applyDate = (TextView) convertView
					.findViewById(R.id.applyDate);
			holder.issuer = (TextView) convertView.findViewById(R.id.issuer);
			holder.maintainPerson = (TextView) convertView
					.findViewById(R.id.maintainPerson);
			holder.maintainDate = (TextView) convertView
					.findViewById(R.id.maintainDate);
			holder.orderState = (TextView) convertView
					.findViewById(R.id.orderState);
			final TowerInfo.Rows tmpTowerInfo = TowerInfo.rows.get(position);

			if (tmpTowerInfo.orderState.equals("1")) {
				holder.orderState.setText("发布");
			} else if (tmpTowerInfo.orderState.equals("2")) {
				holder.orderState.setText("维修申请  ");
			} else if (tmpTowerInfo.orderState.equals("3")) {
				holder.orderState.setText("审批通过");
			} else if (tmpTowerInfo.orderState.equals("4")) {
				holder.orderState.setText("维修完成");
			}
			// holder.maintainNum.setText(Html.fromHtml("<u>"+tmpTowerInfo.maintainNum+"</u>"));
			holder.assetCode.setText(tmpTowerInfo.assetCode);
			holder.assetName.setText(tmpTowerInfo.assetName);
			holder.assetSize.setText(tmpTowerInfo.assetSize);
			holder.department.setText(tmpTowerInfo.department);
			holder.person.setText(tmpTowerInfo.person);
			if (tmpTowerInfo.maintainType.toString().endsWith("1")) {
				holder.maintainType.setText("设备升级");
			} else if (tmpTowerInfo.maintainType.toString().endsWith("2")) {
				holder.maintainType.setText("设备维修");
			} else if (tmpTowerInfo.maintainType.toString().endsWith("3")) {
				holder.maintainType.setText("重做系统");
			}

			holder.content.setText(tmpTowerInfo.content);
			holder.issuer.setText(tmpTowerInfo.issuer);
			holder.maintainPerson.setText(tmpTowerInfo.maintainPerson);
			holder.maintainDate.setText(tmpTowerInfo.maintainDate);

			convertView.setTag(holder);

			CHScrollView ascrollView = (CHScrollView) convertView
					.findViewById(R.id.item_scroll);
			ascrollView.registerScrollChange(new CHScrollView.ScrollChange() {

				@Override
				public void onScrollChanged(int l, int t, int oldl, int oldt) {
					// TODO Auto-generated method stub
					for (CHScrollView mscrollView : mHScrollViews) {
						// 防止重复滑动
//						if (mTouchView != mscrollView)
							mscrollView.smoothScrollTo(l, t);
					}
				}
			});
			addHViews(position, ascrollView);
			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作

		if (1 == requestCode) {
//			downData();

		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// land do nothing is ok
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// port do nothing is ok
		}
	}

	private class ViewHolder {
		TextView maintainNum;
		TextView assetCode;
		TextView assetName;
		TextView assetSize;
		TextView yt;
		TextView department;
		TextView person;
		TextView maintainType;
		TextView content;
		TextView applyDate;
		TextView issuer;
		TextView maintainPerson;
		TextView maintainDate;
		TextView orderState;
	}
}
