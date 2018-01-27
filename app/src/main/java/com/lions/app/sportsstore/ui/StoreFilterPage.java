package com.lions.app.sportsstore.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.structs.ExpandableHeightGridView;

public class StoreFilterPage extends AppCompatActivity {


    BubbleThumbRangeSeekbar priceFilter;
    ExpandableHeightGridView colourList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_filter_page);

        priceFilter = (BubbleThumbRangeSeekbar)findViewById(R.id.price_filter);
        colourList = (ExpandableHeightGridView)findViewById(R.id.filter_colour);



    }
}
