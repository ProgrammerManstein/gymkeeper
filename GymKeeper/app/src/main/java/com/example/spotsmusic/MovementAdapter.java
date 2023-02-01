package com.example.spotsmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class MovementAdapter extends BaseAdapter{
    private List<Movement> list;
    private Context context;
    private LayoutInflater inflater;

    public MovementAdapter(Context context, List<Movement> list) {
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
            convertView = inflater.inflate(R.layout.movement, null);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            holder.type=convertView.findViewById(R.id.photo);
            holder.tab=convertView.findViewById(R.id.tab);
            holder.group=convertView.findViewById(R.id.group);
            holder.ca=convertView.findViewById(R.id.ca);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Movement movement=list.get(position);
        holder.title.setText(movement.getTitle());
        holder.tab.setText(movement.getTab());
        holder.time.setText(String.valueOf(movement.getTime())+"min");
        holder.group.setText(String.valueOf(movement.getGroup())+"组");
        holder.ca.setText(String.valueOf(movement.getCa())+"大卡");
        if(movement.type.equals("无氧训练")){
            holder.type.setImageResource(R.drawable.gym);
        }
        else if(movement.type.equals("耐力有氧")){
            holder.type.setImageResource(R.drawable.run);
        }
        return convertView;
    }

    class ViewHolder {
        TextView title, tab,time,group,ca;
        ImageView type;
    }
}