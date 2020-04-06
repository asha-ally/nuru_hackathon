package com.openinstitute.nuru;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.openinstitute.nuru.Database.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.openinstitute.nuru.app.AppFunctions.func_showToast;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

     JSONObject userAccount;
     EditText _emailText ;
     EditText _passwordText ;
     Button _loginButton;
     Button _signupButton;
     TextView _signupLink ;
     Context context;
     DatabaseHelper databaseHelper;
     SharedPreferences sharedPref;

     String user_email;
     Boolean user_logged;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*if (sharedPref != null) {

        }*/

        _emailText = findViewById(R.id.input_email);
        _emailText.setText(""); /*rage@email.com*/
        _passwordText = findViewById(R.id.input_password);
        _passwordText.setText(""); /*123456*/
        _loginButton = findViewById(R.id.btn_login);
        _signupButton = findViewById(R.id.btn_signup);
        _signupLink = findViewById(R.id.link_signup);
        context = this;
        databaseHelper = new DatabaseHelper(this);

//       databaseHelper.customDbAction();

        //String val = databaseHelper.getLastInsertId("npd_posts", "post_Id");
        //Log.d("last_post",val);

        //sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        sharedPref = getSharedPreferences("loginPrefs", MODE_PRIVATE);



        user_email = sharedPref.getString("user_email", null);
        user_logged = sharedPref.getBoolean("user_logged", false);

        /*if (user_logged == true) {*/
        if(user_email != null){

            //Log.d("user_email", "iko " + user_email);
            //displayMainActivity();
            //finish();

            /*Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent1);*/
        } else {
            //Log.d("user_email_login", "hakuna ");
        }

        //else {

            _loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    validate();

                    /*String email = _emailText.getText().toString();
                    String password = _passwordText.getText().toString();


                    userAccount = databaseHelper.getAccountLogin(email, password);
                    int llen = userAccount.length();


                    if (llen > 0) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        try {
                            editor.putString("email", userAccount.getString("user_email"));
                            editor.putString("id", String.valueOf(userAccount.getInt("user_id")));
                            editor.putString("first_name", userAccount.getString("user_fname"));
                            editor.putString("last_name", userAccount.getString("user_lname"));

                            editor.putString("user_email", userAccount.getString("user_email"));
                            editor.putString("user_id", String.valueOf(userAccount.getInt("user_id")));
                            editor.putString("user_fname", userAccount.getString("user_fname"));
                            editor.putString("user_lname", userAccount.getString("user_lname"));

                            editor.commit();

                            String prefs = sharedPref.getAll().toString();
                            Log.d("data", prefs);
                            sharedPref.edit().putBoolean("logged", true).apply();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        login();
                    } else {
                        func_showToast(context, "Login Failed");
                    }*/



                }
            });



            _signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);

                }
            });



            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });

        //}
    }

    //login logic

    public void login() {
        //Log.d(TAG, "Login");

        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
        try {
            func_showToast(this,"Welcome Back! " + userAccount.getString("user_fname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent1);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    //Successful signup
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Intent intent =new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                this.finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void displayMainActivity() {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //i.putExtra("username", account_phone);
        startActivity(i);
        finish();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    /*public boolean validate() {*/
    public void validate() {
        boolean valid = false;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
            valid = true;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
            valid = true;
        }

        /*return valid;*/




        if(valid){
            userAccount = databaseHelper.getAccountLogin(email, password);
            int llen = userAccount.length();


            if (llen > 0) {
                SharedPreferences.Editor editor = sharedPref.edit();
                try {
                    editor.putString("email", userAccount.getString("user_email"));
                    editor.putString("id", String.valueOf(userAccount.getInt("user_id")));
                    editor.putString("first_name", userAccount.getString("user_fname"));
                    editor.putString("last_name", userAccount.getString("user_lname"));

                    editor.putString("user_email", userAccount.getString("user_email"));
                    editor.putString("user_id", String.valueOf(userAccount.getInt("user_id")));
                    editor.putString("user_fname", userAccount.getString("user_fname"));
                    editor.putString("user_lname", userAccount.getString("user_lname"));
                    editor.putBoolean("user_logged", true);

                    /*editor.commit();*/
                    editor.apply();

                    displayMainActivity();

                    /*String prefs = sharedPref.getAll().toString();*/
                    /*Log.d("data", prefs);
                    sharedPref.edit().putBoolean("logged", true).apply();*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*login();*/
            } else {
                func_showToast(context, "Login Failed");
            }
        }




    }

}
