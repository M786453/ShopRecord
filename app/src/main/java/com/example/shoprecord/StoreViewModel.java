package com.example.shoprecord;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StoreViewModel extends AndroidViewModel {


    private StoreRepository storeRepository;


    private final LiveData<List<StoreItem>> mAllStoreItems;

    public StoreViewModel (Application application){
        super(application);
        storeRepository = new StoreRepository(application);
        mAllStoreItems = storeRepository.getmAllItems();

    }

    LiveData<List<StoreItem>> getmAllStoreItems(){
        return mAllStoreItems;
    }

    public void insert(StoreItem storeItem){
        storeRepository.insert(storeItem);
    }

    public void update(String name,String price,String quantity,int id){
        storeRepository.update(name,price,quantity,id);
    }

    public void delete(int id){
        storeRepository.delete(id);
    }
}
