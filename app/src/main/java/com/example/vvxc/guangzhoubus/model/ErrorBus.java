package com.example.vvxc.guangzhoubus.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vvxc on 2016/5/25.
 */
public class ErrorBus implements Serializable{
    public String code;
    public String msg;
    public ArrayList<String> data;
}
