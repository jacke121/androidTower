package com.baseDao;

import java.util.Date;

import android.text.TextUtils;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

import android.util.SparseArray;
import android.database.sqlite.SQLiteStatement;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AreasDao {
    Lock lock = new ReentrantLock();
    SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String TABLENAME = "Areas";
    COLUMNINDEXS cOLUMNINDEXS = new COLUMNINDEXS();
    COLUMNS cOLUMNS = new COLUMNS();
    public final Object SYNC = new Object();
    private final SQLiteOpenHelper mOpenHelper;

    public AreasDao(SQLiteOpenHelper openHelper) {
        mOpenHelper = openHelper;
        mOpenHelper.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS [Areas] (  [id] INTEGER PRIMARY KEY AUTOINCREMENT,   [area] VARCHAR2(50),   [areastatus] INT,   [count] INT,   [okcount] INT,   [createtime] DATETIME NOT NULL DEFAULT (datetime('now')),   [updatetime] DATETIME NOT NULL,   [LifeStatus] INTEGER NOT NULL,   [upgradeFlag] BIGINT NOT NULL,   [gongbian] NVARCHAR2(20),   [quxian] NVARCHAR2(30),   [qubian] NVARCHAR2(30),   [danwei] NVARCHAR2(40))");
    }

    public Cursor query(String whereClause, String[] whereArgs) {
        final String sql = "SELECT *"
                + " FROM " + (TextUtils.isEmpty(whereClause) ? TABLENAME : TABLENAME + " WHERE " + whereClause);
        return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);
    }


    public boolean insertList(SparseArray<Areas> list) {
        if (null == list || list.size() <= 0) {
            return false;
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        try {
            String sql = "insert into Areas("
                    + cOLUMNS.id + "," + cOLUMNS.area + "," + cOLUMNS.areastatus + "," + cOLUMNS.count + "," + cOLUMNS.okcount + "," + cOLUMNS.createtime + "," + cOLUMNS.updatetime + "," + cOLUMNS.lifeStatus + "," + cOLUMNS.upgradeFlag + "," + cOLUMNS.gongbian + "," + cOLUMNS.quxian + "," + cOLUMNS.qubian + "," + cOLUMNS.danwei + ") " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                Areas entity = list.get(i);
                if (null == entity.id) stat.bindNull(1);
                else
                    stat.bindLong(1, entity.id);
                if (null == entity.area || entity.area.length() == 0) stat.bindNull(2);
                else
                    stat.bindString(2, entity.area);
                if (null == entity.areastatus) stat.bindNull(3);
                else
                    stat.bindLong(3, entity.areastatus);
                if (null == entity.count) stat.bindNull(4);
                else
                    stat.bindLong(4, entity.count);
                if (null == entity.okcount) stat.bindNull(5);
                else
                    stat.bindLong(5, entity.okcount);
                stat.bindString(6, dfu.format(new Date()));
                stat.bindString(7, dfu.format(new Date()));
                if (null == entity.lifeStatus) stat.bindLong(8, 1);
                else
                    stat.bindLong(8, entity.lifeStatus);
                if (null == entity.upgradeFlag) stat.bindLong(9, 1);
                else
                    stat.bindLong(9, entity.upgradeFlag);
                if (null == entity.gongbian || entity.gongbian.length() == 0) stat.bindNull(10);
                else
                    stat.bindString(10, entity.gongbian);
                if (null == entity.quxian || entity.quxian.length() == 0) stat.bindNull(11);
                else
                    stat.bindString(11, entity.quxian);
                if (null == entity.qubian || entity.qubian.length() == 0) stat.bindNull(12);
                else
                    stat.bindString(12, entity.qubian);
                if (null == entity.danwei || entity.danwei.length() == 0) stat.bindNull(13);
                else
                    stat.bindString(13, entity.danwei);
                long result = stat.executeInsert();
                if (result < 0) {
                    return false;
                }
                entity.id = (int) result;
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean updateList(SparseArray<Areas> list) {
        if (null == list || list.size() <= 0) {
            return false;
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        try {
            String sql = "update Areas set id=?,area=?,areastatus=?,count=?,okcount=?,createtime=?,updatetime=?,lifeStatus=?,upgradeFlag=?,gongbian=?,quxian=?,qubian=?,danwei=? where id=?";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                Areas entity = list.get(i);
                if (null == entity.id) stat.bindNull(1);
                else
                    stat.bindLong(1, entity.id);
                if (null == entity.area || entity.area.length() == 0) stat.bindNull(2);
                else
                    stat.bindString(2, entity.area);
                if (null == entity.areastatus) stat.bindNull(3);
                else
                    stat.bindLong(3, entity.areastatus);
                if (null == entity.count) stat.bindNull(4);
                else
                    stat.bindLong(4, entity.count);
                if (null == entity.okcount) stat.bindNull(5);
                else
                    stat.bindLong(5, entity.okcount);
                stat.bindString(6, dfu.format(new Date()));
                stat.bindString(7, dfu.format(new Date()));
                if (null == entity.lifeStatus) stat.bindLong(8, 1);
                else
                    stat.bindLong(8, entity.lifeStatus);
                if (null == entity.upgradeFlag) stat.bindLong(9, 1);
                else
                    stat.bindLong(9, entity.upgradeFlag);
                if (null == entity.gongbian || entity.gongbian.length() == 0) stat.bindNull(10);
                else
                    stat.bindString(10, entity.gongbian);
                if (null == entity.quxian || entity.quxian.length() == 0) stat.bindNull(11);
                else
                    stat.bindString(11, entity.quxian);
                if (null == entity.qubian || entity.qubian.length() == 0) stat.bindNull(12);
                else
                    stat.bindString(12, entity.qubian);
                if (null == entity.danwei || entity.danwei.length() == 0) stat.bindNull(13);
                else
                    stat.bindString(13, entity.danwei);
                stat.bindLong(14, entity.id);
                long result = stat.executeUpdateDelete();
                if (result < 0) {
                    return false;
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public SparseArray<Areas> queryBySql(String sql, String[] whereArgs) {
        Cursor cursor = null;
        int index = 0;
        try {
            synchronized (SYNC) {
                if ((cursor = mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs)) == null || cursor.getCount() < 1)
                    return null;
                SparseArray<Areas> list = new SparseArray<Areas>(cursor.getCount());
                while (cursor.moveToNext()) {
                    Areas entity = new Areas();
                    entity.id = cursor.isNull(cOLUMNINDEXS.id) ? -1 : cursor.getInt(cOLUMNINDEXS.id);
                    entity.area = cursor.isNull(cOLUMNINDEXS.area) ? "" : cursor.getString(cOLUMNINDEXS.area);
                    entity.areastatus = cursor.isNull(cOLUMNINDEXS.areastatus) ? -1 : cursor.getInt(cOLUMNINDEXS.areastatus);
                    entity.count = cursor.isNull(cOLUMNINDEXS.count) ? -1 : cursor.getInt(cOLUMNINDEXS.count);
                    entity.okcount = cursor.isNull(cOLUMNINDEXS.okcount) ? -1 : cursor.getInt(cOLUMNINDEXS.okcount);
                    entity.lifeStatus = cursor.isNull(cOLUMNINDEXS.lifeStatus) ? -1 : cursor.getInt(cOLUMNINDEXS.lifeStatus);
                    entity.upgradeFlag = cursor.isNull(cOLUMNINDEXS.upgradeFlag) ? 0 : cursor.getLong(cOLUMNINDEXS.upgradeFlag);
                    entity.gongbian = cursor.isNull(cOLUMNINDEXS.gongbian) ? "" : cursor.getString(cOLUMNINDEXS.gongbian);
                    entity.quxian = cursor.isNull(cOLUMNINDEXS.quxian) ? "" : cursor.getString(cOLUMNINDEXS.quxian);
                    entity.qubian = cursor.isNull(cOLUMNINDEXS.qubian) ? "" : cursor.getString(cOLUMNINDEXS.qubian);
                    entity.danwei = cursor.isNull(cOLUMNINDEXS.danwei) ? "" : cursor.getString(cOLUMNINDEXS.danwei);
                    list.append(index++, entity);
                }
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
                    entity.id = cursor.isNull(cOLUMNINDEXS.id) ? -1 : cursor.getInt(cOLUMNINDEXS.id);
                    entity.area = cursor.isNull(cOLUMNINDEXS.area) ? "" : cursor.getString(cOLUMNINDEXS.area);
                    entity.areastatus = cursor.isNull(cOLUMNINDEXS.areastatus) ? -1 : cursor.getInt(cOLUMNINDEXS.areastatus);
                    entity.count = cursor.isNull(cOLUMNINDEXS.count) ? -1 : cursor.getInt(cOLUMNINDEXS.count);
                    entity.okcount = cursor.isNull(cOLUMNINDEXS.okcount) ? -1 : cursor.getInt(cOLUMNINDEXS.okcount);
                    entity.lifeStatus = cursor.isNull(cOLUMNINDEXS.lifeStatus) ? -1 : cursor.getInt(cOLUMNINDEXS.lifeStatus);
                    entity.upgradeFlag = cursor.isNull(cOLUMNINDEXS.upgradeFlag) ? 0 : cursor.getLong(cOLUMNINDEXS.upgradeFlag);
                    entity.gongbian = cursor.isNull(cOLUMNINDEXS.gongbian) ? "" : cursor.getString(cOLUMNINDEXS.gongbian);
                    entity.quxian = cursor.isNull(cOLUMNINDEXS.quxian) ? "" : cursor.getString(cOLUMNINDEXS.quxian);
                    entity.qubian = cursor.isNull(cOLUMNINDEXS.qubian) ? "" : cursor.getString(cOLUMNINDEXS.qubian);
                    entity.danwei = cursor.isNull(cOLUMNINDEXS.danwei) ? "" : cursor.getString(cOLUMNINDEXS.danwei);
                    list.append(index++, entity);
                }
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

    public boolean update(Areas entity) {
        lock.lock();
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        try {
            return update0(db, entity, cOLUMNS.id + "=?", new String[]{String.valueOf(entity.id)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
    }

    public boolean clearData(SQLiteDatabase db) {
        try {
            db.execSQL("delete from Areas");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Areas entity) {
        SQLiteDatabase db = null;
        try {
            return delete0(db = mOpenHelper.getWritableDatabase(), cOLUMNS.id + "=?", new String[]{String.valueOf(entity.id)});
        } finally {
        }
    }

    public Long getUpgrade(SQLiteDatabase db) {
        Long strid = 0l;
        Cursor cursor = mOpenHelper.getReadableDatabase().rawQuery("select last_insert_rowid() from Areas", null);
        if (cursor.moveToFirst())
            strid = cursor.getLong(0);
        cursor.close();
        return strid + 1;
    }

    public final class COLUMNINDEXS {
        public final int id = 0;
        public final int area = 1;
        public final int areastatus = 2;
        public final int count = 3;
        public final int okcount = 4;
        public final int createtime = 5;
        public final int updatetime = 6;
        public final int lifeStatus = 7;
        public final int upgradeFlag = 8;
        public final int gongbian = 9;
        public final int quxian = 10;
        public final int qubian = 11;
        public final int danwei = 12;
    }

    public final class COLUMNS {
        public final String id = "[id]";
        public final String area = "[area]";
        public final String areastatus = "[areastatus]";
        public final String count = "[count]";
        public final String okcount = "[okcount]";
        public final String createtime = "[createtime]";
        public final String updatetime = "[updatetime]";
        public final String lifeStatus = "[lifeStatus]";
        public final String upgradeFlag = "[upgradeFlag]";
        public final String gongbian = "[gongbian]";
        public final String quxian = "[quxian]";
        public final String qubian = "[qubian]";
        public final String danwei = "[danwei]";
    }

    private boolean update0(SQLiteDatabase db, Areas entity, String whereClause, String[] whereArgs) {
        ContentValues cv = new ContentValues(1);
        cv.put(cOLUMNS.id, entity.id);
        cv.put(cOLUMNS.area, entity.area);
        cv.put(cOLUMNS.areastatus, entity.areastatus);
        cv.put(cOLUMNS.count, entity.count);
        cv.put(cOLUMNS.okcount, entity.okcount);
        cv.put(cOLUMNS.updatetime, dfu.format(new Date()));
        cv.put(cOLUMNS.lifeStatus, entity.lifeStatus);
        cv.put(cOLUMNS.upgradeFlag, entity.upgradeFlag);
        cv.put(cOLUMNS.gongbian, entity.gongbian);
        cv.put(cOLUMNS.quxian, entity.quxian);
        cv.put(cOLUMNS.qubian, entity.qubian);
        cv.put(cOLUMNS.danwei, entity.danwei);
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
}
