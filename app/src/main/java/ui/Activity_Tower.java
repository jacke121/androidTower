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
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baseDao.Areas;
import com.baseDao.AreasDao;
import com.baseDao.Ganta;
import com.baseDao.GantaDao;
import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Activity_Tower extends Activity implements OnClickListener {
    public EditText ext_zuobiao, ext_towername, txtParenttower;
    RadioGroup radio_dianya, radio_caizhi, radio_xingzhi, use_status;
    private final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private final int PHOTO_REQUEST_CUT = 3;// 结果
    TextView txt_taiqu;
    /* 头像名称 */
    private final String PHOTO_FILE_NAME = "temp_photo.jpg";
    Button btnCancel;
    Button pic_fullview;
    Button tower_head;
    Button nameplate;
    ImageView ivback;
    CheckBox is_reply;
    public Dialog mdlg;
    public String name, message;
    GantaDao gantaDao;
    Spinner sp_huilu;
    SqlHelper helper;
    ImageView iv_fullview, iv_tower_head, iv_nameplate;
    File file_fullview, file_tower_head, file_nameplate;
    AreasDao areasDao;
    Areas curentreas;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setContentView(R.layout.tower_detail);
        //设置初始化视图
        areasDao = new AreasDao(((MyApplication) getApplication()).getSqlHelper());
        SparseArray<Areas> areas = areasDao.queryToList("id =?", new String[]{Activity_TowerList.id + ""});//模糊查询
        if (areas != null) {
            curentreas = areas.get(0);
        }
        iv_fullview = (ImageView) findViewById(R.id.iv_fullview);
        iv_tower_head = (ImageView) findViewById(R.id.iv_tower_head);
        iv_nameplate = (ImageView) findViewById(R.id.iv_nameplate);
        txt_taiqu = (TextView) findViewById(R.id.txt_taiqu);
        txt_taiqu.setText(curentreas.area + curentreas.gongbian + curentreas.quxian + curentreas.qubian);
        MyApplication myApplication = (MyApplication) getApplication();
        helper = myApplication.getSqlHelper();
        gantaDao = new GantaDao(helper);
        pic_fullview = (Button) findViewById(R.id.pic_fullview);
        tower_head = (Button) findViewById(R.id.tower_head);
        nameplate = (Button) findViewById(R.id.nameplate);
        pic_fullview.setOnClickListener(this);
        tower_head.setOnClickListener(this);
        nameplate.setOnClickListener(this);
        ext_zuobiao = (EditText) findViewById(R.id.ext_zuobiao);
        ext_towername = (EditText) findViewById(R.id.txt_towername);
        txtParenttower = (EditText) findViewById(R.id.txtParenttower);
        radio_dianya = (RadioGroup) findViewById(R.id.group_voltage);
        radio_caizhi = (RadioGroup) findViewById(R.id.group_material);
        radio_xingzhi = (RadioGroup) findViewById(R.id.radio_xingzhi);
        use_status = (RadioGroup) findViewById(R.id.use_status);
        sp_huilu = (Spinner) findViewById(R.id.sp_huilu);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        is_reply = (CheckBox) findViewById(R.id.is_reply);
        Button register_btnCancel = (Button) findViewById(R.id.btnCancel);
        register_btnCancel.setOnClickListener(this);
        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
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
            case R.id.btnCancel:
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
            case R.id.btn_save:
                Ganta areas = new Ganta();
                String zuobiao = ext_zuobiao.getText().toString();
                String towername = ext_towername.getText().toString();
                String parenttower = txtParenttower.getText().toString();
                if (file_fullview == null) {
                    showMsg("全貌图片不能为空!");
                    return;
                }
                areas.picquanmao = file_fullview.getAbsolutePath();
                if (is_reply.isChecked()) {
                    if (parenttower.length() > 0) {
                        SparseArray<Ganta> parents = gantaDao.queryToList("name like ?", new String[]{"%" + parenttower + "%"});//模糊查询
                        if (parents != null) {
                            areas.parentid = parents.get(0).id;
                        }
                    } else {
                        showMsg("上一级杆塔不能为空!");
                        return;
                    }
                }
                RadioButton selectcaizhi = (RadioButton) findViewById(radio_caizhi.getCheckedRadioButtonId());
                if (selectcaizhi == null) {
                    showMsg("材质不能为空!");
                    return;
                }
                RadioButton radiodianya = (RadioButton) findViewById(radio_dianya.getCheckedRadioButtonId());
                if (radiodianya == null) {
                    showMsg("电压不能为空!");
                    return;
                }
                RadioButton yunxing = (RadioButton) findViewById(use_status.getCheckedRadioButtonId());
                if (yunxing == null) {
                    showMsg("运行状态不能为空!");
                    return;
                }
                int strHuilu = Integer.parseInt(sp_huilu.getSelectedItem().toString());
                if (zuobiao.equals("") || towername.equals("")) {
                    showMsg("坐标点号不能为空!");
                    return;
                }
                areas.caizhi = selectcaizhi.getText().toString();
                areas.yunxing = yunxing.getText().toString();
                areas.dianya = radiodianya.getText().toString();
                areas.zuobiao = zuobiao;
                areas.name = towername;
                areas.lifeStatus = 1;
                areas.huilu = strHuilu;
                gantaDao.insert(areas);
                Intent resultIntent = new Intent();
                Activity_Tower.this.setResult(RESULT_OK, resultIntent);
                Activity_Tower.this.finish();

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

    File tempFile;
    private Bitmap bitmap;

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
//                iv_fullview, iv_tower_head, iv_nameplate;
//                iv_fullview.setImageBitmap(bitmap);
//                file_fullview, file_tower_head, file_nameplate;
                file_fullview = new File(Environment.getExternalStorageDirectory(),
                        "aaalbg.jpg");
                saveMyBitmap(bitmap, file_fullview);
                boolean delete = tempFile.delete();
                System.out.println("delete = " + delete);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (3 == requestCode && data != null) {
            Bundle b = data.getExtras(); // data为B中回传的Intent
            String str = b.getString("barcode");// str即为回传的值"Hello, this is B speaking"
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveMyBitmap(Bitmap mBitmap, File upFile) {
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

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例
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
}
