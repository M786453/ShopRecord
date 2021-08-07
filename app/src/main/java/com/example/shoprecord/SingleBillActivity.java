package com.example.shoprecord;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SingleBillActivity extends AppCompatActivity {


    private String recipient_name;
    private FloatingActionButton fbtn_add_item;
    private ListView bill_items_listview;
    private TextView txt_recipient_name,txt_bill_date;
    private ArrayList<HashMap<String,String>> bill_items_list;
    private boolean isPopupShowing;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayoutSingleBillParent;
    private SingleBillListAdapter singleBillListAdapter;
//    private int total;
    private String event;
    private String date="";
    private int pos;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bill);
        setTitle("BILL");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //initialize ui components
        fbtn_add_item = findViewById(R.id.fbtn_add_item);
        bill_items_listview = findViewById(R.id.bill_items_listview);
        txt_recipient_name = findViewById(R.id.txt_SingleBill_recipient_name);
        txt_bill_date = findViewById(R.id.txt_SingleBill_Date);
        linearLayoutSingleBillParent = findViewById(R.id.linearLayoutSingleBillParent);

        isPopupShowing = false;
        bill_items_list = new ArrayList<>();
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        singleBillListAdapter = new SingleBillListAdapter(SingleBillActivity.this,bill_items_list);
//        total = 0;

        bill_items_listview.setAdapter(singleBillListAdapter);



        //checking in which mode user is i.e. View OR Add
        event = getIntent().getStringExtra("EVENT");


        if (event.equals("View")){

        pos = getIntent().getIntExtra("POS",-1);

        if (pos==-1){
            FancyToast.makeText(this,"Something Wrong Happen!",FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,false).show();
            finish();

        }else{

                recipient_name = Data.bills_list.get(pos).get("name");
                date = Data.bills_list.get(pos).get("date");
                String key = Data.bills_list.get(pos).get("key");

//                bill_items_list = Data.bill_info_map.get(key);
                Log.i("key",key);
                Log.i("size",Data.bill_info_map.get(key).size()+"");

                for (HashMap<String,String> e: Data.bill_info_map.get(key)){

//                    total += Integer.parseInt(e.get("price"))*Integer.parseInt(e.get("quantity"));
                    bill_items_list.add(e);
                    singleBillListAdapter.notifyDataSetChanged();

                }

                Log.i("list",Data.bill_info_map.get(key).size()+"");


        }

        }else if(event.equals("Add")){

            recipient_name = getIntent().getStringExtra("NAME");

            Date dateObj = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = simpleDateFormat.format(dateObj);

        }


        //setting value
        txt_recipient_name.setText(recipient_name);
        txt_bill_date.setText(date);





        //listening to click events

        fbtn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if(!isPopupShowing) {

                        isPopupShowing = true;

                        View popup_view = layoutInflater.inflate(R.layout.add_item_popup1, null);
                        PopupWindow popupWindow = new PopupWindow(popup_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(linearLayoutSingleBillParent, Gravity.CENTER, 0, 0);

                        //ui components of popup
                        TextView txtInsert = popup_view.findViewById(R.id.txtInsert_single_bill);
                        TextView txtCancel = popup_view.findViewById(R.id.txtCancel_single_bill);
                        AutoCompleteTextView edtItemName = popup_view.findViewById(R.id.edt_item_name_single_bill);
                        EditText edtItemQuantity = popup_view.findViewById(R.id.edt_item_quantity);



                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SingleBillActivity.this,
                                android.R.layout.simple_dropdown_item_1line,Data.store_items_list);
                        edtItemName.setAdapter(adapter);

                        //listening for click events

                        txtInsert.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String in_item_name = edtItemName.getText().toString();
                                String in_item_quantity = edtItemQuantity.getText().toString();

                                //input validation
                                if (in_item_name.isEmpty()) {

                                    FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                } else if (in_item_quantity.isEmpty()) {

                                    FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                }else if (!Data.store_items_list.contains(in_item_name)){

                                    FancyToast.makeText(SingleBillActivity.this, "Item Not Found In Store!", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                                    return;

                                }


                                //finding price

//                                String price = "";
//                                int available_quantity = 0;
//                                int index = 0;
//                                for (int i=0;i<Data.items_list.size();i++){
//
//                                    if (Data.items_list.get(i).get("name").equals(edtItemName.getText().toString())){
//                                        index = i;
//                                        price = Data.items_list.get(i).get("price");
//                                        available_quantity = Integer.parseInt(Data.items_list.get(i).get("quantity"));
//                                        break;
//                                    }
//
//                                }



//                                if (price.equals("")){
//
//                                    FancyToast.makeText(SingleBillActivity.this, "Item Not Found In Store!", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
//                                    return;
//
//                                }else if (available_quantity < Integer.parseInt(edtItemQuantity.getText().toString())){
//
//                                    FancyToast.makeText(SingleBillActivity.this, "Only "+available_quantity +" Items Available In Store!", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
//                                    return;
//
//                                }

                                //item's info from store
                                int available_quantity = Integer.parseInt(Data.store_items_hm.get(in_item_name).get("quantity"));
                                int price = Integer.parseInt(Data.store_items_hm.get(in_item_name).get("price"));

                                if (available_quantity < Integer.parseInt(in_item_quantity)){

                                    FancyToast.makeText(SingleBillActivity.this, "Only "+available_quantity +" Items Available In Store!", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                                    return;

                                }





                                //Item Hashmap
                                HashMap<String, String> item_info = new HashMap<>();
                                item_info.put("name", in_item_name);
                                item_info.put("quantity", in_item_quantity);
                                item_info.put("price", price + "");

                                bill_items_list.add(item_info);
                                Data.store_items_hm.get(in_item_name).put("quantity",(available_quantity - Integer.parseInt(in_item_quantity)) + "");
                                singleBillListAdapter.notifyDataSetChanged();

//                                total += Integer.parseInt(price)*Integer.parseInt(edtItemQuantity.getText().toString());

                                FancyToast.makeText(SingleBillActivity.this, "Item Added", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                popupWindow.setFocusable(false);



                                popupWindow.dismiss();



                            }

                        });

                        txtCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {




                                FancyToast.makeText(SingleBillActivity.this, "Canceled", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
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


        bill_items_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                View popupView = layoutInflater.inflate(R.layout.delete_item_popup,null);
                TextView txtDeleteStatement = popupView.findViewById(R.id.txtDeleteStatement);
                TextView txtDelete = popupView.findViewById(R.id.txtDeleteItem);
                TextView txtCancelDelete = popupView.findViewById(R.id.txtCancelDelete);

                String item_name = bill_items_list.get(i).get("name");
                int quantity = Integer.parseInt(bill_items_list.get(i).get("quantity"));

                txtDeleteStatement.setText("Are You Sure To Delete The Item \""+item_name+"\"?");

                PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(linearLayoutSingleBillParent,Gravity.CENTER,0,0);


                txtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        total -= Integer.parseInt(bill_items_list.get(i).get("price"));

                        int available_quantity = Integer.parseInt(Data.store_items_hm.get(item_name).get("quantity"));
                        Data.store_items_hm.get(item_name).put("quantity",(available_quantity+quantity)+"");
                        bill_items_list.remove(i);
                        singleBillListAdapter.notifyDataSetChanged();

                        popupWindow.setFocusable(false);
                        popupWindow.dismiss();
                        FancyToast.makeText(SingleBillActivity.this,"Deleted",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    }
                });


                txtCancelDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        popupWindow.setFocusable(false);
                       popupWindow.dismiss();
                       FancyToast.makeText(SingleBillActivity.this,"Canceled",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    }
                });


                return true;
            }
        });


        //updating functionality

        bill_items_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                View popup_view = layoutInflater.inflate(R.layout.add_item_popup1, null);
                PopupWindow popupWindow = new PopupWindow(popup_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                AutoCompleteTextView edtItemName = popup_view.findViewById(R.id.edt_item_name_single_bill);


                EditText edtItemQuantity = popup_view.findViewById(R.id.edt_item_quantity);
                TextView txtInsert = popup_view.findViewById(R.id.txtInsert_single_bill);
                TextView txtCancel = popup_view.findViewById(R.id.txtCancel_single_bill);
                TextView txtPopupStatement = popup_view.findViewById(R.id.txtPopupStatement);
                txtPopupStatement.setText("UPDATE ITEM INFO");
                txtInsert.setText("UPDATE");

                edtItemName.setText(bill_items_list.get(i).get("name"));
                edtItemQuantity.setText(bill_items_list.get(i).get("quantity"));

                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(linearLayoutSingleBillParent, Gravity.CENTER, 0, 0);


                txtInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String in_item_name = edtItemName.getText().toString();
                        String in_item_quantity = edtItemQuantity.getText().toString();

                        //input validation
                        if (in_item_name.isEmpty()) {

                            FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        } else if (in_item_quantity.isEmpty()) {

                            FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        }







//                        total -= Integer.parseInt(bill_items_list.get(i).get("price"))*Integer.parseInt(bill_items_list.get(i).get("quantity"));

                        int in_quantity = Integer.parseInt(in_item_quantity);
                        int prev_quantity = Integer.parseInt(bill_items_list.get(i).get("quantity"));
                        int store_item_quantity = Integer.parseInt(Data.store_items_hm.get(in_item_name).get("quantity"));

                        if (in_quantity>prev_quantity){

                            //if more than prev then minus from store

                            int quantity = in_quantity - prev_quantity;



                            if (store_item_quantity<quantity){
                                FancyToast.makeText(SingleBillActivity.this,"Only "+store_item_quantity+" Items Available In Store!",
                                        FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                                return;
                                    }
                            Data.store_items_hm.get(in_item_name).put("quantity",(store_item_quantity - quantity)+"");

                        }else if(in_quantity<prev_quantity){

                            //if less than prev then add in store
                            int quantity = prev_quantity - in_quantity;

                            Data.store_items_hm.get(in_item_name).put("quantity",(store_item_quantity+quantity)+"");

                        }


                        //Item Hashmap
                        HashMap<String, String> item_info = bill_items_list.get(i);
                        item_info.put("name", in_item_name);
                        item_info.put("quantity", in_item_quantity);
                        item_info.put("price", bill_items_list.get(i).get("price"));



                        bill_items_list.set(i,item_info);

//                        total += Integer.parseInt(bill_items_list.get(i).get("price"))*Integer.parseInt(bill_items_list.get(i).get("quantity"));

                        singleBillListAdapter.notifyDataSetChanged();

                        FancyToast.makeText(SingleBillActivity.this, "Updated", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        popupWindow.setFocusable(false);
                        popupWindow.dismiss();

                    }
                });


                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FancyToast.makeText(SingleBillActivity.this, "Canceled", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        popupWindow.setFocusable(false);
                        popupWindow.dismiss();

                    }
                });


            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){

            case R.id.bill_print:
                FancyToast.makeText(SingleBillActivity.this,"Will Print The Bill",
                        FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onSupportNavigateUp() {

        saveData();

        return super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {

        saveData();

        super.onBackPressed();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData(){

        Calendar cal = Calendar.getInstance();
        String key = cal.getTime().toString(); //get date with time which will be used as key for bill_info_map

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dateTime = dateTimeFormatter.format(localDateTime);

        //finding total
        int total = 0;
        for (HashMap<String,String> e: bill_items_list){

            total += Integer.parseInt(e.get("price")) * Integer.parseInt(e.get("quantity"));

        }


        //recipient info hashmap
        HashMap<String,String> recipient_info_hm = new HashMap<>();
        recipient_info_hm.put("name",recipient_name);
        recipient_info_hm.put("dateTime",dateTime);
        recipient_info_hm.put("key",key);
        recipient_info_hm.put("total",total+"");
        recipient_info_hm.put("date",date);

        //setting values

        if (event.equals("View")) {
            Data.bills_list.set(pos, recipient_info_hm);
        }else if(event.equals("Add")){
            Data.bills_list.add(recipient_info_hm);
        }

        Data.bill_info_map.put(key,bill_items_list);

        FancyToast.makeText(SingleBillActivity.this,"Bill Saved",
                FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

        finish();


    }

}