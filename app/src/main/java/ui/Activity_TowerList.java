package ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
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


import com.baseDao.Areas;
import com.baseDao.AreasDao;
import com.baseDao.Ganta;
import com.baseDao.GantaDao;
import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;
import com.listview.CHScrollView;

public class Activity_TowerList extends Activity implements OnClickListener {

    private int selectItem = -1;
    int[] headIds = new int[]{R.id.txt_1100,R.id.txt_1101, R.id.txt_1102, R.id.txt_1103,
            R.id.txt_1104, R.id.txt_1105, R.id.txt_1106, R.id.txt_1107,
            R.id.txt_1108, R.id.txt_1109, R.id.txt_1110, R.id.txt_1111,
            R.id.txt_1112};
    String[] headers = new String[]{"序号", "杆塔名称", "材质", "性质", "台区",
            "回路数", "电压", "运行状态", "坐标点", "上级塔杆", "发布日期", "发布人", "维修人员"};
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
    public static int areaid;
    GantaDao gantaDao;
    SparseArray<Ganta> gantaList;
    SparseArray<Areas> areaslist;
    AreasDao areasDao;
    Areas curentreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题（尽量在前面设置）
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = this.getIntent().getExtras();
        /* 获取Bundle中的数据，注意类型和key */
        if (bundle != null) {
            areaid = bundle.getInt("id");
            if (areaid == 0) {
                finish();
            }
            areasDao = new AreasDao(((MyApplication) getApplication()).getSqlHelper());
            SparseArray<Areas> areas = areasDao.queryToList("id =?", new String[]{areaid + ""});//模糊查询
            if (areas != null) {
                curentreas = areas.get(0);
            }
        }
        setContentView(R.layout.lay_tower_list);
        helper = ((MyApplication) getApplication()).getSqlHelper();
        gantaDao = new GantaDao(helper);
        areasDao = new AreasDao(helper);
        gantaDao.createTable(helper.getWritableDatabase());
        geiDatas();
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("塔杆列表");

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_cailu = (Button) findViewById(R.id.btn_cailu);
        Button btn_delgan = (Button) findViewById(R.id.btn_delgan);
        btn_add.setOnClickListener(this);
        btn_cailu.setOnClickListener(this);
        btn_delgan.setOnClickListener(this);
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
        // mListView = (ListView) this.findViewById(R.id.repair_listview);
        ImageButton b_back = (ImageButton) findViewById(R.id.b_back);
        b_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add: {
                Intent intent = new Intent(Activity_TowerList.this,
                        Activity_Tower.class);
                    /* 把bundle对象assign给Intent */
                startActivityForResult(intent, 1);
            }
            break;
            case R.id.b_back:
                finish();
                break;
            case R.id.btn_delgan:
                if (selectItem == -1) {
                    showMsg("请选择一行!");

                    return;
                }
                Ganta tmpGanta = gantaList.get(selectItem);
                gantaDao.delete(tmpGanta);
                geiDatas();
                adapter.notifyDataSetChanged();// 提醒数据已经变动
                break;

            case R.id.btn_cailu: {
                if (selectItem == -1) {
                    showMsg("请选择一行!");
                    return;
                }
                Intent intent = new Intent(Activity_TowerList.this, Activity_Tower.class);
                if (gantaList.size() > selectItem) {
                    int id = gantaList.get(selectItem).id;
                    Bundle bundle = new Bundle();
					/* 字符、字符串、布尔、字节数组、浮点数等等，都可以传 */
                    bundle.putInt("id", id);
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
    public void showMsg(String msg) {
        new AlertDialog.Builder(Activity_TowerList.this).setTitle("温馨提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
    }
    public void geiDatas() {

//        gantaList = gantaDao.queryBySql("select g.* from Ganta g left join Areas a on a.id=g.areaid where a.id=", new String[]{id + ""});
        gantaList = gantaDao.queryBySql("select g.id,g.name,g.areaid,g.dianya,g.caizhi,g.xingzhi,g.taiquid,g.huilu,g.yunxing,g.zuobiao,g.level,g.parentid,g.picquanmao,g.pictatou,g.picmingpai,g.createtime,g.updatetime,a.area as areaname" +
                ",a.danwei from Ganta g join Areas a on a.id=g.areaid where a.id=?", new String[]{areaid + ""});
        if (gantaList == null) {
            // mListView.setVisibility(View.GONE);
            gantaList = new SparseArray<Ganta>();
            for (int i = 0; i < 0; i++) {
                Ganta rows = new Ganta();
                rows.id = i;
                rows.caizhi = "caizhi";
                rows.name = i + "content";
                rows.pictatou = i + "";
                gantaList.put(gantaList.size(), rows);
            }
        } else {
            areaslist = areasDao.queryToList("", null);
            for (int i = 0; i < gantaList.size(); i++) {
                System.out.println("value-->" + gantaList.get(i));
                for (int j = 0; j < areaslist.size(); j++) {
                    if (gantaList.get(i).taiquid == areaslist.get(j).id) {
                        break;
                    }

                }
            }

        }
    }

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
            return gantaList.size();
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
                    Ganta tmpTowerInfo = gantaList.get(position);
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

            final Ganta tmpTowerInfo = gantaList.get(position);
            // holder.maintainNum.setText(Html.fromHtml("<u>"+tmpTowerInfo.maintainNum+"</u>"));
            holder.txts[0].setText(position + 1 + "");
            holder.txts[1].setText(tmpTowerInfo.areaname + "");
            holder.txts[2].setText(tmpTowerInfo.name + "");
            holder.txts[3].setText(tmpTowerInfo.caizhi + "");
            holder.txts[4].setText(tmpTowerInfo.xingzhi + "");
            holder.txts[5].setText(tmpTowerInfo.taiquid + "");
            holder.txts[6].setText(tmpTowerInfo.huilu + "");
            holder.txts[7].setText(tmpTowerInfo.dianya);
            if (tmpTowerInfo.caizhi.toString().endsWith("1")) {
                holder.txts[8].setText("220V");
            } else if (tmpTowerInfo.caizhi.toString().endsWith("2")) {
                holder.txts[8].setText("380V");
            }

            holder.txts[9].setText(tmpTowerInfo.yunxing + "");
            holder.txts[10].setText(tmpTowerInfo.zuobiao + "");
            holder.txts[11].setText(tmpTowerInfo.level + "");
            holder.txts[12].setText(tmpTowerInfo.parentid + "");

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