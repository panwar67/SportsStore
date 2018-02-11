package com.lions.app.sportsstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.lions.app.sportsstore.R;

import java.util.ArrayList;

/**
 * Created by Panwar on 27/01/18.
 */

public class ColorListAdapter extends BaseAdapter {

    ArrayList<String> result = new ArrayList<String>();
    Context context;
    LayoutInflater inflater = null;

    public ColorListAdapter(Context cont, ArrayList<String> data)
    {
        context = cont;
        result = data;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View root = inflater.inflate(R.layout.filter_item,null);

        final CheckedTextView checkedTextview = (CheckedTextView)root.findViewById(R.id.filter_item_textchecked);
        checkedTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkedTextview.setChecked(!checkedTextview.isChecked());

            }
        });

        return root;
    }
}
