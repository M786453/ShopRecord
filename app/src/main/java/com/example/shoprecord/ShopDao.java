package com.example.shoprecord;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface ShopDao {

    //store queries

    @Insert
    void insertStoreItem(StoreItem storeItem);

    @Query("DELETE FROM store_item")
    void deleteAllStore();

    @Query("SELECT * FROM store_item ORDER BY item_name ASC")
    LiveData<List<StoreItem>> getAlphabetizedWords();

    @Query("DELETE FROM store_item WHERE item_id= :item_id")
    void deleteStoreItem(int item_id);

    @Query("UPDATE store_item SET item_name= :name , item_price= :price, item_quantity= :quantity WHERE item_id= :item_id")
    void updateStoreItem(String name,String price,String quantity,int item_id);

    @Query("UPDATE store_item SET item_quantity= :quantity WHERE item_name= :item_name")
    void updateStoreItem(String quantity,String item_name);

    //recipient queries

    @Insert
    void insertRecipient(Recipient recipient);

    @Query("DELETE FROM recipient_table")
    void deleteAllRecipient();

    @Query("SELECT * FROM recipient_table")
    LiveData<List<Recipient>> getAlphabetizeRecipients();

    @Query("SELECT * FROM recipient_table WHERE bill_key= :key")
    LiveData<Recipient> getRecipientByKey(String key);

    @Query("DELETE FROM recipient_table WHERE recipient_id= :recipient_id")
    void deleteRecipient(int recipient_id);

    @Query("UPDATE recipient_table SET bill_total = :bill_total WHERE bill_key = :key")
    void updateRecipient(String bill_total,String key);

    //bills queries

    @Insert
    void insertBill(Bills bills);

    @Query("DELETE FROM bills_table")
    void deleteAllBills();

    @Query("SELECT * FROM bills_table WHERE bill_key= :bill_key ORDER BY item_name ASC")
    LiveData<List<Bills>> getAlphabetizeBill(String bill_key);

    @Query("SELECT * FROM bills_table ORDER BY item_name ASC")
    LiveData<List<Bills>> getAllAlphabetizeBills();

    @Query("DELETE FROM bills_table WHERE bill_id= :bill_id")
    void deleteBill(int bill_id);

    @Query("DELETE FROM bills_table WHERE bill_key= :bill_key")
    void deleteAllBills(String bill_key);

    @Query("UPDATE bills_table SET item_name= :item_name,item_price= :item_price,item_quantity = :item_quantity WHERE bill_id= :bill_id")
    void updateBills(String item_name,String item_price,String item_quantity,int bill_id);

}
