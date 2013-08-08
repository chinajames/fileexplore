package net.micode.fileexplorer.db;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import android.util.Base64;
import android.util.Log;
import net.micode.fileexplorer.MimeUtils;

public class FileUtil {

	public static  FileData getFileData(File file) {
		FileData data = new FileData();
		data.date_modified = String.valueOf(file.lastModified());
		String path = file.getAbsolutePath();
		if (path.contains(".")) {
			int start = path.lastIndexOf(".");
			String suffix = path.substring(start+1,path.length());
			data.mime_type = MimeUtils.guessMimeTypeFromExtension(suffix);
			data.title = path.substring(0, start); 
		}
		data._display_name = file.getName();
		data._data = path;
		data._id = getId(path);
		data._size = String.valueOf(file.length());
		data.date_added = String.valueOf(new Date().getTime());
		return data;
	}
	
	/**
	 * 将文件添加到数据库
	 * @param file
	 * @param dao
	 */
	public static void addFile(File file,FileDBdao dao){
		
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			Log.e("addFile", listFiles+"");
			if (listFiles!=null) {
				for (File childFile : listFiles) {
					addFile(childFile,dao);
				} 
			}
		}else if (file.isFile()) {
			FileData data = getFileData(file);
			String path = file.getAbsolutePath();
			if (path.contains(".")) {
				int start = path.lastIndexOf(".");
				String extend = path.substring(start+1,path.length());
				if(MimeUtils.guessMimeTypeFromExtension(extend)!=null){
					dao.insertFileData(data);
				}
			}
		}
	} 
	
	public static String getId(String key){
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
