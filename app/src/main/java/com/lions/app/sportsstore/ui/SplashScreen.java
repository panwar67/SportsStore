package com.lions.app.sportsstore.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.structs.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {


    String Down_Url_Stores = "http://sportsstore.4liongroup.com/sports/get_store";
    String Down_Url_Catalogues = "http://sportsstore.4liongroup.com/sports/get_camp";
    String Down_Url_Products = "http://sportsstore.4liongroup.com/sports/get_prd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }


    public boolean Get_All_Stores()
    {

        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, Down_Url_Stores,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        try {


                        JSONArray array = new JSONArray(s);
                            for(int i=0;i<array.length();i++)
                            {
                                Store store = new Store();
                                JSONObject object = array.getJSONObject(i);
                                store.setStore_cat_id(object.getString("STORE_ID"));
                                store.setStore_address(object.getString("STORE_ADDRESS"));
                                store.setStore_id(object.getString("STORE_ID"));

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        //Showing toast message of the response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //  loading.dismiss();

                        //Showing toast
                        Toast.makeText(SplashScreen.this, "Error In Connectivity ", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                return Keyvalue;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);

        return true;
    }


    public boolean Get_All_Products()
    {
        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, Down_Url_Stores,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //  loading.dismiss();

                        //Showing toast
                        Toast.makeText(SplashScreen.this, "Error In Connectivity ", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                return Keyvalue;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);

        return true;
    }
}
