package com.example.spotsmusic.food;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.spotsmusic.R;

//import com.example.diet.fitness.R;

public class FoodNameAdapter extends CursorAdapter {

	public FoodNameAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@SuppressLint("Range")
	private void setView(View view, Cursor cursor) {
		TextView foodItem = (TextView) view;
		foodItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		Log.i("test", "newView");
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.food_name_list_item, null);
		setView(view, cursor);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.i("test", "bindView");
		setView(view, cursor);
	}

	@Override
	public String convertToString(Cursor cursor) {
		Log.i("test", "toString");
		return cursor == null ? "" : cursor.getString(0);
	}
}
