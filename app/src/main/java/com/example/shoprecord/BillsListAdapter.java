package com.example.shoprecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BillsListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    BillsListAdapter(Context context){

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return Data.bills_list.size();
    }

    @Override
    public Object getItem(int i) {
        return Data.bills_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = layoutInflater.inflate(R.layout.bill_recipient_row,null);
        TextView txtRecipientName = view.findViewById(R.id.txt_bill_recipient_name);
        TextView txtBillDate = view.findViewById(R.id.txt_bill_date);
        TextView txtBillTotal = view.findViewById(R.id.txt_bill_total);

        //setting values
        txtRecipientName.setText(Data.bills_list.get(i).get("name"));
        txtBillDate.setText(Data.bills_list.get(i).get("date"));
        txtBillTotal.setText("Rs."+Data.bills_list.get(i).get("total"));

        return view;
    }
}
