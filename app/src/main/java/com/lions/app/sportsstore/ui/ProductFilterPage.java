package com.lions.app.sportsstore.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.BubbleThumbRangeSeekbar;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.structs.ExpandableHeightGridView;
import com.lions.app.sportsstore.structs.Store;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductFilterPage extends AppCompatActivity {

    private BubbleThumbRangeSeekbar priceFilter;
    private ExpandableHeightGridView colourList;
    Button applyFilter;
    int minValue, maxValue;
    String color;
    ArrayList<String> colors = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_filter_page);

        priceFilter = (BubbleThumbRangeSeekbar)findViewById(R.id.price_filter);
        colourList = (ExpandableHeightGridView)findViewById(R.id.filter_colour);
        applyFilter = (Button)findViewById(R.id.apply_product_filter);

        colourList.setExpanded(true);
        colourList.setNumColumns(1);

        colors.add("Red");
        colors.add("Black");
        colors.add("Orange");
        colors.add("Blue");
        colors.add("White");
        colors.add("Green");
        colors.add("Yellow");

        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent();
                HashMap<String ,String> parameters = new HashMap<String, String>();
                parameters.put("min", String.valueOf(minValue));
                parameters.put("max", String.valueOf(maxValue));
                parameters.put("color",color);
                intent.putExtra("parameters", parameters);
                setResult(2,intent);
            }
        });

        priceFilter.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number min, Number max) {

                minValue = min.intValue();
                maxValue = max.intValue();

            }
        });


        colourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                color = colors.get(i);
            }
        });



    }
}
