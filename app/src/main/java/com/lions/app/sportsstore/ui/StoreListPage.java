package com.lions.app.sportsstore.ui;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.lions.app.sportsstore.R;

public class StoreListPage extends AppCompatActivity {

    ImageView toogleView, sort, filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list_page);

        toogleView = (ImageView)findViewById(R.id.toggle_view);
        sort = (ImageView)findViewById(R.id.toggle_order);
        filter = (ImageView)findViewById(R.id.toggle_filter);



    }
}
