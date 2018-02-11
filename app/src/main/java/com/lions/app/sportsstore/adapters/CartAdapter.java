package com.lions.app.sportsstore.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.ui.HomeScreen;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Panwar on 11/02/18.
 */

public class CartAdapter extends BaseAdapter {

    private final DisplayImageOptions options;
    private final ImageLoader imageLoader;
    ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String, String>>();
    Context cont;
    LayoutInflater inflater = null;



    public CartAdapter(Context context, ArrayList<HashMap<String,String>> data)
    {

        cont = context;
        result = data;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).build();
        ImageLoaderConfiguration.Builder config1 = new ImageLoaderConfiguration.Builder(cont);
        config1.defaultDisplayImageOptions(options);
        config1.threadPriority(Thread.NORM_PRIORITY - 2);
        config1.denyCacheImageMultipleSizesInMemory();
        config1.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config1.diskCacheSize(100 * 1024 * 1024); // 50 MiB
        config1.tasksProcessingOrder(QueueProcessingType.LIFO);
        config1.writeDebugLogs();
        imageLoader = ImageLoader.getInstance();
//        imageLoader.destroy();
        imageLoader.init(config1.build());

    }


    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View root = inflater.inflate(R.layout.cart_item,null);

        ImageView dp = (ImageView)root.findViewById(R.id.cart_image);
        TextView product_name = (TextView)root.findViewById(R.id.cart_product_name);
        TextView product_price = (TextView)root.findViewById(R.id.cart_product_price);




        return root;
    }
}
