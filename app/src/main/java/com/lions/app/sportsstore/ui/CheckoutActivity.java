package com.lions.app.sportsstore.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.structs.LocationSession;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class CheckoutActivity extends AppCompatActivity {

    Button pickAddress;
    LocationSession locationSession;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    ImageView map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).build();
        ImageLoaderConfiguration.Builder config1 = new ImageLoaderConfiguration.Builder(CheckoutActivity.this);
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
         map = (ImageView)findViewById(R.id.mapView);
        locationSession = new LocationSession(getApplicationContext());
        pickAddress = (Button)findViewById(R.id.pickaddres);
        pickAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Get_Location_Autocomplete();
            }
        });



    }

    public void Get_Location_Autocomplete()
    {
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        int PLACE_PICKER_REQUEST = 1;
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).setCountry("IN")
                .build();
        SupportPlaceAutocompleteFragment supportPlaceAutocompleteFragment = new SupportPlaceAutocompleteFragment();
        supportPlaceAutocompleteFragment.setFilter(typeFilter);
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
        {
            if(requestCode== RESULT_OK)
            {
                Place place = PlacePicker.getPlace(this,data);
                LatLng latLng = place.getLatLng();
                String latEiffelTower = String.valueOf(latLng.latitude);
                String lngEiffelTower = String.valueOf(latLng.longitude);
                String url = "http://maps.google.com/maps/api/staticmap?center=" + latEiffelTower + "," + lngEiffelTower + "&zoom=15&size=200x200&sensor=false";
                locationSession.CreateLocationSession(place.getAddress(),place.getLatLng().latitude,place.getLatLng().longitude);
                imageLoader.displayImage(url,map);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error Fetching Location",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
