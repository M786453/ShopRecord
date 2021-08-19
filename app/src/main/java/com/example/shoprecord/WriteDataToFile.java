package com.example.shoprecord;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WriteDataToFile {

    /**
     * This will write data from database in JSON format in a file
     * in external storage in order to make backup of the data
     */

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void createDatabaseDirectory(){

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Shop Record");

        if (!file.exists())
            file.mkdirs();

        File file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/Shop Record" + "/Backups");
        if (!file1.exists())
            file1.mkdirs();


    }


    public static void writeStoreData(HashMap<String,HashMap<String,String>> store_item_hm,ArrayList<String> store_item_names_list,String path){

        try{

            JSONObject store_items_table = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            for (String e: store_item_names_list){

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",e);
                jsonObject.put("price",store_item_hm.get(e).get("price"));
                jsonObject.put("quantity",store_item_hm.get(e).get("quantity"));
                jsonArray.put(jsonObject);

            }

            store_items_table.put("store_items_table",jsonArray);

            writeJsonDataToTxtFile(store_items_table.toString(),Environment.getExternalStorageDirectory().getPath()+ "/Shop Record/Backups/"+path);


        }catch (Exception e){

            e.printStackTrace();


        }


    }


    public static void writeRecipientData(ArrayList<HashMap<String,String>> recipient_list,String path){

        try{

            JSONObject recipient_table = new JSONObject();

            JSONArray jsonArray = new JSONArray();

            for (HashMap<String,String> e: recipient_list){

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("id",e.get("id"));
                jsonObject.put("name",e.get("name"));
                jsonObject.put("total",e.get("total"));
                jsonObject.put("date",e.get("date"));
                jsonObject.put("key",e.get("key"));

                jsonArray.put(jsonObject);

            }

            recipient_table.put("recipient_table",jsonArray);

            writeJsonDataToTxtFile(recipient_table.toString(),Environment.getExternalStorageDirectory().getPath()+ "/Shop Record/Backups/"+path);


        }catch (Exception e){

            e.printStackTrace();


        }


    }


    public static void writeBillsData(ArrayList<HashMap<String,String>> bills_list,String path){



        try{

            JSONObject bills_table = new JSONObject();
            JSONArray jsonArray =  new JSONArray();

            for (HashMap<String,String> bill: bills_list){

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",bill.get("id"));
                jsonObject.put("name",bill.get("name"));
                jsonObject.put("quantity",bill.get("quantity"));
                jsonObject.put("price",bill.get("price"));
                jsonObject.put("key",bill.get("key"));

                jsonArray.put(jsonObject);


            }

            bills_table.put("bills_table",jsonArray);

            writeJsonDataToTxtFile(bills_table.toString(),Environment.getExternalStorageDirectory().getPath()+ "/Shop Record/Backups/" + path);





        }catch (Exception e){

            e.printStackTrace();


        }



    }




    public static void writeJsonDataToTxtFile(String jsonString,String path){



        try{
            Log.i("json",jsonString);

            BufferedWriter out = null;
            File file = new File(path);
            out = new BufferedWriter(new FileWriter(file,false));
            out.write(jsonString);
            out.close();

        }catch (IOException e){

            e.printStackTrace();

        }




    }



    public static void createSpecificBackupDirectory(String path){


        File file = new File(Environment.getExternalStorageDirectory().getPath()+ "/Shop Record" + "/Backups/"+path);

        if (!file.exists())
            file.mkdirs();


    }





}
