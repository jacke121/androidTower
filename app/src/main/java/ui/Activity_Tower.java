package ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
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
import com.tool.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Activity_Tower extends Activity implements OnClickListener {
    public EditText ext_zuobiao, ext_towername;
    RadioGroup radio_dianya, radio_caizhi, radio_xingzhi, use_status;
    private final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private final int PHOTO_REQUEST_CUT = 3;// 结果
    TextView txt_taiqu;
    /* 头像名称 */
    private final String PHOTO_FILE_NAME = "temp_photo.jpg";
    Button btnCancel;
    Button btn_fullview, btn_towerhead, btn_nameplate;
    ImageView ivback;
    int picid;
    public String name, message;
    GantaDao gantaDao;
    Spinner sp_huilu;
    SqlHelper helper;
    ImageView iv_fullview, iv_tower_head, iv_nameplate;
    String str_fullview, str_tower_head, str_nameplate;
    AreasDao areasDao;
    Areas curentreas;
    int gantaid;
    Ganta mganta;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setContentView(R.layout.tower_detail);
        helper = ((MyApplication)getApplication()).getSqlHelper();
        gantaDao = new GantaDao(helper);
        areasDao = new AreasDao(helper);
        //设置初始化视图
        Bundle bundle = this.getIntent().getExtras();
        /* 获取Bundle中的数据，注意类型和key */
        if (bundle != null) {
            gantaid = bundle.getInt("id");
            if (gantaid == 0) {
                finish();
            }
            SparseArray<Ganta> gantas=gantaDao.queryToList("id =?", new String[]{gantaid + ""});
            if (gantas == null) {
                finish();
            }
            mganta =gantas .get(0);//模糊查询
        }

        SparseArray<Areas> areas = areasDao.queryToList("id =?", new String[]{Activity_TowerList.areaid + ""});//模糊查询
        if (areas != null) {
            curentreas = areas.get(0);
        }
        iv_fullview = (ImageView) findViewById(R.id.iv_fullview);
        iv_tower_head = (ImageView) findViewById(R.id.iv_tower_head);
        iv_nameplate = (ImageView) findViewById(R.id.iv_nameplate);
        txt_taiqu = (TextView) findViewById(R.id.txt_taiqu);
        txt_taiqu.setText(curentreas.area + curentreas.gongbian + curentreas.quxian + curentreas.qubian);

        btn_fullview = (Button) findViewById(R.id.btn_fullview);
        btn_towerhead = (Button) findViewById(R.id.btn_towerhead);
        btn_nameplate = (Button) findViewById(R.id.btn_nameplate);
        btn_fullview.setOnClickListener(this);
        btn_towerhead.setOnClickListener(this);
        btn_nameplate.setOnClickListener(this);
        ext_zuobiao = (EditText) findViewById(R.id.ext_zuobiao);
        ext_towername = (EditText) findViewById(R.id.ext_towername);
        radio_dianya = (RadioGroup) findViewById(R.id.group_voltage);
        radio_caizhi = (RadioGroup) findViewById(R.id.group_material);
        radio_xingzhi = (RadioGroup) findViewById(R.id.radio_xingzhi);
        use_status = (RadioGroup) findViewById(R.id.use_status);
        sp_huilu = (Spinner) findViewById(R.id.sp_huilu);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        Button register_btnCancel = (Button) findViewById(R.id.btnCancel);
        register_btnCancel.setOnClickListener(this);
        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        register_btnCancel.setOnClickListener(this);
        ivback = (ImageView) findViewById(R.id.title_btn_sequence);
        ivback.setOnClickListener(this);

        if(mganta !=null){
        //编辑
            File file=new File(mganta.picquanmao);
            bitmap= new FileUtil().getThumbnail(this,file);
            iv_fullview.setImageBitmap(bitmap);
             file=new File(mganta.pictatou);
            bitmap= new FileUtil().getThumbnail(this,file);
            iv_tower_head.setImageBitmap(bitmap);
             file=new File(mganta.picmingpai);
            bitmap= new FileUtil().getThumbnail(this,file);
            iv_nameplate.setImageBitmap(bitmap);
            ext_towername.setText(mganta.name);
            str_fullview=mganta.picquanmao;
            str_tower_head=mganta.pictatou;
            str_nameplate=mganta.picmingpai;
            if(mganta.caizhi.equals(" 水泥杆")){
                ((RadioButton)findViewById(R.id.shuini_pole)).setChecked(true);

            }else if(mganta.caizhi.equals(" 木杆")){
                ((RadioButton) findViewById(R.id.wooden_pole)).setChecked(true);
            }else {
                ((RadioButton) findViewById(R.id.steel_pole)).setChecked(true);
            }
            if(mganta.xingzhi.equals(" 直线")){
                ((RadioButton)findViewById(R.id.zhixian)).setChecked(true);

            }else {
                ((RadioButton) findViewById(R.id.naizhang)).setChecked(true);
            }
            if(mganta.dianya.equals(" 交流220V")){
                ((RadioButton) findViewById(R.id.voltage220)).setChecked(true);

            }else {
                ((RadioButton)findViewById(R.id.voltage380)).setChecked(true);
            }
            if(mganta.yunxing.equals(" 在运")){
                ((RadioButton) findViewById(R.id.inuse)).setChecked(true);

            }else if(mganta.yunxing.equals(" 留用")){
                ((RadioButton) findViewById(R.id.liuyong)).setChecked(true);
            }else {
                ((RadioButton)findViewById(R.id.nouse)).setChecked(true);
            }

            ext_zuobiao.setText(mganta.zuobiao);
//            sp_huilu.set
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fullview:
                if(ext_towername.getText().length()==0){
                    showMsg("名称不能为空!");
                    return;
                }
                picid = R.id.btn_fullview;
                camera();
                break;
            case R.id.btn_towerhead:
                if(ext_towername.getText().length()==0){
                    showMsg("名称不能为空!");
                    return;
                }
                picid = R.id.btn_towerhead;
                camera();
                break;
            case R.id.btn_nameplate:
                if(ext_towername.getText().length()==0){
                    showMsg("名称不能为空!");
                    return;
                }
                picid = R.id.btn_nameplate;
                camera();
                break;
            case R.id.btnCancel:
                Intent intent = new Intent(Activity_Tower.this, IndexActivity.class);
                startActivity(intent);
                Activity_Tower.this.finish();
                break;
            case R.id.title_btn_sequence:
                Activity_Tower.this.finish();
                break;
            case R.id.btn_save:


                Ganta areas = new Ganta();
                String zuobiao = ext_zuobiao.getText().toString();
                String towername = ext_towername.getText().toString();
                if (str_fullview == null) {
                    showMsg("全貌图片不能为空!");
                    return;
                }
                if (str_tower_head == null) {
                    showMsg("塔头图片不能为空!");
                    return;
                }
                if (str_nameplate == null) {
                    showMsg("铭牌图片不能为空!");
                    return;
                }

                areas.picquanmao = str_fullview;
                areas.pictatou = str_tower_head;
                areas.picmingpai = str_nameplate;

                RadioButton selectcaizhi = (RadioButton) findViewById(radio_caizhi.getCheckedRadioButtonId());
                if (selectcaizhi == null) {
                    showMsg("材质不能为空!");
                    return;
                }
                RadioButton selectxingzhi = (RadioButton) findViewById(radio_xingzhi.getCheckedRadioButtonId());
                if (selectxingzhi == null) {
                    showMsg("性质不能为空!");
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
                areas.areaid= Activity_TowerList.areaid;
                areas.areaname= curentreas.area;
                areas.xingzhi=selectxingzhi.getText().toString();
                areas.caizhi = selectcaizhi.getText().toString();
                areas.yunxing = yunxing.getText().toString();
                areas.dianya = radiodianya.getText().toString();
                areas.zuobiao = zuobiao;
                areas.name = towername;
                areas.lifeStatus = 1;
                areas.huilu = strHuilu;
                if(mganta!=null){
                    areas.id=mganta.id;
                    gantaDao.update(areas);
                }else{
                    gantaDao.insert(areas);
                }

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
                bitmap= new FileUtil().getThumbnail(this,tempFile);
                switch (picid) {
                    case R.id.btn_fullview:
                        str_fullview =new FileUtil().getSDDir("1tower")+"/"+curentreas.area+ext_towername.getText().toString()+"全貌.jpg";
                        Runnable sendable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    new FileUtil().writeStreamToSDCard( tempFile, str_fullview);
                                } catch (Exception e) {
                                }
                            }
                        };
                        new Thread(sendable).start();
                        iv_fullview.setImageBitmap(bitmap);

                        showMsg("路径："+ str_fullview);

                        break;
                    case R.id.btn_towerhead:
                        str_tower_head =new FileUtil().getSDDir("1tower") + "/" +curentreas.area+ ext_towername.getText().toString() + "塔头.jpg";
                         sendable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    new FileUtil().writeStreamToSDCard(tempFile, str_tower_head);
                                } catch (Exception e) {
                                }
                            }
                        };
                        new Thread(sendable).start();
                        iv_tower_head.setImageBitmap(bitmap);
                        showMsg("路径：" + str_tower_head);
                        break;
                    case R.id.btn_nameplate:
                       str_nameplate = new FileUtil().getSDDir("1tower") + "/" +curentreas.area+ ext_towername.getText().toString()+"铭牌.jpg";
                        sendable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    new FileUtil().writeStreamToSDCard( tempFile, str_nameplate);
                                } catch (Exception e) {
                                }
                            }
                        };
                        new Thread(sendable).start();
                        iv_nameplate.setImageBitmap(bitmap);
                        showMsg("路径：" + str_nameplate);
                        break;
                    default:
                        break;
                }
                boolean delete = tempFile.delete();
//                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(Activity_Tower.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                bitmap = data.getParcelableExtra("data");
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
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
    }
}
