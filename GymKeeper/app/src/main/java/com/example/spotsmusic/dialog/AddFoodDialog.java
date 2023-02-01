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

//import com.example.diet.fitness.R;

public class AddFoodDialog extends Dialog implements View.OnClickListener {

	Activity context;
	private Button addfood_save;
	private SQLiteDatabase database;
	public EditText addfood_name;
	public EditText addfood_type;
	public EditText addfood_nutrient;

	public AddFoodDialog(Activity context) {
		super(context);
		this.context = context;
	}

	public AddFoodDialog(Activity context, SQLiteDatabase database) {
		super(context);
		this.context = context;
		this.database = database;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.food_add_dialog);
		addfood_name = (EditText) findViewById(R.id.addfood_name);
		addfood_type = (EditText) findViewById(R.id.addfood_type);
		addfood_nutrient = (EditText) findViewById(R.id.addfood_nutrient);
		Window dialogWindow = this.getWindow();
		WindowManager m = context.getWindowManager();
		Display d = m.getDefaultDisplay();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.height = (int) (d.getHeight() * 0.6);
		p.width = (int) (d.getWidth() * 0.8);
		dialogWindow.setAttributes(p);
		addfood_save = (Button) findViewById(R.id.addfood_save);
		addfood_save.setOnClickListener(this);
		this.setCancelable(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addfood_save:
			String sql = "insert into food values(?, ?, ?)";
			String name = this.addfood_name.getText().toString().trim();
			String type = this.addfood_type.getText().toString().trim();
			String nutrient = this.addfood_nutrient.getText().toString().trim();
			if (name=="" || nutrient=="" || nutrient.split(";").length < 3)
				break;
			double protein = Double.parseDouble(nutrient.split(";")[0].split(":")[1]);
			double fat = Double.parseDouble(nutrient.split(";")[1].split(":")[1]);
			double carbohydrate = Double.parseDouble(nutrient.split(";")[2].split(":")[1]);
			double heat = protein*4 + fat*9 + carbohydrate*4; 
			nutrient = "热量(千卡):"+ String.format("%.0f", heat) +";" + nutrient;
			database.execSQL(sql, new String[] { name, type, nutrient });
			
			Toast toast=Toast.makeText(context, "成功添加食品数据", Toast.LENGTH_SHORT);
			toast.show();
			
			this.dismiss();
			break;
		}
	}
}
