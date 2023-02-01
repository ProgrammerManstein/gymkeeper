package com.example.spotsmusic.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.spotsmusic.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DBManager {
	private final int BUFF_SIZE = 8192;
	private final String DATABASE_PATH = "/data/data/com.example.spotsmusic/files";
	private final String DATABASE_FILENAME = "fitness.db";
	private Context context;

	public DBManager(Context context) {
		this.context = context;
	}

	public SQLiteDatabase openDatabase() {
		try {
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			if (!dir.exists())
				dir.mkdir();
			if (!(new File(databaseFilename)).exists()) {
				// 获得封装food.db文件的InputStream对象
				InputStream is = context.getResources().openRawResource(R.raw.fitness);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[BUFF_SIZE];
				int count = 0;
				// 复制文件
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
			return database;
		} catch (Exception e) {

		}
		return null;
	}
}
