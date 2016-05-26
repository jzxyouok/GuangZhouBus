package com.example.vvxc.guangzhoubus.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vvxc.guangzhoubus.R;
import com.example.vvxc.guangzhoubus.adapter.DetailListAdapter;
import com.example.vvxc.guangzhoubus.adapter.ErrorListAdapter;
import com.example.vvxc.guangzhoubus.model.BusData;
import com.example.vvxc.guangzhoubus.model.ErrorBus;
import com.example.vvxc.guangzhoubus.model.StationDetail;

import java.util.ArrayList;

/**
 * Created by vvxc on 2016/5/26.
 */
public class DetailFragment extends Fragment {
    private Context context;
    private BusData busData;
    private ListView listView;
    private DetailListAdapter mainListAdaptr;
    private int[] lightItem=new int[1];
    private ArrayList<StationDetail> data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        View view;
        Bundle bundle=getArguments();
        data= (ArrayList<StationDetail>) bundle.getSerializable("bus");

        view =LayoutInflater.from(context).inflate(R.layout.detail_fragment,null);

        listView= (ListView) view.findViewById(R.id.detail_list);
        lightItem[0]=bundle.getInt("lightItem");

        mainListAdaptr=new DetailListAdapter(context,data,lightItem[0]);
        listView.setAdapter(mainListAdaptr);



        return view;
    }

    public void notifyDataChange(int lightItem){
        this.lightItem[0]=lightItem;
        mainListAdaptr=new DetailListAdapter(context,data,lightItem);
        listView.setAdapter(mainListAdaptr);
    }
}
