package com.lions.app.sportsstore.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.lions.app.sportsstore.adapters.ProductListAdapter;
import com.lions.app.sportsstore.database.DBhelper;
import com.lions.app.sportsstore.structs.ExpandableHeightGridView;
import com.lions.app.sportsstore.structs.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductListPage extends AppCompatActivity {

    ImageView sort, filter, toggle;
    DBhelper dBhelper;
    ArrayList<Products> data = new ArrayList<Products>();
    ExpandableHeightGridView product_list;
    String cat_id;
    int numCol;
    String Down_Url_Products = "http://sportsstore.4liongroup.com/sports/get_prd_cat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_page);
        dBhelper = new DBhelper(getApplicationContext());
        product_list = (ExpandableHeightGridView)findViewById(R.id.product_list);
        toggle = (ImageView)findViewById(R.id.toggle_view);
        sort = (ImageView)findViewById(R.id.toggle_order);
        filter = (ImageView)findViewById(R.id.toggle_filter);

        product_list.setExpanded(true);
        product_list.setNumColumns(2);
        numCol=2;
        Intent intent = getIntent();
        cat_id = intent.getStringExtra("cat_id");
        Populate_List(cat_id);

       product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


               Products products = data.get(i);
               Intent intent1 = new Intent(ProductListPage.this,ProductPage.class);
               intent1.putExtra("data",products);
               startActivity(intent1);


           }
       });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numCol==2)
                {
                    Change_Orientation(1);
                    numCol =1;
                }
                else
                {
                    Change_Orientation(2);
                    numCol =2 ;
                }
            }
        });




    }


    public ArrayList<Products> Get_All_Products(final String catId)
    {
        final ArrayList<Products> productList = new ArrayList<Products>();
        final ProgressDialog progressBar = new ProgressDialog(getApplicationContext());
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading.. Please wait");

        StringRequest stringRequest4 = new StringRequest(Request.Method.POST, Down_Url_Products,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Products products = new Products();
                                products.setProduct_id(jsonObject.getString("PRD_ID"));
                                products.setProduct_no_images(jsonObject.getString("NO_IMAGES"));
                                products.setProduct_sizes(jsonObject.getString("SIZE"));
                                products.setProduct_name(jsonObject.getString("PRD_NAME"));
                                products.setProduct_price(jsonObject.getString("PRICE"));
                                products.setProduct_description("Description");
                                products.setProduct_color(jsonObject.getString("COLOR"));
                                products.setProduct_cat_id(jsonObject.getString("CAT_ID"));
                                products.setProduct_url("URL");
                                products.setProduct_quantity(jsonObject.getString("QUANTITY"));
                                products.setProduct_rating("5");
                                productList.add(products);

                            }


                        } catch (JSONException e) {
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
                        Toast.makeText(ProductListPage.this, "Error In Connectivity ", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> Keyvalue = new HashMap<String,String>();
                Keyvalue.put("cat_id",catId);
                return Keyvalue;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);

        return productList;
    }

    public void Populate_List(String aux_cat_id)
    {
        Log.d("aux_cat_id",""+aux_cat_id);
        if (data.isEmpty())
        {
            data = Get_All_Products(aux_cat_id);
            product_list.setAdapter(new ProductListAdapter(getApplicationContext(),data));


        }else
        {
            data.clear();
            data = Get_All_Products(aux_cat_id);
            product_list.setAdapter(new ProductListAdapter(getApplicationContext(),data));

        }

    }


    public void Show_Filter()
    {




    }





    public void Change_Orientation(int numColumns)
    {
        if(numColumns==1)
        {
            product_list.setNumColumns(1);
            product_list.setExpanded(true);
            Populate_List(cat_id);
        }
        else
        {
            product_list.setNumColumns(2);
            product_list.setExpanded(true);
            Populate_List(cat_id);
        }

    }







}
