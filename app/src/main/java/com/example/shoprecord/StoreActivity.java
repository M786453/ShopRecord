package com.example.shoprecord;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Objects;

public class StoreActivity extends AppCompatActivity {

    private TextView empty_text;
    private ListView store_listview;
    private FloatingActionButton fbtn_add_item;
    private LinearLayout linearLayoutStoreParent;
    private boolean isPopupShowing;
    private StoreListAdapter storeListAdapter;
    private  LayoutInflater layoutInflater;
    private AutoCompleteTextView autoCompleteTextView_Search;
    private ArrayAdapter<String> arrayAdapter;
    private ImageView imgSearch;
    private ShopViewModel shopViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        setTitle("STORE");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //initializing ui components
        empty_text = findViewById(R.id.empty_text);
        store_listview = findViewById(R.id.store_listview);
        fbtn_add_item = findViewById(R.id.fbtn_store);
        linearLayoutStoreParent = findViewById(R.id.linearLayoutStoreParent);
        autoCompleteTextView_Search = findViewById(R.id.txt_search);
        imgSearch = findViewById(R.id.imgSearch);

        arrayAdapter = new ArrayAdapter<>(StoreActivity.this, android.R.layout.simple_dropdown_item_1line,Data.store_items_list);

        autoCompleteTextView_Search.setAdapter(arrayAdapter);



        isPopupShowing = false;
        storeListAdapter = new StoreListAdapter(StoreActivity.this);
        store_listview.setAdapter(storeListAdapter);
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);


        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        
        shopViewModel.getmAllStoreItems().observe(this, storeItems -> {


            Data.store_items_list.clear();
            Data.store_items_hm.clear();


            //Update the cached copy of the items in the adapter

            for(StoreItem e: storeItems){

                Data.store_items_list.add(e.getItem_name());

                //item info hm
                HashMap<String,String> item_info_hm = new HashMap<>();
                item_info_hm.put("price",e.getPrice());
                item_info_hm.put("quantity",e.getQuantity());
                item_info_hm.put("id",e.id+"");

                Data.store_items_hm.put(e.getItem_name(),item_info_hm);

                storeListAdapter.notifyDataSetChanged();
                arrayAdapter.notifyDataSetChanged();

            }

            if(Data.store_items_list.size()>0){
                if(empty_text.getVisibility()!=View.GONE) {
                    empty_text.setVisibility(View.GONE);
                    store_listview.setVisibility(View.VISIBLE);
                }
            }

        });

        if(Data.store_items_list.size()>0){

            empty_text.setVisibility(View.GONE);
            store_listview.setVisibility(View.VISIBLE);

        }





        imgSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                String query = autoCompleteTextView_Search.getText().toString();

                if (query.isEmpty()){

                    FancyToast.makeText(StoreActivity.this,"Search Query Is Empty",
                            FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;

                }else if (!Data.store_items_list.contains(query)){

                    FancyToast.makeText(StoreActivity.this,"Item Not Found",
                            FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;

                }


                for (int i=0;i<Data.store_items_list.size();i++){

                    try {
                        if (Data.store_items_list.get(i).equals(query)) {

                            Log.i("pos", i + "");
                            store_listview.smoothScrollToPosition(i);
                            break;
                        }
                    }catch (Exception e){
                        Log.i("Error",e.getMessage());

                    }

                }




            }
        });




        fbtn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if(!isPopupShowing) {

                        isPopupShowing = true;

                        View popup_view = layoutInflater.inflate(R.layout.add_item_popup, null);
                        PopupWindow popupWindow = new PopupWindow(popup_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(linearLayoutStoreParent, Gravity.CENTER, 0, 0);

                        //ui components of popup
                        TextView txtInsert = popup_view.findViewById(R.id.txtInsertSingleBill);
                        TextView txtCancel = popup_view.findViewById(R.id.txtCancelSingleBill);
                        EditText edtItemName = popup_view.findViewById(R.id.edtItemName);
                        EditText edtItemQuantity = popup_view.findViewById(R.id.edtItemQuantity);
                        EditText edtItemPrice = popup_view.findViewById(R.id.edtItemPrice);

                        //listening for click events

                        txtInsert.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                String in_item_name = edtItemName.getText().toString();
                                String in_item_quantity = edtItemQuantity.getText().toString();
                                String in_item_price = edtItemPrice.getText().toString();


                                //input validation
                                if (in_item_name.isEmpty()) {

                                    FancyToast.makeText(StoreActivity.this, "Please Enter Item Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                } else if (in_item_quantity.isEmpty()) {

                                    FancyToast.makeText(StoreActivity.this, "Please Enter Item Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                } else if (in_item_price.isEmpty()) {

                                    FancyToast.makeText(StoreActivity.this, "Please Enter Item Price", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                }else if(Data.store_items_list.contains(in_item_name)){

                                    FancyToast.makeText(StoreActivity.this, "Item With Same Name Already Exists", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                }

                                //local database code block
                                try {


                                    StoreItem storeItem = new StoreItem(in_item_name, in_item_price, in_item_quantity);
                                    shopViewModel.insert(storeItem);

                                }catch (Exception e){

                                    Log.i("Error",e.getMessage());

                                }
                                if(store_listview.getVisibility()!=View.VISIBLE) {

                                    store_listview.setVisibility(View.VISIBLE);
                                    empty_text.setVisibility(View.GONE);

                                }
                                FancyToast.makeText(StoreActivity.this, "Item Added", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                popupWindow.setFocusable(false);



                                popupWindow.dismiss();



                            }

                        });

                        txtCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {




                                FancyToast.makeText(StoreActivity.this, "Canceled", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                popupWindow.setFocusable(false);



                                popupWindow.dismiss();



                            }
                        });

                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                isPopupShowing = false;
                            }
                        });

                    }


                }catch (Exception e){

                    Log.i("Error",e.getMessage());

                }

            }

        });


        store_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (!isPopupShowing) {

                    isPopupShowing = true;

                    View popupView = layoutInflater.inflate(R.layout.delete_item_popup,null);
                    TextView txtDeleteStatement = popupView.findViewById(R.id.txtDeleteStatement);
                    TextView txtDelete = popupView.findViewById(R.id.txtDeleteItem);
                    TextView txtCancelDelete = popupView.findViewById(R.id.txtCancelDelete);

                    String item_name = Data.store_items_list.get(i);

                    txtDeleteStatement.setText("Are You Sure To Delete The Item \""+item_name+"\"?");

                    PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    popupWindow.setFocusable(true);
                    popupWindow.showAtLocation(linearLayoutStoreParent,Gravity.CENTER,0,0);


                    txtDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int id = Integer.parseInt(Data.store_items_hm.get(item_name).get("id"));

                            try {

                                shopViewModel.delete(id);

                            }catch (Exception e){

                                Log.i("Error",e.getMessage());

                            }

                            if (Data.store_items_list.size()==0){

                                store_listview.setVisibility(View.GONE);
                                empty_text.setVisibility(View.VISIBLE);

                            }

                            popupWindow.setFocusable(false);
                            popupWindow.dismiss();
                            FancyToast.makeText(StoreActivity.this,"Deleted",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                        }
                    });


                    txtCancelDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            popupWindow.setFocusable(false);
                            popupWindow.dismiss();
                            FancyToast.makeText(StoreActivity.this,"Canceled",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                        }
                    });

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                            isPopupShowing = false;

                        }
                    });

                }
                return true;


            }
        });


        store_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                View popup_view = layoutInflater.inflate(R.layout.add_item_popup, null);
                PopupWindow popupWindow = new PopupWindow(popup_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                EditText edtItemName = popup_view.findViewById(R.id.edtItemName);
                EditText edtItemPrice = popup_view.findViewById(R.id.edtItemPrice);
                EditText edtItemQuantity = popup_view.findViewById(R.id.edtItemQuantity);
                TextView txtInsert = popup_view.findViewById(R.id.txtInsertSingleBill);
                TextView txtCancel = popup_view.findViewById(R.id.txtCancelSingleBill);
                TextView txtPopupStatement = popup_view.findViewById(R.id.txtPopupStatement);
                txtPopupStatement.setText("UPDATE ITEM INFO");
                txtInsert.setText("UPDATE");


                String tappped_item_name = Data.store_items_list.get(i);

                edtItemName.setText(tappped_item_name);
                edtItemPrice.setText(Data.store_items_hm.get(tappped_item_name).get("price"));
                edtItemQuantity.setText(Data.store_items_hm.get(tappped_item_name).get("quantity"));

                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(linearLayoutStoreParent, Gravity.CENTER, 0, 0);


                txtInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String in_item_name = edtItemName.getText().toString();
                        String in_item_quantity = edtItemQuantity.getText().toString();
                        String in_item_price = edtItemPrice.getText().toString();

                        //input validation
                        if (in_item_name.isEmpty()) {

                            FancyToast.makeText(StoreActivity.this, "Please Enter Item Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        } else if (in_item_quantity.isEmpty()) {

                            FancyToast.makeText(StoreActivity.this, "Please Enter Item Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        } else if (in_item_price.isEmpty()) {

                            FancyToast.makeText(StoreActivity.this, "Please Enter Item Price", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        }

                        //Local database code block

                        try {




                            shopViewModel.update(in_item_name,in_item_price,in_item_quantity,
                                    Integer.parseInt(Data.store_items_hm.get(Data.store_items_list.get(i)).get("id")));

                        }catch (Exception e){

                            Log.i("Error",e.getMessage());

                        }

                        FancyToast.makeText(StoreActivity.this, "Updated", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        popupWindow.setFocusable(false);
                        popupWindow.dismiss();

                    }
                });


                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FancyToast.makeText(StoreActivity.this, "Canceled", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        popupWindow.setFocusable(false);
                        popupWindow.dismiss();

                    }
                });

            }
        });



    }






    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}