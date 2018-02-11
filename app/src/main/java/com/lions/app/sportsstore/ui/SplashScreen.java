package com.lions.app.sportsstore.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.database.DBhelper;
import com.lions.app.sportsstore.structs.Campaigns;
import com.lions.app.sportsstore.structs.Constants;
import com.lions.app.sportsstore.structs.FetchAddressIntentService;
import com.lions.app.sportsstore.structs.LocationSession;
import com.lions.app.sportsstore.structs.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {


    String Down_Url_Stores = "http://sportsstore.4liongroup.com/sports/get_store";
    String Down_Url_Camps = "http://sportsstore.4liongroup.com/sports/get_camp";
    String Down_Url_Products = "http://sportsstore.4liongroup.com/sports/get_prd";
    DBhelper dBhelper;
    String mAddressOutput;
    LocationSession locationSession;
    private AddressResultReceiver mResultReceiver;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        locationSession = new LocationSession(getApplicationContext());
        dBhelper = new DBhelper(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mResultReceiver = new AddressResultReceiver(new Handler());
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()==null)
                {
                   // Get_All_Stores();
                    Get_All_Campaigns();
                    startActivity(new Intent(SplashScreen.this,LoginScreen.class));
                    finish();
                }
                else
                {
                    Get_All_Campaigns();
                    Get_All_Cart(firebaseAuth.getCurrentUser().getUid());
                    startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                    finish();
                }
            }
        };

      /*  HyperTrack.initialize(this, "pk_0b2eef905e2e26f0377e00c929539594f5f781fc");
        HyperTrack.getCurrentLocation(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse response) {

                Location location = (Location) response.getResponseObject();
                Intent intent = new Intent(SplashScreen.this, FetchAddressIntentService.class);

                // Pass the result receiver as an extra to the service.
                intent.putExtra(Constants.RECEIVER, mResultReceiver);

                // Pass the location data as an extra to the service.
                intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);

                // Start the service. If the service isn't already running, it is instantiated and started
                // (creating a process for it if needed); if it is running then it remains running. The
                // service kills itself automatically once all intents are processed.

                startService(intent);


            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {

                Toast.makeText(getApplicationContext(),"Error Occured While Fetching Location, Try Again Later",Toast.LENGTH_LONG).show();
            }
        });
        */
        mAuth.addAuthStateListener(mAuthListener);

    }


    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler)
        {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData)
        {
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            String lat = resultData.getString("latitude");
            String longitude = resultData.getString("longitude");
            if (resultCode == Constants.SUCCESS_RESULT|| resultCode==Constants.FAILURE_RESULT)
            {
                mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                Log.d("address", "" + mAddressOutput+" "+lat+ ""+longitude);
                locationSession.CreateLocationSession(mAddressOutput, Double.valueOf(lat), Double.valueOf(longitude));
                Intent intent = new Intent(SplashScreen.this, FetchAddressIntentService.class);
                stopService(intent);

            }
        }
    }

    public boolean Get_All_Stores()
    {
        final ArrayList<Store> data = new ArrayList<>();
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
                                store.setStore_address(object.getString("STORE_ADDRESS"));
                                store.setStore_desription(object.getString("DESCRIPTION"));
                                store.setStore_long(object.getString("LONGITUDE"));
                                store.setStore_lat(object.getString("LATITUDE"));
                                store.setStore_camp(object.getString("STORE_CAMP"));
                                store.setStore_url(object.getString("STORE_URL"));

                                data.add(store);

                            }
                            dBhelper.Insert_Store_Data(data);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
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
        RequestQueue requestQueue4 = Volley.newRequestQueue(SplashScreen.this);
//        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);

        return true;
    }


    public boolean Get_All_Campaigns()
    {
        final ArrayList<Campaigns> data = new ArrayList<Campaigns>();
        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, Down_Url_Camps ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONArray array = new JSONArray(s);

                            for(int i=0;i<array.length();i++)
                            {
                                Campaigns campaigns = new Campaigns();
                                JSONObject jsonObject = array.getJSONObject(i);
                                campaigns.setCampaignId(jsonObject.getString("CAMP_ID"));
                                campaigns.setCampaignName(jsonObject.getString("NAME"));
                                campaigns.setCampaignUrl(jsonObject.getString("CAMP_URL"));
                                campaigns.setCampaignType(jsonObject.getString("DESCRIPTION"));
                                data.add(campaigns);
                            }
                            dBhelper.Insert_Camp_Data(data);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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
        RequestQueue requestQueue4 = Volley.newRequestQueue(SplashScreen.this);
       // stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);

        return true;
    }


    public boolean Get_All_Cart(final String uid)
    {
        final ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, Down_Url_Camps ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONArray array = new JSONArray(s);

                            for(int i=0;i<array.length();i++)
                            {
                                HashMap<String,String> map = new HashMap<String, String>();
                                JSONObject jsonObject = array.getJSONObject(i);
                                map.put("CART_ID",jsonObject.getString("CART_ID"));
                                map.put("PRD_ID",jsonObject.getString("PRD_ID"));
                                data.add(map);
                            }
                            dBhelper.Insert_Cart(data);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("no","cartdata");
                        }

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
                Keyvalue.put("USER_ID",uid);
                return Keyvalue;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue4 = Volley.newRequestQueue(SplashScreen.this);
        // stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);

        return true;
    }
}
