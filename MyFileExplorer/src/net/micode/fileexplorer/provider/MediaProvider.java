package net.micode.fileexplorer.provider;

import net.micode.fileexplorer.db.FileDBdao;
import net.micode.fileexplorer.db.FileDbHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MediaProvider extends ContentProvider{
	private static final String TAG = "MediaProvider";
//	FileDBdao dao = null;
	public static final String content_uri = "content://net.micode.fileexplorer/dictionary";
private FileDbHelper helper;
	@Override
	public boolean onCreate() {
		helper = new FileDbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.e(TAG, uri.toString()+"selection="+selection+"selectionArgs="+selectionArgs);
		Cursor c = helper.getReadableDatabase().query(FileDbHelper.DICTIONARY_TABLE_NAME, projection, selection, null, null, null, sortOrder);
//        c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
