package ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
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
import com.baseDao.Biao;
import com.baseDao.BiaoDao;
import com.baseDao.Ganta;
import com.baseDao.GantaDao;
import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;
import com.listview.CHScrollView;
import com.tool.FileUtil;
import com.tool.InputStreamCacher;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exceltest.JXLUtil;

public class Activity_AreaList extends Activity implements OnClickListener {
    private int selectItem = -1;
    int[] COLWIDTH_ARR = {5, 40, 15, 30, 30, 30, 30, 30, 30, 30, 30, 10, 30, 30, 30};//单元格宽度
    int[] headIds = new int[]{R.id.txt_1201, R.id.txt_1202, R.id.txt_1203,
            R.id.txt_1204, R.id.txt_1205};
    String[] headers = new String[]{"序号", "台区名", "运行单位", "状态", "操作"};
    // 方便测试，直接写的public
    DataAdapter adapter;
    public HorizontalScrollView mTouchView;
    // 装入所有的HScrollView
    protected List<CHScrollView> mHScrollViews = new ArrayList<CHScrollView>();
    private LayoutInflater mInflater;
    SparseArray<Areas> areaslist;
    LinearLayout repairhead;
    private ListView mListView;
    GantaDao gantaDao;
    BiaoDao biaoDao;
    SqlHelper helper;
    AreasDao areasDao;

   public static String todaystr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题（尽量在前面设置）
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lay_area_list);
        MyApplication myApplication = (MyApplication) getApplication();
        helper = myApplication.getSqlHelper();
        areasDao = new AreasDao(helper);
        gantaDao = new GantaDao(helper);
        biaoDao = new BiaoDao(helper);
        // 查询数据库
        getData();
        ((Button) findViewById(R.id.btn_add)).setOnClickListener(this);
        ((Button) findViewById(R.id.taiqu_export)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.b_back)).setOnClickListener(this);
        Button btn_child = (Button) findViewById(R.id.btn_child);
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
    }

    public void getData() {
        areaslist = areasDao.queryToList("", null);
        if (areaslist == null) {
            // mListView.setVisibility(View.GONE);
            areaslist = new SparseArray<Areas>();
            for (int i = 0; i < 0; i++) {
                Areas rows = new Areas();
                rows.gongbian = "gongbian";
                rows.area = i + "content";
                rows.areastatus = i;
                areaslist.put(areaslist.size(), rows);
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
            case R.id.taiqu_export:
                JXLUtil jXLUtil = new JXLUtil();
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                todaystr   = sdf.format(now);
                InputStream is = getResources().openRawResource(R.raw.model);

                InputStreamCacher inputStreamCacher = new InputStreamCacher(is);
                for (int i = 0; i < areaslist.size(); i++) {
                    String area = areaslist.get(i).area;
                    String filename = new FileUtil().getSDDir("1tower") + "/坐标点批量导入" + todaystr + area + ".xls";
                    new FileUtil().insToFile(inputStreamCacher.getInputStream(), filename);
//                    String[] excelheaders = new String[]{"序号", "台区", "杆塔名称", "单位", "材质", "性质", "回路数", "电压", "运行状态", "坐标点", "发布日期","全貌","塔头","铭牌"};
//                    jXLUtil.initExcel(filename, excelheaders, COLWIDTH_ARR);
                    SparseArray<Ganta> gantaList = gantaDao.queryBySql("select g.id,a.area || g.name as name,g.areaid,g.dianya,g.caizhi,g.xingzhi,g.taiquid,g.huilu,g.yunxing,g.zuobiao,g.level,g.parentid,g.picquanmao,g.pictatou,g.picmingpai,g.createtime,g.updatetime,g.lifeStatus,g.upgradeFlag," +
                            "a.area as areaname,a.danwei from Ganta g join Areas a on a.id=g.areaid where a.id=" + areaslist.get(i).id, null);
                    String[] excelfiles = new String[]{"areaname", "name", "danwei", "caizhi", "xingzhi", "huilu", "dianya", "yunxing", "zuobiao", "createtime", "picquanmao", "pictatou", "picmingpai"};

                    SparseArray<Biao> biaoList = biaoDao.queryBySql("select b.* from Biao b join Ganta g on g.id=b.gantaid where g.areaid=" + areaslist.get(i).id, null);

                    Cursor towercursor  =  helper.getReadableDatabase().rawQuery("select g.id,a.area || pg.name as startname,a.area || g.name as endname,g.areaid,g.yunxing,g.zuobiao,g.createtime,g.updatetime,a.area as areaname,a.danwei from Ganta g join Areas a on a.id=g.areaid join Ganta pg on pg.id=g.parentid where a.id=" + areaslist.get(i).id, null);
                    Cursor biaocursor  =  helper.getReadableDatabase().rawQuery("select g.id,a.area || g.name as startname,a.area || b.name as endname,g.areaid,g.yunxing,g.zuobiao,g.createtime,g.updatetime,a.area as areaname,a.danwei from Ganta g join Areas a on a.id=g.areaid join Biao b on b.gantaid=g.id where a.id=" + areaslist.get(i).id, null);

                    jXLUtil.writeObjInToExcel(gantaList,towercursor, biaoList,biaocursor, filename, excelfiles, this,area);
                }
                showMsg("导出目录：" + new FileUtil().getSDDir("1tower"));
                break;

            case R.id.btn_child: {
                if (selectItem == -1) {
                    showMsg("请选择一行!");
                    return;
                }
                if (areaslist.size() > selectItem) {
                    Integer id = areaslist.get(selectItem).id;
                    bundle = new Bundle();
                    bundle.putInt("id", id);
                    intent = new Intent(Activity_AreaList.this,
                            Activity_TowerList.class);
                    /* 把bundle对象assign给Intent */
                    intent.putExtras(bundle);
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
            convertView = mInflater.inflate(R.layout.lay_area_row, null);
            final Areas tmpAreaInto = areaslist.get(position);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    setSelectItem(position); // 自定义的变量，以便让adapter知道要选中哪一项
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
            holder.area_del = (Button) convertView
                    .findViewById(R.id.area_del);
            holder.area_del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SparseArray<Ganta> gantaList = gantaDao.queryBySql("select g.id,a.area as areaname,g.name,a.danwei,g.caizhi,g.xingzhi,g.huilu,g.dianya,g.yunxing,g.zuobiao,g.updatetime" +
                            " from Ganta g join Areas a on a.id=g.areaid", new String[]{tmpAreaInto.id + ""});
                    if (gantaList != null && gantaList.size() > 0) {
                        showMsg("台区还有杆塔，不能删除!");
                        return;
                    }
                    new AlertDialog.Builder(Activity_AreaList.this)
                            .setTitle("温馨提示")
                            .setMessage("您确定要删除台区吗？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            areasDao.delete(tmpAreaInto);
                                            getData();
                                            notifyDataSetChanged();// 提醒数据已经变动
                                        }
                                    }).setNegativeButton("取消", null).create()
                            .show();

                }
            });
            for (int i = 0; i < headers.length; i++) {
                holder.txts[i] = (TextView) convertView
                        .findViewById(headIds[i]);
            }

            if (tmpAreaInto.areastatus.equals("1")) {
                holder.txts[3].setText("未完成");
            } else if (tmpAreaInto.areastatus.equals("2")) {
                holder.txts[3].setText("已完成");
            }
            holder.txts[0].setText(position + 1 + "");
            holder.txts[1].setText(tmpAreaInto.area);
            holder.txts[2].setText(tmpAreaInto.danwei);
            // holder.maintainNum.setText(Html.fromHtml("<u>"+tmpRepairOrder.maintainNum+"</u>"));

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
            getData();
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

    public void showMsg(String msg) {
        new AlertDialog.Builder(Activity_AreaList.this).setTitle("温馨提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
    }

    private class ViewHolder {
        TextView[] txts = new TextView[5];
        Button area_del;
    }
}
