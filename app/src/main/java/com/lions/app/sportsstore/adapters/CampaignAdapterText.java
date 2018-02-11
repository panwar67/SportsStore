package com.lions.app.sportsstore.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.structs.Campaigns;

import java.util.ArrayList;

/**
 * Created by Panwar on 28/01/18.
 */

public class CampaignAdapterText extends BaseAdapter {

    ArrayList<Campaigns> result = new ArrayList<Campaigns>();
    Context context;
    LayoutInflater inflater = null;


    public CampaignAdapterText(Context cont, ArrayList<Campaigns> data)
    {
        result = data;
        context = cont;
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

        View root = inflater.inflate(R.layout.list_item,null);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"SourceSansProRegular.otf");

        TextView listItem = (TextView)root.findViewById(R.id.list_item);
        listItem.setText(result.get(i).getCampaignName());
        listItem.setTypeface(typeface);
        return root;
    }
}
