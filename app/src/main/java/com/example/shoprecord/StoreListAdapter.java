package com.example.shoprecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoreListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;


    StoreListAdapter(Context context){

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return Data.store_items_list.size();
    }

    @Override
    public Object getItem(int i) {
        return Data.store_items_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = layoutInflater.inflate(R.layout.item_layout,null);
        TextView txt_item_name = view.findViewById(R.id.itemName);
        TextView txt_item_quantity = view.findViewById(R.id.item_quantity);
        TextView txt_item_price = view.findViewById(R.id.item_price);

        //setting values to ui components

        String item_name = Data.store_items_list.get(i);

        txt_item_name.setText(item_name);
        txt_item_price.setText(Data.store_items_hm.get(item_name).get("price"));
        txt_item_quantity.setText(Data.store_items_hm.get(item_name).get("quantity"));


        return view;
    }
}
