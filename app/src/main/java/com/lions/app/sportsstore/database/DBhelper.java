package com.lions.app.sportsstore.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lions.app.sportsstore.structs.Products;
import com.lions.app.sportsstore.structs.Store;

import java.util.ArrayList;
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


    public DBhelper(Context context) {
        super(context, "SportStore", null, 1);
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


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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

    public ArrayList<Store> Get_Stores_by_Catalogue(String cat_id)
    {
        ArrayList<Store> data = new ArrayList<Store>();

        Cursor res = read.rawQuery("select * from "+Store_table_name+" where "+ Store_cat_id+ " = '"+cat_id+"'",null);
        res.moveToFirst();
        while (!res.isAfterLast())
        {
            Store store = new Store();
            store.setStore_name(res.getString(res.getColumnIndex(Store_name)));
            store.setStore_desription(res.getString(res.getColumnIndex(Store_description)));
            store.setStore_address(res.getString(res.getColumnIndex(Store_Address)));
            store.setStore_cat_id(res.getString(res.getColumnIndex(Store_cat_id)));
            store.setStore_id(res.getString(res.getColumnIndex(Store_id)));
            store.setStore_lat(res.getString(res.getColumnIndex(Store_lat)));
            store.setStore_long(res.getString(res.getColumnIndex(Store_long)));
            store.setStore_url(res.getString(res.getColumnIndex(Store_Url)));
            data.add(store);
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

    public Products Get_Product_by_Id(String pro_id)
    {
        Products products = new Products();
        Cursor res = read.rawQuery("select * from "+Store_table_name+" where "+pro_id+" = '"+pro_id+"'",null);
        res.moveToFirst();

        return products;
    }




}
