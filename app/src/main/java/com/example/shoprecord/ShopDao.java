package com.example.shoprecord;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface ShopDao {


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


}
