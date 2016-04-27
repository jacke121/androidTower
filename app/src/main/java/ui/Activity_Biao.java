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
import android.widget.TextView;

import com.baseDao.Biao;
import com.baseDao.BiaoDao;
import com.baseDao.GantaDao;
import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;

public class Activity_Biao extends Activity implements OnClickListener {
    public static EditText txt_name,ext_zuobiao;
    TextView ext_code;
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
        curentreas =(Biao)intent.getSerializableExtra("biao");

        setContentView(R.layout.lay_biao_detail);

        initView();
        if(curentreas.id!=null){
            txt_name.setText(curentreas.name);
            ext_zuobiao.setText(curentreas.zuobiao);
//        txt_gongbian = (EditText) findViewById(R.id.txt_gongbian);

        }
            ext_code.setText(curentreas.code);
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
        ext_zuobiao = (EditText) findViewById(R.id.ext_zuobiao);
//        txt_gongbian = (EditText) findViewById(R.id.txt_gongbian);
        ext_code = (TextView) findViewById(R.id.txt_biaocode);
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
                String code = ext_code.getText().toString();
                String zuobiao = ext_zuobiao.getText().toString();

                if (name.equals("") || code.equals("")) {
                    showMsg("信息不能为空!");
                } else {
                    curentreas.code=code;
                    curentreas.name=name;
                    curentreas.zuobiao=zuobiao;
                    curentreas.upgradeFlag=1l;
                    curentreas.lifeStatus=1;
                    if(curentreas.id==null){
                        biaoDao.insertList(new SparseArray<Biao>() {
                            {
                                put(0, curentreas);
                            }
                        });
                    }else{
                        biaoDao.update(curentreas);
                    }

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
