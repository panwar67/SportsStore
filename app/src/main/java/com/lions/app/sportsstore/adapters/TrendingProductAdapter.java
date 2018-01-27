package com.lions.app.sportsstore.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.structs.Products;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Panwar on 08/01/18.
 */

public class TrendingProductAdapter extends BaseAdapter {

    ArrayList<Products> result = new ArrayList<Products>();
    Context context;
    LayoutInflater inflater = null;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    public TrendingProductAdapter(Context cont, ArrayList<Products> data)
    {
        context = cont;
        result = data;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).build();
        ImageLoaderConfiguration.Builder config1 = new ImageLoaderConfiguration.Builder(context);
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

        View root = inflater.inflate(R.layout.trending_item,null);

        ImageView imageView = (ImageView)root.findViewById(R.id.trending_products_image);
        TextView product_name = (TextView)root.findViewById(R.id.trending_products_name);
        RatingBar ratingBar = (RatingBar)root.findViewById(R.id.trending_products_rating);
        TextView product_price = (TextView)root.findViewById(R.id.trending_products_price);

        imageLoader.displayImage(result.get(i).getProduct_url(),imageView);
        product_name.setText(result.get(i).getProduct_name());
        ratingBar.setRating(Float.parseFloat(result.get(i).getProduct_rating()));
        product_price.setText("Price: Rs. "+result.get(i).getProduct_price());


        return root;
    }
}
