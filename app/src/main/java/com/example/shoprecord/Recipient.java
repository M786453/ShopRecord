package com.example.shoprecord;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipient_table")
public class Recipient {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipient_id")
    public int id = 0;

    @NonNull
    @ColumnInfo(name = "recipient_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "bill_total")
    private String total;

    @NonNull
    @ColumnInfo(name = "bill_key")
    private String key;

    @NonNull
    @ColumnInfo(name = "bill_date")
    private String date;

    public Recipient(@NonNull String name,@NonNull String total,@NonNull String key,@NonNull String date){


        this.name = name;
        this.total = total;
        this.key = key;
        this.date = date;

    }


    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getTotal() {
        return total;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    @NonNull
    public String getDate() {
        return date;
    }
}
