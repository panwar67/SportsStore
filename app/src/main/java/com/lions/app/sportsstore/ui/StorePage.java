package com.lions.app.sportsstore.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.lions.app.sportsstore.database.DBhelper;
import com.lions.app.sportsstore.structs.Catalogues;
import com.lions.app.sportsstore.structs.ExpandableHeightGridView;
import com.lions.app.sportsstore.structs.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorePage extends AppCompatActivity {

    TextView storeName, storeDes, title_catalogue;
    ExpandableHeightGridView storeCatalogueList;
    String storeID;
    DBhelper dBhelper;
    Store store;
    ArrayList<Catalogues> catalogues = new ArrayList<Catalogues>();
    String Down_Url_Catalogues = "http://sportsstore.4liongroup.com/sports/get_cat_store";

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreID() {
        return storeID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_page);

        Intent intent = getIntent();
        setStoreID(intent.getStringExtra("store_id"));

        dBhelper = new DBhelper(getApplicationContext());
        store = dBhelper.Get_Store_by_Id(storeID);

        storeCatalogueList = (ExpandableHeightGridView)findViewById(R.id.store_catalogue_list);
        storeName = (TextView)findViewById(R.id.store_name);
        storeDes = (TextView)findViewById(R.id.store_description);
        title_catalogue = (TextView)findViewById(R.id.store_catalogue);

        storeName.setText(store.getStore_name());
        storeDes.setText(store.getStore_desription());

        Get_All_Catalogues();

        storeCatalogueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent1 = new Intent(StorePage.this,ProductListPage.class);
                intent1.putExtra("cat_id",catalogues.get(i).getCatId());
                startActivity(intent1);



            }
        });
    }

    public boolean Get_All_Catalogues()
    {
        final ProgressDialog progressBar = new ProgressDialog(getApplicationContext());
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading.. Please wait");
        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, Down_Url_Catalogues,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONArray array = new JSONArray(s);
                            for(int i =0; i<array.length();i++)
                            {
                                JSONObject jsonObject = array.getJSONObject(i);
                                Catalogues catalogue = new Catalogues();
                                catalogue.setCatId(jsonObject.getString("CAT_ID"));
                                catalogue.setCatName(jsonObject.getString("CAT_NAME"));
                                catalogue.setTags(jsonObject.getString("CAT_TAGS"));
                                catalogue.setUrl(jsonObject.getString("CAT_URL"));
                                catalogues.add(catalogue);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            progressBar.cancel();
                            progressBar.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //  loading.dismiss();
                        progressBar.cancel();
                        progressBar.dismiss();
                        //Showing toast
                        Toast.makeText(StorePage.this, "Network Error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("store_id", getStoreID());
                return Keyvalue;
            }
        };
        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue4.add(stringRequest4);

        return true;
 }


}
