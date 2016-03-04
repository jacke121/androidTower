package com.baseDao;

import java.util.Date;

import android.text.TextUtils;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

import android.util.SparseArray;


public class AreasDao {
    public static final String TABLENAME = "Areas";
    public static final Object SYNC = new Object();
    private final SQLiteOpenHelper mOpenHelper;

    public AreasDao(SQLiteOpenHelper openHelper) {
        mOpenHelper = openHelper;
    }

    public Cursor query(String whereClause, String[] whereArgs) {
        final String sql = "SELECT *"
                + " FROM " + (TextUtils.isEmpty(whereClause) ? TABLENAME : TABLENAME + " WHERE " + whereClause);
        return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);
    }


    public SparseArray<Areas> queryToList(String whereClause, String[] whereArgs) {
        Cursor cursor = null;
        int index = 0;
        try {
            synchronized (SYNC) {
                if ((cursor = query(whereClause, whereArgs)) == null || cursor.getCount() < 1)
                    return null;
                SparseArray<Areas> list = new SparseArray<Areas>(cursor.getCount());
                while (cursor.moveToNext()) {
                    Areas entity = new Areas();
                    entity.id = cursor.isNull(COLUMNINDEXS.id) ? -1 : cursor.getInt(COLUMNINDEXS.id);
                    entity.area = cursor.isNull(COLUMNINDEXS.area) ? "" : cursor.getString(COLUMNINDEXS.area);
                    entity.areastatus = cursor.isNull(COLUMNINDEXS.areastatus) ? -1 : cursor.getInt(COLUMNINDEXS.areastatus);
                    entity.count = cursor.isNull(COLUMNINDEXS.count) ? -1 : cursor.getInt(COLUMNINDEXS.count);
                    entity.okcount = cursor.isNull(COLUMNINDEXS.okcount) ? -1 : cursor.getInt(COLUMNINDEXS.okcount);
                    entity.lifeStatus = cursor.isNull(COLUMNINDEXS.lifeStatus) ? -1 : cursor.getInt(COLUMNINDEXS.lifeStatus);
                    entity.upgradeFlag = cursor.isNull(COLUMNINDEXS.upgradeFlag) ? -1 : cursor.getInt(COLUMNINDEXS.upgradeFlag);
                    entity.gongbian = cursor.isNull(COLUMNINDEXS.gongbian) ? "" : cursor.getString(COLUMNINDEXS.gongbian);
                    entity.quxian = cursor.isNull(COLUMNINDEXS.quxian) ? "" : cursor.getString(COLUMNINDEXS.quxian);
                    entity.qubian = cursor.isNull(COLUMNINDEXS.qubian) ? "" : cursor.getString(COLUMNINDEXS.qubian);
                    entity.danwei = cursor.isNull(COLUMNINDEXS.danwei) ? "" : cursor.getString(COLUMNINDEXS.danwei);
                    list.append(index++, entity);
                }
                ;
                cursor.close();
                return list;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public int insert(Areas entity) {
        SQLiteDatabase db = null;
        try {
            return insert0(db = mOpenHelper.getWritableDatabase(), entity);
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean update(Areas entity) {
        SQLiteDatabase db = null;
        try {
            return update0(db = mOpenHelper.getWritableDatabase(), entity, COLUMNS.id + "=?", new String[]{String.valueOf(entity.id)});
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean delete(Areas entity) {
        SQLiteDatabase db = null;
        try {
            return delete0(db = mOpenHelper.getWritableDatabase(), COLUMNS.id + "=?", new String[]{String.valueOf(entity.id)});
        } finally {
            if (db != null) db.close();
        }
    }

    public static final class COLUMNINDEXS {
        public static final int id = 0;
        public static final int area = 1;
        public static final int areastatus = 2;
        public static final int count = 3;
        public static final int okcount = 4;
        public static final int createtime = 5;
        public static final int updatetime = 6;
        public static final int lifeStatus = 7;
        public static final int upgradeFlag = 8;
        public static final int gongbian = 9;
        public static final int quxian = 10;
        public static final int qubian = 11;
        public static final int danwei = 12;
    }

    public static final class COLUMNS {
        public static final String id = "[id]";
        public static final String area = "[area]";
        public static final String areastatus = "[areastatus]";
        public static final String count = "[count]";
        public static final String okcount = "[okcount]";
        public static final String createtime = "[createtime]";
        public static final String updatetime = "[updatetime]";
        public static final String lifeStatus = "[lifeStatus]";
        public static final String upgradeFlag = "[upgradeFlag]";
        public static final String gongbian = "[gongbian]";
        public static final String quxian = "[quxian]";
        public static final String qubian = "[qubian]";
        public static final String danwei = "[danwei]";
    }

    private int insert0(SQLiteDatabase db, Areas entity) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS.area, entity.area);
        cv.put(COLUMNS.areastatus, entity.areastatus);
        cv.put(COLUMNS.count, entity.count);
        cv.put(COLUMNS.okcount, entity.okcount);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(COLUMNS.createtime, df.format(new Date()));
        SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(COLUMNS.updatetime, dfu.format(new Date()));
        cv.put(COLUMNS.lifeStatus, entity.lifeStatus);
        cv.put(COLUMNS.upgradeFlag, entity.upgradeFlag);
        cv.put(COLUMNS.gongbian, entity.gongbian);
        cv.put(COLUMNS.quxian, entity.quxian);
        cv.put(COLUMNS.qubian, entity.qubian);
        cv.put(COLUMNS.danwei, entity.danwei);
        int strid = -1;
        if (db.insert(TABLENAME, null, cv) > 0) {
            Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLENAME, null);
            if (cursor.moveToFirst())
                strid = cursor.getInt(0);
            cursor.close();
        }
        return strid;
    }

    private boolean update0(SQLiteDatabase db, Areas entity, String whereClause, String[] whereArgs) {
        ContentValues cv = new ContentValues(1);
        cv.put(COLUMNS.id, entity.id);
        cv.put(COLUMNS.area, entity.area);
        cv.put(COLUMNS.areastatus, entity.areastatus);
        cv.put(COLUMNS.count, entity.count);
        cv.put(COLUMNS.okcount, entity.okcount);
        SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(COLUMNS.updatetime, dfu.format(new Date()));
        cv.put(COLUMNS.lifeStatus, entity.lifeStatus);
        cv.put(COLUMNS.upgradeFlag, entity.upgradeFlag);
        cv.put(COLUMNS.gongbian, entity.gongbian);
        cv.put(COLUMNS.quxian, entity.quxian);
        cv.put(COLUMNS.qubian, entity.qubian);
        cv.put(COLUMNS.danwei, entity.danwei);
        return db.update(TABLENAME, cv, whereClause, whereArgs) > 0;
    }

    private boolean delete0(SQLiteDatabase db, String whereClause, String[] whereArgs) {
        return db.delete(TABLENAME, whereClause, whereArgs) > 0;
    }

    public boolean drop(SQLiteDatabase db) {
        try {
            db.execSQL("drop table if exists Areas");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS [Areas] (  [id] INTEGER PRIMARY KEY,   [area] VARCHAR2(50),   [areastatus] INT,   [count] INT,   [okcount] INT,   [createtime] DATETIME NOT NULL DEFAULT (datetime('now')),   [updatetime] DATETIME NOT NULL,   [LifeStatus] INTEGER NOT NULL,   [upgradeFlag] BIGINT NOT NULL,   [gongbian] NVARCHAR2(20),   [quxian] NVARCHAR2(30),   [qubian] NVARCHAR2(30),   [danwei] NVARCHAR2(40))");
    }
}
