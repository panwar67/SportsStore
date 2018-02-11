package com.lions.app.sportsstore.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.adapters.ProductListAdapter;
import com.lions.app.sportsstore.database.DBhelper;
import com.lions.app.sportsstore.structs.NewImageSlider;
import com.lions.app.sportsstore.structs.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductPage extends AppCompatActivity {

    String productId;
    Products products;
    Button addToCart;
    SliderLayout sliderLayout;
    PagerIndicator pagerIndicator;
    TextView productName, productPrice, productDescription, Title_Description;
    ImageView back;
    DBhelper dBhelper;
    String Down_Url_Cart ="http://sportsstore.4liongroup.com/sports/insert_cart/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page_head);

        //getActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        dBhelper = new DBhelper(getApplicationContext());
        products = (Products) intent.getSerializableExtra("data");
        addToCart = (Button)findViewById(R.id.addtoCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!dBhelper.IsProductUnique(products.getProduct_id()))
                {

                }
                else {
                    Toast.makeText(getApplicationContext(),"Already In Cart",Toast.LENGTH_SHORT).show();
                }

            }
        });
        back = (ImageView)findViewById(R.id.back);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onBackPressed();
                return false;
            }
        });
        sliderLayout = (SliderLayout)findViewById(R.id.slider);
        pagerIndicator = (PagerIndicator)findViewById(R.id.custom_indicator);
        sliderLayout.setCustomIndicator(pagerIndicator);
        productName = (TextView)findViewById(R.id.product_name);
        productPrice = (TextView)findViewById(R.id.product_price);
        productDescription = (TextView)findViewById(R.id.product_description);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"SourceSansProRegular.otf");

        productName.setTypeface(typeface);
        productPrice.setTypeface(typeface);
        productDescription.setTypeface(typeface);

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
        productPrice.setText(""+string+" "+products.getProduct_price());
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
        else
        {
                sliderLayout.addSlider(new NewImageSlider(getApplicationContext()).image("http://sportsstore.4liongroup.com/productimages/"+uid+".jpeg"));

        }

    }


    public void CartEntry(final String prd_Id, final String user_id, final String quantity)
    {
        Log.d("request","reached");
        final ProgressDialog progressBar = new ProgressDialog(ProductPage.this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading.. Please wait");
        progressBar.show();

        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, Down_Url_Cart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                   try {
                           Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            progressBar.dismiss();
                            progressBar.cancel();
                            e.printStackTrace();
                        }
                        progressBar.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //  loading.dismiss();
                        progressBar.dismiss();
                        //Showing toast
                        Toast.makeText(ProductPage.this, "Error In Connectivity ", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("USER_ID",user_id);
                Keyvalue.put("PRD_ID",prd_Id);
                Keyvalue.put("QUANTITY",quantity);
                return Keyvalue;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue4 = Volley.newRequestQueue(ProductPage.this);
//        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
