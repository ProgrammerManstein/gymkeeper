package com.example.spotsmusic.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spotsmusic.R;
import com.example.spotsmusic.analysis.PieChartView;
import com.example.spotsmusic.dao.DBManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Instances of this class are fragments representing a single
// object in our collection.
public class DietFragment extends Fragment {
    private ListView listview;
    private SQLiteDatabase database;
    private TextView diet_total;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.diet_activity, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        context=getContext();

        listview = (ListView)view.findViewById(R.id.diet_list);
        diet_total = (TextView)view.findViewById(R.id.diet_total);

        DBManager dbmanager = new DBManager(context);
        database = dbmanager.openDatabase();

        setData();
        itemOnLongClick();

    }

    private void setData() {
        Date date = new Date();
        String time = String.format("%tY%tm%td", date, date, date);
        Cursor cursor = database.rawQuery("select id as _id, comment, name, count, heat, protein, fat, carbohydrate from diet where time=?", new String[] { time });
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context, R.layout.diet_list_item, cursor,
                new String[] { "_id", "comment", "heat", "name", "count", "protein", "fat", "carbohydrate" },
                new int[] {R.id.diet_list_item_id, R.id.diet_list_item_comment, R.id.diet_list_item_heat, R.id.diet_list_item_name, R.id.diet_list_item_count,
                        R.id.diet_list_item_protein, R.id.diet_list_item_fat, R.id.diet_list_item_carbohydrate});
        listview.setAdapter(adapter);
        Cursor cursor_total = database.rawQuery("select sum(heat) as total from diet where time=?", new String[] { time });
        cursor_total.moveToFirst();
        String total = String.format("%.0f", cursor_total.getDouble(0));
        diet_total.setText("今日总热量" + total + "大卡");
    }

    private void itemOnLongClick() {
        this.listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(context)
                        .setItems(R.array.menu, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String[] PK = getResources().getStringArray(R.array.menu);
                                if(PK[which].equals("删除")) {
                                    //Log.i("test", "itemDelete:"+position);
                                    itemDelete(position);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
                return true;
            }
        });
    }

    private void itemDelete(int position) {
        //Log.i("test", "itemdelete...");
        try{
            //listview的getChildat只能获取当前页面显示的item,滚动条以下的item获取后使用（第二条语句）会nullpointerexecption
            //View view = this.listview.getChildAt(position);
            //TextView item_id = (TextView)view.findViewById(R.id.diet_list_item_id);
            //改成先获取adapter再取item即可解决问题
            TextView item_id = (TextView)listview.getAdapter().getView(position, null, null).findViewById(R.id.diet_list_item_id);

            int id = Integer.parseInt(item_id.getText().toString());
            database.execSQL("delete from diet where id=?", new Integer[]{id});
            setData();
        } catch(Exception e) {
            //Log.i("exception:", ""+e);
            Toast toast=Toast.makeText(context, "删除出错", Toast.LENGTH_SHORT);
            //显示toast信息
            toast.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}
