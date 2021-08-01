package com.example.shoprecord;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import java.util.Objects;

public class BillingActivity extends AppCompatActivity {


    private FloatingActionButton fbtn_add_bill;
    private LinearLayout linearLayoutBillParent;
    private boolean isShowingPopup;
    private TextView txtEmptyBill;
    private ListView bill_recipient_listview;
    private BillsListAdapter billsListAdapter;
    private LayoutInflater layoutInflater;
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

        isShowingPopup = false;
        billsListAdapter = new BillsListAdapter(BillingActivity.this);
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

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


        bill_recipient_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                View popupView = layoutInflater.inflate(R.layout.delete_item_popup,null);
                TextView txtDeleteStatement = popupView.findViewById(R.id.txtDeleteStatement);
                TextView txtDelete = popupView.findViewById(R.id.txtDeleteItem);
                TextView txtCancelDelete = popupView.findViewById(R.id.txtCancelDelete);

                String item = Data.bills_list.get(i).get("name");

                txtDeleteStatement.setText("Are You Sure To Delete The Bill \""+item+"\"?");

                PopupWindow popupWindow = new PopupWindow(popupView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(linearLayoutBillParent,Gravity.CENTER,0,0);


                txtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String key = Data.bills_list.get(i).get("key");
                        Data.bill_info_map.remove(key);
                        Data.bills_list.remove(i);
                        billsListAdapter.notifyDataSetChanged();

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

