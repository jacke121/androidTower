package com.baseDao;

import java.util.Date;

import android.text.TextUtils;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

import android.util.SparseArray;


public class GantaDao {
    public static final String TABLENAME = "Ganta";
    public static final Object SYNC = new Object();
    private final SQLiteOpenHelper mOpenHelper;

    public GantaDao(SQLiteOpenHelper openHelper) {
        mOpenHelper = openHelper;
    }

    public Cursor query(String whereClause, String[] whereArgs) {
        final String sql = "SELECT *"
                + " FROM " + (TextUtils.isEmpty(whereClause) ? TABLENAME : TABLENAME + " WHERE " + whereClause);
        return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);
    }

    public SparseArray<Ganta> queryBySql(String sql, String[] whereArgs) {
        Cursor cursor = null;
        int index = 0;
        try {
            synchronized (SYNC) {
                if ((cursor = mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs)) == null || cursor.getCount() < 1)
                    return null;
                SparseArray<Ganta> list = new SparseArray<Ganta>(cursor.getCount());
                while (cursor.moveToNext()) {
                    Ganta entity = new Ganta();
                    entity.id = cursor.isNull(COLUMNINDEXS.id) ? -1 : cursor.getInt(COLUMNINDEXS.id);
                    entity.name = cursor.isNull(COLUMNINDEXS.name) ? "" : cursor.getString(COLUMNINDEXS.name);
                    entity.areaid = cursor.isNull(COLUMNINDEXS.areaid) ? -1 : cursor.getInt(COLUMNINDEXS.areaid);
                    entity.dianya = cursor.isNull(COLUMNINDEXS.dianya) ? "" : cursor.getString(COLUMNINDEXS.dianya);
                    entity.caizhi = cursor.isNull(COLUMNINDEXS.caizhi) ? "" : cursor.getString(COLUMNINDEXS.caizhi);
                    entity.xingzhi = cursor.isNull(COLUMNINDEXS.xingzhi) ? "" : cursor.getString(COLUMNINDEXS.xingzhi);
                    entity.taiquid = cursor.isNull(COLUMNINDEXS.taiquid) ? -1 : cursor.getInt(COLUMNINDEXS.taiquid);
                    entity.huilu = cursor.isNull(COLUMNINDEXS.huilu) ? -1 : cursor.getInt(COLUMNINDEXS.huilu);
                    entity.yunxing = cursor.isNull(COLUMNINDEXS.yunxing) ? "" : cursor.getString(COLUMNINDEXS.yunxing);
                    entity.zuobiao = cursor.isNull(COLUMNINDEXS.zuobiao) ? "" : cursor.getString(COLUMNINDEXS.zuobiao);
                    entity.level = cursor.isNull(COLUMNINDEXS.level) ? -1 : cursor.getInt(COLUMNINDEXS.level);
                    entity.parentid = cursor.isNull(COLUMNINDEXS.parentid) ? -1 : cursor.getInt(COLUMNINDEXS.parentid);
                    entity.picquanmao = cursor.isNull(COLUMNINDEXS.picquanmao) ? "" : cursor.getString(COLUMNINDEXS.picquanmao);
                    entity.pictatou = cursor.isNull(COLUMNINDEXS.pictatou) ? "" : cursor.getString(COLUMNINDEXS.pictatou);
                    entity.picmingpai = cursor.isNull(COLUMNINDEXS.picmingpai) ? "" : cursor.getString(COLUMNINDEXS.picmingpai);
                    entity.lifeStatus = cursor.isNull(COLUMNINDEXS.lifeStatus) ? -1 : cursor.getInt(COLUMNINDEXS.lifeStatus);
                    entity.upgradeFlag = cursor.isNull(COLUMNINDEXS.upgradeFlag) ? -1 : cursor.getInt(COLUMNINDEXS.upgradeFlag);
                    entity.areaname = cursor.isNull(COLUMNINDEXS.areaname) ? "" : cursor.getString(COLUMNINDEXS.areaname);
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

    public SparseArray<Ganta> queryToList(String whereClause, String[] whereArgs) {
        Cursor cursor = null;
        int index = 0;
        try {
            synchronized (SYNC) {
                if ((cursor = query(whereClause, whereArgs)) == null || cursor.getCount() < 1)
                    return null;
                SparseArray<Ganta> list = new SparseArray<Ganta>(cursor.getCount());
                while (cursor.moveToNext()) {
                    Ganta entity = new Ganta();
                    entity.id = cursor.isNull(COLUMNINDEXS.id) ? -1 : cursor.getInt(COLUMNINDEXS.id);
                    entity.name = cursor.isNull(COLUMNINDEXS.name) ? "" : cursor.getString(COLUMNINDEXS.name);
                    entity.areaid = cursor.isNull(COLUMNINDEXS.areaid) ? -1 : cursor.getInt(COLUMNINDEXS.areaid);
                    entity.dianya = cursor.isNull(COLUMNINDEXS.dianya) ? "" : cursor.getString(COLUMNINDEXS.dianya);
                    entity.caizhi = cursor.isNull(COLUMNINDEXS.caizhi) ? "" : cursor.getString(COLUMNINDEXS.caizhi);
                    entity.xingzhi = cursor.isNull(COLUMNINDEXS.xingzhi) ? "" : cursor.getString(COLUMNINDEXS.xingzhi);
                    entity.taiquid = cursor.isNull(COLUMNINDEXS.taiquid) ? -1 : cursor.getInt(COLUMNINDEXS.taiquid);
                    entity.huilu = cursor.isNull(COLUMNINDEXS.huilu) ? -1 : cursor.getInt(COLUMNINDEXS.huilu);
                    entity.yunxing = cursor.isNull(COLUMNINDEXS.yunxing) ? "" : cursor.getString(COLUMNINDEXS.yunxing);
                    entity.zuobiao = cursor.isNull(COLUMNINDEXS.zuobiao) ? "" : cursor.getString(COLUMNINDEXS.zuobiao);
                    entity.level = cursor.isNull(COLUMNINDEXS.level) ? -1 : cursor.getInt(COLUMNINDEXS.level);
                    entity.parentid = cursor.isNull(COLUMNINDEXS.parentid) ? -1 : cursor.getInt(COLUMNINDEXS.parentid);
                    entity.picquanmao = cursor.isNull(COLUMNINDEXS.picquanmao) ? "" : cursor.getString(COLUMNINDEXS.picquanmao);
                    entity.pictatou = cursor.isNull(COLUMNINDEXS.pictatou) ? "" : cursor.getString(COLUMNINDEXS.pictatou);
                    entity.picmingpai = cursor.isNull(COLUMNINDEXS.picmingpai) ? "" : cursor.getString(COLUMNINDEXS.picmingpai);
                    entity.lifeStatus = cursor.isNull(COLUMNINDEXS.lifeStatus) ? -1 : cursor.getInt(COLUMNINDEXS.lifeStatus);
                    entity.upgradeFlag = cursor.isNull(COLUMNINDEXS.upgradeFlag) ? -1 : cursor.getInt(COLUMNINDEXS.upgradeFlag);
                    entity.areaname = cursor.isNull(COLUMNINDEXS.areaname) ? "" : cursor.getString(COLUMNINDEXS.areaname);
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

    public int insert(Ganta entity) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        try {
            entity.upgradeFlag = getUpgrade(db);
            return insert0(db, entity);
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean update(Ganta entity) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        try {
            entity.upgradeFlag = getUpgrade(db);
            return update0(db, entity, COLUMNS.id + "=?", new String[]{String.valueOf(entity.id)});
        } finally {
            if (db != null) db.close();
        }
    }

    public boolean delete(Ganta entity) {
        SQLiteDatabase db = null;
        try {
            return delete0(db = mOpenHelper.getWritableDatabase(), COLUMNS.id + "=?", new String[]{String.valueOf(entity.id)});
        } finally {
            if (db != null) db.close();
        }
    }

    public int getUpgrade(SQLiteDatabase db) {
        int strid = 0;
        Cursor cursor = db.rawQuery("select last_insert_rowid() from Ganta", null);
        if (cursor.moveToFirst())
            strid = cursor.getInt(0);
        cursor.close();
        return strid + 1;
    }

    public static final class COLUMNINDEXS {
        public static final int id = 0;
        public static final int name = 1;
        public static final int areaid = 2;
        public static final int dianya = 3;
        public static final int caizhi = 4;
        public static final int xingzhi = 5;
        public static final int taiquid = 6;
        public static final int huilu = 7;
        public static final int yunxing = 8;
        public static final int zuobiao = 9;
        public static final int level = 10;
        public static final int parentid = 11;
        public static final int picquanmao = 12;
        public static final int pictatou = 13;
        public static final int picmingpai = 14;
        public static final int createtime = 15;
        public static final int updatetime = 16;
        public static final int lifeStatus = 17;
        public static final int upgradeFlag = 18;
        public static final int areaname = 19;
        public static final int danwei = 20;
    }

    public static final class COLUMNS {
        public static final String id = "[id]";
        public static final String name = "[name]";
        public static final String areaid = "[areaid]";
        public static final String dianya = "[dianya]";
        public static final String caizhi = "[caizhi]";
        public static final String xingzhi = "[xingzhi]";
        public static final String taiquid = "[taiquid]";
        public static final String huilu = "[huilu]";
        public static final String yunxing = "[yunxing]";
        public static final String zuobiao = "[zuobiao]";
        public static final String level = "[level]";
        public static final String parentid = "[parentid]";
        public static final String picquanmao = "[picquanmao]";
        public static final String pictatou = "[pictatou]";
        public static final String picmingpai = "[picmingpai]";
        public static final String createtime = "[createtime]";
        public static final String updatetime = "[updatetime]";
        public static final String lifeStatus = "[lifeStatus]";
        public static final String upgradeFlag = "[upgradeFlag]";
        public static final String areaname = "[areaname]";
        public static final String danwei = "[danwei]";
    }

    private int insert0(SQLiteDatabase db, Ganta entity) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS.name, entity.name);
        cv.put(COLUMNS.areaid, entity.areaid);
        cv.put(COLUMNS.dianya, entity.dianya);
        cv.put(COLUMNS.caizhi, entity.caizhi);
        cv.put(COLUMNS.xingzhi, entity.xingzhi);
        cv.put(COLUMNS.taiquid, entity.taiquid);
        cv.put(COLUMNS.huilu, entity.huilu);
        cv.put(COLUMNS.yunxing, entity.yunxing);
        cv.put(COLUMNS.zuobiao, entity.zuobiao);
        cv.put(COLUMNS.level, entity.level);
        cv.put(COLUMNS.parentid, entity.parentid);
        cv.put(COLUMNS.picquanmao, entity.picquanmao);
        cv.put(COLUMNS.pictatou, entity.pictatou);
        cv.put(COLUMNS.picmingpai, entity.picmingpai);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(COLUMNS.createtime, df.format(new Date()));
        SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(COLUMNS.updatetime, dfu.format(new Date()));
        cv.put(COLUMNS.lifeStatus, entity.lifeStatus);
        cv.put(COLUMNS.upgradeFlag, entity.upgradeFlag);
        cv.put(COLUMNS.areaname, entity.areaname);
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

    private boolean update0(SQLiteDatabase db, Ganta entity, String whereClause, String[] whereArgs) {
        ContentValues cv = new ContentValues(1);
        cv.put(COLUMNS.id, entity.id);
        cv.put(COLUMNS.name, entity.name);
        cv.put(COLUMNS.areaid, entity.areaid);
        cv.put(COLUMNS.dianya, entity.dianya);
        cv.put(COLUMNS.caizhi, entity.caizhi);
        cv.put(COLUMNS.xingzhi, entity.xingzhi);
        cv.put(COLUMNS.taiquid, entity.taiquid);
        cv.put(COLUMNS.huilu, entity.huilu);
        cv.put(COLUMNS.yunxing, entity.yunxing);
        cv.put(COLUMNS.zuobiao, entity.zuobiao);
        cv.put(COLUMNS.level, entity.level);
        cv.put(COLUMNS.parentid, entity.parentid);
        cv.put(COLUMNS.picquanmao, entity.picquanmao);
        cv.put(COLUMNS.pictatou, entity.pictatou);
        cv.put(COLUMNS.picmingpai, entity.picmingpai);
        SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put(COLUMNS.updatetime, dfu.format(new Date()));
        cv.put(COLUMNS.lifeStatus, entity.lifeStatus);
        cv.put(COLUMNS.upgradeFlag, entity.upgradeFlag);
        cv.put(COLUMNS.areaname, entity.areaname);
        cv.put(COLUMNS.danwei, entity.danwei);
        return db.update(TABLENAME, cv, whereClause, whereArgs) > 0;
    }

    private boolean delete0(SQLiteDatabase db, String whereClause, String[] whereArgs) {
        return db.delete(TABLENAME, whereClause, whereArgs) > 0;
    }

    public boolean drop(SQLiteDatabase db) {
        try {
            db.execSQL("drop table if exists Ganta");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS [Ganta] (  [id] INTEGER PRIMARY KEY AUTOINCREMENT,   [name] NVARCHAR2(50),   [areaid] INTEGER,   [dianya] NVARCHAR2(50),   [caizhi] NVARCHAR2(11),   [xingzhi] NVARCHAR2(11),   [taiquid] INT,   [huilu] INT,   [yunxing] NVARCHAR2(11),   [zuobiao] NVARCHAR2(50),   [level] INT,   [parentid] INTEGER,   [picquanmao] VARCHAR2(100),   [pictatou] NVARCHAR2(100),   [picmingpai] NVARCHAR2(100),   [createtime] DATETIME NOT NULL DEFAULT (datetime('now')),   [updatetime] DATETIME NOT NULL,   [LifeStatus] INTEGER NOT NULL,   [upgradeFlag] BIGINT NOT NULL,   [areaname] NVARCHAR2(100),   [danwei] NVARCHAR2(50))");
    }
}
