package com.example.shoprecord;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StoreRepository {


    private ShopDao shopDao;
    private LiveData<List<StoreItem>> mAllItems;

    StoreRepository(Application application){

        ShopRoomDatabase db = ShopRoomDatabase.getDatabase(application);
        shopDao = db.dao();
        mAllItems = shopDao.getAlphabetizedWords();


    }


    LiveData<List<StoreItem>> getmAllItems(){
        return mAllItems;
    }

    void insert(StoreItem storeItem){

        ShopRoomDatabase.databaseWriteExecutor.execute(() -> {
            shopDao.insertStoreItem(storeItem);

        });

    }

    void update(String name, String price, String quantity, int id){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{
            shopDao.updateStoreItem(name,price,quantity,id);
        });

    }

    void delete(int id){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            ShopRoomDatabase.databaseWriteExecutor.execute(() ->{
                shopDao.deleteStoreItem(id);
            });
        });


    }





}
