package com.example.vvxc.guangzhoubus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vvxc.guangzhoubus.R;
import com.example.vvxc.guangzhoubus.model.StationDetail;

import java.util.ArrayList;

/**
 * Created by vvxc on 2016/5/26.
 */
public class DetailListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<StationDetail> data;
    private int lightItem;
    public DetailListAdapter(Context context, ArrayList<StationDetail> data,int lightItem){
        this.context=context;
        this.data=data;
        this.lightItem=lightItem;

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(context).inflate(R.layout.detail_item,null);
            viewHolder=new ViewHolder();
            viewHolder.textView= (TextView) view.findViewById(R.id.detail_item);
            viewHolder.next= (ImageView) view.findViewById(R.id.next);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(data.get(position).stateName);
        if (position==lightItem){
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.colorLight));
        }else{
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.textColor));
        }
        if (position==data.size()-1){
             viewHolder.next.setVisibility(View.GONE);
        }else {
            viewHolder.next.setVisibility(View.VISIBLE);
        }

        view.setEnabled(false);
        return view;
    }
    static class ViewHolder{
        TextView textView;
        ImageView next;
    }
}
