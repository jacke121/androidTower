package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baseDao.Areas;
import com.baseDao.AreasDao;
import com.baseDao.SqlHelper;
import com.google.gson.Gson;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;
import com.listview.CHScrollView;
//import com.objs.AreaInfo;

import java.util.ArrayList;
import java.util.List;

public class Activity_AreaList extends Activity {
	Button add_new;
	ImageButton b_back;
	SharedPreferences sp;
	private int selectItem = -1;
	int[] headIds = new int[] { R.id.txt_1201, R.id.txt_1202, R.id.txt_1203,
			R.id.txt_1204, R.id.txt_1205 };
	String[] headers = new String[] { "序号", "城市", "台区名称","已完成", "操作" };
	// 方便测试，直接写的public
	Button sence_check, repair_record;
	DataAdapter adapter;
	public HorizontalScrollView mTouchView;
	// 装入所有的HScrollView
	protected List<CHScrollView> mHScrollViews = new ArrayList<CHScrollView>();
//	AreaInfo repairOrder;
	private LayoutInflater mInflater;
	SparseArray<Areas> areaslist;
	LinearLayout repairhead;
	private ListView mListView;
	Gson gson;
	String message;
	SqlHelper helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题（尽量在前面设置）
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_area_list);
		MyApplication myApplication = (MyApplication) getApplication();
		helper=myApplication.getSqlHelper();
		AreasDao od = new AreasDao(helper);
		od.createTable(helper.getWritableDatabase());
		// 查询数据库
		 areaslist = od.queryToList("", null);
		if (areaslist == null || areaslist.size() == 0) {
		}

		TextView title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("台区列表");
		ImageButton b_back = (ImageButton) findViewById(R.id.b_back);
		b_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		sence_check = (Button) findViewById(R.id.sence_check);
		repair_record = (Button) findViewById(R.id.repair_regis);
		sence_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (selectItem == -1) {
					new AlertDialog.Builder(Activity_AreaList.this)
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
				String cityname = areaslist.get(selectItem).city;
				String content =areaslist.get(selectItem).area;
				Bundle bundle = new Bundle();
				/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
				bundle.putString("content", content);
			}
		});

		repair_record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (selectItem == -1) {
					new AlertDialog.Builder(Activity_AreaList.this)
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
				if (areaslist.size() > selectItem) {
					String cityname =areaslist.get(selectItem).city;

					Bundle bundle = new Bundle();

					String content =areaslist.get(selectItem).area;
					/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
					bundle.putString("maintainNum", cityname);
					bundle.putString("content", content);

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
		// 添加头滑动事件
		mHScrollViews.add(headerScroll);
		mListView = (ListView) findViewById(R.id.scroll_list);
		if (areaslist == null) {
			// mListView.setVisibility(View.GONE);
			areaslist = new SparseArray<Areas>();
			 for (int i = 0; i < 30; i++) {
				 Areas rows = new Areas();
			 rows.city = "" + i;
			 rows.area = i + "content";
			 rows.areastatus=i;
				 areaslist.put(areaslist.size(),rows);
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
	Dialog mdlg;
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
			return areaslist.size();
		}

		public void setSelectItem(int tselectItem) {
			selectItem = tselectItem;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// if (convertView == null) {
			convertView = mInflater.inflate(R.layout.lay_area_row, null);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setSelectItem(position); // 自定义的变量，以便让adapter知道要选中哪一项
					Areas tmpRepairOrder =areaslist.get(position);
					if (tmpRepairOrder.areastatus.equals("1")) {
						// holder.orderState.setText("发布");
						repair_record.setEnabled(false);
						sence_check.setEnabled(true);
					} else if (tmpRepairOrder.areastatus.equals("2")
							|| tmpRepairOrder.areastatus.equals("4")) {
						// holder.orderState.setText("维修申请  ");
						repair_record.setEnabled(false);
						sence_check.setEnabled(false);
					} else if (tmpRepairOrder.areastatus.equals("3")) {

						repair_record.setEnabled(true);
						sence_check.setEnabled(false);
						// holder.orderState.setText("审批通过");
					}
					notifyDataSetChanged();// 提醒数据已经变动
				}
			});
			if (position == 0 && selectItem == -1) {
				convertView.setBackgroundColor(Color.WHITE);
			} else if (position == selectItem) {
				convertView.setBackgroundColor(Color.YELLOW);
			} else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}

			final ViewHolder holder = new ViewHolder();
			// holder.maintainNum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			holder.areanmae = (TextView) convertView
					.findViewById(R.id.areaname);

			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.orderState = (TextView) convertView
					.findViewById(R.id.orderState);
			holder.cityname=(TextView) convertView
					.findViewById(R.id.cityname);
			final Areas tmpAreaInto = areaslist.get(position);

			if (tmpAreaInto.areastatus.equals("1")) {
				holder.orderState.setText("发布");
			} else if (tmpAreaInto.areastatus.equals("2")) {
				holder.orderState.setText("  ");
			} else if (tmpAreaInto.areastatus.equals("3")) {
				holder.orderState.setText("审批通过");
			} else if (tmpAreaInto.areastatus.equals("4")) {
				holder.orderState.setText("维修完成");
			}
			holder.cityname.setText(tmpAreaInto.city);
			// holder.maintainNum.setText(Html.fromHtml("<u>"+tmpRepairOrder.maintainNum+"</u>"));
			holder.areanmae.setText(tmpAreaInto.area);
//			holder.content.setText(tmpAreaInto.content);

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
		TextView cityname;
		TextView areanmae;
		TextView content;
		TextView orderState;
	}
}
