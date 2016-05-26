package com.example.vvxc.guangzhoubus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vvxc.guangzhoubus.R;
import com.example.vvxc.guangzhoubus.ui.MainActivity;

import java.util.ArrayList;

/**
 * Created by vvxc on 2016/5/25.
 */
public class ErrorListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> data;

    public ErrorListAdapter(Context context, ArrayList<String> data){
        this.context=context;
        this.data=data;

    }
    @Override
    public int getCount() {
        return data==null?0:data.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(context).inflate(R.layout.error_item,null);
            viewHolder=new ViewHolder();

            viewHolder.textView= (TextView) view.findViewById(R.id.posibble_bus);
            view.setTag(viewHolder);

        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(data.get(position));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity= (MainActivity) context;
                mainActivity.completeSearch(data.get(position));
            }
        });

        return view;
    }

    static class ViewHolder{
        TextView textView;
    }
}
