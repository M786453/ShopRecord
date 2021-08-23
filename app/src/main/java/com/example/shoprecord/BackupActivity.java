package com.example.shoprecord;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ProgressDialog;
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
import java.util.Objects;

public class BackupActivity extends AppCompatActivity {


    private ShopViewModel shopViewModel;
    private ArrayList<HashMap<String,String>> billsTableData;
    private ArrayList<HashMap<String,String>> recipient_table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        billsTableData = new ArrayList<>();
        recipient_table = new ArrayList<>();

        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        getRecipientsTableData();
        getBillsTableData();



        findViewById(R.id.btnBackupDo).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(BackupActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    try{



                        ProgressDialog progressDialog = new ProgressDialog(BackupActivity.this);
                        progressDialog.setTitle("BACKUP OF SHOP RECORD");
                        progressDialog.setMessage("Doing Backup...");
                        progressDialog.show();



                        WriteDataToFile.createDatabaseDirectory();

                        Date dateObj = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String date = simpleDateFormat.format(dateObj);

                        WriteDataToFile.createSpecificBackupDirectory(date);

                        WriteDataToFile.writeStoreData(Data.store_items_hm, Data.store_items_list, date + "/store.txt");



                        WriteDataToFile.writeRecipientData(recipient_table, date + "/recipients.txt");


                        WriteDataToFile.writeBillsData(billsTableData, date + "/bills.txt");


                        progressDialog.dismiss();

                        FancyToast.makeText(BackupActivity.this, "Backup Done", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{

                    ActivityCompat.requestPermissions(BackupActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},9999);

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




    private void getRecipientsTableData(){



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

            Log.i("recipient",recipient_table.toString());

        });


    }


    private void getBillsTableData(){




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

            Log.i("bills",billsTableData.toString());

        });



    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}