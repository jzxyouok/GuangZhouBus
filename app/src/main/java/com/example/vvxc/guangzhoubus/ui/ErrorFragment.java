package com.example.vvxc.guangzhoubus.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vvxc.guangzhoubus.R;
import com.example.vvxc.guangzhoubus.adapter.ErrorListAdapter;
import com.example.vvxc.guangzhoubus.adapter.MainListAdaptr;
import com.example.vvxc.guangzhoubus.model.BusData;
import com.example.vvxc.guangzhoubus.model.ErrorBus;

/**
 * Created by vvxc on 2016/5/25.
 */
public class ErrorFragment extends Fragment{
    private Context context;
    private BusData busData;
    private ListView listView;
    private ErrorListAdapter mainListAdaptr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getActivity();
        View view;
        Bundle bundle=getArguments();
        ErrorBus data= (ErrorBus) bundle.getSerializable("bus");

        view =LayoutInflater.from(context).inflate(R.layout.error_fragment,null);

        listView= (ListView) view.findViewById(R.id.posibble_list);

        mainListAdaptr=new ErrorListAdapter(context,data.data);
        listView.setAdapter(mainListAdaptr);


        return view;
    }

    public void notifyDataChange(){
        mainListAdaptr.notifyDataSetChanged();


    }
}
