package com.example.vvxc.guangzhoubus.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vvxc.guangzhoubus.R;
import com.example.vvxc.guangzhoubus.model.StationDetail;
import com.example.vvxc.guangzhoubus.model.SuccessBusData;
import com.example.vvxc.guangzhoubus.ui.MainActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by vvxc on 2016/5/17.
 */
public class MainListAdaptr extends BaseAdapter {
    private Context context;
    private SuccessBusData successBusData;
    private FragmentManager fragmentManager;
    public MainListAdaptr(Context context, SuccessBusData successBusData,FragmentManager fragmentManager){
        this.context=context;
        this.successBusData=successBusData;
        this.fragmentManager=fragmentManager;
    }
    @Override
    public int getCount() {
        return successBusData.buses==null?0:successBusData.buses.size();
    }

    @Override
    public Object getItem(int position) {
        return successBusData.buses.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item,null);

            viewHolder=new ViewHolder();
            viewHolder.position= (TextView) view.findViewById(R.id.position);
            viewHolder.station= (TextView) view.findViewById(R.id.end_station);
            viewHolder.state= (TextView) view.findViewById(R.id.state);
            viewHolder.time= (TextView) view.findViewById(R.id.time);
            view.setTag(viewHolder);

        }else{
            view =convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.position.setText(position+1+".");
        viewHolder.time.setText(successBusData.buses.get(position).reporTime+"秒前");
        viewHolder.state.setText(Integer.parseInt(successBusData.buses.get(position).state)==1?"已到站":"未到站");
        final int stationId=Integer.parseInt(successBusData.buses.get(position).station);
        viewHolder.station.setText(successBusData.stations.get(stationId-1).stateName);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity= (MainActivity) context;
                activity.showDeailFragment(successBusData.stations,stationId-1);
            }
        });

        return view;
    }

    static class ViewHolder{
        TextView busName;
        TextView position;
        TextView station;
        TextView state;
        TextView time;
    }
}
