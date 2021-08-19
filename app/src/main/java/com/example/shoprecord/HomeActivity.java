package com.example.shoprecord;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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


                startActivity(new Intent(HomeActivity.this,BackupActivity.class));


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



}