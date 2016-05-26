package com.example.vvxc.guangzhoubus.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vvxc.guangzhoubus.R;
import com.example.vvxc.guangzhoubus.adapter.MainListAdaptr;
import com.example.vvxc.guangzhoubus.model.BusData;

/**
 * Created by vvxc on 2016/5/19.
 */
public class MainListFragment extends Fragment {
    private Context context;
    private BusData busData;
    private ListView listView;
    private MainListAdaptr mainListAdaptr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        View view;
        Bundle bundle=getArguments();
        busData= (BusData) bundle.getSerializable("bus");
        String busName=bundle.getString("busName");

        view =LayoutInflater.from(context).inflate(R.layout.list_fragment,null);

        listView= (ListView) view.findViewById(R.id.main_list);

        mainListAdaptr=new MainListAdaptr(context,busData.data,getFragmentManager());
        listView.setAdapter(mainListAdaptr);


        return view;
    }

    public void notifyDataChange(){
        mainListAdaptr.notifyDataSetChanged();


    }
}
