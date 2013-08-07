package net.micode.fileexplorer.provider;

import java.security.Provider;

import net.micode.fileexplorer.db.FileDBdao;
import net.micode.fileexplorer.db.FileDbHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MediaProvider extends ContentProvider{
	FileDBdao dao = null;
	@Override
	public boolean onCreate() {
		dao = FileDBdao.getInstance(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = dao.getHelper().getReadableDatabase().query(FileDbHelper.DICTIONARY_TABLE_NAME, projection, selection, null, null, null, sortOrder);
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
