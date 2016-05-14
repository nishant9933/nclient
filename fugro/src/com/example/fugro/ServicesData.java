package com.example.fugro;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HIMANSHU on 4/13/2016.
 */
public class ServicesData implements Serializable{

    public String SETTINGS;
    public String CONTENT_ENCODING;
    public String CONTENT_TYPE;
    public String JSON_REQUEST_BEHAVIOR;
    public String MAX_JSON_LENGTH;
    public String RECURSION_LIMIT;
    public ArrayList<ServiceDataFirstSubCategory> MAIN_SERVICE_DATA_ARRAY = new ArrayList<ServiceDataFirstSubCategory>();

}
