package com.lions.app.sportsstore.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.lions.app.sportsstore.structs.Campaigns;
import com.lions.app.sportsstore.structs.Products;
import com.lions.app.sportsstore.structs.Store;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Panwar on 27/12/17.
 */

public class DBhelper  extends SQLiteOpenHelper {

    String dbname;
    SQLiteDatabase read, write;
    String Store_table_name = "STORES";
    String Store_id = "STORE_ID";
    String Store_name = "STORE_NAME";
    String Store_lat = "STORE_LAT";
    String Store_long = "STORE_LONG";
    String Store_description = "STORE_DESC";
    String Store_cat_id = "STORE_CAT_ID";
    String Store_Address = "STORE_ADD";
    String Store_Url = "STORE_URL";
    String Store_camp = "STORE_CAMP";
    String Store_tags = "STORE_TAGS";


    String Products_table_name = "PRODUCTS";
    String Product_name = "PRO_NAME";
    String Product_id = "PRO_ID";
    String Product_description = "PRO_DES";
    String Product_price = "PRO_PRICE";
    String Product_sizes = "PRO_SIZE";
    String Product_color = "PRO_COLOR";
    String Product_cat_id = "PRO_CAT_ID";
    String Product_url = "PRO_URL";
    String Product_no_images = "PRO_NO_IMG";


    String Campaign_table_name = "CAMPAIGNS";
    String camp_name = "CAMP_NAME";
    String camp_url = "CAMP_URL";
    String camp_id = "CAMP_ID";
    String camp_type = "CAMP_TYPE";

    String Cart_table_name = "CART";
    String cart_id = "CART_ID";
    String prd_id = "PRD_ID";


    public DBhelper(Context context) {
        super(context, "SportStore11", null, 2);
        read = this.getReadableDatabase();
        write = this.getWritableDatabase();
    }





    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(" CREATE TABLE " + Store_table_name + " (" +
                Store_id + " TEXT, " +
                Store_name + " TEXT NOT NULL, " +
                Store_lat + " TEXT NOT NULL, " +
                Store_long + " TEXT NOT NULL, " +
                Store_description + " TEXT , " +
                Store_camp + "TEXT, "+
                Store_Url + "TEXT, "+
                Store_Address + "TEXT, "+
                Store_tags + "TEXT, "+
                Store_cat_id + " INTEGER NOT NULL);"
        );

        sqLiteDatabase.execSQL("CREATE TABLE "+Products_table_name+" ("+
                Product_id + " TEXT , " +
                Product_name +"TEXT, " +
                Product_price + "TEXT ," +
                Product_sizes + "TEXT ," +
                Product_color + "TEXT, " +
                Product_description +"TEXT, "+
                Product_url +"TEXT, "+
                Product_no_images +"TEXT," +
                Product_cat_id + " INTEGER NOT NULL);"

        );

        sqLiteDatabase.execSQL("CREATE TABLE "+Campaign_table_name+"( CAMP_NAME TEXT , CAMP_URL TEXT, CAMP_TYPE TEXT, CAMP_ID TEXT);"



        );

        sqLiteDatabase.execSQL("CREATE TABLE "+Cart_table_name+" ("+
                cart_id + " TEXT , " +
                prd_id +"TEXT);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public  boolean IsProductUnique(  String Value) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from CART  where PRD_ID = '"+Value+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    public void Insert_Cart(ArrayList<HashMap<String,String>> temp)
    {
        try
        {
            write.execSQL("DELETE FROM "+Cart_table_name);
            write.beginTransaction();
            for (int i=0;i<temp.size();i++)
            {
                ContentValues contentValues = new ContentValues();
                //contentValues.put(cart_id,temp.get(i).getCampaignName());
                //contentValues.put(prd_id,temp.get(i).getCampaignUrl());
                contentValues.put(cart_id,temp.get(i).get("CART_ID"));
                contentValues.put(prd_id,temp.get(i).get("PRD_ID"));

                long row = write.insertWithOnConflict(Cart_table_name,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                Log.d("inserted_camp",""+row);
            }
            write.setTransactionSuccessful();
            write.endTransaction();
        }
        catch (SQLiteException e)
        {
            Log.d("error", e.toString());
        }

    }

    public ArrayList<HashMap<String,String>> Get_Cart()
    {
        ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String, String>>();
        Cursor res = read.rawQuery("select * from CART",null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            HashMap<String,String> map =new HashMap<>();
            map.put("CART_ID",res.getString(res.getColumnIndex("CART_ID")));
            map.put("PRD_ID",res.getString(res.getColumnIndex("PRD_ID")));
            res.moveToNext();
        }

        return data;
    }



    public void Insert_Camp_Data(ArrayList<Campaigns> data)
    {

        try
        {
            write.execSQL("DELETE FROM CAMPAIGNS");
            write.beginTransaction();
            for (int i=0;i<data.size();i++)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put(camp_name,data.get(i).getCampaignName());
                contentValues.put(camp_url,data.get(i).getCampaignUrl());
                contentValues.put(camp_id,data.get(i).getCampaignId());
                contentValues.put(camp_type,data.get(i).getCampaignType());

                long row = write.insertWithOnConflict(Campaign_table_name,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                Log.d("inserted_camp",""+row);
            }
            write.setTransactionSuccessful();
            write.endTransaction();
        }
        catch (SQLiteException e)
        {
            Log.d("error", e.toString());
        }
    }

    public void Insert_Store_Data(ArrayList<Store> data)
    {
        try {
            write.beginTransaction();
            for (int i = 0; i < data.size(); i++)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Store_name,data.get(i).getStore_name());
                contentValues.put(Store_id,data.get(i).getStore_id());
                contentValues.put(Store_description,data.get(i).getStore_desription());
                contentValues.put(Store_lat,data.get(i).getStore_lat());
                contentValues.put(Store_long,data.get(i).getStore_long());
                contentValues.put(Store_cat_id,data.get(i).getStore_cat_id());
                contentValues.put(Store_Address,data.get(i).getStore_address());
                contentValues.put(Store_Url,data.get(i).getStore_url());
                contentValues.put(Store_camp,data.get(i).getStore_camp());
                contentValues.put(Store_tags,data.get(i).getStore_tags());

                long row = write.insertWithOnConflict(Store_table_name,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                Log.d("inserted_store",""+row);
            }
            write.setTransactionSuccessful();
            write.endTransaction();
        } catch (SQLiteException e)
        {
            Log.d("error", e.toString());
        }

    }

    public void Insert_Product_Data(ArrayList<Products> data)
    {

        try {
            write.beginTransaction();
            for (int i = 0; i < data.size(); i++)
            {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Product_id,data.get(i).getProduct_id());
                contentValues.put(Product_name,data.get(i).getProduct_name());
                contentValues.put(Product_description,data.get(i).getProduct_description());
                contentValues.put(Product_color,data.get(i).getProduct_color());
                contentValues.put(Product_price,data.get(i).getProduct_price());
                contentValues.put(Product_sizes,data.get(i).getProduct_sizes());
                contentValues.put(Product_cat_id,data.get(i).getProduct_cat_id());
                contentValues.put(Product_url,data.get(i).getProduct_url());
                contentValues.put(Product_no_images,data.get(i).getProduct_no_images());
                long row = write.insertWithOnConflict(Store_table_name,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
                Log.d("inserted_product",""+row);
            }
            write.setTransactionSuccessful();
            write.endTransaction();

        } catch (SQLiteException e)
        {
            Log.d("error", e.toString());

        }


    }


    public ArrayList<Products> Get_Product_by_Catalogue(String id)
    {

        ArrayList<Products> data  = new ArrayList<Products>();
        Cursor res = read.rawQuery("select * from " + Products_table_name + " where " + Product_cat_id + " = '" + id+"'" , null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Products products = new Products();
            products.setProduct_id(res.getString(res.getColumnIndex(Product_id)));
            products.setProduct_cat_id(res.getString(res.getColumnIndex(Product_cat_id)));
            products.setProduct_color(res.getString(res.getColumnIndex(Product_color)));
            products.setProduct_description(res.getString(res.getColumnIndex(Product_description)));
            products.setProduct_name(res.getString(res.getColumnIndex(Product_name)));
            products.setProduct_sizes(res.getString(res.getColumnIndex(Product_sizes)));
            products.setProduct_price(res.getString(res.getColumnIndex(Product_price)));
            products.setProduct_url(res.getString(res.getColumnIndex(Product_url)));
            products.setProduct_no_images(res.getString(res.getColumnIndex(Product_no_images)));
            data.add(products);
        }
        return data;
    }


    public Store Get_Store_by_Id(String store_id)
    {
        Store store = new Store();
        Cursor res = read.rawQuery("select * from "+Store_table_name+" where "+Store_id+" = '"+store_id+"'",null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            store.setStore_name(res.getString(res.getColumnIndex(Store_name)));
            store.setStore_desription(res.getString(res.getColumnIndex(Store_description)));
            store.setStore_address(res.getString(res.getColumnIndex(Store_Address)));
            store.setStore_url(res.getString(res.getColumnIndex(Store_Url)));
            store.setStore_long(res.getString(res.getColumnIndex(Store_long)));
            store.setStore_lat(res.getString(res.getColumnIndex(Store_lat)));
            store.setStore_id(res.getString(res.getColumnIndex(Store_id)));
            store.setStore_cat_id(res.getString(res.getColumnIndex(Store_cat_id)));

        }

        return store;
    }

    public ArrayList<Store> Get_Stores_by_Campaign(String camp)
    {
        ArrayList<Store> data = new ArrayList<Store>();
        Cursor res = read.rawQuery("select * from "+Store_table_name+" where "+Store_camp+" like '%"+camp+"%'",null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Store store = new Store();
            store.setStore_name(res.getString(res.getColumnIndex(Store_name)));
            store.setStore_desription(res.getString(res.getColumnIndex(Store_description)));
            store.setStore_address(res.getString(res.getColumnIndex(Store_Address)));
            store.setStore_url(res.getString(res.getColumnIndex(Store_Url)));
            store.setStore_long(res.getString(res.getColumnIndex(Store_long)));
            store.setStore_lat(res.getString(res.getColumnIndex(Store_lat)));
            store.setStore_id(res.getString(res.getColumnIndex(Store_id)));
            store.setStore_cat_id(res.getString(res.getColumnIndex(Store_cat_id)));
            data.add(store);
            res.moveToNext();

        }

        return  data;
    }

    public ArrayList<Store> Get_Stores_by_Tags(String tags)
    {
        ArrayList<Store> data = new ArrayList<Store>();
        Cursor res = read.rawQuery("select * from "+Store_table_name+" where "+Store_tags+" like '%"+tags+"%'",null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Store store = new Store();
            store.setStore_name(res.getString(res.getColumnIndex(Store_name)));
            store.setStore_desription(res.getString(res.getColumnIndex(Store_description)));
            store.setStore_address(res.getString(res.getColumnIndex(Store_Address)));
            store.setStore_url(res.getString(res.getColumnIndex(Store_Url)));
            store.setStore_long(res.getString(res.getColumnIndex(Store_long)));
            store.setStore_lat(res.getString(res.getColumnIndex(Store_lat)));
            store.setStore_id(res.getString(res.getColumnIndex(Store_id)));
            store.setStore_cat_id(res.getString(res.getColumnIndex(Store_cat_id)));
            data.add(store);

        }
        return data;
    }




    public Products Get_Product_by_Id(String pro_id)
    {
        Products products = new Products();
        Cursor res = read.rawQuery("select * from "+Store_table_name+" where "+pro_id+" = '"+pro_id+"'",null);
        res.moveToFirst();




        return products;
    }

    public ArrayList<Campaigns> Get_All_Campaigns(String type)
    {
        ArrayList<Campaigns> data = new ArrayList<Campaigns>();
        Cursor res = read.rawQuery("select * from "+Campaign_table_name+" where "+camp_type+" = '"+type+"'",null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Campaigns campaigns = new Campaigns();
            campaigns.setCampaignId(res.getString(res.getColumnIndex(camp_id)));
            campaigns.setCampaignName(res.getString(res.getColumnIndex(camp_name)));
            campaigns.setCampaignUrl(res.getString(res.getColumnIndex(camp_url)));
            campaigns.setCampaignType(res.getString(res.getColumnIndex(camp_type)));

            data.add(campaigns);
            res.moveToNext();
        }

        return  data;
    }

    public ArrayList<Store> Get_Stores_by_Location(LatLng latLng)
    {
        Double lat = latLng.latitude;
        Double longitude = latLng.longitude;
        ArrayList<Store> data = new ArrayList<Store>();
        Cursor res = read.rawQuery("select * from "+Store_table_name,null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Store store = new Store();
            store.setStore_name(res.getString(res.getColumnIndex(Store_name)));
            store.setStore_desription(res.getString(res.getColumnIndex(Store_description)));
            store.setStore_address(res.getString(res.getColumnIndex(Store_Address)));
            store.setStore_url(res.getString(res.getColumnIndex(Store_Url)));
            store.setStore_long(res.getString(res.getColumnIndex(Store_long)));
            store.setStore_lat(res.getString(res.getColumnIndex(Store_lat)));
            store.setStore_id(res.getString(res.getColumnIndex(Store_id)));
            Double storeLat = Double.valueOf(res.getString(res.getColumnIndex(Store_lat)));
            Double storeLong = Double.valueOf(res.getString(res.getColumnIndex(Store_long)));
            store.setStore_distance(String.valueOf(distance(lat, longitude,storeLat,storeLong)));
            store.setStore_cat_id(res.getString(res.getColumnIndex(Store_cat_id)));
            data.add(store);

        }

        return data;

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        Log.d("distance",""+dist);
        return dist;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public ArrayList<Store> Get_Location_Sorted_List( ArrayList<Store> data)
    {
        Collections.sort(data, new Comparator<Store>()
        {
            @Override
            public int compare(Store store, Store t1) {

                Float d1 = Float.valueOf(store.getStore_distance());
                Float d2 = Float.valueOf(t1.getStore_distance());

                return d1.compareTo(d2);
            }


        });

        return data;
    }



}
