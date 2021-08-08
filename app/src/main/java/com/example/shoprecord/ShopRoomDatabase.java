package com.example.shoprecord;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {StoreItem.class,Bills.class,Recipient.class}, version = 1, exportSchema = false)
public abstract class ShopRoomDatabase extends RoomDatabase {


    public abstract ShopDao dao();

    private static volatile ShopRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ShopRoomDatabase getDatabase(final Context context){

        if (INSTANCE == null){

            synchronized (ShopRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ShopRoomDatabase.class,"shop_database").addCallback(sRoomDatabaseCallback).build();
                }


            }


        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //If you want to keep data through app restarts,
            //comment out the following block

//            databaseWriteExecutor.execute(() -> {
//
//                //Populate the database in the background.
//                //If you want to start with more items, just add them
//                ShopDao shopDao = INSTANCE.dao();
//                shopDao.deleteAllStore();
//            });
        }
    };


}
