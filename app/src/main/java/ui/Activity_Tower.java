package ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lbg.yan01.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Activity_Tower extends Activity implements OnClickListener {
    public static EditText reg_username, reg_password, reg_repassword;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
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
        this.mFace = (ImageView) findViewById(R.id.iv_fullview);

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
                camera();
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
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }
    File  tempFile;
    private Bitmap bitmap;
    private ImageView mFace;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(Activity_Tower.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                bitmap = data.getParcelableExtra("data");
                this.mFace.setImageBitmap(bitmap);

                File upFile = new File(Environment.getExternalStorageDirectory(),
                         "aaalbg.jpg");
                saveMyBitmap( bitmap,upFile);
                 boolean delete = tempFile.delete();
                 System.out.println("delete = " + delete);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (3 == requestCode && data != null) {
            Bundle b = data.getExtras(); // data为B中回传的Intent
            String str = b.getString("barcode");// str即为回传的值"Hello, this is B speaking"

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public void saveMyBitmap( Bitmap mBitmap,File upFile) {
        // 首先将byte数组转为bitmap
        try {
            FileOutputStream out = new FileOutputStream(upFile);
            if (mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
    /**
     * 剪切图片
     *
     * @function:
     * @author:Jerry
     * @date:2013-12-30
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例�?�?
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
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
