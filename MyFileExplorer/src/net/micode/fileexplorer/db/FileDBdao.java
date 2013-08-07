package net.micode.fileexplorer.db;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.micode.fileexplorer.BuildConfig;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class FileDBdao {
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
				Object object = field.get(data);
				values.put(field.getName(),
						object == null ? null : object.toString());
				// 通过ContentValue中的数据拼接sql语句,执行插入操作,id 为表中的一个列名
			}
			db = helper.getWritableDatabase();
			db.insert(FileDbHelper.DICTIONARY_TABLE_NAME, null, values);
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
	
	public String getId(String key){
		 MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(key.getBytes());
			byte input[] = md.digest();
			return Base64.encodeToString(input, 0, input.length, Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
