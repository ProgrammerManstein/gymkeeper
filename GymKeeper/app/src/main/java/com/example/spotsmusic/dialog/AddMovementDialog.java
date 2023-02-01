package com.example.spotsmusic.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.spotsmusic.R;

//import com.example.diet.fitness.R;

public class AddMovementDialog extends Dialog implements View.OnClickListener {

	Activity context;
	private Button add;
	private SQLiteDatabase db;
	public EditText title;
	public EditText tab;
	public EditText group;
	public EditText ca;
	public EditText time;
	public RadioGroup type;
	String typeString;

	public AddMovementDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public AddMovementDialog(Activity context, SQLiteDatabase database) {
		super(context);
		this.context = context;
		this.db = database;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.movement_add_dialog);
		title = (EditText) findViewById(R.id.title);
		tab = (EditText) findViewById(R.id.tab);
		group = (EditText) findViewById(R.id.group);
		ca=(EditText) findViewById(R.id.ca);
		time=(EditText) findViewById(R.id.time);
		Window dialogWindow = this.getWindow();
		WindowManager m = context.getWindowManager();
		Display d = m.getDefaultDisplay();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.height = (int) (d.getHeight() * 0.9);
		p.width = (int) (d.getWidth() * 1);
		dialogWindow.setAttributes(p);
		add = (Button) findViewById(R.id.addmovement);
		add.setOnClickListener(this);
		this.setCancelable(true);
		final String[] items={"无氧训练","耐力有氧"};
		type=findViewById(R.id.type);
		type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				if(i==R.id.yes){
					typeString=items[1];
				}
				else{
					typeString=items[0];
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		int count = 0;
		Cursor cursor = db.query("Start",null,null,null,null,null,null);
		if (cursor.moveToFirst()){
			do{
				String name = cursor.getString(1);

				//判断数据库中是否已经存在输入，或者是否存在输入的信息相同的信息
				if (name==title.getText().toString()){
					count ++;
				}
			}while (cursor.moveToNext());
		}


//                    如果输入的信息不相同，也就是count没有进行运算
		if (count == 0){
			ContentValues values = new ContentValues();
			values.put("Title",title.getText().toString());
			values.put("Tab",tab.getText().toString());
			values.put("Time",Integer.parseInt(time.getText().toString()) );
			values.put("GroupTime",Integer.parseInt(group.getText().toString()));
			values.put("Ca",Integer.parseInt(ca.getText().toString()));
			values.put("Type",typeString);
			long res = db.insert("Start",null,values);

			values.clear();

			Toast.makeText(getContext(),"保存成功！" + String.valueOf(res),Toast.LENGTH_SHORT).show();
			this.dismiss();

		}else{
			Toast.makeText(getContext(),"已有此运动！",Toast.LENGTH_SHORT).show();
		}

	}
}
