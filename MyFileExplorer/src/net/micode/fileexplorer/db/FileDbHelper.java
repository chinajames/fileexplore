package net.micode.fileexplorer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore.MediaColumns;

public class FileDbHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 2;
	private static final String DB_NAME = "external.db";
	public static final String DICTIONARY_TABLE_NAME = "dictionary";
	private static String DICTIONARY_TABLE_CREATE  = 
												  "CREATE TABLE " +
												  DICTIONARY_TABLE_NAME +
												 " ("+ MediaColumns._ID+" TEXT,"//id
												  + MediaColumns.DATA + " TEXT,"//结尾类型　如：.zip
												  +MediaColumns.DATE_ADDED+" TEXT,"//添加时间
												  +MediaColumns.DISPLAY_NAME+" TEXT,"//地址path
												  +MediaColumns.TITLE+" TEXT,"//文件名
												  +MediaColumns.MIME_TYPE+" TEXT,"//文件的mime类型
												  +MediaColumns.SIZE+" TEXT,"//文件的大小
												  +MediaColumns.DATE_MODIFIED+" TEXT"
												 +")";

	public FileDbHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + DICTIONARY_TABLE_NAME);
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

}
