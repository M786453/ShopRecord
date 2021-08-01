package com.example.shoprecord;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {

    static ArrayList<HashMap<String,String>> items_list = new ArrayList<>();    // items_info list present in store
    static ArrayList<HashMap<String,String>> bills_list = new ArrayList<>();    // bill recipient info list
    static HashMap<String,ArrayList<HashMap<String,String>>> bill_info_map = new HashMap<>();  // key::Items_info_list of bill

}
