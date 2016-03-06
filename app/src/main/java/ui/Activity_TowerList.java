package ui;

import java.util.ArrayList;
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
import android.net.Uri;
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


import com.baseDao.AreasDao;
import com.baseDao.Ganta;
import com.baseDao.GantaDao;
import com.baseDao.SqlHelper;
import com.google.android.gms.appindexing.Action;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;
import com.listview.CHScrollView;

public class Activity_TowerList extends Activity implements OnClickListener  {

	Button add_new;
	ImageButton b_back;
	SharedPreferences sp;
	private int selectItem = -1;
	int[] headIds = new int[]{R.id.txt_1101, R.id.txt_1102, R.id.txt_1103,
			R.id.txt_1104, R.id.txt_1105, R.id.txt_1106, R.id.txt_1107,
			R.id.txt_1108, R.id.txt_1109, R.id.txt_1110, R.id.txt_1111,
			R.id.txt_1112, R.id.txt_1113, R.id.txt_1114};
	String[] headers = new String[]{"序号", "杆塔名称", "材质", "性质", "台区",
			"回路数", "电压", "运行状态", "坐标点", "上级塔杆", "发布日期", "发布人", "维修人员",
			"计划维修日期"};
	// 方便测试，直接写的public
	Button btn_add, btn_cailu;
	DataAdapter adapter;
	public HorizontalScrollView mTouchView;
	// 装入所有的HScrollView
	protected List<CHScrollView> mHScrollViews = new ArrayList<CHScrollView>();
	private LayoutInflater mInflater;
	SqlHelper helper;
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
			if (id == 0) {
//				showMsg("aaa");
			} else {
//				showMsg("aaa");
			}
		}

		setContentView(R.layout.lay_tower_list);

		MyApplication myApplication = (MyApplication) getApplication();
		helper=myApplication.getSqlHelper();
		 gantaDao = new GantaDao(helper);
		gantaDao.createTable(helper.getWritableDatabase());
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

		btn_add.setOnClickListener(this);

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
				if (areaslist.size() > selectItem) {
					int id = areaslist.get(selectItem).id;
					Bundle bundle = new Bundle();
					/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
					bundle.putInt("id", id);

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

		geiDatas();
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
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
	}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Bundle bundle = new Bundle();
                Intent intent = new Intent(Activity_TowerList.this,
                        Activity_Tower.class);
                bundle.putString("type", "add");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                break;
            case R.id.b_back:
                finish();
                break;
            case R.id.btn_child: {
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
                if (areaslist.size() > selectItem) {
                    Integer id =areaslist.get(selectItem).id;

                    bundle = new Bundle();
                    bundle.putInt("id",id);
                    intent = new Intent(Activity_TowerList.this,
                            Activity_TowerList.class);
					/* 把bundle对象assign给Intent */
                    intent.putExtras(bundle);
                    // startActivity(intent);
                    startActivityForResult(intent, 1);
                }
            }
            break;

            case R.id.title_btn_sequence:
                Activity_TowerList.this.finish();
                break;
            default:
                break;
        }
    }
	public void geiDatas() {
		areaslist = gantaDao.queryToList("", null);
		if (areaslist == null) {
			// mListView.setVisibility(View.GONE);
			areaslist = new SparseArray<Ganta>();
			for (int i = 0; i < 15; i++) {
				Ganta rows = new Ganta();
				rows.caizhi = 1;
				rows.name = i + "content";
				rows.pictatou = i + "";
				areaslist.put(areaslist.size(), rows);
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

	public void onScrollChanged(int l, int t, int oldl, int oldt) {
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
			convertView = mInflater.inflate(R.layout.lay_tower_row, null);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setSelectItem(position); // 自定义的变量，以便让adapter知道要选中哪一项
					Ganta tmpTowerInfo = areaslist.get(position);
					if (tmpTowerInfo.caizhi.equals("1")) {
						// holder.orderState.setText("发布");
						btn_cailu.setEnabled(false);
						btn_add.setEnabled(true);
					} else if (tmpTowerInfo.caizhi.equals("2")
							|| tmpTowerInfo.caizhi.equals("4")) {
						// holder.orderState.setText("维修申请  ");
						btn_cailu.setEnabled(false);
						btn_add.setEnabled(false);
					} else if (tmpTowerInfo.caizhi.equals("3")) {

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

			for (int i = 0; i < headers.length; i++) {
				holder.txts[i] = (TextView) convertView
						.findViewById(headIds[i]);
			}

			final Ganta tmpTowerInfo =areaslist.get(position);

			if (tmpTowerInfo.caizhi.equals("1")) {
				holder.txts[2].setText("发布");
			} else if (tmpTowerInfo.caizhi.equals("2")) {
				holder.txts[2].setText("发布");
			}
			// holder.maintainNum.setText(Html.fromHtml("<u>"+tmpTowerInfo.maintainNum+"</u>"));
			holder.txts[3].setText(tmpTowerInfo.caizhi+"");
			holder.txts[4].setText(tmpTowerInfo.xingzhi+"");
			holder.txts[5].setText(tmpTowerInfo.taiquid+"");
			holder.txts[6].setText(tmpTowerInfo.huilu+"");
			holder.txts[7].setText(tmpTowerInfo.dianya);
			if (tmpTowerInfo.caizhi.toString().endsWith("1")) {
				holder.txts[8].setText("220V");
			} else if (tmpTowerInfo.caizhi.toString().endsWith("2")) {
				holder.txts[8].setText("380V");
			}

			holder.txts[9].setText(tmpTowerInfo.yunxing+"");
			holder.txts[10].setText(tmpTowerInfo.zuobiao+"");
			holder.txts[11].setText(tmpTowerInfo.level+"");
			holder.txts[12].setText(tmpTowerInfo.parentid+"");

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
			geiDatas();
			adapter.notifyDataSetInvalidated();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		TextView[] txts = new TextView[14];
	}
}