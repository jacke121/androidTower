package ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.baseDao.Biao;
import com.baseDao.BiaoDao;
import com.baseDao.Ganta;
import com.baseDao.GantaDao;
import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;

public class Activity_Biao extends Activity implements OnClickListener {
    public static EditText txt_name,txt_danwei;
    Button btn_save;
    ImageView title_btn_sequence;
    String msg = null;
    public static Dialog mdlg;
    public String msg_show;
    SqlHelper helper;
    GantaDao   gantaDao;
    BiaoDao biaoDao;
    String type;
    int biaoid;
    Biao  curentreas;
  Biao  mganta;
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        MyApplication myApplication = (MyApplication) getApplication();
        helper = myApplication.getSqlHelper();
        gantaDao = new GantaDao(helper);
        biaoDao  = new BiaoDao(helper);
        //设置初始化视图
        Intent intent = getIntent();
        Biao cc =(Biao)intent.getSerializableExtra("biao");
        setContentView(R.layout.lay_biao_detail);

//        Bundle bundle = this.getIntent().getExtras();
//        /* 获取Bundle中的数据，注意类型和key */
//        if (bundle != null) {
//            biaoid = bundle.getInt("id");
//            if (biaoid == 0) {
//                finish();
//            }
//            SparseArray<Biao> gantas = biaoDao.queryToList("id =?", new String[]{biaoid + ""});
//            if (gantas == null) {
//                finish();
//            }
//            mganta = gantas.get(0);//模糊查询
//        }
//        SparseArray<Ganta> gantas = gantaDao.queryToList("id =?", new String[]{biaoid + ""});
//        if (gantas == null) {
//            finish();
//        }
//        SparseArray<Biao> areas = biaoDao.queryToList("id =?", new String[]{Activity_BiaoList.gantaid + ""});//模糊查询
//        if (areas != null) {
//            curentreas  = areas.get(0);
//        }


        //设置初始化视图
        initView();
    }

    @SuppressLint("HandlerLeak")
    public Handler mMsgReciver = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mdlg != null) {
                        mdlg.dismiss();
                        mdlg = null;
                        showregisterMessage("注册失败重新注册");
                    }
                    break;
                case 12:
              break;

            }
        }
    };

    public void showregisterMessage(final String msg) {
        // 创建提示框提醒是否登录成功
        Builder builder = new Builder(Activity_Biao.this);
        builder.setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (msg.contains("成功")) {
                            //进入首次设置界面
                            Intent intent = new Intent();
                            intent.setClass(Activity_Biao.this, IndexActivity.class);
                            startActivity(intent);
                        } else {
                            //进入首次设置界面
                            Intent intent = new Intent();
                            intent.setClass(Activity_Biao.this, Activity_Biao.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /**
     * 创建初始化视图的方法
     */
    private void initView() {
        txt_name = (EditText) findViewById(R.id.ext_name);
//        txt_quxian = (EditText) findViewById(R.id.txt_quxian);
//        txt_gongbian = (EditText) findViewById(R.id.txt_gongbian);
        txt_danwei = (EditText) findViewById(R.id.txt_danwei);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        Button register_btnCancel = (Button) findViewById(R.id.btnCancel);
        register_btnCancel.setOnClickListener(this);
        title_btn_sequence = (ImageView) findViewById(R.id.title_btn_sequence);
        title_btn_sequence.setOnClickListener(this);
    }

    public void showMsg(String msg) {
        new Builder(Activity_Biao.this).setTitle("温馨提示")
                .setMessage(msg + "!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
               String name = txt_name.getText().toString();
                String danwei = txt_danwei.getText().toString();
                if (name.equals("") || danwei.equals("")) {
                    showMsg("信息不能为空!");
                } else {
                    final Biao areas = new Biao();
                    areas.danwei=danwei;
                    areas.name=name;
                    areas.upgradeFlag=1l;
                    areas.lifeStatus=1;
                    biaoDao.insertList(new SparseArray<Biao>() {
                        {
                            put(0, areas);
                        }
                    });
                    Intent resultIntent = new Intent();
                    Activity_Biao.this.setResult(RESULT_OK, resultIntent);
                    Activity_Biao.this.finish();
                }
                break;
            case R.id.btnCancel:
                Intent intent = new Intent(Activity_Biao.this, IndexActivity.class);
                startActivity(intent);
                Activity_Biao.this.finish();
                break;
            case R.id.title_btn_sequence:
                Activity_Biao.this.finish();
                break;
            default:
                break;
        }
    }
}
