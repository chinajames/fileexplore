package net.micode.fileexplorer.db;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import net.micode.fileexplorer.BuildConfig;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class FileDBdao {
	private static final String TAG = "FileDBdao";
	private static FileDBdao instance= null;
	private static FileDbHelper helper;
	private FileDBdao(){};
	public static FileDBdao getInstance(Context context){
		if (instance==null) {
			instance = new FileDBdao();
		}
		if (helper ==null) {
			helper = new FileDbHelper(context);
		}
		return instance;
	}
	public FileDbHelper getHelper(){
		return helper;
	}
	public void insertFileData(FileData data){
		SQLiteDatabase db = null;
		try {
			if (data==null) {
				throw new RuntimeException("data can not be null");
			}
			if (TextUtils.isEmpty(data._id)) {
				throw new RuntimeException("data's _id can not be empty");
			}
			ContentValues values = new ContentValues();
			Field[] fields = data.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				if (field.getName().equalsIgnoreCase("serialVersionUID")) {
					continue;
				}
				values.put(field.getName(),field.get(data) == null ? null : field.get(data).toString());
				// 通过ContentValue中的数据拼接sql语句,执行插入操作,id 为表中的一个列名
			}
			db = helper.getWritableDatabase();
			db.insert(FileDbHelper.DICTIONARY_TABLE_NAME, null, values);
			Log.e(TAG, "insert success");
		} catch (IllegalArgumentException e) {
			if (BuildConfig.DEBUG) {
				Log.e("IllegalArgumentException",
						data._display_name,e);
			}
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			if (BuildConfig.DEBUG) {
				Log.e("IllegalArgumentException",
						data._display_name,e);
			}
			throw new RuntimeException(e);
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}
	
	/**
	 * 通过路径查找文件信息
	 * @param path
	 * @return
	 */
	public FileData getFileDataByPath(String path){
		String id = FileUtil.getId(path);
		SQLiteDatabase db = null;
		FileData data = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			String selection = MediaColumns._ID+"=?";
			String[] selectionArgs = {id};
			cursor = db.query(FileDbHelper.DICTIONARY_TABLE_NAME, null, selection, selectionArgs, null, null, null);
			if (cursor.moveToFirst()) {
				data = new FileData();
				String[] columnNames = cursor.getColumnNames();
				Field[] fields = data.getClass().getDeclaredFields();
				for (String columnName : columnNames) {
					for (Field field : fields) {
						if (columnName.equalsIgnoreCase(field.getName())) {
							field.setAccessible(true);
							field.set(data, cursor.getString(cursor.getColumnIndex(columnName)));
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}finally{
			if (cursor!=null) {
				cursor.close();
			}
			if (db!=null) {
				db.close();
			}
		}
		return data;
	}
	/**
	 * 通过条件查找filedata
	 * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
	 * @param You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
	 * @return 查询到的结果
	 */
	public List<FileData> getFileDataByPath(String selection,String[] selectionArgs){
//		String id = getId(path);
		List<FileData> files = new ArrayList<FileData>();
		SQLiteDatabase db = null;
		FileData data = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
//			String selection = MediaColumns._ID+"=?";
//			String[] selectionArgs = {id};
			cursor = db.query(FileDbHelper.DICTIONARY_TABLE_NAME, null, selection, selectionArgs, null, null, null);
			while (cursor.moveToNext()) {
				data = new FileData();
				String[] columnNames = cursor.getColumnNames();
				Field[] fields = data.getClass().getDeclaredFields();
				for (String columnName : columnNames) {
					for (Field field : fields) {
						if (columnName.equalsIgnoreCase(field.getName())) {
							field.setAccessible(true);
							field.set(data, cursor.getString(cursor.getColumnIndex(columnName)));
						}
					}
				}
				files.add(data);
				data = null;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}finally{
			if (cursor!=null) {
				cursor.close();
			}
			if (db!=null) {
				db.close();
			}
		}
		return files;
	}
	public void updataFileData(FileData data){
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			Field[] fields = data.getClass().getDeclaredFields();
			ContentValues values = new ContentValues();
			for (Field field : fields) {
				 if (field.getName().equalsIgnoreCase(MediaColumns._ID)) {
					continue;
				}
				 values.put(field.getName(), field.get(data)==null?null:field.get(data).toString());
			}
			String whereClause = MediaColumns._ID+"=?";
			String[] whereArgs = {FileUtil.getId(data._display_name)};
			db.update(FileDbHelper.DICTIONARY_TABLE_NAME, values, whereClause, whereArgs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}finally{
			if (db!=null) {
				db.close();
			}
		}
	}
	/**
	 * 删除一条filedata
	 * @param data
	 */
	public void delFileData(FileData data){
		SQLiteDatabase db = null;
		try {
				db = helper.getWritableDatabase();
				String whereClause = MediaColumns._ID+"=?";
				String[] whereArgs = {FileUtil.getId(data._display_name)};
				db.delete(FileDbHelper.DICTIONARY_TABLE_NAME, whereClause, whereArgs);
			
		} finally{
			if (db!=null) {
				db.close();
			}
		}
	}
	
	
	
	/**
	 * 删除所有的记录
	 */
	public void delAllFilaData(){
			SQLiteDatabase db = null;
			try {
				db = helper.getWritableDatabase();
				db.delete(FileDbHelper.DICTIONARY_TABLE_NAME, null, null);
			} finally{
				if (db!=null) {
					db.close();
				}
			}
			
	}
	/**
	 * 获取所有的记录数目
	 */
	public int  getTotalCount(){
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			Cursor query = db.query(FileDbHelper.DICTIONARY_TABLE_NAME, new String[]{"count(_id)"}, null, null, null, null, null);
			if (query.moveToFirst()) {
				return query.getInt(0);
			}
		} finally{
			if (db!=null) {
				db.close();
			}
		}
		return 0;
		
	}

}
