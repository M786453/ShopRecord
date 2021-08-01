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
    private int total;
    private String event;
    private String date="";
    private int pos;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bill);
        setTitle("BILL");


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
        total = 0;

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

                    total += Integer.parseInt(e.get("price"));
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

                        View popup_view = layoutInflater.inflate(R.layout.add_item_popup, null);
                        PopupWindow popupWindow = new PopupWindow(popup_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        popupWindow.setFocusable(true);
                        popupWindow.showAtLocation(linearLayoutSingleBillParent, Gravity.CENTER, 0, 0);

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

                                    FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                } else if (edtItemQuantity.getText().toString().isEmpty()) {

                                    FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                } else if (edtItemPrice.getText().toString().isEmpty()) {

                                    FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Price", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                    return;

                                }

                                //Item Hashmap
                                HashMap<String, String> item_info = new HashMap<>();
                                item_info.put("name", edtItemName.getText().toString());
                                item_info.put("quantity", edtItemQuantity.getText().toString());
                                item_info.put("price", edtItemPrice.getText().toString());

                                bill_items_list.add(item_info);

                                singleBillListAdapter.notifyDataSetChanged();

                                total += Integer.parseInt(edtItemPrice.getText().toString());

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

                String item = bill_items_list.get(i).get("name");

                txtDeleteStatement.setText("Are You Sure To Delete The Item \""+item+"\"?");

                PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(linearLayoutSingleBillParent,Gravity.CENTER,0,0);


                txtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        total -= Integer.parseInt(bill_items_list.get(i).get("price"));
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

                View popup_view = layoutInflater.inflate(R.layout.add_item_popup, null);
                PopupWindow popupWindow = new PopupWindow(popup_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                EditText edtItemName = popup_view.findViewById(R.id.edtItemName);
                EditText edtItemPrice = popup_view.findViewById(R.id.edtItemPrice);
                EditText edtItemQuantity = popup_view.findViewById(R.id.edtItemQuantity);
                TextView txtInsert = popup_view.findViewById(R.id.txtInsertRecipient);
                TextView txtCancel = popup_view.findViewById(R.id.txtCancelRecipient);

                txtInsert.setText("UPDATE");

                edtItemName.setText(bill_items_list.get(i).get("name"));
                edtItemPrice.setText(bill_items_list.get(i).get("price"));
                edtItemQuantity.setText(bill_items_list.get(i).get("quantity"));

                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(linearLayoutSingleBillParent, Gravity.CENTER, 0, 0);


                txtInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        //input validation
                        if (edtItemName.getText().toString().isEmpty()) {

                            FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        } else if (edtItemQuantity.getText().toString().isEmpty()) {

                            FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        } else if (edtItemPrice.getText().toString().isEmpty()) {

                            FancyToast.makeText(SingleBillActivity.this, "Please Enter Item Price", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            return;

                        }

                        total -= Integer.parseInt(bill_items_list.get(i).get("price"));


                        //Item Hashmap
                        HashMap<String, String> item_info = bill_items_list.get(i);
                        item_info.put("name", edtItemName.getText().toString());
                        item_info.put("quantity", edtItemQuantity.getText().toString());
                        item_info.put("price", edtItemPrice.getText().toString());



                        bill_items_list.set(i,item_info);

                        total += Integer.parseInt(bill_items_list.get(i).get("price"));

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
            case R.id.bill_done:

                Calendar cal = Calendar.getInstance();
                String key = cal.getTime().toString(); //get date with time which will be used as key for bill_info_map

                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String dateTime = dateTimeFormatter.format(localDateTime);

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

                break;
            case R.id.bill_share:
                FancyToast.makeText(SingleBillActivity.this,"Will Share The Bill",
                        FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                break;


        }

        return super.onOptionsItemSelected(item);

    }


}