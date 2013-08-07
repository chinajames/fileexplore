package net.micode.fileexplorer.provider;

import java.io.File;

import net.micode.fileexplorer.db.FileDBdao;
import net.micode.fileexplorer.db.FileUtil;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class MediaScannerService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		new Thread(){
			public void run() {
				FileDBdao dao = FileDBdao.getInstance(getApplicationContext());
				File file  = Environment.getExternalStorageDirectory();
				FileUtil.addFile(file, dao);
			};
		}.start();
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		super.onStart(intent, startId);
	}
}
