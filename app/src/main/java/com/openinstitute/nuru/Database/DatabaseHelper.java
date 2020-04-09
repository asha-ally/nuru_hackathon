package com.openinstitute.nuru.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.openinstitute.nuru.app.AppFunctions.func_formatDateFromString;
import static java.lang.System.currentTimeMillis;

public class DatabaseHelper extends  SQLiteOpenHelper {

    private String TAG = "DatabaseHandler";


    // All Static variables
    private String              path;
    private SQLiteDatabase      myDataBase;


    // Database Name
    private static final String DATABASE_NAME = "db_notepad_lite";

    // General Fields
    private static final String KEY_RECORD_ID = "record_id";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_RECORD_CAT = "category_id";
    private static final String KEY_ACCOUNT_ID = "account_id";



    // Accounts table name
    private static final String TABLE_ACCOUNTS = "npd_users";
    private static final String KEY_USER_ACC_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PASS = "user_password";
    private static final String KEY_USER_FNAME = "user_fname";
    private static final String KEY_USER_LNAME = "user_lname";
    private static final String KEY_USER_PHONE = "user_number";
    private static final String KEY_USER_DP= "user_dp";
    private static final String KEY_USER_DATE = "user_date";


    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNTS + " ( " +
            "  `" + KEY_USER_ACC_ID + "` INTEGER  primary key autoincrement," +
            "  `" + KEY_USER_FNAME + "` TEXT," +
            "  `" + KEY_USER_LNAME + "` TEXT," +
            "  `" + KEY_USER_PHONE + "` TEXT," +
            "  `" + KEY_USER_EMAIL + "` TEXT," +
            "  `" + KEY_USER_PASS + "` TEXT," +
            "  `" + KEY_USER_DP + "` TEXT," +
            "  `" + KEY_USER_DATE + "` TEXT" +
            ")";



    // POSTS Table
    public static final String TABLE_POSTS= "npd_posts";
    private static final String KEY_POST_ACC_ID = "user_id";
    public static final String KEY_POST_ID = "post_Id";
    private static final String KEY_POST_SESSION = "post_session";
    private static final String KEY_POST_DETAIL = "post_detail";
    private static final String KEY_POST_DATE = "record_date";
    private static final String KEY_POST_TITLE= "post_title";
    private static final String KEY_POST_TAG="post_tag";
    private static final String KEY_POST_CATEGORY="post_category";
    private static final String KEY_POST_PROJECT="post_project";
    private static final String KEY_POST_LONGITUDE="post_longitude";
    private static final String KEY_POST_LATITUDE="post_latitude";


    private static final String CREATE_POSTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_POSTS+ " ( " +
            " `" + KEY_POST_ID + "` integer primary key autoincrement," +
            " `" + KEY_POST_ACC_ID + "` TEXT DEFAULT 0 NOT NULL," +
            " `" + KEY_POST_SESSION + "` TEXT," +
            " `" + KEY_POST_DETAIL + "` TEXT," +
            " `" + KEY_POST_DATE + "` TEXT," +
            " `" + KEY_POST_TITLE + "` TEXT  , " +
            " `" + KEY_POST_TAG+ "` TEXT ,"  +
            " `" + KEY_POST_CATEGORY+ "` TEXT ,"  +
            " `" + KEY_POST_PROJECT+ "` TEXT ,"  +
            " `" + KEY_POST_LONGITUDE+ "` TEXT ,"  +
            " `" + KEY_POST_LATITUDE+ "` TEXT "  +

            ")";

    //Resources Table
    public static final String TABLE_RESOURCES= "npd_resources";
    private static final String KEY_RES_ID = "post_Id";
    private static final String KEY_RES_IMAGE= "post_imageUrl";
    private static final String KEY_RES_AUDIO= "post_audioUrl";


    private static final String CREATE_RESOURCES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_RESOURCES+ " ( " +
            " `" + KEY_RES_ID + "` INTEGER primary key autoincrement," +
            " `" + KEY_RES_IMAGE+ "` TEXT," +
            " `" + KEY_RES_AUDIO + "` TEXT " +
            ")";


    // Organization Table
    public static final String TABLE_ORG= "npd_organization";
    private static final String KEY_ORG_ID = "organization_id";
    private static final String KEY_ORG_NAME = "organization_name";
    private static final String KEY_ORG_EMAIL = "organization_email";
    private static final String KEY_ORG_PASSWORD = "organization_password";


    private static final String CREATE_ORG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORG+ " ( " +
            " `" + KEY_ORG_ID + "` INTEGER primary key autoincrement," +
            " `" + KEY_ORG_NAME + "` TEXT ," +
            " `" + KEY_ORG_EMAIL + "` TEXT ," +
            " `" + KEY_ORG_PASSWORD + "` TEXT " +
            ")";

    // Project Table
    public static final String TABLE_PROJECTS= "npd_project";
    private static final String KEY_PROJECT_ID = "project_id";
    private static final String KEY_PROJECT_NAME = "project_name";
    private static final String KEY_PROJECT_ORG_ID = "organization_id";
    private static final String KEY_PROJECT_POST_ID = "post_id";


    private static final String CREATE_PROJECTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROJECTS+ " ( " +
            " `" + KEY_PROJECT_ID + "` INTEGER primary key autoincrement," +
            " `" + KEY_PROJECT_NAME + "` TEXT DEFAULT 0 NOT NULL," +
            " `" + KEY_PROJECT_ORG_ID + "` INTEGER," +
            " `" + KEY_PROJECT_POST_ID+ "` INTEGER" +
            ")";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        path = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ACCOUNTS_TABLE);
        database.execSQL(CREATE_POSTS_TABLE);
        database.execSQL(CREATE_ORG_TABLE);
        database.execSQL(CREATE_RESOURCES_TABLE);
        database.execSQL(CREATE_PROJECTS_TABLE);
    }


    /* CUSTOM FUNCTION FOR RANDOM EVENTS*/
    public void customDbAction(){
        SQLiteDatabase db = this.getWritableDatabase();

        String dropTablePosts = "DROP TABLE IF EXISTS " + TABLE_POSTS+ "; ";
        //String alterTablePosts = "ALTER TABLE " + TABLE_POSTS + " ADD " + KEY_POST_ID + " TYPE integer primary key autoincrement USING (" + KEY_POST_ID + "::integer);";

        db.execSQL(dropTablePosts); //IF EXISTS
        db.execSQL(CREATE_POSTS_TABLE);
        //Log.d("customDbAction", "created");
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORG);
//        db.execSQL(CREATE_ORG_TABLE);
//
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES + "; "); //IF EXISTS
//        db.execSQL(CREATE_RESOURCES_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS + "; "); //IF EXISTS
//        db.execSQL(CREATE_PROJECTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        // Create tables again
        onCreate(db);
    }


    //Check if table exists
    public boolean isTableExists(String tableName, boolean openDb) {

        boolean result = false;

        SQLiteDatabase mDatabase = this.getWritableDatabase();
        Cursor cursor = null;

        String query = "SELECT DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "' ";
        try {
            cursor = mDatabase.rawQuery(query, null);
            if(cursor != null) {

                //Log.d("isTableRecs", " " + cursor.getCount() + "");

                if(cursor.getCount() > 0) {
                    //Log.d("isTableExists", "Table " + tableName + " exists!");
                    cursor.close();
                    return true;
                } else {
                    //Log.d("isTableExists", "Creating table " + tableName + "because it doesn't exist!" );
                    onCreate(mDatabase);
                }
            }
        } catch (SQLiteException e){
            //Log.e(TAG, "error " + e.getMessage() );
            //Log.d("isTableExists", "Table " + tableName + " doesn't exist!");
            if (e.getMessage().contains("no such table")){

                // create table
                // re-run query, etc.

                //onCreate(mDatabase);
                //cursor.close();
            }

        }

        cursor.close();
        return false;
    }




    // Adding new account
    public void addAccountNew(JSONObject user_Data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        String field_Data = null;
        String user_status = null;



        //if (field_Data != null || user_status.equals("active")) {

            Timestamp action_time = new Timestamp(System.currentTimeMillis());
            String action_time_id = String.valueOf(action_time.getTime());
            String action_date = func_formatDateFromString(action_time_id);

            try {
                //Log.d("user_Data", String.valueOf(user_Data));
                //values.put(KEY_USER_ACC_ID, user_Data.getString("id") ); // Account ID
                values.put(KEY_USER_FNAME, user_Data.getString("fname") );
                values.put(KEY_USER_LNAME, user_Data.getString("lname") );
                values.put(KEY_USER_PHONE, user_Data.getString("number") );
                values.put(KEY_USER_EMAIL, user_Data.getString("email") );
                values.put(KEY_USER_PASS, user_Data.getString("password") );
                //values.put(KEY_USER_DP, user_Data.getString("profile_image") );
                values.put(KEY_USER_DATE, action_date );


                db.insert(TABLE_ACCOUNTS, null, values);
                db.close();

                //Log.d("accountNew", "Malizad");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        //}

    }
    public void addAccount(JSONObject user_Data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        String field_Data = null;
        String user_status = null;



        //if (field_Data != null || user_status.equals("active")) {

        Timestamp action_time = new Timestamp(System.currentTimeMillis());
        String action_time_id = String.valueOf(action_time.getTime());
        String action_date = func_formatDateFromString(action_time_id);

        try {
            //Log.d("organization_Data", String.valueOf(user_Data));
            //values.put(KEY_USER_ACC_ID, user_Data.getString("id") ); // Account ID
            values.put(KEY_ORG_NAME, user_Data.getString("oname") );
            values.put(KEY_ORG_EMAIL, user_Data.getString("email") );
            values.put(KEY_ORG_PASSWORD, user_Data.getString("password") );


            db.insert(TABLE_ORG, null, values);
            db.close();

            //Log.d("accountNew", "Malizad");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //}

    }


    public void addAccount(User account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ACC_ID, account.getUserId()); // Account ID
        values.put(KEY_USER_FNAME, account.getUser_fname());
        values.put(KEY_USER_LNAME, account.getUser_lname());
        values.put(KEY_USER_PHONE, account.getUser_number());
        values.put(KEY_USER_EMAIL, account.getUser_email());
        values.put(KEY_USER_PASS, account.getUser_password());
        values.put(KEY_USER_DATE, account.getUser_date());

        // Inserting Row
        db.insert(TABLE_ACCOUNTS, null, values);
        db.close(); // Closing database connection
    }



    public int getAccountExist( String user_email, String user_pass) {
        SQLiteDatabase db = this.getReadableDatabase();

        String md5_pass = user_pass; //md5(user_pass);

        String selectQuery  = "SELECT  * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_USER_EMAIL +" = '"+ user_email +"'  AND " + KEY_USER_PASS + " LIKE '%"+ md5_pass +"%' ";
        Cursor cursor       = db.rawQuery(selectQuery, null);
        int result          = cursor.getCount();
        cursor.close();


        return result;

    }


    public JSONObject getAccount( String user_email, String user_pass) {

        JSONObject currentRec = new JSONObject();

//        String md5_pass = md5(user_pass);
//        + md5_pass +"%'

        String selectQuery  = "SELECT  * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_USER_EMAIL +" = '"+ user_email +"'  AND " + KEY_USER_PASS + " LIKE '%";

        //String selectQuery  = "SELECT  * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_USER_EMAIL +" = '"+ user_email +"'  AND " + KEY_USER_PASS + " = '"+ user_pass +"' ";
        SQLiteDatabase db   = this.getReadableDatabase();
        Cursor cursor       = db.rawQuery(selectQuery, null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            try {

                currentRec.put(KEY_USER_ACC_ID, cursor.getString(cursor.getColumnIndex(KEY_USER_ACC_ID)));
                currentRec.put(KEY_USER_FNAME, cursor.getString(cursor.getColumnIndex(KEY_USER_FNAME)));
                currentRec.put(KEY_USER_LNAME, cursor.getString(cursor.getColumnIndex(KEY_USER_LNAME)));
                currentRec.put(KEY_USER_PHONE, cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE)));
                currentRec.put(KEY_USER_EMAIL, cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)));
                currentRec.put(KEY_USER_DP, cursor.getString(cursor.getColumnIndex(KEY_USER_DP)));

                //result = currentRec.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        return currentRec;

    }



    public JSONObject getAccountLogin( String user_email, String user_password) {


        ArrayList<HashMap<String, String>> userArray = new ArrayList<HashMap<String,String>>();
        //HashMap<String, String> currentRec = new HashMap<String, String>();

        JSONObject currentRec = new JSONObject();

        String result = "zii";

        String selectQuery  = "SELECT  * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_USER_EMAIL +" = '"+ user_email +"'  AND " + KEY_USER_PASS + " = '"+ user_password +"' ";
        SQLiteDatabase db   = this.getReadableDatabase();
        Cursor cursor       = db.rawQuery(selectQuery, null);

        //Log.d("login recs", String.valueOf(cursor.getCount()));

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            try {
                currentRec.put(KEY_USER_ACC_ID, cursor.getString(cursor.getColumnIndex(KEY_USER_ACC_ID)));
                currentRec.put(KEY_USER_FNAME, cursor.getString(cursor.getColumnIndex(KEY_USER_FNAME)));
                currentRec.put(KEY_USER_LNAME, cursor.getString(cursor.getColumnIndex(KEY_USER_LNAME)));
                currentRec.put(KEY_USER_PHONE, cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE)));
                currentRec.put(KEY_USER_EMAIL, cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)));


                //result = currentRec.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //userArray.add(currentRec);
        }
        cursor.close();

        return currentRec;
    }


    // Getting All Accounts
    public List<User> getAllAccounts() {
        List<User> accountList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User account = new User();
                //account.setID(Integer.parseInt(cursor.getString(0)));
                account.setUserId(Integer.parseInt(cursor.getString(0)));
                account.setUser_fname(cursor.getString(1));
                account.setUser_lname(cursor.getString(2));
                account.setUser_number(cursor.getString(3));
                account.setUser_email(cursor.getString(4));
                account.setUser_password(cursor.getString(5));
                // Adding account to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return account list
        return accountList;
    }




    /* @Updating account profile */
    public int updateAccount(User account, String account_type) {
        SQLiteDatabase db = this.getWritableDatabase();

        /*dbAccount accountData = new dbAccount("", "", "", txt_firstname, txt_lastname, mobileNo, user_email, "", "");*/

        String user_key_field = KEY_USER_ACC_ID;


        ContentValues values = new ContentValues();

        values.put(KEY_USER_FNAME, account.getUser_fname());
        values.put(KEY_USER_LNAME, account.getUser_lname());
        values.put(KEY_USER_PHONE, account.getUser_number());

        if(account_type == "fb"){
            //user_key_field = KEY_USER_EMAIL;
        }

        // updating row
        return db.update(TABLE_ACCOUNTS, values, user_key_field + " = ?",
                new String[] { String.valueOf(account.getUserId()) });

    }


    /* @Change account password */
    public boolean changeAccountPassword(String user_email, String user_pass, String new_pass) {

        SQLiteDatabase db   = this.getWritableDatabase();

        String selectQuery  = "SELECT  * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_USER_EMAIL +" = '"+ user_email +"'  AND " + KEY_USER_PASS + " = '"+ user_pass +"' ";
        Cursor cursor       = db.rawQuery(selectQuery, null);

        if (cursor.getCount() != 0) {
            cursor.close();

            ContentValues values = new ContentValues();
            values.put(KEY_USER_PASS, new_pass);

            db.update(TABLE_ACCOUNTS, values, KEY_USER_EMAIL + " = ?",
                    new String[] { user_email });

            db.close();

            return true;
        } else {
            return false;
        }


    }


    // Deleting single account
    public void deleteAccount(User account) { //
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNTS, KEY_USER_ACC_ID+ " = ?",
                new String[]{String.valueOf(account.getUserId())});

        db.close();
    }

    // Getting accounts Count
    public int getAccountsCount() {
        String countQuery = " SELECT  count(*) FROM " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int num_accounts= cursor.getInt(0);
        cursor.close();
        return num_accounts;
    }




    /**
     * **************************************************************************
     *  USER POSTS TABLE
     ****************************************************************************
     **/



    public JSONObject getResourceFiles( String post_id) {

        JSONArray recsAll = new JSONArray();
        JSONObject recsCurrent = null;


        String selectQuery  = "SELECT  * FROM " + TABLE_RESOURCES+ " WHERE " + KEY_RES_ID +" = '"+ post_id +"' ";
        SQLiteDatabase db   = this.getReadableDatabase();
        Cursor cursor       = db.rawQuery(selectQuery, null);

        if (cursor.getCount() != 0) {

            if (cursor.moveToFirst()) {

                String value;
                String key;
                String[] propertyNames = cursor.getColumnNames();

                do {
                    recsCurrent = new JSONObject();
                    int cols = propertyNames.length;
                    for (int i = 0; i < cols; i++) {

                        //Log.d("getColumnNames ", String.valueOf(i) + " " + cursor.getColumnName(i));

                        try {
                            key = cursor.getColumnName(i);
                            value = cursor.getString(cursor.getColumnIndex(key));
                            //Log.d("getRecord ", "Key: "+ key + " Value: " + value);
                            recsCurrent.put(key, value);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    recsAll.put(recsCurrent);

                } while (cursor.moveToNext());
            }

            //Log.d("getColumnNames ", String.valueOf(propertyNames.length) + " " + cursor.getColumnName(0));
        }
        cursor.close();

        JSONObject recsFinal = new JSONObject();
        if(recsAll.length() != 0) {
            try {
                recsFinal.put("files", recsAll);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return recsFinal;

    }




    /**
     * **************************************************************************
     *  INSERT INTO BOOKINGS TABLE
     ****************************************************************************
     **/



    public String post_Edit(JSONObject post, String post_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Log.d("post_post", String.valueOf(post));

        Timestamp action_time = new Timestamp(System.currentTimeMillis());
        String action_time_id = String.valueOf(action_time.getTime());
        String action_date = func_formatDateFromString(action_time_id);

        String insert = post_id;
        String image_url = "";



        try {
            contentValues.put(KEY_POST_ACC_ID, post.getString("user_id"));
            contentValues.put(KEY_POST_DETAIL, post.getString("description"));
            contentValues.put(KEY_POST_PROJECT,post.getString("post_project"));
            contentValues.put(KEY_POST_TAG,post.getString("post_tag"));
            contentValues.put(KEY_POST_CATEGORY,post.getString("post_category"));
            contentValues.put(KEY_POST_LONGITUDE,post.getString("post_longitude"));
            contentValues.put(KEY_POST_LATITUDE,post.getString("post_latitude"));
            //contentValues.put(KEY_POST_AUDIO,post.getString("audioUrl"));

            db.update(TABLE_POSTS, contentValues, KEY_POST_ID + " = ? ", new String[] { post_id });

            db.close();

            insert = post_id;
            image_url = post.getString("image_url");

            if(image_url.length() > 4){

                JSONObject resData = new JSONObject();

                try {
                    resData.put("post_id", insert);
                    resData.put("image_url", image_url);
                    resData.put("description", "");

                    //addResource(resData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return insert;
    }





    public String post_Add(JSONObject post) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Log.d("post_post", String.valueOf(post));

        Timestamp action_time = new Timestamp(System.currentTimeMillis());
        String action_time_id = String.valueOf(action_time.getTime());
        String action_date = func_formatDateFromString(action_time_id);

        String insert = "0";
        String image_url = "";
        // TODO: PASS EXTRA FIELDS TO THE LINE BELOW
    //title,description,date,imageUrl,audioUrl,user_id

        try {
            contentValues.put(KEY_POST_ACC_ID,post.getString("user_id"));
            contentValues.put(KEY_POST_TITLE, "none");
            contentValues.put(KEY_POST_DETAIL, post.getString("description"));
            contentValues.put(KEY_POST_DATE,action_date);
            //contentValues.put(KEY_POST_IMAGE,post.getString("imageUrl"));
            contentValues.put(KEY_POST_PROJECT,post.getString("post_project"));
            contentValues.put(KEY_POST_TAG,post.getString("post_tag"));
            contentValues.put(KEY_POST_SESSION,post.getString("post_session"));
            contentValues.put(KEY_POST_CATEGORY,post.getString("post_category"));
            contentValues.put(KEY_POST_LONGITUDE,post.getString("post_longitude"));
            contentValues.put(KEY_POST_LATITUDE,post.getString("post_latitude"));
            //contentValues.put(KEY_POST_AUDIO,post.getString("audioUrl"));

            sqLiteDatabase.insert(TABLE_POSTS, null, contentValues);
            //Log.d("post_contentValues",contentValues.toString());

            insert = getLastInsertId("npd_posts", "post_Id");
            image_url = post.getString("image_url");

            if(image_url.length() > 4){

                JSONObject resData = new JSONObject();

                try {
                    resData.put("post_id", insert);
                    resData.put("image_url", image_url);
                    resData.put("description", "");

                    addResource(resData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            sqLiteDatabase.close();

            //String val = databaseHelper.getLastInsertId("npd_posts", "post_Id");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return insert;
    }




    public String getLastInsertId(String d_table, String d_column){

        SQLiteDatabase db   = this.getReadableDatabase();
        String selectQuery  = "SELECT  * FROM " + d_table + "  ";
        Cursor cursor       = db.rawQuery(selectQuery, null);

        String value = "";
        String key;

        if(cursor.moveToLast()){
            key = cursor.getColumnName(0);
            value = cursor.getString( cursor.getColumnIndex(key) );

        }

        db.close();

        return value;
    }



    public void addResource(JSONObject post) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Log.d("post_resource", String.valueOf(post));

        String insert = "0";

        // TODO: PASS EXTRA FIELDS TO THE LINE BELOW
        //title,description,date,imageUrl,audioUrl,user_id

        try {
            contentValues.put(KEY_RES_ID, post.getString("post_id"));
            contentValues.put(KEY_RES_IMAGE, post.getString("image_url"));
            contentValues.put(KEY_RES_AUDIO, post.getString("description"));

            sqLiteDatabase.insert(TABLE_RESOURCES, null, contentValues);
            //Log.d("resource_Values", contentValues.toString());

            //TODO: GET LastInsertID()
            //sqLiteDatabase.execSQL("SELECT "+ KEY_POST_ID +" from "+ TABLE_POSTS +" order by "+ KEY_POST_ID +" DESC limit 1 " );

            sqLiteDatabase.close();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*return insert;*/
    }


    public long addPosttoProject(JSONObject post) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Log.d("post_post", String.valueOf(post));

        long insert = 0;

        // TODO: PASS EXTRA FIELDS TO THE LINE BELOW
        //title,description,date,imageUrl,audioUrl,user_id

        try {
            //contentValues.put(KEY_POST_ACC_ID,post.getString("user_id"));
            contentValues.put(KEY_POST_TITLE, "none");
            contentValues.put(KEY_POST_DETAIL, post.getString("description"));
            contentValues.put(KEY_POST_DATE,post.getString("date"));
            //contentValues.put(KEY_POST_IMAGE,post.getString("imageUrl"));
            contentValues.put(KEY_POST_PROJECT,post.getString("post_project"));
            contentValues.put(KEY_POST_TAG,post.getString("post_tag"));
            contentValues.put(KEY_POST_CATEGORY,post.getString("post_category"));
            //contentValues.put(KEY_POST_AUDIO,post.getString("audioUrl"));

            sqLiteDatabase.insert(TABLE_POSTS, null, contentValues);
            //Log.d("contentValues",contentValues.toString());

            //TODO: GET LastInsertID()
            //sqLiteDatabase.execSQL("SELECT "+ KEY_POST_ID +" from "+ TABLE_POSTS +" order by "+ KEY_POST_ID +" DESC limit 1 " );


            sqLiteDatabase.close();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return insert;
    }




    // Getting All RECORDS FROM A TABLE

    public JSONObject getRecords( String tbl_name, String tbl_sort_column, String tbl_filter ) {

        JSONArray recsAll = new JSONArray();
        JSONObject recsCurrent = null;

        String tbl_crit = "";

        String selectQuery  = "SELECT  * FROM " + tbl_name + " " + tbl_crit + " ORDER BY " + tbl_sort_column + " DESC; ";

        if(tbl_name.equals("npd_posts")){
            /*tbl_crit = " WHERE "+KEY_POST_ACC_ID+" = '"+ tbl_filter +"' ";*/
            /* WHERE np.user_id = '"+ tbl_filter +"' */
            //
            }
        /*,  GROUP_CONCAT(nr.post_imageUrl, ';') as `post_images`*/
        selectQuery  = "select np.*, nr.*, np.post_Id from npd_posts np left join npd_resources nr on np.post_Id = nr.post_Id  WHERE np.user_id = '"+ tbl_filter +"'  GROUP BY np.post_Id ORDER BY np.post_Id DESC; ";
             //selectQuery  = "select np.*, nr.* from npd_posts np left join npd_resources nr on np.post_Id = nr.post_Id   ORDER BY np.post_Id DESC; ";



        SQLiteDatabase db   = this.getReadableDatabase();
        Cursor cursor       = db.rawQuery(selectQuery, null);

        if (cursor.getCount() != 0) {

            if (cursor.moveToFirst()) {

                String value;
                String key;
                String[] propertyNames = cursor.getColumnNames();

                do {
                    recsCurrent = new JSONObject();
                    int cols = propertyNames.length;
                    for (int i = 0; i < cols; i++) {

                        /*Log.d("getColumnNames ", String.valueOf(i) + " " + cursor.getColumnName(i));*/

                        try {
                            key = cursor.getColumnName(i);
                            value = cursor.getString(cursor.getColumnIndex(key));

                            if(value == null) { value = ""; }
                            /*Log.d("getRecord ", "Key: "+ key + " Value: " + value);*/
                            recsCurrent.put(key, value);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    recsAll.put(recsCurrent);

                } while (cursor.moveToNext());
            }

            //Log.d("getColumnNames ", String.valueOf(propertyNames.length) + " " + cursor.getColumnName(0));
        }
        cursor.close();

        JSONObject recsFinal = new JSONObject();
        if(recsAll.length() != 0) {
            try {
                recsFinal.put("records", recsAll);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return recsFinal;


    }
    public Post getRecordById(int id) {
        Post post= new Post();
//        getRecords(TABLE_POSTS)
        String query = "SELECT* FROM npd_posts WHERE post_Id=?";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst() == true) {
            post.setPost_Id(cursor.getInt(cursor.getColumnIndex("post_id")));
            post.setRecord_date(cursor.getString(cursor.getColumnIndex("record_date")));
            post.setPost_details(cursor.getString(cursor.getColumnIndex("post_details")));
        }
        sqLiteDatabase.close();
        return post;


    }
    public void deletePost(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String tableName = " npd_posts ";
        String whereClause = " post_id = ? ";
        String[] whereArgs = new String[]{String.valueOf(id)};
        sqLiteDatabase.delete(tableName, whereClause, whereArgs);

        sqLiteDatabase.close();

    }

    public int updateNote(Post post) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("post_title", post.getPost_title());
        contentValues.put("post_details", post.getPost_details());
        String tableName = "notes";
        String whereClause = "id=?";


        return sqLiteDatabase.update(tableName, contentValues, whereClause,
                new String[]{String.valueOf(post.getPost_Id())});
    }




}

