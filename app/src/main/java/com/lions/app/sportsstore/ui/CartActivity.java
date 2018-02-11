package com.lions.app.sportsstore.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.database.DBhelper;
import com.lions.app.sportsstore.structs.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
    ExpandableHeightGridView cartList;
    DBhelper dBhelper;
    String Down_Url_Product = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dBhelper = new DBhelper(getApplicationContext());

        data = dBhelper.Get_Cart();


    }

    public void CartEntry(final String prd_Id, final String user_id, final String quantity)
    {
        Log.d("request","reached");
        final ProgressDialog progressBar = new ProgressDialog(CartActivity.this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading.. Please wait");
        progressBar.show();

        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, Down_Url_Product,
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
                        Toast.makeText(CartActivity.this, "Error In Connectivity ", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue4 = Volley.newRequestQueue(CartActivity.this);
//        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);


    }






}
