package com.example.shoprecord;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ShopViewModel extends AndroidViewModel {


    private ShopRepository shopRepository;


    private final LiveData<List<StoreItem>> mAllStoreItems;

    private final LiveData<List<Recipient>> mAllRecipient;

    private final LiveData<List<Bills>> mAllBills;

    public ShopViewModel(Application application){
        super(application);
        shopRepository = new ShopRepository(application);
        mAllStoreItems = shopRepository.getmAllItems();

        mAllRecipient = shopRepository.getmAllRecipients();
        mAllBills = shopRepository.getAllAlphabetizeBills();

    }

    //store functions

    LiveData<List<StoreItem>> getmAllStoreItems(){
        return mAllStoreItems;
    }

    public void insert(StoreItem storeItem){
        shopRepository.insert(storeItem);
    }

    public void update(String name,String price,String quantity,int id){
        shopRepository.update(name,price,quantity,id);
    }

    public void delete(int id){
        shopRepository.delete(id);
    }

    public void updateStoreItem(String quantity,String item_name){

        shopRepository.updateStoreItem(quantity,item_name);

    }

    //bill functions

    LiveData<List<Bills>> getAllAlphabetizeBills(){

        return mAllBills;
    }

    LiveData<List<Bills>> getmAllBills(String key){

        return shopRepository.getmAllBills(key);

    }

    public void insertBill(Bills bills){

        shopRepository.insertBill(bills);

    }


    public void deleteBill(int bill_id){

        shopRepository.deleteBill(bill_id);

    }

    public void updateBill(String item_name,String item_price,String item_quantity,int id){


        shopRepository.updateBill(item_name,item_price,item_quantity,id);

    }

    public void deleteAllBills(String key){

        shopRepository.deleteAllBills(key);

    }




    //recipient functions

    LiveData<List<Recipient>> getmAllRecipient(){

        return mAllRecipient;

    }

    LiveData<Recipient> getRecipientByKey(String key){

       return shopRepository.getRecipientByKey(key);

    }


    public void insertRecipient(Recipient recipient){

        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{


            shopRepository.insertRecipient(recipient);

        });

    }


    public void deleteRecipient(int id){


        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{

            shopRepository.deleteRecipient(id);

        });

    }


    public void updateRecipient(String bill_total,String key){


        ShopRoomDatabase.databaseWriteExecutor.execute(() ->{


            shopRepository.updateRecipient(bill_total,key);

        });

    }
}
