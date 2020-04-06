package com.openinstitute.nuru;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.openinstitute.nuru.Database.DatabaseHelper;
import com.openinstitute.nuru.Database.Post;
import com.openinstitute.nuru.Database.TagList;
import com.openinstitute.nuru.app.Globals;
import com.openinstitute.nuru.app.WebAsyncPost;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.Chip;
import com.openinstitute.nuru.utils.VolleyMultipartRequest;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.openinstitute.nuru.app.AppFunctions.base64Encode;
import static com.openinstitute.nuru.app.AppFunctions.func_getUserCoded;
import static com.openinstitute.nuru.app.AppFunctions.func_showAlerts;
import static com.openinstitute.nuru.app.AppFunctions.func_showToast;
import static com.openinstitute.nuru.app.AppFunctions.isInternetConnected;
import static com.openinstitute.nuru.app.Globals.CONF_APP_NAME;
import static com.openinstitute.nuru.app.Globals.CONF_FILE_UPLOAD;
import static com.openinstitute.nuru.app.Globals.msg_no_internet;


public class PostActivity extends AppCompatActivity {

    private static final String TAG = PostActivity.class.toString();

    EditText etTitle;
    EditText etDescription;
    Button btnSave;
    private String description;
    Button btnAddphoto;
    private BroadcastReceiver broadcastReceiver;
    Button btnAddVoiceNote;
    Button btnInsertFile;
    private String title;
    private String imageUrl;
    private String date;
    private String audioUrl;
    private ImageView imgView;
    private String Url;

    private String user_id;
    private String user_email;
    private static String user_email_code;
    int postId;
    private String post_projects;
    private String post_tag;
    private String post_session;
    private String post_category;
    private String post_longitude;
    private String post_latitude;

    public static String res_image_folder;
    public static File res_nuru_folder;




    private String POSTS_API_URL="https://nuru.live/dashboard/api/ajbk_post.php";
    //private String POSTS_API_URL="http://sand-box.online/nuru/api/ajbk_post.php";
    //private String POSTS_API_URL="http://10.0.2.2/oireporting_web/api/ajbk_post.php";

    Bundle bundle;
    String bundleData;
    String form_activity;
    String form_action;
    String form_data;
    String form_post_id;
    String form_post_position;
    String form_post_session;
	LinearLayout linearLayout;

    /* Rage Camera */
    /*private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    static final int PICTURE_RESULT = 1;
    private Uri file;
    String mCurrentPhotoPath;*/
    /* ----- */

    /* RAGE CAMERA */
    public static final int  MEDIA_TYPE_IMAGE                    = 1;
    public static final int  MEDIA_TYPE_VIDEO                    = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static String    file_hash;
    public static final int PICTURE_RESULT = 1;
    private String           filePath;

    private Uri fileUri;
    private Uri file;
    /*ImageView imageView;*/
    Bitmap help1;

    String picturePath;
    /* End:: RAGE CAMERA */

    private  Bitmap bitmap;




    static final int REQUEST_IMAGE_CAPTURE = 1;
    TextView textFile;

    private static final int PICKFILE_RESULT_CODE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    SharedPreferences prefs;
    Context context;
    ImageView imageView;
    Spinner projects;
    Spinner category;
    Spinner tag;

    DatabaseHelper databaseHelper;
    private WebAsyncPost asyncForm;
    TextView tvlongitude;
    TextView tvlatitude;

    /* Rage Chips*/
    ChipGroup txt_tags_chipGroup;
    TextInputEditText txt_tags;
    List<TagList> chip_tags;
    String[] chip_tags_default;
    String[] chip_tags_selected;

    String chips_saved;

    View focusView;

    Boolean internetConnected = false;



    //TODO -- getOrientation() akulaku




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        context = this;

        this.setTitle("Share What's Up!");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UploadService.NAMESPACE = "com.openinstitute.nuru";

        bundle = getIntent().getExtras();

        form_activity = bundle.getString("PostActivity");
        form_action = bundle.getString("PostAction", null);
        form_data = bundle.getString("PostData", null);
        form_post_id = bundle.getString("PostId", null);
        form_post_position = bundle.getString("PostPosition", null);
        form_post_session = bundle.getString("PostSession", null);
	    linearLayout = this.findViewById(R.id.container);


        //Log.d("test_int", ""+ Integer.parseInt("success"));
        /*Log.d("form_data", form_data);*/
        /*Log.d("form_post_session", form_post_session);*/

        focusView = null;
        imageUrl = "";

        etDescription = findViewById(R.id.etDescription);

        /* Rage Chips*/
        chip_tags_default =  Globals.CONF_POST_TAGS ;
        chip_tags = new ArrayList<>();

        txt_tags_chipGroup = findViewById(R.id.txt_tags_chipGroup);
        txt_tags    = findViewById(R.id.txt_tags);

        txt_tags_chipGroup.setBackground(txt_tags.getBackground());
        txt_tags.setBackground(null);



//        imageView = findViewById(R.id.imgView);
        imgView = findViewById(R.id.thumbnail);
        databaseHelper = new DatabaseHelper(this);
        btnSave = findViewById(R.id.btnSave);

        prefs = getApplicationContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        user_id = prefs.getString("user_email",null);
        user_email = prefs.getString("user_email",null);

        /*Log.d(TAG, "user_email: " + user_email);*/
        user_email_code = func_getUserCoded(user_email);

        btnAddphoto = findViewById(R.id.btnAddPhoto);
        btnAddVoiceNote = findViewById(R.id.btnAddVoiceNote);
        btnInsertFile = findViewById(R.id.btnInsertFile);
        /*projects = findViewById(R.id.project_array);*/
        /*tag = findViewById(R.id.tag_array);*/
        /*category = findViewById(R.id.category_array);*/
        tvlongitude = findViewById(R.id.tvlongitude);
        tvlatitude = findViewById(R.id.tvlatitude);

        Intent intent = new Intent(getApplicationContext(),GPS_Service.class);
        startService(intent);

        getPostId();



        displayPost();
        CheckPermissions();

        if(!form_action.equals("_edit")){
            addDefaultChips();
            /*jsonObject.getString("post_details")*/
        } else {

            addSavedChips(chips_saved);
        }
        /*String tags = jsnobject.getString("post_tags").replace("|", "; ");*/




        txt_tags.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int startIndex = 0;
                int endIndex = s.length();
                boolean startFound = false;


                if( s.length() != 0 ){
                    String lastChar = s.subSequence(endIndex - 1, endIndex).toString();
                    if( lastChar.equals(",") ){
                        //Log.d(TAG, "rage_chip_changed: " + s.toString());
                        String trimmed = s.subSequence(startIndex, endIndex - 1).toString();

                        addChip(trimmed, "y");

                    }
                }
            }
        });



        btnAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });


        btnInsertFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);*/
                func_showAlerts(context, "Coming soon. We'll keep you posted.", "warning");
            }

        });


        btnAddVoiceNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(getBaseContext(),VoiceRecordActivity.class);
                startActivity(intent);*/

                func_showAlerts(context, "Coming soon. We'll keep you posted", "warning");

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override

            public void onClick(View v) {

                String tags_selected = "";
                for (int i=0; i < txt_tags_chipGroup.getChildCount();i++){
                    Chip chip_item = (Chip) txt_tags_chipGroup.getChildAt(i);
                    if (chip_item.isChecked()){
                        /*chip_tags_selected = new String[]{ chip_item.getText().toString() };*/
                        tags_selected += chip_item.getText().toString() + "|";
                        /*Log.d("chipGroup_"+i, tags_selected );*/
                    }
                }

                Timestamp action_time = new Timestamp(System.currentTimeMillis());
                String action_time_id = String.valueOf(action_time.getTime());

                //Log.d("chip_tags_selected", tags_selected);

                description = etDescription.getText().toString();

                /*post_projects= projects.getSelectedItem().toString();*/
                post_projects = "COVID-19";
                /*post_tag = tag.getSelectedItem().toString();*/
                post_tag = tags_selected;
                post_session = action_time_id;
                post_category = "post_activity"; // category.getSelectedItem().toString();
                post_longitude = tvlongitude.getText().toString();
                post_latitude = tvlatitude.getText().toString();

                int len_description = description.length();
                int len_tags_selected = tags_selected.length();

                boolean cancel = false;

                if(len_description == 0) {
                    func_showAlerts(context, "Comments / Details required", "warning" );
                    focusView = etDescription;
                    cancel = true;
                }
                else
                if(len_tags_selected == 0) {
                    func_showAlerts(context, "Select or Enter at least one Tag", "warning" );
                    focusView = txt_tags;
                    cancel = true;
                }

                if (cancel) {

                    focusView.requestFocus();

                } else {


                    String result = "0";
    //
                    if(form_action.equals("_edit")) {
                        post_session = form_post_session;
                        Log.d("form_post_id", form_post_session);
                    }
                    //PostActivity post =new PostActivity(title,description,date,imageUrl,audioUrl,user_id);
                    //databaseHelper.addPost(post);

                    JSONObject post_b = new JSONObject();
                    try {

                        if(form_action.equals("_edit")){
                            post_b.put("post_id", form_post_id);
                        }
                        //post_b.put("imageUrl", Url);
                        post_b.put("user_id", user_email);
                        post_b.put("description", description);
                        post_b.put("post_project",post_projects);
                        post_b.put("post_tag",post_tag);
                        post_b.put("post_session", post_session);
                        post_b.put("post_category", post_category);
                        post_b.put("post_longitude",post_longitude);
                        post_b.put("post_latitude",post_latitude);
                        post_b.put("image_url", imageUrl);
    //                    Log.d("url",imageUrl);


                        if(form_action.equals("_edit")){
                            result = databaseHelper.post_Edit(post_b, form_post_id);
                        } else {
                            result = databaseHelper.post_Add(post_b);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*Log.d("post_result", String.valueOf(result));*/



                    if(form_action.equals("_edit")) {
                        /*Intent intent = new Intent(v.getContext(), ViewPost.class);
                        intent.putExtra("PostActivity", form_activity);
                        intent.putExtra("PostData", bundleData);
                        intent.putExtra("PostId", form_post_id);
                        intent.putExtra("PostPosition", form_post_position);
                        startActivity(intent);*/
                    }



                    internetConnected = isInternetConnected(context);
                    if (internetConnected) {

                        if(imageUrl.length() > 4) {
                            String up_file_name = user_email_code + "-" + post_session + "-" + Math.random();
                            files_uploadMultipart(up_file_name, result);
                        }

                        String booking_sess = "" + Math.random();
                        String form_category = "transfer";
                        String jsonString = String.valueOf(post_b); /*Log.d("jsonString", form_category + " -- " + jsonString);*/
                        String resultJson = base64Encode(jsonString); /*Log.d("jsonString", form_category + " -- " + resultJson);*/
                        asyncForm = new WebAsyncPost(context, user_email, form_category, resultJson, POSTS_API_URL, "POST");
                        asyncForm.execute();






                        //func_showAlerts(context, "Saved to server.", "");
                    } else {
                        func_showAlerts(context, msg_no_internet, "warning");
                    }

                    refreshMainActivity();

                    finish();


                    //postNote("none",description,user_id);

                    stopService(intent);

                }
            }


        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver =new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    /*tvlongitude.append("" + intent.getExtras().get("gps_longitude"));
                    tvlatitude.append("" + intent.getExtras().get("gps_latitude"));*/

                    tvlatitude.setText("" + intent.getExtras().get("gps_latitude"));
                    tvlongitude.setText("" + intent.getExtras().get("gps_longitude"));
                    Log.d("lat_long",tvlatitude.toString() + " - " + tvlongitude.toString());
                }
            };
            registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);

        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void refreshMainActivity(){

        /*MainActivity.getInstance().refreshList();*/
        MainActivity.getmInstanceActivity().refreshList();
    }


    private String selectImage() {

        files_test_getExternalStoragePath();

        final CharSequence[] options = {  "Take Photo", "Choose from Gallery","Cancel" }; /*"Take Photo",*/
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",f));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 1);*/
                    files_getCamera();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    String path = null;

                    Intent intent = new   Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                    //startActivityForResult(intent, 2);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        Uri photoURI = null;
                        File photoFile = files_getResultMediaFile(MEDIA_TYPE_IMAGE);
                        path = photoFile.getAbsolutePath();

                        filePath = photoFile.getAbsolutePath();
                        fileUri = FileProvider.getUriForFile(context,
                                getString(R.string.file_provider_authority),
                                photoFile);
                        //fileUri = Uri.fromFile(photoFile);

                        //Log.d("file_choose_Uri", String.valueOf(fileUri));
                        //Log.d("file_choose_Path", path);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        startActivityForResult(intent, 2);
                    }

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

        return imageUrl;
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                String picturePath = filePath;

                /*Bitmap*/ bitmap = (BitmapFactory.decodeFile(picturePath));
                Log.w("image_path_a", picturePath+"");
                //imageView.setImageBitmap(bitmap);

                //imageUrl = picturePath;

		        ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                if (imageUrl!=null){
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("image_path_a", imageUrl+"");
                    imageView.setImageBitmap(thumbnail);}
                else {
                    imageView.setVisibility(View.GONE);
                }
                if (linearLayout != null) {
                    linearLayout.addView(imageView);
                    imageUrl = picturePath;
                }


            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);

                c.close();

                Bitmap bitmap_one = (BitmapFactory.decodeFile(picturePath));
                Log.w("image_path_b", picturePath+"");


                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                imageView.setImageBitmap(bitmap_one);
                imageUrl = picturePath;

                bitmap = bitmap_one;

                if (linearLayout != null) {
                    linearLayout.addView(imageView);
                    imageUrl = picturePath;
                }


            }
        }
    }


    public void addSavedChips(String de_chips){
        //String tags = de_chips.replace("|", "; ");
        //Log.d("chips_saved_b", chips_saved);

        String[] pieces = chips_saved.split("\\|");

        for (String tag_item : pieces) {
            addChip(tag_item, "y");
        }

        addDefaultChips();
    }

    public void addDefaultChips(){

        for (String tag_item : chip_tags_default) {
            if( !form_action.equals("_edit") || !chips_saved.contains(tag_item)){
                addChip(tag_item, "n");
            }
        }
    }

    public void addChip(String text, String checkd){
        Chip chip = new Chip(this);

        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);

        if(checkd.equals("y")){
            chip.setChecked(true);
        } else {
            chip.setChecked(false);
        }

        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_tags_chipGroup.removeView(chip);
            }
        });


        txt_tags_chipGroup.addView(chip);
        txt_tags.setText("");

        /*txt_tags_chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if(chip != null){
                    //Toast.makeText(AndroidChipActivity6.this, chip.getText().toString(),Toast.LENGTH_LONG).show();
                    Log.d("chipGroup_"+i, chip.getText().toString());
                }
            }
        });*/

        /*for (int i=0; i < txt_tags_chipGroup.getChildCount();i++){
            Chip chip_item = (Chip) txt_tags_chipGroup.getChildAt(i);
            if (chip_item.isChecked()){
                chip_tags_selected = new String[]{ chip_item.getText().toString() };
                Log.d("chipGroup_"+i, chip_item.getText().toString());
            }
        }*/

    }

    public void getPostId(){
        /*Bundle bundle = getIntent().getExtras();*/
        if(bundle != null){
            postId = bundle.getInt("KEY_POST_ID",0);
        }
    }

    public void displayPost(){

        String post = form_data;

        try {
            //JSONArray jsonArray = new JSONArray(post);
            JSONObject jsonObject = new JSONObject(post);
            //Log.d("displayPostObject", String.valueOf(jsonObject));
            //Log.d("displayPostObject", jsonObject.getString("post_Id"));

            chips_saved = jsonObject.getString("post_tags");
            //Log.d("chips_saved", chips_saved);

            etDescription.setText( jsonObject.getString("post_details"));
            //etTitle.setText(jsonObject.getString("record_date"));
            tvlongitude.setText(jsonObject.getString("post_longitude"));
            tvlatitude.setText(jsonObject.getString("post_latitude"));
//            String imageUrl=jsonObject.getString("image_url");
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            imageView.setImageResource(R.drawable.inducesmilelog);
            String imageUrl=jsonObject.getString("image_url");

            if (imageUrl!=null){
                Bitmap thumbnail = (BitmapFactory.decodeFile(imageUrl));
                Log.w("image_path_a", imageUrl+"");
                imageView.setImageBitmap(thumbnail);}
            else {
                imageView.setVisibility(View.GONE);
            }
            if (linearLayout != null) {
                linearLayout.addView(imageView);
            }
            //projects.setSelection();
        }
        catch (JSONException e) {
            Log.e("displayPost", e.getMessage());
        }

        /*Post post= new Post();
        JSONObject jsnobject = post.getPostAll();
        try {
            etDescription.setText( jsnobject.getString("post_details"));
            Log.d("edited", jsnobject.getString("post_details"));
            etTitle.setText(jsnobject.getString("record_date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }


    public boolean CheckPermissions() {
            if (Build.VERSION.SDK_INT >=23 && ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION,CAMERA,WRITE_EXTERNAL_STORAGE,RECORD_AUDIO,READ_EXTERNAL_STORAGE, FOREGROUND_SERVICE},100);
                return true;
            }
            return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults [0] == PackageManager.PERMISSION_GRANTED && grantResults [1]== PackageManager.PERMISSION_GRANTED  && grantResults [2]== PackageManager.PERMISSION_GRANTED && grantResults [3]== PackageManager.PERMISSION_GRANTED&&grantResults [4]== PackageManager.PERMISSION_GRANTED &&grantResults [5]== PackageManager.PERMISSION_GRANTED){
                /*Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();*/

            }
        }
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(PostActivity.this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
    }

    private  void postNote(final String title, final String description,final String user_id){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, POSTS_API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Log.d("post response",s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int user_id=jsonObject.getInt("user_id");
                    String title= jsonObject.getString("title");
                    String description=jsonObject.getString("description");


                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("json_error",volleyError.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<String, String>();
                params.put("user_id",String.valueOf(user_id));
                params.put("title",title);
                params.put("description",description);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);

    }



    public void asyncResponse(String response){
        int num_response = Integer.parseInt(response);

        if(response.equals("Success") ||  num_response > 0){
            func_showAlerts(context, "Saved to server.", "");
        } else {
            func_showAlerts(context, "Save to server FAILED!", "warning");
        }
        //Log.d("asyncResponse_Transfer", response);
        //db.updateFormPostSync(session_id, form_category);
    }





    /*
     * ==============================================================================
     * RAGE UPLOAD FUNCTION
     * ==============================================================================
     * */

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void files_uploadMultipart(String params, String post_entry_id) {
        //getting name for the image
        String name = params; //editText.getText().toString().trim();

        //getting the actual path of the image
        String path = imageUrl; //files_uploadGetPath(fileUri);
        //String path = filePath; //getPath(filePath);


        //Log.d("upload_Uri", String.valueOf(fileUri));
        //Log.d("upload_Path", path);

        /*try {

            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
            uploadBitmap(bitmap);

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
*/

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, CONF_FILE_UPLOAD)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .addParameter("post_entry_id", post_entry_id)
                    .addParameter("user_id", user_email)
                    .setMaxRetries(3);

            // For Android > 8, we need to set an Channel to the UploadNotificationConfig.
            // So, here, we create the channel and set it to the MultipartUploadRequest
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel("Upload", "Upload service", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);

                UploadNotificationConfig notificationConfig = new UploadNotificationConfig();
                notificationConfig.setNotificationChannelId("Upload");
                uploadRequest.setNotificationConfig(notificationConfig);

            } else {
                // If android < Oreo, just set a simple notification (or remove if you don't wanna any notification
                // Notification is mandatory for Android > 8
                uploadRequest.setNotificationConfig(new UploadNotificationConfig());
            }

            uploadRequest.startUpload(); //Starting the upload



            Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();

        } catch (Exception exc) {
            Toast.makeText(this, "NOT uploaded " + exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    public String files_uploadGetPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }



    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, CONF_FILE_UPLOAD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //Log.d("uploadBitmap_resp", String.valueOf(response.data));
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            //Log.d("uploadBitmap", String.valueOf(obj));

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            //Log.d("uploadBitmap_err", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Log.d("uploadBitmap_err", error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }







    /*
     * ==============================================================================
     * RAGE CAMERA COLLECTION
     * ==============================================================================
     * */


    public void files_getCamera() { /*View v*/

        /*imageView = findViewById(R.id.login_logo);*/
        /*String[] email_pieces = (String.valueOf(user_email)).split("@");
        file_hash = email_pieces[0];*/

        String path = null;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Uri photoURI = null;
            File photoFile = files_getResultMediaFile(MEDIA_TYPE_IMAGE); //createImageFileWith();
            path = photoFile.getAbsolutePath();

            filePath = photoFile.getAbsolutePath();
            fileUri = FileProvider.getUriForFile(context,
                    getString(R.string.file_provider_authority),
                    photoFile);
            /*fileUri = Uri.fromFile(photoFile);*/

            //Log.d("fileUri", String.valueOf(fileUri));
            //Log.d("filePath", path);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //fileUri

            startActivityForResult(takePictureIntent, PICTURE_RESULT); /*CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE*/
        }

        /*return filePath;*/
    }



    /** Create a File for saving an image or video */
    private static File files_getResultMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        //File file = new File(context.getFilesDir(), "");
        //Log.d("getFilesDir", String.valueOf(file));

        File mediaStorageDir;

        String de_path = files_getExternalSdCardPath();
        //String de_path_b =  files_test_getExternalStoragePath();

        //Log.d("de_path", de_path);
        //Log.d("de_path_b", res_nuru_folder.getPath());

        mediaStorageDir = new File(de_path + "/nuru/" + "/nuru_images/" );
        //mediaStorageDir = new File(res_image_folder );

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();

            if (!mediaStorageDir.mkdirs()) {
                //Log.d("NuruCamera", "failed to create directory");
                return null;
            }
        }

        String fileName;
        String dateString = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());


        fileName = user_email_code + "_" + dateString;
        file_hash = dateString;


        /*if (file_hash != null) {
            fileName = file_hash + "_" + dateString;
        }
        else {
            fileName = dateString;
        }*/

        File mediaFile;
        String mediaDir = mediaStorageDir.getPath(); //.replace("/data/data/", "/");
        //String mediaDir = res_nuru_folder.getPath(); //.replace("/data/data/", "/");
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File( mediaDir + File.separator + fileName + ".jpg");
        }
        else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName + ".mp4");
        }
        else {
            return null;
        }

        return mediaFile;
    }


    public File files_test_getExternalStoragePath() {

        //File internalCardFile = null;
        String path = null;

        ContextWrapper cw = new ContextWrapper( getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("nuru_images", Context.MODE_PRIVATE);

        if (files_testIsWritable(directory)) {
            path = directory.getAbsolutePath();
        }

        if (path != null) {
            res_nuru_folder = new File(path);
        }

        return res_nuru_folder;
    }




    public static String files_getExternalSdCardPath() {
        File sdCardFile = null;
        String path = null;

        sdCardFile = files_getSecondaryStorage();

        //sdCardFile = files_test_getExternalStoragePath();


                /*sdCardFile = new File(Environment.getDataDirectory(),context.getPackageName()+ "/"+ cache_images_folder +"/");*/
                /*sdCardFile = new File(Environment.getDataDirectory(),CONF_APP_NAME+ "/media/");
                if (!sdCardFile.exists()) {
                        sdCardFile.mkdir();
                }*/

                /*final File sdCardFile = new File(Environment.getExternalStorageDirectory(),
                        context.getPackageName());
                if (!sdCardFile.exists()) {
                        sdCardFile.mkdir();
                }*/

        path = sdCardFile.getAbsolutePath();
        /*path = res_nuru_folder.getAbsolutePath();*/
        /*Log.d("sdCardFile", path);*/
        return path;
    }


    /**
     * Gets the external sd card path from a system variable, if the result
     * was null, this function call the function files_getSdcardByPossiblePaths to
     * verify the path manually.
     *
     * @return externalSdCard the external sd card {@link File}
     */
    private static File files_getSecondaryStorage() {
        String value = System.getenv("SECONDARY_STORAGE");
        File externalSdCard = null;

        if (!TextUtils.isEmpty(value)) {
            String[] paths = value.split(":");

            for (String path : paths) {
                File file = new File(path);

                if (files_testIsWritable(file)) {
                    externalSdCard = file;
                }
            }
        }

        if (externalSdCard == null) {
            externalSdCard = files_getSdcardByPossiblePaths();

        }

        return externalSdCard;
    }


    /**
     *
     * There are a lot of different kind of android versions and trademarks,
     * so, the implementation of each version are different.
     *
     * This function verifies in a list of possibilities the path of the
     * external sd card.
     *
     * @return sdCardFile the external sd card {@link File}
     */
    private static File files_getSdcardByPossiblePaths() {
        File sdCardFile = null;
        String path = null;

        List<String> sdCardPossiblePath = Arrays.asList("external_sd", "ext_sd", "external", "extSdCard");

        for (String sdPath : sdCardPossiblePath) {
            File file = new File("/mnt/", sdPath);

            if (files_testIsWritable(file)) {
                path = file.getAbsolutePath();
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
        }
        else {
            /*sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());*/
	    sdCardFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        }

        return sdCardFile;
    }

    /**
     * Test whether a file is writable or not.
     *
     * @param {@link File}
     * @return {@link Boolean} isWritable
     *
     */
    public static boolean files_testIsWritable(File file) {
        boolean isWritable = false;

        if (file.isDirectory() && file.canWrite()) {
            String path = file.getAbsolutePath();

            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            File testWritable = new File(path, "test_" + timeStamp);

            if (testWritable.mkdirs()) {
                testWritable.delete();
                isWritable = true;
            }
        }

        return isWritable;
    }




    protected void onActivityResultXX(int requestCode, int resultCode, Intent data) {
        Log.e("cam_requestCode", String.valueOf(requestCode));
        Log.e("cam_resultCode", String.valueOf(resultCode));
        Log.e("cam_intent_data", String.valueOf(data));

        /*if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }*/

        /*String picha = fileUri.getPath().replace("/ftms_files", "/storage/sdcard0");
        Log.d("fileResultseg", picha);*/

        if(requestCode == PICTURE_RESULT) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    help1 = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);
                    imageView.setImageBitmap( ThumbnailUtils.extractThumbnail(help1,help1.getWidth(),help1.getHeight()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }



    /*
     * ==============================================================================
     * END :: RAGE CAMERA COLLECTION
     * ==============================================================================
     * */




}



