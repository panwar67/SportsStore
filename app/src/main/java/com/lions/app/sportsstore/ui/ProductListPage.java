package com.lions.app.sportsstore.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListPage extends AppCompatActivity {

    ImageView sort, filter, toggle, back, refresh;
    DBhelper dBhelper;
    ArrayList<Products> data, temp = new ArrayList<Products>();
    ExpandableHeightGridView product_list;
    String cat_id;
    int numCol;
    String Down_Url_Products = "http://sportsstore.4liongroup.com/sports/get_prd_camp/";
    Button removeFilter;
    LinearLayout emptyView;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_head);
        dBhelper = new DBhelper(getApplicationContext());
        product_list = (ExpandableHeightGridView)findViewById(R.id.product_list);
        toggle = (ImageView)findViewById(R.id.toggle_view);
        sort = (ImageView)findViewById(R.id.toggle_order);
        filter = (ImageView)findViewById(R.id.toggle_filter);
        emptyView = (LinearLayout)findViewById(R.id.product_list_empty);
        back = (ImageView)findViewById(R.id.back);
        refresh = (ImageView)findViewById(R.id.refresh);

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onBackPressed();
                return false;
            }
        });

        refresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Get_All_Products(cat_id);
                return false;
            }
        });

        emptyView.setVisibility(View.GONE);
        removeFilter = (Button) findViewById(R.id.remove_product_filter);
/*        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.productlistswipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Populate_List(cat_id);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        */
        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                boolean enable = false;
                if(product_list != null && product_list.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = product_list.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = product_list.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh product_page_head
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                //swipeRefreshLayout.setEnabled(enable);
            }
        });


        numCol=2;
        Intent intent = getIntent();
        cat_id = intent.getStringExtra("camp_id");
        Populate_List(cat_id);

        removeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emptyView.setVisibility(View.GONE);
                product_list.setAdapter(new ProductListAdapter(getApplicationContext(),data));

            }
        });

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

                final CharSequence[] items = { "Price : Low to High", "Price : High to Low" };

                AlertDialog.Builder builder2 = new AlertDialog.Builder(ProductListPage.this)
                        .setTitle("Order By")
                        .setSingleChoiceItems( items, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0)
                                {

                                    Collections.sort(data, new MapComparators() );
                                    product_list.setAdapter(new ProductListAdapter(getApplicationContext(),data));

                                }
                                else
                                if (which==1){

                                    Collections.sort(data, new MapComparators() );
                                    Collections.reverse(data);
                                    product_list.setAdapter(new ProductListAdapter(getApplicationContext(),data));


                                }

                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog2 = builder2.create();
                alertdialog2.show();


            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1  = new Intent(ProductListPage.this,ProductFilterPage.class);
                startActivityForResult(intent1,2);


            }
        });

        Change_Orientation(2);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dat) {
        super.onActivityResult(requestCode, resultCode, dat);

        HashMap<String,String> parameters = (HashMap<String, String>) dat.getSerializableExtra("parameters");
        temp.clear();

        for(int i=0;i<data.size();i++)
        {
            int min = Integer.parseInt(parameters.get("min"));
            int max = Integer.parseInt(parameters.get("max"));
            int price = Integer.parseInt(data.get(i).getProduct_price());

            if(price>min && price<max && data.get(i).getProduct_color().equals(parameters.get("color")))
            {
                temp.add(data.get(i));
            }

        }

        if(temp.isEmpty())
        {
            product_list.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"No items found",Toast.LENGTH_SHORT).show();

        }else
        {
            product_list.setVisibility(View.VISIBLE);
            product_list.setAdapter(new ProductListAdapter(getApplicationContext(),temp));

        }



    }

    public ArrayList<Products> Get_All_Products(final String catId)
    {
        Log.d("request","reached");
        final ArrayList<Products> productList = new ArrayList<Products>();
        final ProgressDialog progressBar = new ProgressDialog(ProductListPage.this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading.. Please wait");
        progressBar.show();

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
                                products.setProduct_description(jsonObject.getString("DESCRIPTION"));
                                products.setProduct_color(jsonObject.getString("COLOR"));
                                products.setProduct_cat_id(jsonObject.getString("CAT_ID"));
                                products.setProduct_url(jsonObject.getString("PRD_URL"));
                                products.setProduct_quantity(jsonObject.getString("QUANTITY"));
                                products.setProduct_rating(jsonObject.getString("RATING"));
                                productList.add(products);

                            }

                            product_list.setExpanded(true);
                            product_list.setNumColumns(2);
                            product_list.setAdapter(new ProductListAdapter(getApplicationContext(),productList));


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
                Keyvalue.put("CAMP_ID",catId);
                return Keyvalue;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue4 = Volley.newRequestQueue(ProductListPage.this);
//        stringRequest4.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to the queue
        requestQueue4.add(stringRequest4);

        return productList;
    }

    public void Populate_List(String aux_cat_id)
    {
        Log.d("aux_cat_id",""+aux_cat_id);

        try{

            data.clear();
            data = Get_All_Products(aux_cat_id);
            product_list.setAdapter(new ProductListAdapter(getApplicationContext(),data));


        }catch (NullPointerException e)
        {
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
            product_list.setAdapter(new ProductListAdapter(getApplicationContext(),data));

        }
        else
        {
            product_list.setNumColumns(2);
            product_list.setExpanded(true);
            product_list.setAdapter(new ProductListAdapter(getApplicationContext(),data));
        }

    }







}
