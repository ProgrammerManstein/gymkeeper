package com.example.spotsmusic.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.spotsmusic.Movement;
import com.example.spotsmusic.MovementAdapter;
import com.example.spotsmusic.R;
import com.example.spotsmusic.dao.StartDatabase;
import com.example.spotsmusic.dao.DBManager;
import com.example.spotsmusic.databinding.TodayMovementBinding;
import com.example.spotsmusic.dialog.AddMovementDialog;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// Instances of this class are fragments representing a single
// object in our collection.
public class StartFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    Button addToday;
    Button buttonAdd;
    Button start;
    ListView listView;
    TextView cost;
    TextView time;
    TextView ca;
    int Cost=0;
    int Time=0;
    double Ca=0;
    boolean intimer = false;
    int tickSec= 0;
    DecoView arcView;
    private List<Movement> movements = new ArrayList<>();
    private List<Movement> today = new ArrayList<>();
    SQLiteDatabase db;
    StartDatabase dbHelper;
    String type;
    AlertDialog alertDialog = null;

    DBManager dbmanager;
    SQLiteDatabase database;
    Timer timer=new Timer();

    String content;
    AddMovementDialog addMovementDialog;
    MovementAdapter todayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        Context context = getContext();
        dbmanager = new DBManager(context);
        database = dbmanager.openDatabase();
        listView=view.findViewById(R.id.list_today);
        cost=view.findViewById(R.id.cost);
        time=view.findViewById(R.id.time);
        ca=view.findViewById(R.id.diet);
        dbHelper = new StartDatabase(context, "Start", null, 1);
        db = dbHelper.getWritableDatabase();
        buttonAdd = (Button) view.findViewById(R.id.button_add);
        start=view.findViewById(R.id.start);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovement();
            }
        });
        addToday = (Button) view.findViewById(R.id.add_today);
        setData();

        arcView = (DecoView)view.findViewById(R.id.dynamicArcView);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!intimer){
                    timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        tick();
                    }
                }, 0, 1000);
                intimer= true;
            }else {
                    intimer=false;
                timer.cancel();
                timer.purge();
                start.setText("开始计时");
            }}
        });
// Create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 200, 200, 200))
                .setRange(0, (float)Ca+0.1f, (float) Ca+0.1f)
                .setLineWidth(32f)
                .build());

//Create data series track
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0,(float) Ca+0.1f,Cost)
                .setLineWidth(32f)
                .build();

        int series1Index = arcView.addSeries(seriesItem1);

        // Create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255,150,150,150))
                .setRange(0, (float)Ca+0.1f, (float) Ca+0.1f)
                .setLineWidth(32f)
                .setInset(new PointF(32f,32f))
                .build());

//Create data series track
        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 235, 200, 37))
                .setRange(0,(float) 1,(float) 1)
                .setLineWidth(32f)
                .setInset(new PointF(32f,32f))
                .build();

        int series2Index = arcView.addSeries(seriesItem1);



        addToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodayMovementBinding binding = TodayMovementBinding.inflate(getLayoutInflater(), null, false);

                db = dbHelper.getWritableDatabase();
                movements = new ArrayList<>();
                Cursor cursor = db.query("Start", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String title = cursor.getString(1);
                        String tab = cursor.getString(2);
                        int time = cursor.getInt(3);
                        int group = cursor.getInt(4);
                        int ca = cursor.getInt(5);
                        String type = cursor.getString(6);
                        Movement movementInfo = new Movement(title, tab, time, group, ca, type);
                        movements.add(movementInfo);
                        Log.d("xxx", movementInfo.getTitle());
                    } while (cursor.moveToNext());
                }

                final MovementAdapter adapter = new MovementAdapter(context, movements);
                binding.listList.setAdapter(adapter);
                binding.listList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!today.contains(movements.get(i))) {
                            today.add(movements.get(i));
                        }
                        Movement movement=movements.get(i);
                        Cost+=movement.getCa();
                        Time+=movement.getTime();

                        cost.setText(String.format("%d大卡",Cost));
                        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                                .setRange(0,(float) Ca+0.1f,Cost%(float)Ca)
                                .setLineWidth(32f)
                                .build();

                        int series1Index = arcView.addSeries(seriesItem1);

                        arcView.addSeries(new SeriesItem.Builder(Color.argb(255,150,150,150))
                                .setRange(0, (float)Time+0.1f, (float) Time)
                                .setLineWidth(32f)
                                .setInset(new PointF(32f,32f))
                                .build());

                        time.setText(String.format("%dmin",Time));

                        todayAdapter=new MovementAdapter(context,today);
                        listView.setAdapter(todayAdapter);
                        getDialog().dismiss();
                        return true;
                    }
                });

                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getContext());
                alertBuilder.setView(binding.getRoot());

                alertDialog=alertBuilder.create();
                alertDialog.show();

            }
        });
    }


        AlertDialog getDialog() {
            return alertDialog;
        }
        private void addMovement () {
            addMovementDialog = new AddMovementDialog(requireActivity(), db);
            addMovementDialog.show();
        }

    private void setData() {
        Date date = new Date();
        String time = String.format("%tY%tm%td", date, date, date);

        Cursor cursor = database.rawQuery("select id as _id, comment, name, count, heat, protein, fat, carbohydrate from diet where time=?", new String[] { time });

        Cursor cursor_total = database.rawQuery("select sum(heat) as total from diet where time=?", new String[] { time });
        cursor_total.moveToFirst();
        String total = String.format("%.0f", cursor_total.getDouble(0));
        Ca=cursor_total.getDouble(0);
        ca.setText(total + "大卡");
    }

        @Override
        public void onResume () {
            super.onResume();
        }

        void tick() {
        tickSec++;
        start.setText(String.format("%02d : %02d", tickSec / 60, tickSec % 60));
            SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 235, 200, 37))
                    .setRange(0,(float) Time + 1,(float) (tickSec / 60)%Time)
                    .setLineWidth(32f)
                    .setInset(new PointF(32f,32f))
                    .build();
            if (Time == tickSec / 60) {
                timer.cancel();
                timer.purge();
                start.setText("开始计时");
            }
            int series2Index = arcView.addSeries(seriesItem2);
        }
    }