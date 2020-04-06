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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static com.openinstitute.nuru.app.AppFunctions.func_showAlerts;

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
    String bundlePostSession;
    private BroadcastReceiver broadcastReceiver;
    MapFragment mapFragment;
    LinearLayout linearLayout;

    String val_longitude;
    String val_latitude;

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
        bundlePostSession = (String) ibundle.get("PostSession");

        tvDescription= findViewById(R.id.tvDescription);
        tvDate=findViewById(R.id.tvDate);
        tvTags=findViewById(R.id.tvTags);

        btnViewLocation = findViewById(R.id.btnViewLocation);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        getPostId();


        longitude=findViewById(R.id.longitude);
        latitude=findViewById(R.id.latitude);
//        iview=findViewById(R.id.post_imageview);
        linearLayout=findViewById(R.id.container);


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
                //Log.d("postList asha", String.valueOf(asha));

                String PostSession = "";


                    //PostSession = asha.getString("post_session");

                    Intent intent = new Intent(v.getContext(),PostActivity.class);

                    intent.putExtra("PostActivity", String.valueOf(clickedPost));
                    intent.putExtra("PostData", bundleData); /*thePostData*/
                    intent.putExtra("PostAction", "_edit");
                    intent.putExtra("PostId", bundlePostId);
                    intent.putExtra("PostSession", bundlePostSession);
                    intent.putExtra("PostPosition", (String) ibundle.get("PostPosition"));
                    startActivity(intent);



                finish();

            }
        });


        btnViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String latLong = val_latitude +","+ val_longitude;
                Log.d("latLong", latLong);
                Intent postMap = new Intent(v.getContext(), MapActivity.class);
                postMap.putExtra("post_coordinates", latLong);
                startActivity(postMap);*/
                func_showAlerts(context, "Coming soon. We'll keep you posted.", "warning");

            }
        });


        /*longitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMap();
            }
        });*/


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
            //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
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
            JSONObject jsnResources = databaseHelper.getResourceFiles(bundlePostId);

            //Log.d("jsnResources", String.valueOf(jsnResources));


            JSONObject jsnobject = new JSONObject(bundleData);

            //Log.d("bundlePostId", bundlePostId);
            //Log.d("bundleData", jsnobject.toString());

            String tags = jsnobject.getString("post_tags").replace("|", "; ");

            val_latitude = jsnobject.getString("post_latitude");
            val_longitude = jsnobject.getString("post_longitude");

            tvDescription.setText( jsnobject.getString("post_details"));
            tvTags.setText(tags);
            tvDate.setText(jsnobject.getString("record_date"));

            longitude.setText(val_longitude);
            latitude.setText(val_latitude);



            //Add Imageview
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            imageView.setImageResource(R.drawable.inducesmilelog);
            String imageUrl = jsnobject.getString("image_url");

            if (!imageUrl.equals("")){
                Bitmap thumbnail = (BitmapFactory.decodeFile(imageUrl));
                Log.w("image_path_a", imageUrl+"");
                imageView.setImageBitmap(thumbnail);}
            else {
                imageView.setVisibility(View.GONE);
            }
            if (linearLayout != null) {
                linearLayout.addView(imageView);
            }





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void refreshMainActivity(){
        MainActivity.getmInstanceActivity().refreshList();
    }

}
