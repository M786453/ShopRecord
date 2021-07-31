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
        return Data.items_list.size();
    }

    @Override
    public Object getItem(int i) {
        return Data.items_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = layoutInflater.inflate(R.layout.item_layout,null);
        TextView item_name = view.findViewById(R.id.itemName);
        TextView item_quantity = view.findViewById(R.id.item_quantity);
        TextView item_price = view.findViewById(R.id.item_price);

        //setting values to ui components
        item_name.setText(Data.items_list.get(i).get("name"));
        item_price.setText(Data.items_list.get(i).get("price"));
        item_quantity.setText(Data.items_list.get(i).get("quantity"));


        return view;
    }
}
