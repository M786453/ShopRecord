package com.example.shoprecord;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {


    private ShopViewModel shopViewModel;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();



        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){


            WriteDataToFile.createDatabaseDirectory();


        }else{

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},9999);

        }



        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        shopViewModel.getmAllStoreItems().observe(this, storeItems -> {


            Data.store_items_list.clear();
            Data.store_items_hm.clear();


            //Update the cached copy of the items in the adapter

            for(StoreItem e: storeItems){

                Data.store_items_list.add(e.getItem_name());

                //item info hm
                HashMap<String,String> item_info_hm = new HashMap<>();
                item_info_hm.put("price",e.getPrice());
                item_info_hm.put("quantity",e.getQuantity());
                item_info_hm.put("id",e.id+"");

                Data.store_items_hm.put(e.getItem_name(),item_info_hm);

            }
        });



        findViewById(R.id.btnBilling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,BillingActivity.class));
            }
        });

        findViewById(R.id.btnStore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,StoreActivity.class));
            }
        });


        findViewById(R.id.btnBackup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    try{

                    ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                    progressDialog.setTitle("BACKUP OF SHOP RECORD");
                    progressDialog.setMessage("Doing Backup...");
                    progressDialog.show();

                    WriteDataToFile.createDatabaseDirectory();

                    Date dateObj = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String date = simpleDateFormat.format(dateObj);

                    WriteDataToFile.createSpecificBackupDirectory(date);

                    WriteDataToFile.writeStoreData(Data.store_items_hm, Data.store_items_list, date + "/store.txt");


                    WriteDataToFile.writeRecipientData(getRecipientsTableData(), date + "/recipients.txt");


                    WriteDataToFile.writeBillsData(getBillsTableData(), date + "/bills.txt");


                    progressDialog.dismiss();

                    FancyToast.makeText(HomeActivity.this, "Backup Done", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                }catch(Exception e){
                        e.printStackTrace();
                }
                }else{

                    ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},9999);

                }

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 ) {

            if (requestCode == 9999 & grantResults[0] == RESULT_OK) {

                WriteDataToFile.createDatabaseDirectory();

            }

        }

    }


    private ArrayList<HashMap<String,String>> getRecipientsTableData(){

        ArrayList<HashMap<String,String>> recipient_table = new ArrayList<>();

        shopViewModel.getmAllRecipient().observe(this,recipients -> {

            recipient_table.clear();


            for (Recipient e: recipients){

                HashMap<String,String> recipient = new HashMap<>();
                recipient.put("id",e.id+"");
                recipient.put("name",e.getName());
                recipient.put("total",e.getTotal());
                recipient.put("key",e.getKey());
                recipient.put("date",e.getDate());


                recipient_table.add(recipient);

            }


        });

        return recipient_table;
    }


    private ArrayList<HashMap<String,String>> getBillsTableData(){


        ArrayList<HashMap<String,String>> billsTableData = new ArrayList<>();

        shopViewModel.getAllAlphabetizeBills().observe(this,bills -> {

            billsTableData.clear();

            for (Bills bill: bills){

                HashMap<String,String> b = new HashMap<>();
                b.put("id",bill.id + "");
                b.put("name",bill.getName());
                b.put("quantity",bill.getQuantity());
                b.put("price",bill.getPrice());
                b.put("key",bill.getKey());

                billsTableData.add(b);
            }


        });


        return billsTableData;
    }

}