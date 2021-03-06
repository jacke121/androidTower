package ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.lbg.yan01.R;

import java.io.File;

import exceltest.JXLUtil;
import exceltest.KeypointAcquisitionBean;

public class MainActivity extends Activity {

	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.step_one);

		btn = (Button)findViewById(R.id.btn_login);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						Activity_AreaList.class);
				startActivityForResult(intent, 1);
//				initData();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static final String[] COLNAME_ARR = { "序号", "第一列",
	"第二列" };
public static final int[] COLWIDTH_ARR = { 5, 15, 30};//单元格宽度
public static final String[] FIELD_ARR = { "Guid","Isupload", "Line", "Lineid", "Pile", "Remarks", "temperature"
	                                      , "Testtime", "Userid", "Value", "Voltage", "weather", "year"};//要写入exceld的字段
public static final String[] FIELD_ARR1 = { "序号", "Guid","Isupload", "Line", "Lineid", "Pile", "Remarks", "temperature"
    , "Testtime", "Userid", "Value", "Voltage", "weather", "year"};//要写入exceld的字段

	public static void makeDir(File dir) {
		if(! dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}
	File file;
	public void initData(){
		file = new File(getSDPath()+"/Test");
		makeDir(file);
		new JXLUtil().initExcel(file.toString() + "/text.xls", FIELD_ARR1, COLWIDTH_ARR);


		SparseArray<KeypointAcquisitionBean> list = new SparseArray<>();
		for(int i=0;i<25;i++){
			KeypointAcquisitionBean bean = new KeypointAcquisitionBean();
			bean.setGuid(i+"");
			bean.setIsupload(0);
			bean.setLine("北京管线");
			bean.setLineid("北京管线"+i);
			bean.setPile("KK"+i);
			bean.setRemarks("北京KK"+i);
			bean.setTemperature("星球");
			bean.setTesttime("2014-1-3");
			bean.setUserid("小明");
			bean.setValue("222"+i);
			bean.setVoltage("1111"+i);
			bean.setWeather("晴天");
			bean.setYear("2014");
			list.put(i,bean);
		}
		new JXLUtil().writeObjListToExcel(list, getSDPath() + "/Test/text.xls", FIELD_ARR, this);
	}
	public String getSDPath(){ 
		File sdDir = null; 
		boolean sdCardExist = Environment.getExternalStorageState() 
		.equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if (sdCardExist) 
		{ 
		sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
		} 
		String dir = sdDir.toString();
		return dir; 

		} 
}


