package com.example.shoprecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleBillListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String,String>> bill_items_list;
    SingleBillListAdapter(Context context,ArrayList<HashMap<String,String>> bill_items_list){

        this.context = context;
        this.bill_items_list = bill_items_list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return bill_items_list.size();
    }

    @Override
    public Object getItem(int i) {
        return bill_items_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = layoutInflater.inflate(R.layout.receipt_item_layout,null);
        TextView txtItemName = view.findViewById(R.id.bill_item_name);
        TextView txtItemPrice = view.findViewById(R.id.bill_item_price);
        TextView txtItemQuantity = view.findViewById(R.id.bill_item_quantity);

        //setting values
        txtItemName.setText(bill_items_list.get(i).get("name"));
        txtItemPrice.setText(bill_items_list.get(i).get("price"));
        txtItemQuantity.setText(bill_items_list.get(i).get("quantity"));





        return view;
    }



}
