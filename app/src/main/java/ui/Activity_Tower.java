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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lbg.yan01.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class Activity_Tower extends Activity implements OnClickListener {
    public static EditText reg_username, reg_password, reg_repassword, edtValidCode,
            register_npwd, register_rpwd;
    Button btnCancel;
    Button pic_fullview;
    Button tower_head;
    Button nameplate;
    ImageView ivback;
    String msg = null;
    public static Dialog mdlg;
    public static String name, pwd, rpwd, registerMessage, uuid, uuid_psw, message;
    public String msg_show;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setContentView(R.layout.tower_detail);
        //设置初始化视图

        pic_fullview = (Button) findViewById(R.id.pic_fullview);
        tower_head = (Button) findViewById(R.id.tower_head);
        nameplate = (Button) findViewById(R.id.nameplate);
        pic_fullview.setOnClickListener(this);
        tower_head.setOnClickListener(this);
        nameplate.setOnClickListener(this);
        reg_username = (EditText) findViewById(R.id.register_eTxtUser);
        reg_username = (EditText) findViewById(R.id.register_eTxtUser);
        reg_password = (EditText) findViewById(R.id.register_eTxtPwd);
        reg_repassword = (EditText) findViewById(R.id.register_eTxtPwdSure);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        Button register_btnCancel = (Button) findViewById(R.id.btnCancel);
        register_btnCancel.setOnClickListener(this);
        Button btn_save = (Button) findViewById(R.id.btn_save);
        register_btnCancel.setOnClickListener(this);
        ivback = (ImageView) findViewById(R.id.title_btn_sequence);
        ivback.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic_fullview:
                showMsg("pic_fullview");
                break;
            case R.id.tower_head:
                showMsg("tower_head");
                break;
            case R.id.register_btnCancel:
                Intent intent = new Intent(Activity_Tower.this, IndexActivity.class);
                startActivity(intent);
                Activity_Tower.this.finish();
                break;
            case R.id.title_btn_sequence:
                Activity_Tower.this.finish();
                break;
            case R.id.nameplate:
                showMsg("nameplate");
                break;
            default:
                break;
        }
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
                    if (mdlg != null) {
                        mdlg.dismiss();
                        new Builder(Activity_Tower.this)
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
                case 13:
                    if (mdlg != null) {
                        mdlg.dismiss();
                        mdlg = null;

                        if (msg_show != null && msg_show.equals("恭喜您注册成功")) {
                            //用户存储

                            showregisterMessage(msg_show);
                        } else if (msg_show.equals("用户名已存在")) {
                            showregisterMessage("用户名已存在,请重新输入！");
                        } else {
                            showregisterMessage("注册失败！");

                        }

                    }
                    break;
            }
        }
    };

    public void showregisterMessage(final String msg) {
        // 创建提示框提醒是否登录成功
        Builder builder = new Builder(Activity_Tower.this);
        builder.setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (msg.contains("成功")) {
                            //进入首次设置界面
                            Intent intent = new Intent();
                            intent.setClass(Activity_Tower.this, IndexActivity.class);
                            startActivity(intent);
                        } else {
                            //进入首次设置界面
                            Intent intent = new Intent();
                            intent.setClass(Activity_Tower.this, Activity_Tower.class);
                            startActivity(intent);
                        }


                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void showMsg(String msg) {
        new Builder(Activity_Tower.this).setTitle("温馨提示")
                .setMessage(msg + "!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
    }

    /**
     * 获取Struts2 Http 登录的请求信息
     *
     * @param userName
     * @param password
     * @return
     */
    public String registerRemoteService(String userName, String password, String repassword) {

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("userName", reg_username.getText().toString());
        params.put("password", reg_password.getText().toString());
        params.put("repassword", reg_repassword.getText().toString());

        return msg;
    }


}
