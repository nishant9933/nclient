package com.example.fugro;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HIMANSHU on 4/13/2016.
 */
public class ServiceDataFirstSubCategory  implements Serializable {

    public String SERVICE_ID;
    public String SERVICE_NAME;
    public String SERVICE_DESCRIPTION;
    public String IS_ACTIVE;
    public String IS_DELETED;
    public String CREATED_DATE;
    public String UPDATED_DATE;
    public ArrayList<ServiceDataSecondSubCategory> SUB_SERVICE_DATA_ARRAY = new ArrayList<ServiceDataSecondSubCategory>();

}
