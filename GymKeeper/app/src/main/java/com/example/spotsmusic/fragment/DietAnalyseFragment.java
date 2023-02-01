package com.example.spotsmusic.fragment;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotsmusic.R;
import com.example.spotsmusic.analysis.PieChartView;
import com.example.spotsmusic.dao.DBManager;
import com.example.spotsmusic.dialog.AddDietDialog;
import com.example.spotsmusic.dialog.AddFoodDialog;
import com.example.spotsmusic.food.FoodNameAdapter;
import com.example.spotsmusic.food.NutrientAdapter;

import java.util.ArrayList;
import java.util.List;

// Instances of this class are fragments representing a single
// object in our collection.
public class DietAnalyseFragment extends Fragment {
    private SQLiteDatabase database;
    private PieChartView pieChartView;
    private TextView text_left;
    private TextView text_right;
    private Spinner timeSpinner;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.analysis_activity, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        context=getContext();

        DBManager dbmanager = new DBManager(context);
        database = dbmanager.openDatabase();

        pieChartView = (PieChartView) view.findViewById(R.id.analysis_pie_chart);
        text_left = (TextView) view.findViewById(R.id.analysis_text_left);
        text_right = (TextView) view.findViewById(R.id.analysis_text_right);
        timeSpinner = (Spinner) view.findViewById(R.id.analysis_time);

        setData();

    }

    private void showChart(String time) {
        Cursor cursor = database.rawQuery("select sum(carbohydrate) as total_carbohydrate, sum(protein) as total_protein, sum(fat) as total_fat, sum(heat) as total_heat from diet where time=?", new String[] { time });
        cursor.moveToFirst();
        Double total_carbohydrate = cursor.getDouble(0);
        Double total_protein = cursor.getDouble(1);
        Double total_fat = cursor.getDouble(2);
        Double total_heat = cursor.getDouble(3);
        PieChartView.PieItemBean[] items = new PieChartView.PieItemBean[]{
                new PieChartView.PieItemBean("碳水化合物", (float)(total_carbohydrate * 4)),
                new PieChartView.PieItemBean("脂肪", (float)(total_fat * 9)),
                new PieChartView.PieItemBean("蛋白质", (float)(total_protein * 4))
        };
        pieChartView.setPieItems(items);

        text_left.setText("总热量(大卡):\n碳水化合物(克):\n蛋白质(克):\n脂肪(克):");
        text_right.setText(String.format("%.0f", total_heat) +"\n" + String.format("%.2f", total_carbohydrate) +"\n" + String.format("%.2f", total_protein) +"\n" + String.format("%.2f", total_fat));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setData() {
        List time_list = new ArrayList();
        Cursor cursor = database.rawQuery("select distinct time from diet", null);
        while(cursor.moveToNext()){
            time_list.add(cursor.getString(0));
        }
        ArrayAdapter timeAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, time_list);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //样式

        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setSelection(time_list.size() - 1, true);
        timeSpinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner timeSp = (Spinner) parent;
            String time = (String) timeSp.getItemAtPosition(position);
            showChart(time);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

}
