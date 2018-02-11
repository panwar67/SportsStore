package com.lions.app.sportsstore.ui;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.database.DBhelper;
import com.lions.app.sportsstore.structs.Store;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreListPage extends AppCompatActivity {

    ImageView toogleView, sort, filter;
    DBhelper dBhelper;
    String  parameters ;
    ArrayList<Store> Stores = new ArrayList<Store>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list_page);

        toogleView = (ImageView)findViewById(R.id.toggle_view);
        sort = (ImageView)findViewById(R.id.toggle_order);
        filter = (ImageView)findViewById(R.id.toggle_filter);
        dBhelper = new DBhelper(getApplicationContext());
        Intent intent = getIntent();

        parameters = intent.getStringExtra("campaign");
        Stores = dBhelper.Get_Stores_by_Campaign(parameters);



    }
}
