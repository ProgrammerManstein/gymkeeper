package com.example.spotsmusic.fragment;

import static com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.spotsmusic.dao.PlanDatabase;
import com.example.spotsmusic.PlanAdapter;
import com.example.spotsmusic.Plan;
import com.example.spotsmusic.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Instances of this class are fragments representing a single
// object in our collection.
public class TrainFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    Button buttonAdd;
    ListView listViewPlan;
    private List<Plan> plans = new ArrayList<>();
    SQLiteDatabase db;
    PlanDatabase dbHelper;
    String type;
    String content;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        Context context=getContext();
        buttonAdd = view.findViewById(R.id.button_add);
        listViewPlan = view.findViewById(R.id.list_list);

        dbHelper = new PlanDatabase(this.getContext(), "Plan", null, 1);

        //加入计划
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker Dpicker= MaterialDatePicker.Builder.datePicker()
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                        .build();
                Dpicker.show(getParentFragmentManager(), "tag");
                Calendar date = Calendar.getInstance();

                Dpicker.addOnPositiveButtonClickListener(view1 -> {
                    date.setTimeInMillis((Long)Dpicker.getSelection());
                    MaterialTimePicker Tpicker= new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(12)
                            .setMinute(00)
                            .setInputMode(INPUT_MODE_KEYBOARD)
                            //.setTitle("Select Appointment time")
                            .build();
                    Tpicker.show(getParentFragmentManager(), "tag");
                    Tpicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            date.set(Calendar.HOUR_OF_DAY, Tpicker.getHour());
                            date.set(Calendar.MINUTE,Tpicker.getMinute());
                            if(date.before(Calendar.getInstance())){
                                Toast.makeText(context,"请设置晚于目前的时间！",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            AlertDialog alertDialog;
                            AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getContext());
                            alertBuilder.setTitle("选择训练类型");
                            alertBuilder.setMessage("训练标题");
                            EditText editText=new EditText(getContext());
                            final String[] items={"无氧训练","耐力有氧"};
                            LinearLayout linearLayout=new LinearLayout(getContext());
                            linearLayout.setGravity(LinearLayout.VERTICAL);
                            RadioGroup group=new RadioGroup(getContext());
                            RadioButton radio1=new RadioButton(getContext());
                            RadioButton radio2=new RadioButton(getContext());
                            radio1.setText("无氧");
                            radio2.setText("有氧");
                            group.addView(radio1);
                            group.addView(radio2);
                            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    if(i==radio1.getId()){
                                        type=items[0];
                                    }
                                    else{
                                        type=items[1];
                                    }
                                }
                            });
                            linearLayout.addView(editText, 800,ViewGroup.LayoutParams.WRAP_CONTENT);
                            linearLayout.addView(group);
                            alertBuilder.setView(linearLayout);
                            alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    content=editText.getText().toString();
                                    int count = 0;
                                    //定义可操作的数据库对象
                                    db =dbHelper.getWritableDatabase();
                                    //设置Cursor对象，用来查看数据库中的信息
                                    Cursor cursor = db.query("Plan",null,null,null,null,null,null);
                                    if (cursor.moveToFirst()){
                                        do{
                                            Long time = cursor.getLong(1);

                                            //判断数据库中是否已经存在输入，或者是否存在输入的信息相同的信息
                                            if (time==date.getTimeInMillis()){
                                                count ++;
                                            }
                                        }while (cursor.moveToNext());
                                    }


//                    如果输入的信息不相同，也就是count没有进行运算
                                    if (count == 0){
                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put("Content",content);
                                        values.put("Time", date.getTimeInMillis());
                                        values.put("Type",type);
                                        long res = db.insert("Plan",null,values);
                                        plans.add(new Plan(content,date,type));
                                        values.clear();
                                        setCalendar(date,type,content);
                                        Toast.makeText(getContext(),"保存成功！" + String.valueOf(res),Toast.LENGTH_SHORT).show();
                                        PlanAdapter adapter=new PlanAdapter(context,plans);
                                        listViewPlan.setAdapter(adapter);
                                    }else{
                                        Toast.makeText(getContext(),"该时间已有运动！",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            alertDialog=alertBuilder.create();
                            alertDialog.show();
                            //数据库添加运动计划

                        }
                    });
                    Tpicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    Tpicker.addOnCancelListener(view2 -> {

                    });
                    Tpicker.addOnDismissListener(view2 -> {

                    });
                });
                Dpicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Dpicker.addOnCancelListener(view1 -> {

                });
                Dpicker.addOnDismissListener(view1 -> {

                });

            }
        });
        //get data of database
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Plan", null, null, null, null, null, "Time");
        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(2);
                Long time = cursor.getLong(1);
                String spotsType=cursor.getString(3);
                Calendar cal=Calendar.getInstance();
                cal.setTimeInMillis(time);
                Plan planInfo = new Plan(content,cal,spotsType);
                if (planInfo.getCal().getTimeInMillis()<Calendar.getInstance().getTimeInMillis()-1000*60*60*24*10){
                    db.delete("Plan","Time = ?",new String[]{String.format("%d",planInfo.getCal().getTimeInMillis())});
                }
                else {
                    plans.add(planInfo);
                }
            } while (cursor.moveToNext());
        }
        final PlanAdapter adapter = new PlanAdapter(this.getContext(), plans);
        listViewPlan.setAdapter(adapter);
        listViewPlan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeleteDialog(i);
                return true;
            }
        });
    }
    private void DeleteDialog(final int positon){
        Context context=this.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("完成健身计划");
        builder.setTitle("提示");

        builder.setNeutralButton("放弃训练", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Plan plan_check = plans.get(positon);
                String checkContent = plan_check.getContent();
                Calendar  checkCla = plan_check.getCal();
                plans.remove(positon);
                PlanAdapter adapter=new PlanAdapter(context,plans);
                listViewPlan.setAdapter(adapter);
                db.delete("Plan","Time = ? and Content = ?",new String[]{String.format("%d",checkCla.getTimeInMillis()) ,checkContent});
                Toast.makeText(context,"偷懒了！",Toast.LENGTH_SHORT).show();
            }
        });
        if(plans.get(positon).getCal().getTimeInMillis()<Calendar.getInstance().getTimeInMillis()) {
            builder.setPositiveButton("已完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Plan plan_check = plans.get(positon);
                    String checkContent = plan_check.getContent();
                    Calendar checkCla = plan_check.getCal();
                    plans.remove(positon);
                    PlanAdapter adapter = new PlanAdapter(context, plans);
                    listViewPlan.setAdapter(adapter);
                    db.delete("Plan", "Time = ? and Content = ?", new String[]{String.format("%d", checkCla.getTimeInMillis()), checkContent});
                    Toast.makeText(context, "自律成功！", Toast.LENGTH_SHORT).show();
                }
            });
        }
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void setCalendar(Calendar calendar,String title, String content){
        ContentResolver cr = requireActivity().getContentResolver();
        ContentValues values = new ContentValues();
        Cursor cur = null;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
         final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,                           // 0
        };
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        cur.moveToFirst();
        final int PROJECTION_ID_INDEX = 0;
        Long calID = cur.getLong(PROJECTION_ID_INDEX);
        long startMillis=calendar.getTimeInMillis();
        long endMillis=calendar.getTimeInMillis()+600000;
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, content);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
