package com.example.spotsmusic.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spotsmusic.R;

import java.util.Date;

public class AddDietDialog extends Dialog implements View.OnClickListener {

	private Activity context;
	private Button adddiet_save;
	private SQLiteDatabase database;
	private String name;
	private String nutrient;
	public EditText adddiet_comment;
	public EditText adddiet_name;
	public EditText adddiet_count;
	

	public AddDietDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public AddDietDialog(Activity context, SQLiteDatabase database, String name, String nutrient) {
		super(context);
		this.context = context;
		this.database = database;
		this.name = name;
		this.nutrient = nutrient;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.diet_add_dialog);
		adddiet_comment = (EditText) findViewById(R.id.adddiet_comment);
		adddiet_name = (EditText) findViewById(R.id.adddiet_name);
		adddiet_name.setText(this.name);
		adddiet_count = (EditText) findViewById(R.id.adddiet_count);
		Window dialogWindow = this.getWindow();
		WindowManager m = context.getWindowManager();
		Display d = m.getDefaultDisplay();
		// 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		// 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.6);
		// 高度设置为屏幕的0.6
		p.width = (int) (d.getWidth() * 0.8);
		// 宽度设置为屏幕的0.8
		dialogWindow.setAttributes(p);
		// 根据id在布局中找到控件对象
		adddiet_save = (Button) findViewById(R.id.adddiet_save);
		// 为按钮绑定点击事件监听器
		adddiet_save.setOnClickListener(this);
		this.setCancelable(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.adddiet_save:
			//id integer primary key autoincrement,time text,comment text,name text,count double,heat double,protein double,fat double,carbohydrate double
			String sql = "insert into diet values(null, ?, ?, ?, ?, ?, ?, ?, ?)";
			String comment = this.adddiet_comment.getText().toString().trim();
			String count = this.adddiet_count.getText().toString().trim();
			if (comment.isEmpty() || count.isEmpty())
				break;
			Date date = new Date();
			String time = String.format("%tY%tm%td", date, date, date);
			double db_count = Double.parseDouble(count);
			double db_heat = db_count / 100.0 * Double.parseDouble(nutrient.split(";")[0]);
			double db_protein = db_count / 100.0 * Double.parseDouble(nutrient.split(";")[1]);
			double db_fat = db_count / 100.0 * Double.parseDouble(nutrient.split(";")[2]);
			double db_carbohydrate = db_count / 100.0 * Double.parseDouble(nutrient.split(";")[3]);
			db_heat = Double.parseDouble(new java.text.DecimalFormat("#.00").format(db_heat));//������λС��
			db_protein = Double.parseDouble(new java.text.DecimalFormat("#.00").format(db_protein));
			db_fat = Double.parseDouble(new java.text.DecimalFormat("#.00").format(db_fat));
			db_carbohydrate = Double.parseDouble(new java.text.DecimalFormat("#.00").format(db_carbohydrate));
			//Log.d("test", time +"-" + db_count +"-"+db_heat +"-"+db_protein +"-"+db_fat +"-"+db_carbohydrate);
			database.execSQL(sql, new Object[] {time, comment, this.name, db_count, db_heat, db_protein, db_fat, db_carbohydrate});
			
			Toast toast=Toast.makeText(context, "成功添加饮食", Toast.LENGTH_SHORT);
			toast.show();
			
			this.dismiss();
			break;
		}
	}
}