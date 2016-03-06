package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;
import com.listview.CHScrollView;

import java.util.ArrayList;
import java.util.List;

public class Activity_AreaList extends Activity implements OnClickListener {
	Button btn_add;
	ImageButton b_back;
	private int selectItem = -1;
	int[] headIds = new int[] { R.id.txt_1201, R.id.txt_1202, R.id.txt_1203,
			R.id.txt_1204, R.id.txt_1205 };
	String[] headers = new String[] { "序号", "城市", "台区名称","状态", "操作" };
	// 方便测试，直接写的public
	DataAdapter adapter;
	public HorizontalScrollView mTouchView;
	// 装入所有的HScrollView
	protected List<CHScrollView> mHScrollViews = new ArrayList<CHScrollView>();
	private LayoutInflater mInflater;
	SparseArray<Areas> areaslist;
	LinearLayout repairhead;
	private ListView mListView;
	SqlHelper helper;
    AreasDao areasDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题（尽量在前面设置）
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_area_list);
		MyApplication myApplication = (MyApplication) getApplication();
		helper=myApplication.getSqlHelper();
		 areasDao = new AreasDao(helper);
		areasDao.createTable(helper.getWritableDatabase());
		// 查询数据库
        geiDatas();
        btn_add= (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

		Button btn_child= (Button) findViewById(R.id.btn_child);
		btn_child.setOnClickListener(this);
		TextView title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("台区列表");

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

		adapter = new DataAdapter();
		mListView.setAdapter(adapter);
		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		b_back = (ImageButton) findViewById(R.id.b_back);
		b_back.setOnClickListener(this);

	}
    public void geiDatas(){
        areaslist = areasDao.queryToList("", null);
        if (areaslist == null) {
            // mListView.setVisibility(View.GONE);
            areaslist = new SparseArray<Areas>();
            for (int i = 0; i < 15; i++) {
                Areas rows = new Areas();
                rows.gongbian="gongbian";
                rows.area = i + "content";
                rows.areastatus=i;
                areaslist.put(areaslist.size(),rows);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
				Bundle bundle = new Bundle();
                Intent intent = new Intent(Activity_AreaList.this,
                        Activity_Area.class);
				bundle.putString("type", "add");
				intent.putExtras(bundle);
				startActivityForResult(intent, 1);
                break;
            case R.id.b_back:
                finish();
                break;
			case R.id.btn_child: {
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
				if (areaslist.size() > selectItem) {
					Integer id =areaslist.get(selectItem).id;

					 bundle = new Bundle();
					bundle.putInt("id",id);
					intent = new Intent(Activity_AreaList.this,
							Activity_TowerList.class);
					/* 把bundle对象assign给Intent */
					intent.putExtras(bundle);
					// startActivity(intent);
					startActivityForResult(intent, 1);
				}
			}
				break;

            case R.id.title_btn_sequence:
                Activity_AreaList.this.finish();
                break;
            default:
                break;
        }
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
					} else if (tmpRepairOrder.areastatus.equals("2")	) {
						// holder.orderState.setText("维修申请  ");
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


			holder.rownember = (TextView) convertView
					.findViewById(R.id.rownember);
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
			holder.rownember.setText(position+1+"");
			holder.cityname.setText(tmpAreaInto.quxian);
			// holder.maintainNum.setText(Html.fromHtml("<u>"+tmpRepairOrder.maintainNum+"</u>"));
			holder.areanmae.setText(tmpAreaInto.area);
			convertView.setTag(holder);
			CHScrollView ascrollView = (CHScrollView) convertView
					.findViewById(R.id.item_scroll);
			ascrollView.registerScrollChange(new CHScrollView.ScrollChange() {

				@Override
				public void onScrollChanged(int l, int t, int oldl, int oldt) {
					// TODO Auto-generated method stub
					for (CHScrollView mscrollView : mHScrollViews) {
						// 防止重复滑动
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
            // 刷新界面
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
		TextView rownember;
		TextView cityname;
		TextView areanmae;
		TextView content;
		TextView orderState;
	}
}
