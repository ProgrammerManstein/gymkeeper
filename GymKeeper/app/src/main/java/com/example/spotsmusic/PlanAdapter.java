package com.example.spotsmusic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class PlanAdapter extends BaseAdapter{
    private List<Plan> list;
    private Context context;
    private LayoutInflater inflater;

    public PlanAdapter(Context context, List<Plan> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.traininglist, null);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.content=(TextView)convertView.findViewById(R.id.content);
            holder.type=convertView.findViewById(R.id.photo);
            holder.state=convertView.findViewById(R.id.state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Plan plan=list.get(position);
        Calendar cal=plan.getCal();
        holder.time.setText(String.format("%d月 %d日 %d:%02d ",cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE)));
        holder.content.setText(plan.getContent());
        if(plan.type.equals("无氧训练")){
            holder.type.setImageResource(R.drawable.gym);
        }
        else if(plan.type.equals("耐力有氧")){
            holder.type.setImageResource(R.drawable.run);
        }
        if(plan.getCal().getTimeInMillis()<Calendar.getInstance().getTimeInMillis()){
            holder.state.setText("已过期");
            holder.state.setTextColor(0xFFBB86FC);
        }
        else {
            holder.state.setText("未进行");
            holder.state.setTextColor(0xFF03DA7D);
        }

        return convertView;
    }

    class ViewHolder {
        TextView time, content,state;
        ImageView type;
    }
}