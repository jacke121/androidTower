package ui;
import java.util.LinkedHashMap;
import java.util.Map;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.baseDao.Areas;
import com.baseDao.AreasDao;
import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;

import webClient.DialogFactory;

public class Activity_Area extends Activity implements OnClickListener {
    public static EditText txt_qubian, txt_quxian, txt_gongbian, txt_danwei;
    Button btn_save;
    ImageView title_btn_sequence;
    String msg = null;
    public static Dialog mdlg;
    public String msg_show;
    SqlHelper helper;
    AreasDao areasDao;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
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
        txt_qubian = (EditText) findViewById(R.id.txt_qubian);
        txt_quxian = (EditText) findViewById(R.id.txt_quxian);
        txt_gongbian = (EditText) findViewById(R.id.txt_gongbian);
        txt_danwei = (EditText) findViewById(R.id.txt_danwei);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        Button register_btnCancel = (Button) findViewById(R.id.register_btnCancel);
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

    public String registerRemoteService(String userName, String password, String repassword) {
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("userName", txt_qubian.getText().toString());
        params.put("password", txt_quxian.getText().toString());
        params.put("repassword", txt_gongbian.getText().toString());
        return msg;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
               String qubian = txt_qubian.getText().toString();
                String  quxian = txt_quxian.getText().toString();
                String gongbian = txt_gongbian.getText().toString();
//					registerRemoteService(name,pwd);
                if (qubian.equals("") || quxian.equals("") || gongbian.equals("")) {
                    showMsg("信息不能为空!");
                } else {
                    Areas areas = new Areas();
                    areas.city = "";
                    areas.area = "";
                    areasDao.insert(areas);
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
}
