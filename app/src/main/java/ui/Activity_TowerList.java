package ui;

import java.text.SimpleDateFormat;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baseDao.Areas;
import com.baseDao.AreasDao;
import com.baseDao.Biao;
import com.baseDao.Ganta;
import com.baseDao.GantaDao;
import com.baseDao.SqlHelper;
import com.dyr.custom.CustomDialog;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;
import com.listview.CHScrollView;
import com.popujar.PopuItem;
import com.popujar.PopuJar;

public class Activity_TowerList extends Activity implements OnClickListener {

    private int selectItem = -1;
    private int parentItem = -1;
    int[] headIds = new int[]{R.id.txt_1100, R.id.txt_1101, R.id.txt_1102, R.id.txt_1103,
            R.id.txt_1104, R.id.txt_1105, R.id.txt_1106, R.id.txt_1107,
            R.id.txt_1108, R.id.txt_1109, R.id.txt_1110};
    String[] headers = new String[]{"序号", "台区名", "杆塔名称", "坐标点", "性质", "回路数", "电压", "运行状态", "材质", "发布人", "发布日期"};
    // 方便测试，直接写的public
    Button btn_add, btn_cailu, btn_guanlian;
    SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    AreasDao areasDao;
    Areas curentreas;
    PopuJar mPopuJar;
    TextView title_text2;
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
        getDatas();
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("塔杆列表");

        PopuItem addItem = new PopuItem(1, "删除");
        PopuItem acceptItem = new PopuItem(2, "取消");

        mPopuJar = new PopuJar(Activity_TowerList.this, PopuJar.HORIZONTAL);

        mPopuJar.addPopuItem(addItem);
        mPopuJar.addPopuItem(acceptItem);

        //setup the action item click listener
        mPopuJar.setOnPopuItemClickListener(new PopuJar.OnPopuItemClickListener() {
            @Override
            public void onItemClick(PopuJar PopuJar, int position, int actionId) {
                PopuItem PopuItem = PopuJar.getPopuItem(position);
                if (actionId == 1) { //Add item selected
                    if (selectItem == -1) {
                        showMsg("请选择一行!");
                        return;
                    }
                    Ganta tmpGanta = gantaList.get(selectItem);
                    gantaDao.delete(tmpGanta);
                    getDatas();
                    adapter.notifyDataSetChanged();// 提醒数据已经变动
                } else if (actionId == 2) {
                } else if (actionId == 3) {
                }
            }
        });

        //setup on dismiss listener, set the icon back to normal
        mPopuJar.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                mMoreIv.setImageResource(R.drawable.ic_list_more);
            }
        });
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_cailu = (Button) findViewById(R.id.btn_cailu);
        title_text2= (TextView) findViewById(R.id.title_text2);
        Button btn_delgan = (Button) findViewById(R.id.btn_delgan);
        btn_guanlian = (Button) findViewById(R.id.btn_guanlian);
        btn_guanlian.setOnClickListener(this);
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

            case R.id.btn_guanlian:
                if (selectItem == -1) {
                    showMsg("请选择一行!");
                    return;
                }
                if (parentItem == -1) {
                    if (gantaList.size() > selectItem) {
                        parentItem = selectItem;
                        btn_guanlian.setText("完成关联");
                        title_text2.setText("上一级杆塔："+(parentItem+1));
                        showMsg("开始选择下一级杆塔");
                    }
                } else {
                    //caozuo
                    if(parentItem==selectItem){
                        showMsg("上下级杆塔不能相同！");
                        return;
                    }
                    gantaList.get(selectItem).parentid = gantaList.get(parentItem).id;
                    gantaDao.update(gantaList.get(selectItem));
                    title_text2.setText("");
                    showMsg("完成杆塔");
                    btn_guanlian.setText("关联杆塔");
                    parentItem = -1;
                    getDatas();
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_add: {
                if (null == gantaList || gantaList.size() == 0) {
                    Intent intent = new Intent(Activity_TowerList.this,
                            Activity_Tower.class);
                    intent.putExtra("type", 0);
                    /* 把bundle对象assign给Intent */
                    startActivityForResult(intent, 1);
                } else if (gantaList.size() > 0 && selectItem == -1) {
                    showAlertDialog(0);
                } else {
                    showAlertDialog(1);
                }


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
                if (gantaList.size() > selectItem) {
                    Intent intent = new Intent(Activity_TowerList.this,
                            Activity_BiaoList.class);
                    intent.putExtra("ganta", gantaList.get(selectItem));
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.btn_cailu: {
                if (selectItem == -1) {
                    showMsg("请选择一行!");
                    return;
                }
                Intent intent = new Intent(Activity_TowerList.this, Activity_Tower.class);
                if (gantaList.size() > selectItem) {
                    intent.putExtra("type", 3);
                    intent.putExtra("ganta", gantaList.get(selectItem));
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

    public void getDatas() {
//        gantaList = gantaDao.queryBySql("select g.* from Ganta g left join Areas a on a.id=g.areaid where a.id=", new String[]{id + ""});
        gantaList = gantaDao.queryBySql("select g.id,g.name,g.areaid,g.dianya,g.caizhi,g.xingzhi,g.taiquid,g.huilu,g.yunxing,g.zuobiao,g.level,g.parentid,g.picquanmao,g.pictatou,g.picmingpai,g.createtime,g.updatetime,g.lifeStatus,g.upgradeFlag," +
                "a.area as areaname,a.danwei from Ganta g join Areas a on a.id=g.areaid where a.id=?", new String[]{areaid + ""});
        if (gantaList == null) {
            gantaList = new SparseArray<Ganta>();
            for (int i = 0; i < 0; i++) {
                Ganta rows = new Ganta();
                rows.id = i;
                rows.caizhi = "caizhi";
                rows.name = i + "content";
                rows.pictatou = i + "";
                gantaList.put(gantaList.size(), rows);
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
            final View finalConvertView = convertView;
            LinearLayout item_Linear = (LinearLayout) convertView.findViewById(R.id.item_Linear);

            item_Linear.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selectItem = position;
                    mPopuJar.show(finalConvertView);
                    return true;
                }
            });
            item_Linear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectItem(position); // 自定义的变量，以便让adapter知道要选中哪一项
                    Ganta tmpTowerInfo = gantaList.get(position);
                    if (tmpTowerInfo.caizhi.equals("1")) {
                        // holder.orderState.setText("发布");
                        btn_cailu.setEnabled(false);
                        btn_add.setEnabled(true);
                    }
                    notifyDataSetChanged();// 提醒数据已经变动
                }
            });
            final Ganta tmpTowerInfo = gantaList.get(position);
            // holder.maintainNum.setText(Html.fromHtml("<u>"+tmpTowerInfo.maintainNum+"</u>"));
            holder.txts[0].setText(position + 1 + "");
            holder.txts[1].setText(tmpTowerInfo.areaname + "");
            holder.txts[2].setText(tmpTowerInfo.name + "");
            holder.txts[3].setText(tmpTowerInfo.zuobiao + "");
            holder.txts[4].setText(tmpTowerInfo.xingzhi + "");
            holder.txts[5].setText(tmpTowerInfo.huilu + "");
            holder.txts[6].setText(tmpTowerInfo.dianya);
            holder.txts[7].setText(tmpTowerInfo.yunxing + "");
            holder.txts[8].setText(tmpTowerInfo.caizhi);
            holder.txts[9].setText(IndexActivity.userName);
            holder.txts[10].setText(dfu.format(tmpTowerInfo.createtime));

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
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
            case 1:
                break;
            default:
                break;
        }
        if (1 == requestCode) {
            getDatas();
            adapter.notifyDataSetChanged();
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
        TextView[] txts = new TextView[11];
    }

    public void showAlertDialog(final int mtype) {

        final CustomDialog.Builder builder = new CustomDialog.Builder(this);

        if (mtype > 0) {
            builder.setMessage("请选择新增杆塔类型：");
        }
        builder.setTitle("温馨提示");
        builder.setType(mtype);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Activity_TowerList.this,
                        Activity_Tower.class);
                Ganta tmpGanta = new Ganta();
                if (selectItem > -1) {
                    tmpGanta = gantaList.get(selectItem);
                }
                if (mtype == 0) {
                    intent.putExtra("type", 0);
                } else {
                    intent.putExtra("type", builder.getGantatype());
                    intent.putExtra("ganta", tmpGanta);
                }
                    /* 把bundle对象assign给Intent */
                startActivityForResult(intent, 1);
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}