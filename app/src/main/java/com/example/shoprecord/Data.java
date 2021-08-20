package com.example.shoprecord;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {

    static ArrayList<String> store_items_list = new ArrayList<>();    // store item's name list
    static ArrayList<HashMap<String,String>> bills_list = new ArrayList<>();    // bill recipient info list
    static HashMap<String,ArrayList<HashMap<String,String>>> bill_info_map = new HashMap<>();  // key::Items_info_list of bill
    static HashMap<String,HashMap<String,String>> store_items_hm = new HashMap<>(); // can get price and quantity of item present in store by using its name
    static ArrayList<String> billDateList = new ArrayList<>();
}
