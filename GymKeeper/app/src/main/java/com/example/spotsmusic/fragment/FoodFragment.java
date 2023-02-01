package com.example.spotsmusic.fragment;

import static com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotsmusic.R;
import com.example.spotsmusic.dao.DBManager;
import com.example.spotsmusic.dialog.AddDietDialog;
import com.example.spotsmusic.dialog.AddFoodDialog;
import com.example.spotsmusic.food.FoodNameAdapter;
import com.example.spotsmusic.food.NutrientAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Instances of this class are fragments representing a single
// object in our collection.
public class FoodFragment extends Fragment implements TextWatcher {
    private SQLiteDatabase database;
    private AutoCompleteTextView actvFood;
    private Button searchBtn;
    private Button addfoodBtn;
    private Button adddietBtn;
    private Button deletefoodBtn;
    private TextView foodName;
    private ListView nutrientList;
    private AddFoodDialog addfoodDialog;
    private AddDietDialog adddietDialog;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_activity, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        context=getContext();

        DBManager dbmanager = new DBManager(context);
        database = dbmanager.openDatabase();

        actvFood = (AutoCompleteTextView)view.findViewById(R.id.actvFood);
        actvFood.addTextChangedListener(this);

        searchBtn = (Button) view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFood();
            }
        });

        addfoodBtn = (Button) view.findViewById(R.id.addfoodBtn);
        addfoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood();
            }
        });

        foodName = (TextView) view.findViewById(R.id.nutrientResult);

        adddietBtn = (Button) view.findViewById(R.id.adddietBtn);
        adddietBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDiet();
            }
        });

        deletefoodBtn = (Button) view.findViewById(R.id.deletefoodBtn);
        deletefoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        nutrientList = (ListView) view.findViewById(R.id.nutrientList);
    }

    private void searchFood() {
        String sql = "select name,type,nutrient from food where name=?";
        if (actvFood.getText().toString().isEmpty())
            return;
        Cursor cursor = database.rawQuery(sql, new String[] { actvFood.getText().toString() });
        String result = "未找到该食品";
        if (cursor.getCount() > 0) {
            // 必须使用moveToFirst方法将记录指针移动到第1条记录的位置
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("name")) + "-";
            result = result + cursor.getString(cursor.getColumnIndex("type"));
            String nutrient = cursor.getString(cursor.getColumnIndex("nutrient"));
            NutrientAdapter nutrientAdapter = new NutrientAdapter();
            nutrientList.setAdapter(nutrientAdapter.getAdapter(context, nutrient));
            adddietBtn.setVisibility(View.VISIBLE);
            deletefoodBtn.setVisibility(View.VISIBLE);
        } else {
            nutrientList.setAdapter(null);
            adddietBtn.setVisibility(View.INVISIBLE);
            deletefoodBtn.setVisibility(View.INVISIBLE);
        }
        foodName.setText(result);
    }

    private void addFood() {
        addfoodDialog = new AddFoodDialog(requireActivity(), database);
        addfoodDialog.show();
    }

    private void addDiet() {
        String name = foodName.getText().toString().split("-")[0];
        TextView heat_tv = (TextView)nutrientList.getChildAt(0).findViewById(R.id.nutrient_content);
        TextView protein_tv = (TextView)nutrientList.getChildAt(1).findViewById(R.id.nutrient_content);
        TextView fat_tv = (TextView)nutrientList.getChildAt(2).findViewById(R.id.nutrient_content);
        TextView carbohydrate_tv = (TextView)nutrientList.getChildAt(3).findViewById(R.id.nutrient_content);
        String nutrient = heat_tv.getText().toString() + ";" + protein_tv.getText().toString() + ";" + fat_tv.getText().toString() + ";" + carbohydrate_tv.getText().toString();
        adddietDialog = new AddDietDialog(requireActivity(), database, name, nutrient);
        adddietDialog.show();
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(requireActivity()).setTitle("提示").setMessage("确定删除该食物数据?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteFood();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        }).show();
    }

    private void deleteFood() {
        String sql = "delete from food where name=?";
        String name = foodName.getText().toString().split("-")[0];
        database.execSQL(sql, new String[] { name });
        adddietBtn.setVisibility(View.INVISIBLE);
        deletefoodBtn.setVisibility(View.INVISIBLE);

        Toast toast=Toast.makeText(context, "成功删除食品数据", Toast.LENGTH_SHORT);
        //显示toast信息
        toast.show();
    }

    // AutoCompleteTextView的重写函数3个
    @Override
    public void afterTextChanged(Editable s) {
        Cursor cursor = database.rawQuery("select name as _id from food where name like ?",
                new String[] { "%" + s.toString() + "%" });
        FoodNameAdapter myAdapter = new FoodNameAdapter(context, cursor, true);
        actvFood.setAdapter(myAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        if(actvFood.getAdapter() == null) {
            Cursor cursor = database.rawQuery("select name as _id from food where name like ?",
                    new String[] { "%" + s.toString() + "%" });
            FoodNameAdapter myAdapter = new FoodNameAdapter(context, cursor, true);
            actvFood.setAdapter(myAdapter);
        }
    }
}
