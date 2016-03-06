package com.lbg.yan01;

//Download by http://www.codefans.net
import android.app.Application;
import android.view.WindowManager;

import com.baseDao.SqlHelper;

public class MyApplication extends Application {

	public SqlHelper getSqlHelper() {
		if(sqlHelper==null) {
			String DATABASE_PATH = "/data/data/"
					+ getPackageName() + "/databases/";
			DATABASE_PATH="";
			sqlHelper = new SqlHelper(this, DATABASE_PATH + "tower", 1);
		}
		return sqlHelper;
	}
	private SqlHelper sqlHelper;
}
