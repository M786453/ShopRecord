package com.example.shoprecord;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;

public class StoreActivity extends AppCompatActivity {

    private TextView empty_text;
    private ListView store_listview;
    private FloatingActionButton fbtn_add_item;
    private LinearLayout linearLayoutStoreParent;
    private boolean isPopupShowing;
    private StoreListAdapter storeListAdapter;
    private  LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        setTitle("STORE");

        //initializing ui components
        empty_text = findViewById(R.id.empty_text);
        store_listview = findViewById(R.id.store_listview);
        fbtn_add_item = findViewById(R.id.fbtn_store);
        linearLayoutStoreParent = findViewById(R.id.linearLayoutStoreParent);

        isPopupShowing = false;
        storeListAdapter = new StoreListAdapter(StoreActivity.this);
        store_listview.setAdapter(storeListAdapter);
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        if(Data.items_list.size()>0){

            empty_text.setVisibility(View.GONE);
            store_listview.setVisibility(View.VISIBLE);

        }


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
                        TextView txtInsert = popup_view.findViewById(R.id.txtInsertRecipient);
                        TextView txtCancel = popup_view.findViewById(R.id.txtCancelRecipient);
                        EditText edtItemName = popup_view.findViewById(R.id.edtItemName);
                        EditText edtItemQuantity = popup_view.findViewById(R.id.edtItemQuantity);
                        EditText edtItemPrice = popup_view.findViewById(R.id.edtItemPrice);

                        //listening for click events

                        txtInsert.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                //input validation
                                if (edtItemName.getText().toString().isEmpty()) {

                                    FancyToast.makeText(StoreActivity.this, "Please Enter Item Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                } else if (edtItemQuantity.getText().toString().isEmpty()) {

                                    FancyToast.makeText(StoreActivity.this, "Please Enter Item Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                } else if (edtItemPrice.getText().toString().isEmpty()) {

                                    FancyToast.makeText(StoreActivity.this, "Please Enter Item Price", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                }

                                //Item Hashmap
                                HashMap<String, String> item_info = new HashMap<>();
                                item_info.put("name", edtItemName.getText().toString());
                                item_info.put("quantity", edtItemQuantity.getText().toString());
                                item_info.put("price", edtItemPrice.getText().toString());

                                Data.items_list.add(item_info);

                                storeListAdapter.notifyDataSetChanged();

                                if(store_listview.getVisibility()!=View.VISIBLE) {

                                    store_listview.setVisibility(View.VISIBLE);
                                    empty_text.setVisibility(View.GONE);

                                }
                                FancyToast.makeText(StoreActivity.this, "Item Added", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                popupWindow.setFocusable(false);

                                isPopupShowing = false;

                                popupWindow.dismiss();



                            }

                        });

                        txtCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {




                                FancyToast.makeText(StoreActivity.this, "Canceled", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                popupWindow.setFocusable(false);

                                isPopupShowing = false;

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

                    String item = Data.items_list.get(i).get("name");

                    txtDeleteStatement.setText("Are You Sure To Delete The Item \""+item+"\"?");

                    PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    popupWindow.setFocusable(true);
                    popupWindow.showAtLocation(linearLayoutStoreParent,Gravity.CENTER,0,0);


                    txtDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Data.items_list.remove(i);
                            storeListAdapter.notifyDataSetChanged();
                            if (Data.items_list.size()==0){
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
                TextView txtInsert = popup_view.findViewById(R.id.txtInsertRecipient);
                TextView txtCancel = popup_view.findViewById(R.id.txtCancelRecipient);

                txtInsert.setText("UPDATE");

                edtItemName.setText(Data.items_list.get(i).get("name"));
                edtItemPrice.setText(Data.items_list.get(i).get("price"));
                edtItemQuantity.setText(Data.items_list.get(i).get("quantity"));

                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(linearLayoutStoreParent, Gravity.CENTER, 0, 0);


                txtInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        //input validation
                        if (edtItemName.getText().toString().isEmpty()) {

                            FancyToast.makeText(StoreActivity.this, "Please Enter Item Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        } else if (edtItemQuantity.getText().toString().isEmpty()) {

                            FancyToast.makeText(StoreActivity.this, "Please Enter Item Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        } else if (edtItemPrice.getText().toString().isEmpty()) {

                            FancyToast.makeText(StoreActivity.this, "Please Enter Item Price", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        }

                        //Item Hashmap
                        HashMap<String, String> item_info = Data.items_list.get(i);
                        item_info.put("name", edtItemName.getText().toString());
                        item_info.put("quantity", edtItemQuantity.getText().toString());
                        item_info.put("price", edtItemPrice.getText().toString());



                        Data.items_list.set(i,item_info);


                        storeListAdapter.notifyDataSetChanged();

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
}