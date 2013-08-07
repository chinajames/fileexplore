package net.micode.fileexplorer.db.test;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.http.client.utils.URLEncodedUtils;

import net.micode.fileexplorer.MimeUtils;
import net.micode.fileexplorer.db.FileDBdao;
import net.micode.fileexplorer.db.FileData;
import net.micode.fileexplorer.db.FileUtil;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;

public class DaoTest extends AndroidTestCase{

	public void TestAdd(){
		FileDBdao dao = FileDBdao.getInstance(getContext());
		int count = 5;
		for (int i = 0; i < count; i++) {
			FileData data = new FileData();
			data._data= ".zip";
			data._display_name = "/ssss/ssss/ssss.zip";
			data._id = i+"";
			data._size = "1222222";
			data.date_added = new Date().toString();
			data.date_modified = new Date().toString();
			dao.insertFileData(data);
		}
		assertEquals(dao.getTotalCount(), count);
	}
	
	public void testAddsd(){
			FileDBdao dao = FileDBdao.getInstance(getContext());
			File file  = Environment.getExternalStorageDirectory();
			Log.e("testAddsd", file.toString());
			FileUtil.addFile(file, dao);
	}
	


}
