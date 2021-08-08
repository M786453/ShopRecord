package com.example.shoprecord;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ShopRepository {


    private ShopDao shopDao;
    private LiveData<List<StoreItem>> mAllItems;

    private LiveData<List<Recipient>> mAllRecipients;

    ShopRepository(Application application){

        ShopRoomDatabase db = ShopRoomDatabase.getDatabase(application);
        shopDao = db.dao();
        mAllItems = shopDao.getAlphabetizedWords();

        mAllRecipients = shopDao.getAlphabetizeRecipients();

    }

    //store functions

    void updateStoreItem(String quantity,String item_name){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopDao.updateStoreItem(quantity,item_name);

        });


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


   //recipient functions

    LiveData<List<Recipient>> getmAllRecipients(){

        return mAllRecipients;

    }


    void insertRecipient(Recipient recipient){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopDao.insertRecipient(recipient);

        });
    }

    void deleteRecipient(int recipient_id){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopDao.deleteRecipient(recipient_id);

        });

    }

    void updateRecipient(String bill_total,String recipient_name){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopDao.updateRecipient(bill_total,recipient_name);

        });

    }


    //bill functions

    LiveData<List<Bills>> getmAllBills(String key){

        return shopDao.getAlphabetizeBill(key);

    }


    void insertBill(Bills bills){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopDao.insertBill(bills);

        });

    }


    void deleteBill(int bill_id){


        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopDao.deleteBill(bill_id);

        });


    }


    void updateBill(String item_name,String item_price,String item_quantity,int id){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopDao.updateBills(item_name,item_price,item_quantity,id);

        });


    }

    void deleteAllBills(String bill_key){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopDao.deleteAllBills(bill_key);

        });


    }
}
