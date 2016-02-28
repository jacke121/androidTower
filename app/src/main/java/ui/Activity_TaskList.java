package ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lbg.yan01.R;
import com.listview.CHScrollView;
import com.objs.RepairOrder;
import com.tool.DialogFactory;

public class Activity_TaskList extends Activity {

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
	Button sence_check, repair_record;
	DataAdapter adapter;
	public HorizontalScrollView mTouchView;
	// 装入所有的HScrollView
	protected List<CHScrollView> mHScrollViews = new ArrayList<CHScrollView>();
	RepairOrder repairOrder;
	private LayoutInflater mInflater;

	LinearLayout repairhead;
	private ListView mListView;
	Gson gson;
	int scrolledX;
	int scrolledY;
	String typeName;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题（尽量在前面设置）
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Bundle bundle = this.getIntent().getExtras();
		/* 获取Bundle中的数据，注意类型和key */
		if (bundle != null) {
			typeName = bundle.getString("type");
		}

		setContentView(R.layout.lay_task_list);

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
		if (typeName.equals("check")) {
			repair_record.setVisibility(View.GONE);
		} else if (typeName.equals("record")) {
			sence_check.setVisibility(View.GONE);
		}
		sence_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (selectItem == -1) {
					new AlertDialog.Builder(Activity_TaskList.this)
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
				String maintainNum = repairOrder.rows.get(selectItem).maintainNum;
				String assetCode = repairOrder.rows.get(selectItem).assetCode;
				String content = repairOrder.rows.get(selectItem).content;
				String assetName = repairOrder.rows.get(selectItem).assetName;
//				Intent intent = new Intent(Activity_TaskList.this,
//						Activity_SceneCheck.class);
				Bundle bundle = new Bundle();
				/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
				bundle.putString("maintainNum", maintainNum);
				bundle.putString("assetCode", assetCode);
				bundle.putString("content", content);
				bundle.putString("assetName", assetName);
				bundle.putInt("maintainType",
						repairOrder.rows.get(selectItem).maintainType);
				/* 把bundle对象assign给Intent */
//				intent.putExtras(bundle);
//				startActivityForResult(intent, 1);
				// startActivity(intent);
			}
		});

		repair_record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (selectItem == -1) {
					new AlertDialog.Builder(Activity_TaskList.this)
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
				if (repairOrder.rows.size() > selectItem) {
					String maintainNum = repairOrder.rows.get(selectItem).maintainNum;
					String assetCode = repairOrder.rows.get(selectItem).assetCode;
					String assetName = repairOrder.rows.get(selectItem).assetName;


//						intent = new Intent(Activity_TaskList.this,
//								Activity_RepairStep.class);

					Bundle bundle = new Bundle();

					String content = repairOrder.rows.get(selectItem).content;
					/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
					bundle.putString("maintainNum", maintainNum);
					bundle.putString("content", content);
					/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
					bundle.putString("assetCode", assetCode);
					bundle.putString("assetName", assetName);
					bundle.putString("department",
							repairOrder.rows.get(selectItem).department);
					bundle.putString("issuer",
							repairOrder.rows.get(selectItem).issuer);

					bundle.putInt("maintainType",
							repairOrder.rows.get(selectItem).maintainType);
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

		// mListView.setOnScrollListener(new OnScrollListener() {
		//
		// /**
		// * 滚动状态改变时调用
		// */
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// // 不滚动时保存当前滚动到的位置
		// if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
		//
		// // ListPos=list.getFirstVisiblePosition();
		// // scrolledX = mListView.getScrollX();
		// // scrolledY = mListView.getScrollY();
		// scrolledY = mListView.getFirstVisiblePosition();
		//
		// }
		// }
		//
		// /**
		// * 滚动时调用
		// */
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem, int
		// visibleItemCount, int totalItemCount) {
		// }
		// });
		if (repairOrder == null) {
			// mListView.setVisibility(View.GONE);
			repairOrder = new RepairOrder();
			 for (int i = 0; i < 30; i++) {

			 RepairOrder.Rows rows = repairOrder.new Rows();

			 rows.maintainNum = "" + i;
			 rows.assetCode = "code" + i;
			 rows.assetName = "name" + i;
			 rows.maintainPerson = "weixiuren" + i;
			 rows.maintainDate = "release_date" + i;
			 rows.department = "department" + i;
			 rows.maintainType = i;
			 rows.person = "person" + i;
			 rows.yt = "yt" + i;
			 rows.assetSize = i + "";
			 rows.maintainType = i;
			 rows.content = i + "content";
			 rows.applyDate = i + "applyDate";
			 rows.issuer = i + "issuer";
			 rows.orderState=i+"";
			 repairOrder.rows.add(rows);
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
	@SuppressLint("HandlerLeak")
	private Handler mMsgReciver = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 1:
				if (mdlg != null) {
					mdlg.dismiss();
					mdlg = null;
					java.lang.reflect.Type type = new TypeToken<RepairOrder>() {
					}.getType();
					gson = new Gson();
					repairOrder = gson.fromJson(message, type);

					List<RepairOrder.Rows> mqList = repairOrder.rows;
					Iterator iter = mqList.iterator();
					while (iter.hasNext()) {

						RepairOrder.Rows s = (RepairOrder.Rows) iter.next();
						if (typeName.equals("check")
								&& !s.orderState.equals("1")) {
							iter.remove();
						} else if (typeName.equals("record")
								&& !s.orderState.equals("3")) {
							iter.remove();
						}
					}

					List<String> maintainNums = new ArrayList<String>();

					for (RepairOrder.Rows element : repairOrder.rows) {
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
					new AlertDialog.Builder(Activity_TaskList.this)
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
			return repairOrder.rows.size();
		}

		public void setSelectItem(int tselectItem) {
			selectItem = tselectItem;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			// if (convertView == null) {

			convertView = mInflater.inflate(R.layout.lay_task_row, null);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setSelectItem(position); // 自定义的变量，以便让adapter知道要选中哪一项
					RepairOrder.Rows tmpRepairOrder = repairOrder.rows.get(position);
					if (tmpRepairOrder.orderState.equals("1")) {
						// holder.orderState.setText("发布");
						repair_record.setEnabled(false);
						sence_check.setEnabled(true);
					} else if (tmpRepairOrder.orderState.equals("2")
							|| tmpRepairOrder.orderState.equals("4")) {
						// holder.orderState.setText("维修申请  ");
						repair_record.setEnabled(false);
						sence_check.setEnabled(false);
					} else if (tmpRepairOrder.orderState.equals("3")) {

						repair_record.setEnabled(true);
						sence_check.setEnabled(false);
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
			final RepairOrder.Rows tmpRepairOrder = repairOrder.rows.get(position);

			if (tmpRepairOrder.orderState.equals("1")) {
				holder.orderState.setText("发布");
			} else if (tmpRepairOrder.orderState.equals("2")) {
				holder.orderState.setText("维修申请  ");
			} else if (tmpRepairOrder.orderState.equals("3")) {
				holder.orderState.setText("审批通过");
			} else if (tmpRepairOrder.orderState.equals("4")) {
				holder.orderState.setText("维修完成");
			}
			holder.maintainNum.setText(tmpRepairOrder.maintainNum);
			// holder.maintainNum.setText(Html.fromHtml("<u>"+tmpRepairOrder.maintainNum+"</u>"));
			holder.assetCode.setText(tmpRepairOrder.assetCode);
			holder.assetName.setText(tmpRepairOrder.assetName);
			holder.assetSize.setText(tmpRepairOrder.assetSize);
			holder.yt.setText(tmpRepairOrder.yt);
			holder.department.setText(tmpRepairOrder.department);
			holder.person.setText(tmpRepairOrder.person);
			if (tmpRepairOrder.maintainType.toString().endsWith("1")) {
				holder.maintainType.setText("设备升级");
			} else if (tmpRepairOrder.maintainType.toString().endsWith("2")) {
				holder.maintainType.setText("设备维修");
			} else if (tmpRepairOrder.maintainType.toString().endsWith("3")) {
				holder.maintainType.setText("重做系统");
			}

			holder.content.setText(tmpRepairOrder.content);
			holder.applyDate.setText(tmpRepairOrder.applyDate);
			holder.issuer.setText(tmpRepairOrder.issuer);
			holder.maintainPerson.setText(tmpRepairOrder.maintainPerson);
			holder.maintainDate.setText(tmpRepairOrder.maintainDate);

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
