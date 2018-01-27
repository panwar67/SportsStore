package com.lions.app.sportsstore.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.database.DBhelper;
import com.lions.app.sportsstore.structs.NewImageSlider;
import com.lions.app.sportsstore.structs.Products;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class ProductPage extends AppCompatActivity {

    String productId;
    Products products;

    SliderLayout sliderLayout;
    PagerIndicator pagerIndicator;
    TextView productName, productPrice, productDescription, Title_Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        getActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        products = (Products) intent.getSerializableExtra("data");

        sliderLayout = (SliderLayout)findViewById(R.id.slider);
        pagerIndicator = (PagerIndicator)findViewById(R.id.custom_indicator);
        productName = (TextView)findViewById(R.id.product_name);
        productPrice = (TextView)findViewById(R.id.product_price);
        productDescription = (TextView)findViewById(R.id.product_description);


        String string = "\u20B9";
        byte[] utf8 = null;
        try {
            utf8 = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert utf8 != null;
        try {

            string = new String(utf8, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Setup_ImageSlider(products.getProduct_id(), Integer.parseInt(products.getProduct_no_images()));

        productName.setText(products.getProduct_name());
        productPrice.setText("Price"+string+" "+products.getProduct_price());
        productDescription.setText(products.getProduct_description());


    }

    @Override
    public boolean onNavigateUp(){
        onBackPressed();
        return true;
    }


    public void Setup_ImageSlider(String uid, int n)
    {

        if(n>0)
        {
            for(int i=0; i<n;i++)
            {
                sliderLayout.addSlider(new NewImageSlider(getApplicationContext()).image("http://sportsstore.4liongroup.com/productimages/"+uid+"_"+i+".jpeg"));

            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
