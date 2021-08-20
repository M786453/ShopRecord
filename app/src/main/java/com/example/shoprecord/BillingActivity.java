package com.example.shoprecord;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringTokenizer;

public class BillingActivity extends AppCompatActivity {


    private FloatingActionButton fbtn_add_bill;
    private LinearLayout linearLayoutBillParent;
    private boolean isShowingPopup;
    private TextView txtEmptyBill;
    private ListView bill_recipient_listview;
    private BillsListAdapter billsListAdapter;
    private LayoutInflater layoutInflater;
    private ShopViewModel shopViewModel;
    private AutoCompleteTextView autoCompleteTextView_Recipient;
    private ImageView imgSearchRecipient;
    private ArrayAdapter<String> bill_name_adapter;
    private ArrayList<String> bill_name_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        setTitle("BILLING");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //initialize ui components
        fbtn_add_bill = findViewById(R.id.fbtn_add_bill);
        linearLayoutBillParent = findViewById(R.id.linearLayoutBillParent);
        bill_recipient_listview = findViewById(R.id.bills_listview);
        txtEmptyBill = findViewById(R.id.txtEmptyBill);
        autoCompleteTextView_Recipient = findViewById(R.id.txt_search_recipient);
        imgSearchRecipient = findViewById(R.id.imgSearchRecipient);
        bill_name_list = new ArrayList<>();


        isShowingPopup = false;
        billsListAdapter = new BillsListAdapter(BillingActivity.this);
        bill_name_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, bill_name_list);
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        bill_recipient_listview.setAdapter(billsListAdapter);
//        autoCompleteTextView_Recipient.setAdapter(bill_id_adapter);

        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        shopViewModel.getmAllRecipient().observe(this,recipients -> {


        Data.bills_list.clear();
        bill_name_list.clear();
        Data.billDateList.clear();

        for (Recipient e: recipients){


            HashMap<String,String> recipient_info = new HashMap<>();

            recipient_info.put("id",e.id+"");
            recipient_info.put("name",e.getName());
            recipient_info.put("key",e.getKey());
            recipient_info.put("date",e.getDate());
            recipient_info.put("total",e.getTotal());

            Data.bills_list.add(recipient_info);
//            if (!bill_name_list.contains(e.getName()))
            bill_name_list.add(e.getName());



            StringTokenizer stringTokenizer = new StringTokenizer(e.getDate()," ");
            String date = stringTokenizer.nextToken();
            if (!Data.billDateList.contains(date)) {

                Data.billDateList.add(date);

            }else{

                Data.billDateList.add("");

            }

        }

            billsListAdapter.notifyDataSetChanged();
            bill_name_adapter.notifyDataSetChanged();
            autoCompleteTextView_Recipient.setAdapter(bill_name_adapter);
            if (Data.bills_list.size()>0){

                bill_recipient_listview.setVisibility(View.VISIBLE);
                txtEmptyBill.setVisibility(View.GONE);

            }else{
                bill_recipient_listview.setVisibility(View.GONE);
                txtEmptyBill.setVisibility(View.VISIBLE);
            }

        });

        if (Data.bills_list.size()>0){

            bill_recipient_listview.setVisibility(View.VISIBLE);
            txtEmptyBill.setVisibility(View.GONE);

        }




        imgSearchRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = autoCompleteTextView_Recipient.getText().toString();

                if (query.isEmpty()){

                    FancyToast.makeText(BillingActivity.this,"Search Query Is Empty",
                            FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;

                }else if (!bill_name_list.contains(query)){

                    FancyToast.makeText(BillingActivity.this,"Bill Not Found",
                            FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;

                }


                for (int i = 0; i< bill_name_list.size(); i++){

                    try {
                        if (bill_name_list.get(i).equals(query)) {

                            Log.i("pos", i + "");
                            bill_recipient_listview.smoothScrollToPosition(i);
                            break;
                        }
                    }catch (Exception e){
                        Log.i("Error",e.getMessage());

                    }

                }
            }
        });

        fbtn_add_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                if (!isShowingPopup) {

                    isShowingPopup = true;


                    View popup_view = layoutInflater.inflate(R.layout.recipient_info_popup, null);

                    //ui components
                    EditText edtRecipientName = popup_view.findViewById(R.id.edtRecipientName);
                    TextView txtInsertRecipient = popup_view.findViewById(R.id.txtInsertSingleBill);
                    TextView txtCancelRecipient = popup_view.findViewById(R.id.txtCancelSingleBill);

                    //popup window
                    PopupWindow popupWindow = new PopupWindow(popup_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    popupWindow.setFocusable(true);
                    popupWindow.showAtLocation(linearLayoutBillParent, Gravity.CENTER, 0, 0);

                    //listening to click events

                    txtInsertRecipient.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View view) {

                            if (edtRecipientName.getText().toString().isEmpty()) {

                                FancyToast.makeText(BillingActivity.this, "Please Enter Recipient Name", FancyToast.LENGTH_SHORT,
                                        FancyToast.INFO, false).show();
                                return;

                            }



                            //Recipient Data

                            String recipient_name = edtRecipientName.getText().toString().toUpperCase();





                            //intent
                            Intent intent = new Intent(BillingActivity.this,SingleBillActivity.class);
                            intent.putExtra("EVENT","Add");
                            intent.putExtra("NAME",recipient_name);


                            popupWindow.setFocusable(false);
                            popupWindow.dismiss();



                            FancyToast.makeText(BillingActivity.this, "Inserted", FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS, false).show();


                            //transition
                            startActivity(intent);



                        }
                    });


                    txtCancelRecipient.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            popupWindow.setFocusable(false);
                            popupWindow.dismiss();

                            FancyToast.makeText(BillingActivity.this, "Canceled", FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS, false).show();


                        }
                    });

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                            isShowingPopup = false;

                        }
                    });

                }

            }catch (Exception e){

                    e.printStackTrace();

                }
            }
        });



        bill_recipient_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent intent = new Intent(BillingActivity.this,SingleBillActivity.class);
            intent.putExtra("EVENT","View");
            intent.putExtra("POS",i);

            startActivity(intent);


            }
        });


        bill_recipient_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                View popupView = layoutInflater.inflate(R.layout.delete_bill_popup,null);

                TextView txtDelete = popupView.findViewById(R.id.txtDeleteBill);
                TextView txtCancelDelete = popupView.findViewById(R.id.txtCancelDeleteBill);
                RadioGroup radioGroup = popupView.findViewById(R.id.radio_group);
                boolean[] canModifyStore = new boolean[1];

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        switch (i){

                            case R.id.rbtnYes:
                                canModifyStore[0] = true;
                                break;
                            case R.id.rbtnNo:
                                canModifyStore[0] = false;
                                break;

                        }


                    }
                });


                PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(linearLayoutBillParent,Gravity.CENTER,0,0);


                txtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




                        String key = Data.bills_list.get(i).get("key");

                        try {

                            if(radioGroup.getCheckedRadioButtonId() == -1){

                                FancyToast.makeText(BillingActivity.this,"Please Select Option",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                                return;

                            }

                            if (canModifyStore[0]){



                                shopViewModel.getmAllBills(key).observe(BillingActivity.this,bills -> {


                                    for (Bills e: bills){




                                            if (Data.store_items_list.contains(e.getName())) {


                                                long store_item_qty = Long.parseLong(Data.store_items_hm.get(e.getName()).get("quantity"));
                                                long bill_item_qty = Long.parseLong(e.getQuantity());

                                                shopViewModel.updateStoreItem((store_item_qty + bill_item_qty) + "", e.getName());
                                                Data.store_items_hm.get(e.getName()).put("quantity", (store_item_qty + bill_item_qty) + "");


                                            }

                                    }

                                });

                            }

                            shopViewModel.deleteAllBills(key);

                            int id = Integer.parseInt(Data.bills_list.get(i).get("id"));
                            shopViewModel.deleteRecipient(id);


                        }catch (Exception e){

                            Log.i("Error",e.getMessage());

                        }


                        popupWindow.setFocusable(false);
                        popupWindow.dismiss();
                        FancyToast.makeText(BillingActivity.this,"Deleted",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    }
                });


                txtCancelDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        popupWindow.setFocusable(false);
                        popupWindow.dismiss();
                        FancyToast.makeText(BillingActivity.this,"Canceled",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    }
                });

                return true;
            }
        });


    }


    @Override
    protected void onResume() {


            if (Data.bills_list.size()>0){
                billsListAdapter.notifyDataSetChanged();
                bill_recipient_listview.setVisibility(View.VISIBLE);
                txtEmptyBill.setVisibility(View.GONE);
        }


        super.onResume();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

