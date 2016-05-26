package com.example.vvxc.guangzhoubus.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.apistore.sdk.ApiCallBack;
import com.example.vvxc.guangzhoubus.R;
import com.example.vvxc.guangzhoubus.model.BusData;
import com.example.vvxc.guangzhoubus.model.BusDetail;
import com.example.vvxc.guangzhoubus.model.ErrorBus;
import com.example.vvxc.guangzhoubus.model.StationDetail;
import com.example.vvxc.guangzhoubus.myApi.MyApi;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Gson gson;
    private Bundle bundle;

    private Button button;
    private EditText searchBus;
    private CardView searchLayout;
    private FloatingActionButton floatingActionButton;
    private RadioGroup directionGroup;
    private RadioButton forward;
    private RadioButton revese;
    private Bundle errorBundle;
    private Fragment correntFragment;



    private FragmentManager fragmentManager;
    private MainListFragment mainListFragement;
    private ErrorFragment errorFragment;
    private DetailFragment detailFragment;

    private static final int ANIMATE_TO_BUTTON=0;
    private static final int ANIMATE_TO_SEARCH=1;
    private static final int TRUE=1;
    private static final int FALSE=0;
    private static final String FORWARD="0";
    private static final String REVESE="1";

    private int isFirst=TRUE;
    private int isFirstError=TRUE;
    private int state=-1;
    private int isError=FALSE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        init();
    }

    private void init() {
        fragmentManager=getFragmentManager();
        mainListFragement =new MainListFragment();
        gson=new Gson();
        bundle=new Bundle();
        errorBundle=new Bundle();
        errorFragment=new ErrorFragment();

    }

    private void findView() {
        searchBus= (EditText) findViewById(R.id.search_bus);
        searchLayout= (CardView) findViewById(R.id.search_layout);
        button= (Button) findViewById(R.id.search);
        floatingActionButton= (FloatingActionButton) findViewById(R.id.float_buton);
        directionGroup= (RadioGroup) findViewById(R.id.direction_group);
        forward= (RadioButton) findViewById(R.id.forward);
        revese= (RadioButton) findViewById(R.id.reverse);

        button.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.search:
                if (searchBus.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "请输入要查找的公交", Toast.LENGTH_SHORT).show();
                }else{
                    animateToButton();
                    getBusData();
                }
                break;
            case R.id.float_buton:animateToSearch();

                if (correntFragment!=null){

                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
                    fragmentTransaction.hide(correntFragment).commit();
                    correntFragment=null;
                }
                break;

        }

    }

    private void getBusData() {
        final String busName=searchBus.getText().toString();
        final String direction;
        if (directionGroup.getCheckedRadioButtonId()==forward.getId()){
            direction=FORWARD;
        }else {
            direction=REVESE;
        }
        MyApi.getBusData(busName,direction,new ApiCallBack(){
            @Override
            public void onSuccess(int i, String s) {

                String code=s.substring(8,12);
                switch (code){
                    case "1000":
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
                        BusData data;
                        data=gson.fromJson(s,BusData.class);
                        if (data.data.buses==null){
                            Toast.makeText(MainActivity.this, "当前没有对应公交正在行驶。", Toast.LENGTH_SHORT).show();
                        }
                        //如果是第一次查询，则发送数据到fragment，如果是第二次查询，则只需要修改数据，并通知listview修改数据。
                        if (isFirst==TRUE){
                            bundle.putSerializable("bus",data);
                            bundle.putString("direction",direction);
                            bundle.putString("busName",busName);
                            mainListFragement.setArguments(bundle);
                        }
                        else{
                            BusData temp= (BusData) mainListFragement.getArguments().getSerializable("bus");
                            if (temp.data.buses==null){
                                temp.data.buses=new ArrayList<BusDetail>();
                            }
                            if (temp.data.stations==null){
                                temp.data.stations=new ArrayList<StationDetail>();
                            }
                            temp.data.buses.clear();
                            if (data.data.buses!=null)
                                temp.data.buses.addAll(data.data.buses);

                            temp.data.stations.clear();
                            temp.data.stations.addAll(data.data.stations);

                            mainListFragement.getArguments().putString("direction",direction);
                            mainListFragement.getArguments().putString("busName",busName);
                            mainListFragement.notifyDataChange();


                        }
                        showMainList(fragmentTransaction);

                        break;
                    case "1001":
                        Toast.makeText(MainActivity.this, "找不到对应公交", Toast.LENGTH_SHORT).show();
                        isError=TRUE;
                        break;
                    case "1002":
                        FragmentTransaction mfragmentTransaction=fragmentManager.beginTransaction();
                        mfragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
                        ErrorBus edata;
                        edata=gson.fromJson(s,ErrorBus.class);
                        //如果是第一次查询，则发送数据到fragment，如果是第二次查询，则只需要修改数据，并通知listview修改数据。
                        if (isFirstError==TRUE){
                            errorBundle.putSerializable("bus",edata);
                            errorFragment.setArguments(errorBundle);
                        }
                        else{
                            ErrorBus temp= (ErrorBus) errorFragment.getArguments().getSerializable("bus");
                            if (temp.data==null){
                                temp.data=new ArrayList<String>();
                            }
                            temp.data.clear();
                            if (edata.data!=null)
                                temp.data.addAll(edata.data);


                            errorFragment.notifyDataChange();

                        }
                        showErrorList(mfragmentTransaction);

                        break;

                }

                super.onSuccess(i, s);
            }

            @Override
            public void onError(int i, String s, Exception e) {
                super.onError(i, s, e);
            }
        });
    }

    private void showErrorList(FragmentTransaction fragmentTransaction) {
        if (isFirstError==TRUE){
            fragmentTransaction.add(R.id.frame_layout,errorFragment).commit();
            isFirstError=FALSE;
        }
        else{
            fragmentTransaction.show(errorFragment).commit();
        }
        correntFragment=errorFragment;
    }

    private void showMainList(FragmentTransaction fragmentTransaction) {
        if (isFirst==TRUE){
            fragmentTransaction.add(R.id.frame_layout,mainListFragement).commit();
            isFirst=FALSE;
        }
        else{
            fragmentTransaction.show(mainListFragement).commit();
        }
        correntFragment=mainListFragement;
    }

    private void animateToSearch() {
        state=ANIMATE_TO_SEARCH;
        int layoutScollX=-(searchLayout.getRight()-searchLayout.getLeft())/2+floatingActionButton.getRight()-searchLayout.getLeft();
        int layoutScollY=(searchLayout.getBottom()-searchLayout.getTop())/2+floatingActionButton.getBottom()-searchLayout.getBottom();


        AnimatorSet animationSet=new AnimatorSet();
        animationSet.playTogether(
                ObjectAnimator.ofFloat(searchLayout,"translationX",layoutScollX,0),
                ObjectAnimator.ofFloat(searchLayout,"translationY",layoutScollY,0),
                ObjectAnimator.ofFloat(searchLayout,"rotation",360,0),
                ObjectAnimator.ofFloat(searchLayout,"scaleX",0,1),
                ObjectAnimator.ofFloat(searchLayout,"scaleY",0,1),
                ObjectAnimator.ofFloat(searchLayout,"alpha",0,1),

                ObjectAnimator.ofFloat(floatingActionButton,"alpha",1,0),
                ObjectAnimator.ofFloat(floatingActionButton,"scaleX",1,0),
                ObjectAnimator.ofFloat(floatingActionButton,"scaleY",1,0)
        );
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                searchLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (state==ANIMATE_TO_SEARCH)
                    floatingActionButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animationSet.setDuration(800).start();
    }

    private void animateToButton() {
        state=ANIMATE_TO_BUTTON;
        int layoutScollX=-(searchLayout.getRight()-searchLayout.getLeft())/2+floatingActionButton.getRight()-searchLayout.getLeft();
        int layoutScollY=(searchLayout.getBottom()-searchLayout.getTop())/2+floatingActionButton.getBottom()-searchLayout.getBottom();


        AnimatorSet animationSet=new AnimatorSet();
        animationSet.playTogether(
                ObjectAnimator.ofFloat(searchLayout,"translationX",0,layoutScollX),
                ObjectAnimator.ofFloat(searchLayout,"translationY",0,layoutScollY),
                ObjectAnimator.ofFloat(searchLayout,"rotation",0,360),
                ObjectAnimator.ofFloat(searchLayout,"scaleX",1,0),
                ObjectAnimator.ofFloat(searchLayout,"scaleY",1,0),
                ObjectAnimator.ofFloat(searchLayout,"alpha",1,0),

                ObjectAnimator.ofFloat(floatingActionButton,"alpha",0,1),
                ObjectAnimator.ofFloat(floatingActionButton,"scaleX",0,1),
                ObjectAnimator.ofFloat(floatingActionButton,"scaleY",0,1)
        );
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                floatingActionButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (state==ANIMATE_TO_BUTTON)
                    searchLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animationSet.setDuration(800).start();
    }

    public void showDeailFragment(ArrayList<StationDetail> data,int lightItem){
        Bundle bundle;
        FragmentTransaction mfragmentTransaction=fragmentManager.beginTransaction();
        mfragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);

        if (detailFragment==null) {
            detailFragment=new DetailFragment();
            bundle=new Bundle();
            bundle.putSerializable("bus",data);
            bundle.putInt("lightItem",lightItem);
            detailFragment.setArguments(bundle);
            mfragmentTransaction.add(R.id.frame_layout,detailFragment);
        }else{
 //           bundle=detailFragment.getArguments();
 //           ArrayList<StationDetail>temp= (ArrayList<StationDetail>) bundle.getSerializable("bus");
//            if(temp!=data){
//                temp.clear();
//                temp.addAll(data);
//            }
            detailFragment.notifyDataChange(lightItem);
            mfragmentTransaction.show(detailFragment);
        }

        mfragmentTransaction.hide(correntFragment);
        correntFragment=detailFragment;

        
        mfragmentTransaction.commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&&correntFragment==detailFragment&&correntFragment!=null) {
            // 返回上一个fragment
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
            fragmentTransaction.show(mainListFragement).hide(correntFragment).commit();
            correntFragment=mainListFragement;

            return true;
        }else if (keyCode == KeyEvent.KEYCODE_BACK&&correntFragment==mainListFragement
                ||correntFragment==errorFragment&&correntFragment!=null){
            animateToSearch();
            if (correntFragment!=null){
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
                fragmentTransaction.hide(correntFragment).commit();
                correntFragment=null;
            }
            return true;
            }else if (keyCode == KeyEvent.KEYCODE_BACK&&isError==TRUE){
                animateToSearch();
                isError=FALSE;

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void completeSearch(String busName){
        searchBus.setText(busName);
        animateToSearch();
        if (correntFragment!=null){
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
            fragmentTransaction.hide(correntFragment).commit();
            correntFragment=null;
        }

    }
}
