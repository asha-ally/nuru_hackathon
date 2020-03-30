package com.openinstitute.nuru;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.openinstitute.nuru.Database.DatabaseHelper;
import com.openinstitute.nuru.Database.Post;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewPost extends AppCompatActivity implements OnMapReadyCallback {
    TextView tvDescription, tvDate, tvTags, longitude, latitude;
    Button btnEdit;
    Button btnDelete;
    Button btnViewLocation;
    DatabaseHelper databaseHelper;
    Context context;
    Bundle ibundle;
    String bundleData;
    String bundlePostId;
    private BroadcastReceiver broadcastReceiver;
    MapFragment mapFragment;

    int postId;
    GoogleMap mGoogleMap;
    ImageView iview;
    String longi;
    String lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        context=this;

        databaseHelper=new DatabaseHelper(context);
        ibundle = getIntent().getExtras();
        bundleData = (String) ibundle.get("PostData");
        bundlePostId = (String) ibundle.get("PostId");

        tvDescription= findViewById(R.id.tvDescription);
        tvDate=findViewById(R.id.tvDate);
        tvTags=findViewById(R.id.tvTags);

        btnViewLocation = findViewById(R.id.btnViewLocation);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        getPostId();


        longitude=findViewById(R.id.longitude);
        latitude=findViewById(R.id.latitude);
        iview=findViewById(R.id.post_imageview);


        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                btnViewLocation.setText("");
                btnDelete.setText("");
                btnEdit.setText("");
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                btnViewLocation.setText("");
                btnDelete.setText("");
                btnEdit.setText("");
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        /*Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/


//        JSONObject data = ibundle.
        /*Log.d("bundleData", bundleData);
        Log.d("bundlePostId", bundlePostId);*/


        displayPost();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(R.string.lbl_caution);
                alertDialogBuilder.setMessage("You clicked on Delete. Are your sure?").setCancelable(false).setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        databaseHelper.deletePost(bundlePostId); /*postId*/
                        refreshMainActivity();
                        finish();

                    }
                }).setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Post clickedPost = new Post();
                JSONObject asha = clickedPost.getPostAll();
                String thePostData = String.valueOf(asha);
                Log.d("postList asha", String.valueOf(asha));

                Intent intent = new Intent(v.getContext(),PostActivity.class);

                intent.putExtra("PostActivity", String.valueOf(clickedPost));
                intent.putExtra("PostData", bundleData); /*thePostData*/
                intent.putExtra("PostAction", "_edit");
                intent.putExtra("PostId", bundlePostId);
                intent.putExtra("PostPosition", (String) ibundle.get("PostPosition"));
                startActivity(intent);

                finish();

            }
        });
        longitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                DisplayMap();


            }



        });


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);


        LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
          drawMarker(point);

    }
    public boolean DisplayMap (){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available


            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getParent(), requestCode);
            dialog.show();

        }
        else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
           mapFragment.getMapAsync(this);


//            LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
//
//
//
////            drawMarker(point);

        }
        return false;
    }


    private void drawMarker(LatLng point){
        mGoogleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(point);

        markerOptions.title("Post Taken from here");


        markerOptions.snippet("Latitude:"+point.latitude+",Longitude:"+point.longitude);

       mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));

    }



    public void getPostId(){
        /*Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            postId = bundle.getInt("KEY_POST_ID",0);

        }*/

    }


    public void displayPost(){
        try {
            JSONObject jsnobject = new JSONObject(bundleData);

            /*Log.d("ka value", jsnobject.getString("post_details"));*/
            String tags = jsnobject.getString("post_tags").replace("|", "; ");

            tvDescription.setText( jsnobject.getString("post_details"));
            tvTags.setText(tags);
            tvDate.setText(jsnobject.getString("record_date"));
            longitude.setText(jsnobject.getString("post_longitude"));
            latitude.setText(jsnobject.getString("post_latitude"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void refreshMainActivity(){
        MainActivity.getmInstanceActivity().refreshList();
    }

}
