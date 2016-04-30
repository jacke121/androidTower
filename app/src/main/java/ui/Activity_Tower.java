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
import com.google.android.gms.appindexing.Action;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;
import com.tool.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_Tower extends Activity implements OnClickListener {
    public EditText ext_zuobiao, ext_towername;
    RadioGroup radio_dianya, radio_caizhi, radio_xingzhi, use_status;
    private final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private final int PHOTO_REQUEST_CUT = 3;// 结果
    TextView txt_taiqu;
    /* 头像名称 */
    private String PHOTO_FILE_NAME = "temp_photo.jpg";
    Button btnCancel;
    Button btn_fullview, btn_towerhead, btn_nameplate;
    Bitmap bm_fullview, bm_towerhead, bm_nameplate;
    File file_fullview, file_towerhead, file_nameplate;
    File tempFile;
    ImageView ivback;
    int picid;
    public String name, message;
    GantaDao gantaDao;
    Spinner sp_huilu;
    SqlHelper helper;
    ImageView iv_fullview, iv_tower_head, iv_nameplate;
    String str_fullview, str_tower_head, str_nameplate;

    AreasDao areasDao;
    static Areas curentreas;
    Ganta parentGanta;
    Ganta currentGanta;
    int gantatype;//0代表新增空杆塔，1代表新增父杆塔，2代表子杆塔，3代表修改杆塔
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    String areaname, baseString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setContentView(R.layout.tower_detail);
        helper = ((MyApplication) getApplication()).getSqlHelper();
        gantaDao = new GantaDao(helper);
        areasDao = new AreasDao(helper);
        //设置初始化视图
        gantatype = this.getIntent().getIntExtra("type", -1);
        /* 获取Bundle中的数据，注意类型和key */
        if (gantatype == 0) {
            parentGanta = null;
        } else {
            parentGanta = (Ganta) this.getIntent().getSerializableExtra("ganta");
        }

        SparseArray<Areas> areas = areasDao.queryToList("id =?", new String[]{Activity_TowerList.areaid + ""});//模糊查询
        if (areas != null) {
            curentreas = areas.get(0);
        }

        char[] srcChar;
        areaname = curentreas.area;
//        areaname = new PinyinTool().toPinYin(curentreas.area);//.makeStringByStringSet(Pinyin.getPinyin(curentreas.area));
        baseString = new FileUtil().getSDDir("1tower/" + areaname) + "/";
        iv_fullview = (ImageView) findViewById(R.id.iv_fullview);
        iv_tower_head = (ImageView) findViewById(R.id.iv_tower_head);
        iv_nameplate = (ImageView) findViewById(R.id.iv_nameplate);
        txt_taiqu = (TextView) findViewById(R.id.txt_taiqu);


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
        Button btn_save = (Button) findViewById(R.id.btn_save);

        ivback = (ImageView) findViewById(R.id.title_btn_sequence);
        if (parentGanta != null && gantatype == 3) {
            //编辑
            File file = new File(parentGanta.picquanmao);
            bitmap = new FileUtil().getThumbnail(this, file);
            iv_fullview.setImageBitmap(bitmap);
            file = new File(parentGanta.pictatou);
            bitmap = new FileUtil().getThumbnail(this, file);
            iv_tower_head.setImageBitmap(bitmap);
            file = new File(parentGanta.picmingpai);
            bitmap = new FileUtil().getThumbnail(this, file);
            iv_nameplate.setImageBitmap(bitmap);
            ext_towername.setText(parentGanta.name);
            str_fullview = parentGanta.picquanmao;
            str_tower_head = parentGanta.pictatou;
            str_nameplate = parentGanta.picmingpai;
            if (parentGanta.caizhi.equals(" 水泥杆")) {
                ((RadioButton) findViewById(R.id.shuini_pole)).setChecked(true);

            } else if (parentGanta.caizhi.equals(" 木杆")) {
                ((RadioButton) findViewById(R.id.wooden_pole)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.steel_pole)).setChecked(true);
            }
            if (parentGanta.xingzhi.equals(" 直线")) {
                ((RadioButton) findViewById(R.id.zhixian)).setChecked(true);

            } else {
                ((RadioButton) findViewById(R.id.naizhang)).setChecked(true);
            }
            if (parentGanta.dianya.equals(" 交流220V")) {
                ((RadioButton) findViewById(R.id.voltage220)).setChecked(true);

            } else {
                ((RadioButton) findViewById(R.id.voltage380)).setChecked(true);
            }
            if (parentGanta.yunxing.equals(" 在运")) {
                ((RadioButton) findViewById(R.id.inuse)).setChecked(true);

            } else if (parentGanta.yunxing.equals(" 留用")) {
                ((RadioButton) findViewById(R.id.liuyong)).setChecked(true);
            } else {
                ((RadioButton) findViewById(R.id.nouse)).setChecked(true);
            }

            ext_zuobiao.setText(parentGanta.zuobiao);
//            sp_huilu.set
        } else if (parentGanta != null && gantatype == 2) {
            if (parentGanta.name.length() > 3) {
                String str = parentGanta.name.substring(parentGanta.name.length() - 3);
                int index = getNumbers(str);
                if (index > 0) {
                    str = str.replace(index + "", (index + 1) + "");
                    String name = parentGanta.name.substring(0, parentGanta.name.length() - 3) + str;
                    ext_towername.setText(name);
                } else {
                    ext_towername.setText(parentGanta.name);
                }
            } else {
                ext_towername.setText(parentGanta.name);
            }
        }

        if (txt_taiqu != null) {
            txt_taiqu.setText(curentreas.area);// + curentreas.gongbian + curentreas.quxian + curentreas.qubian);
            register_btnCancel.setOnClickListener(this);
            btn_save.setOnClickListener(this);
            register_btnCancel.setOnClickListener(this);
            ivback.setOnClickListener(this);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    //截取数字
    public int getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            if (matcher.group(0).length() > 0) {
                return Integer.parseInt(matcher.group(0));
            } else {
                return -1;
            }
        }
        return -1;
    }

    // 截取非数字
    public String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
    //onSaveInstanceState

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        其实这里什么都不要做
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fullview:
                if (ext_towername.getText().length() == 0) {
                    showMsg("名称不能为空!");
                    return;
                }
                picid = R.id.btn_fullview;
                PHOTO_FILE_NAME = ext_towername.getText().toString() + "全貌.jpg";
                camera();
                break;
            case R.id.btn_towerhead:
                if (ext_towername.getText().length() == 0) {
                    showMsg("名称不能为空!");
                    return;
                }
                picid = R.id.btn_towerhead;

                PHOTO_FILE_NAME = ext_towername.getText().toString() + "塔头.jpg";
                camera();
                break;
            case R.id.btn_nameplate:
                if (ext_towername.getText().length() == 0) {
                    showMsg("名称不能为空!");
                    return;
                }
                picid = R.id.btn_nameplate;
                PHOTO_FILE_NAME = ext_towername.getText().toString() + "铭牌.jpg";
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
                currentGanta = new Ganta();
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

                currentGanta.picquanmao = str_fullview;
                currentGanta.pictatou = str_tower_head;
                currentGanta.picmingpai = str_nameplate;
                bm_fullview = new FileUtil().createImageThumbnail(Environment.getExternalStorageDirectory() + "/" + ext_towername.getText().toString() + "全貌.jpg");

                new FileUtil().saveMyBitmap(bm_fullview, str_fullview);

                File aaa = new File(Environment.getExternalStorageDirectory() + "/" + ext_towername.getText().toString() + "全貌.jpg");
                if (aaa.exists()) {
                    aaa.delete();
                }
                bm_towerhead = new FileUtil().createImageThumbnail(Environment.getExternalStorageDirectory() + "/" + ext_towername.getText().toString() + "塔头.jpg");

                new FileUtil().saveMyBitmap(bm_towerhead, str_tower_head);

                aaa = new File(Environment.getExternalStorageDirectory() + "/" + ext_towername.getText().toString() + "塔头.jpg");
                if (aaa.exists()) {
                    aaa.delete();
                }
                bm_nameplate = new FileUtil().createImageThumbnail(Environment.getExternalStorageDirectory() + "/" + ext_towername.getText().toString() + "铭牌.jpg");

                new FileUtil().saveMyBitmap(bm_nameplate, str_nameplate);

                aaa = new File(Environment.getExternalStorageDirectory() + "/" + ext_towername.getText().toString() + "铭牌.jpg");
                if (aaa.exists()) {
                    aaa.delete();
                }
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
                currentGanta.areaid = Activity_TowerList.areaid;
                currentGanta.areaname = curentreas.area;
                currentGanta.xingzhi = selectxingzhi.getText().toString();
                currentGanta.caizhi = selectcaizhi.getText().toString();
                currentGanta.yunxing = yunxing.getText().toString();
                currentGanta.dianya = radiodianya.getText().toString();
                currentGanta.zuobiao = zuobiao;
                currentGanta.name = towername;
                currentGanta.lifeStatus = 1;
                currentGanta.huilu = strHuilu;
                if (parentGanta != null) {
                    if (gantatype == 1) {
                        gantaDao.insertList(new SparseArray<Ganta>() {
                            {
                                put(0, currentGanta);
                            }
                        });
                        parentGanta.parentid = currentGanta.id;
                        gantaDao.update(parentGanta);
                    } else if (gantatype == 2) {
                        currentGanta.parentid = parentGanta.id;
                        gantaDao.insertList(new SparseArray<Ganta>() {
                            {
                                put(0, currentGanta);
                            }
                        });
                    } else if (gantatype == 3) {
                        currentGanta.id = parentGanta.id;
                        gantaDao.update(currentGanta);
                    }
                } else {
                    gantaDao.insertList(new SparseArray<Ganta>() {
                        {
                            put(0, currentGanta);
                        }
                    });
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


    private Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {

                tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                bitmap = new FileUtil().getThumbnail(this, tempFile);

                switch (picid) {
                    case R.id.btn_fullview:

                        str_fullview = baseString + curentreas.area + ext_towername.getText().toString() + "全貌.jpg";
                        Runnable sendable = new Runnable() {
                            @Override
                            public void run() {
                                try {

//                                    new FileUtil().writeStreamToSDCard( tempFile, str_fullview);
                                } catch (Exception e) {
                                }
                            }
                        };
                        new Thread(sendable).start();
                        iv_fullview.setImageBitmap(bitmap);

                        showMsg("路径：" + str_fullview);

                        break;
                    case R.id.btn_towerhead:
                        bm_towerhead = new FileUtil().createImageThumbnail(Environment.getExternalStorageDirectory() + "/" + PHOTO_FILE_NAME);

                        str_tower_head = baseString + curentreas.area + ext_towername.getText().toString() + "塔头.jpg";
                        sendable = new Runnable() {
                            @Override
                            public void run() {
                                try {
//                                    new FileUtil().saveMyBitmap(bitmap, str_fullview);
//                                    new FileUtil().writeStreamToSDCard(tempFile, str_tower_head);
                                } catch (Exception e) {
                                }
                            }
                        };
                        new Thread(sendable).start();
                        iv_tower_head.setImageBitmap(bitmap);
                        showMsg("路径：" + str_tower_head);
                        break;
                    case R.id.btn_nameplate:
                        bm_nameplate = new FileUtil().createImageThumbnail(Environment.getExternalStorageDirectory() + "/" + PHOTO_FILE_NAME);
                        str_nameplate = baseString + curentreas.area + ext_towername.getText().toString() + "铭牌.jpg";
                        sendable = new Runnable() {
                            @Override
                            public void run() {
                                try {
//                                    new FileUtil().saveMyBitmap(bitmap, str_fullview);
//                                    new FileUtil().writeStreamToSDCard( tempFile, str_nameplate);
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
//                boolean delete = tempFile.delete();
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

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            // land do nothing is ok
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // port do nothing is ok
//        }
//    }

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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Activity_Tower Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ui/http/host/path")
        );
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Activity_Tower Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ui/http/host/path")
        );
    }
}
