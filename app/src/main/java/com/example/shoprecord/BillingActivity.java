package com.example.shoprecord;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class BillingActivity extends AppCompatActivity {


    private FloatingActionButton fbtn_add_bill;
    private LinearLayout linearLayoutBillParent;
    private boolean isShowingPopup;
    private TextView txtEmptyBill;
    private ListView bill_recipient_listview;
    private BillsListAdapter billsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        setTitle("BILLING");

        //initialize ui components
        fbtn_add_bill = findViewById(R.id.fbtn_add_bill);
        linearLayoutBillParent = findViewById(R.id.linearLayoutBillParent);
        bill_recipient_listview = findViewById(R.id.bills_listview);
        txtEmptyBill = findViewById(R.id.txtEmptyBill);

        isShowingPopup = false;
        billsListAdapter = new BillsListAdapter(BillingActivity.this);

        bill_recipient_listview.setAdapter(billsListAdapter);

        if (Data.bills_list.size()>0){

            bill_recipient_listview.setVisibility(View.VISIBLE);
            txtEmptyBill.setVisibility(View.GONE);

        }

        fbtn_add_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                if (!isShowingPopup) {

                    isShowingPopup = true;

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popup_view = layoutInflater.inflate(R.layout.recipient_info_popup, null);

                    //ui components
                    EditText edtRecipientName = popup_view.findViewById(R.id.edtRecipientName);
                    TextView txtInsertRecipient = popup_view.findViewById(R.id.txtInsertRecipient);
                    TextView txtCancelRecipient = popup_view.findViewById(R.id.txtCancelRecipient);

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

                            String recipient_name = edtRecipientName.getText().toString();





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
}

