package com.lions.app.sportsstore.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.lions.app.sportsstore.R;
import com.lions.app.sportsstore.adapters.CampaignAdapter;
import com.lions.app.sportsstore.adapters.CampaignAdapterText;
import com.lions.app.sportsstore.database.DBhelper;
import com.lions.app.sportsstore.structs.Campaigns;
import com.lions.app.sportsstore.structs.ExpandableHeightGridView;
import com.lions.app.sportsstore.structs.LocationSession;
import com.lions.app.sportsstore.structs.Store;
import com.lions.app.sportsstore.structs.UserSession;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , PlaceSelectionListener {

    TextView  titleFresh, offersYou;
    ExpandableHeightGridView offersList;
    ListView drawerList;
    CircleImageView dp;
    TwoWayView freshArrivals;
    LocationSession locationSession;
    UserSession userSession;
    DBhelper dBhelper;
    ArrayList<Store> storeData = new ArrayList<Store>();
    ArrayList<Campaigns> campaignDataFresh = new ArrayList<Campaigns>();
    ArrayList<Campaigns> campaignDataOffers = new ArrayList<Campaigns>();
    ArrayList<Campaigns> campaignDataLists = new ArrayList<Campaigns>();
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).build();
        ImageLoaderConfiguration.Builder config1 = new ImageLoaderConfiguration.Builder(HomeScreen.this);
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
        dBhelper = new DBhelper(getApplicationContext());
        userSession = new UserSession(getApplicationContext());
        freshArrivals = (TwoWayView)findViewById(R.id.fresh_arrivals);
        drawerList = (ListView)findViewById(R.id.cat_list);
        offersList = (ExpandableHeightGridView)findViewById(R.id.near_you);
       // addressBar = (TextView)findViewById(R.id.address_bar);
        titleFresh = (TextView)findViewById(R.id.title_fresh_arrivals);
        offersYou = (TextView)findViewById(R.id.title_offers);
        dp = (CircleImageView)findViewById(R.id.profile_image);

        imageLoader.displayImage(userSession.getUserDetails().get("dp"),dp);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(),"SourceSansProRegular.otf");

        titleFresh.setTypeface(typeface);
        offersYou.setTypeface(typeface);
        campaignDataFresh = dBhelper.Get_All_Campaigns("Fresh");
        campaignDataOffers = dBhelper.Get_All_Campaigns("Offers");
        campaignDataLists = dBhelper.Get_All_Campaigns("List");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        freshArrivals.setAdapter(new CampaignAdapter(getApplicationContext(),campaignDataFresh));
        offersList.setAdapter(new CampaignAdapter(getApplicationContext(),campaignDataOffers));
        drawerList.setAdapter(new CampaignAdapterText(getApplicationContext(),campaignDataLists));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeScreen.this,ProductListPage.class);
                intent.putExtra("camp_id",campaignDataLists.get(i).getCampaignId());
                startActivity(intent);
            }
        });

/*        addressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Get_Location_Autocomplete();
            }
        });*/

        freshArrivals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(HomeScreen.this,ProductListPage.class);
                intent.putExtra("camp_id",campaignDataFresh.get(i).getCampaignId());
                startActivity(intent);
            }
        });

        offersList.setExpanded(true);
        offersList.setNumColumns(2);
        offersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeScreen.this,ProductListPage.class);
                intent.putExtra("camp_id",campaignDataOffers.get(i).getCampaignId());
                startActivity(intent);
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
                locationSession.CreateLocationSession(place.getAddress(),place.getLatLng().latitude,place.getLatLng().longitude);

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error Fetching Location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        locationSession = new LocationSession(getApplicationContext());
  //      addressBar.setText(locationSession.getUserDetails().get("Address"));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPlaceSelected(Place place) {

        Toast.makeText(getApplicationContext(),"Selected",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(Status status) {

    }
}
