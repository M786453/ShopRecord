package com.example.shoprecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {


    private ShopViewModel shopViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();



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
    }
}