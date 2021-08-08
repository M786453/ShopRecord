package com.example.shoprecord;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bills_table")
public class Bills {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_id")
    public int id = 0;

    @NonNull
    @ColumnInfo(name = "item_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "item_price")
    private String price;

    @NonNull
    @ColumnInfo(name = "item_quantity")
    private String quantity;

    @NonNull
    @ColumnInfo(name = "bill_key")
    private String key;

    public Bills(@NonNull String name,@NonNull String price, @NonNull String quantity, @NonNull String key){

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.key = key;

    }


    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPrice() {
        return price;
    }

    @NonNull
    public String getQuantity() {
        return quantity;
    }

    @NonNull
    public String getKey() {
        return key;
    }
}
