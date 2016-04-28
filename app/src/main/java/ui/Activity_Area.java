package ui;
import java.util.LinkedHashMap;
import java.util.Map;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.baseDao.Areas;
import com.baseDao.AreasDao;
import com.baseDao.SqlHelper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;

public class Activity_Area extends Activity implements OnClickListener {
    public static EditText txt_name, txt_danwei;// txt_quxian, txt_gongbian, ;
    Button btn_save;
    ImageView title_btn_sequence;
    public static Dialog mdlg;
    public String msg_show;
    SqlHelper helper;
    AreasDao areasDao;
    String type;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        Bundle bundle = this.getIntent().getExtras();
        /* 获取Bundle中的数据，注意类型和key */
        if (bundle != null) {
            type = bundle.getString("type");
            if (type.equals("add")) {
//               showMsg("aaa");
            } else {
//                showMsg("aaa");
            }
        }
        MyApplication myApplication = (MyApplication) getApplication();
        helper = myApplication.getSqlHelper();
        areasDao = new AreasDao(helper);

        // 查询数据库
        setContentView(R.layout.area_detail);
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
        Builder builder = new Builder(Activity_Area.this);
        builder.setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (msg.contains("成功")) {
                            //进入首次设置界面
                            Intent intent = new Intent();
                            intent.setClass(Activity_Area.this, IndexActivity.class);
                            startActivity(intent);
                        } else {
                            //进入首次设置界面
                            Intent intent = new Intent();
                            intent.setClass(Activity_Area.this, Activity_Area.class);
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
        new Builder(Activity_Area.this).setTitle("温馨提示")
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
                    final Areas areas = new Areas();
                    areas.danwei = danwei;
                    areas.areastatus = 1;
                    areas.area = name;
                    areas.count = 1;
                    areas.okcount = 0;
                    areas.lifeStatus = 1;
                 areasDao.insertList(new SparseArray<Areas>() { {put(0, areas);} });
                    Intent resultIntent = new Intent();
                    Activity_Area.this.setResult(RESULT_OK, resultIntent);
                    Activity_Area.this.finish();
                }
                break;
            case R.id.btnCancel:
                Intent intent = new Intent(Activity_Area.this, IndexActivity.class);
                startActivity(intent);
                Activity_Area.this.finish();
                break;
            case R.id.title_btn_sequence:
                Activity_Area.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
