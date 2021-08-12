package com.example.shoprecord;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
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
    private String event;
    private String date="";
    private int pos;
    private String key;
    private int total;
    private ShopViewModel shopViewModel;
    private ArrayList<String> item_name_list;
    private TextView txtTotal,txtBillNo;



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
        txtTotal = findViewById(R.id.txtTotal);
        txtBillNo = findViewById(R.id.bill_no);
        linearLayoutSingleBillParent = findViewById(R.id.linearLayoutSingleBillParent);
        item_name_list = new ArrayList<>();
        total = 0;
        isPopupShowing = false;
        bill_items_list = new ArrayList<>();
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        singleBillListAdapter = new SingleBillListAdapter(SingleBillActivity.this,bill_items_list);
        bill_items_listview.setAdapter(singleBillListAdapter);


        Log.i("Store_Item",Data.store_items_list.toString());


        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);



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
                key = Data.bills_list.get(pos).get("key");
                String id = Data.bills_list.get(pos).get("id");
                txtBillNo.setText("Bill#: "+id);


        }

        }else if(event.equals("Add")){

            recipient_name = getIntent().getStringExtra("NAME");

            Calendar cal = Calendar.getInstance();
            key = cal.getTime().toString(); //get date with time which will be used as key for bill_info_map

            Date dateObj = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            date = simpleDateFormat.format(dateObj);

            Recipient recipient = new Recipient(recipient_name,"0",key,date);
            shopViewModel.insertRecipient(recipient);
            try {
                shopViewModel.getRecipientByKey(key).observe(this,recipient1 -> {

                    int id = recipient1.id;
                    txtBillNo.setText("Bill#: "+id+"");
                    Log.i("Recipient_id", recipient1.id + "");

                });

            }catch (Exception e){
                Log.i("Recipient_error",e.getMessage());
            }
            }

        Log.i("key",key);

        //setting value
        txt_recipient_name.setText(recipient_name);
        txt_bill_date.setText(date);



        shopViewModel.getmAllBills(key).observe(this,bills -> {
            item_name_list.clear();
            bill_items_list.clear();
            int total = 0;
            for (Bills e: bills){

                HashMap<String,String> bill_info = new HashMap<>();
                bill_info.put("id",e.id+"");
                bill_info.put("name",e.getName());
                bill_info.put("quantity",e.getQuantity());
                bill_info.put("price",e.getPrice());
                bill_info.put("key",e.getKey());

                bill_items_list.add(bill_info);
                item_name_list.add(e.getName());

                total += Integer.parseInt(e.getQuantity())*Integer.parseInt(e.getPrice());
            }

            txtTotal.setText("TOTAL: "+total+"");
            shopViewModel.updateRecipient(total+"",key);
            singleBillListAdapter.notifyDataSetChanged();

            Log.i("bill_item", bill_items_list.toString());

        });





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

                                }else if(item_name_list.contains(in_item_name)){

                                    FancyToast.makeText(SingleBillActivity.this, "Item With Same Name Already Exist!", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                                    return;

                                }else if (in_item_quantity.equals("0")){

                                    FancyToast.makeText(SingleBillActivity.this, "Quantity Must Be Above Zero", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                                    return;

                                }





                                //item's info from store
                                int available_quantity = Integer.parseInt(Data.store_items_hm.get(in_item_name).get("quantity"));
                                int price = Integer.parseInt(Data.store_items_hm.get(in_item_name).get("price"));

                                if (available_quantity < Integer.parseInt(in_item_quantity)){

                                    FancyToast.makeText(SingleBillActivity.this, "Only "+available_quantity +" Items Available In Store!", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                                    return;

                                }


                                try {

                                    //add bill_item in bill table
                                    Bills bills = new Bills(in_item_name,price+"",in_item_quantity,key);
                                    shopViewModel.insertBill(bills);


                                    //updating store table
                                    shopViewModel.updateStoreItem((available_quantity - Integer.parseInt(in_item_quantity))+"",in_item_name);
                                    Data.store_items_hm.get(in_item_name).put("quantity",(available_quantity - Integer.parseInt(in_item_quantity))+"");


                                }catch (Exception e){

                                    Log.i("Error",e.getMessage());

                                }


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

                        boolean canModifyStore=true;

                        if (!Data.store_items_list.contains(item_name))
                            canModifyStore = false;

                        int available_quantity=0;
                        if (canModifyStore)
                        available_quantity = Integer.parseInt(Data.store_items_hm.get(item_name).get("quantity"));


                        try{
                                if (canModifyStore) {
                                    shopViewModel.updateStoreItem((available_quantity + quantity) + "", item_name);
                                    Data.store_items_hm.get(item_name).put("quantity", (available_quantity + quantity) + "");
                                }
                            shopViewModel.deleteBill(Integer.parseInt(bill_items_list.get(i).get("id")));


                        }catch (Exception e){

                            Log.i("Error",e.getMessage());

                        }

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

                        }else if (!Data.store_items_list.contains(in_item_name)){

                            FancyToast.makeText(SingleBillActivity.this, "Item Not Found In Store", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        }else if (in_item_quantity.equals("0")){

                            FancyToast.makeText(SingleBillActivity.this, "Quantity Must Be Above Zero", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        }


                        int in_quantity = Integer.parseInt(in_item_quantity);
                        int prev_quantity = Integer.parseInt(bill_items_list.get(i).get("quantity"));
                        int store_item_quantity = Integer.parseInt(Data.store_items_hm.get(in_item_name).get("quantity"));

                        try{
                        if (in_quantity > prev_quantity) {

                            //if more than prev then minus from store

                            int quantity = in_quantity - prev_quantity;


                            if (store_item_quantity < quantity) {
                                FancyToast.makeText(SingleBillActivity.this, "Only " + store_item_quantity + " Items Available In Store!",
                                        FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                return;
                            }

                            shopViewModel.updateStoreItem((store_item_quantity - quantity) + "", in_item_name);
                            Data.store_items_hm.get(in_item_name).put("quantity",(store_item_quantity - quantity) + "");

                        } else if (in_quantity < prev_quantity) {

                            //if less than prev then add in store
                            int quantity = prev_quantity - in_quantity;

                            shopViewModel.updateStoreItem((store_item_quantity + quantity) + "", in_item_name);
                            Data.store_items_hm.get(in_item_name).put("quantity",(store_item_quantity + quantity) + "");
                        }



                        shopViewModel.updateBill(in_item_name,bill_items_list.get(i).get("price"),in_item_quantity,
                                Integer.parseInt(bill_items_list.get(i).get("id")));



                        FancyToast.makeText(SingleBillActivity.this, "Updated", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        popupWindow.setFocusable(false);
                        popupWindow.dismiss();

                    }catch (Exception e){

                            Log.i("Error",e.getMessage());

                        }

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

                //checking for bluetooth permission
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.BLUETOOTH},1000);

                }else {
                    printText();
                }
                


                break;

        }

        return super.onOptionsItemSelected(item);

    }

    private void printText() {

        try {

            EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);

            int total = calculateTotal();

            //header

            String text =
                    "[L]\n"+
                    "[C]<b>New Shehzad Auto's And Spare Parts</b>\n" +
                    "[C]<b>Ph# 03427870419</b>\n" +
                    "[L]"+recipient_name+"[R]"+date+"\n" +
                    "[C]===============================================\n" +
                    "[L]<b>Item Name</b>[C]<b>Qty</b>[C]<b>Price</b>[R]<b>Total</b>\n"+
                    "[C]===============================================\n";

            //body
            for (HashMap<String,String> e:bill_items_list){

                text += "[L]"+e.get("name")+"[C]"+e.get("quantity")+"[C]"+e.get("price")+"[R]"+(Integer.parseInt(e.get("price"))*Integer.parseInt(e.get("quantity")))+"\n";

            }


            //footer
                text += "[R]"+total+"\n";
            printer.printFormattedText(text);

            FancyToast.makeText(this,"Printed",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();


        }catch (Exception e){

            Log.i("Printer_Error",e.getMessage());

        }
    }

    private int calculateTotal() {

        //finding total
        int total = 0;
        for (HashMap<String,String> e: bill_items_list){

            total += Integer.parseInt(e.get("price")) * Integer.parseInt(e.get("quantity"));

        }

        return total;

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onSupportNavigateUp() {

        finish();

        return super.onSupportNavigateUp();
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==1000 && resultCode == RESULT_OK){

            printText();

        }else{

            FancyToast.makeText(this,"Cannot Print Without Bluetooth Permission",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();

        }
    }
}