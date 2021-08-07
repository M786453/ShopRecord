package com.example.shoprecord;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "store_item")
public class StoreItem {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    public int id = 0;


    @NonNull
    @ColumnInfo(name = "item_name")
    private String item_name;

    @NonNull
    @ColumnInfo(name = "item_price")
    private String price;

    @NonNull
    @ColumnInfo(name = "item_quantity")
    private String quantity;

    public StoreItem(@NonNull String item_name,@NonNull String price,@NonNull String quantity){

        this.item_name = item_name;
        this.price = price;
        this.quantity = quantity;


    }

    @NonNull
    public String getItem_name() {
        return item_name;
    }

    @NonNull
    public String getPrice() {
        return price;
    }

    @NonNull
    public String getQuantity() {
        return quantity;
    }



}
