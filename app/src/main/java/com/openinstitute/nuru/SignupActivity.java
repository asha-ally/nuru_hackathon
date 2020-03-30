package com.openinstitute.nuru;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.openinstitute.nuru.Database.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    DatabaseHelper databaseHelper;
    EditText _fname;
    EditText _emailText;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;
    EditText _lname;
    EditText _number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Database Connection
        databaseHelper =new DatabaseHelper(this);
       // Log.d("blabla", "wabebee");
        //databaseHelper.customDbAction();


        _fname =findViewById(R.id.input_fname);
        _lname=findViewById(R.id.input_lname);
        _number=findViewById(R.id.input_number);
        _emailText =findViewById(R.id.input_email1);
        _passwordText =findViewById(R.id.input_password1);
        _signupButton =findViewById(R.id.btn_signup);
         _loginLink =findViewById(R.id.link_login);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
//        _linkorglogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             Intent intent= new Intent(getBaseContext(),OrganizationLoginActivity.class);
//             startActivity(intent);
//            }
//        });


    }

    //signup logic

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String fname = _fname.getText().toString();
        String lname = _lname.getText().toString();
        String number = _number.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();



        JSONObject user =new JSONObject();
        try {
            user.put("fname",fname);
            user.put("lname",lname);
            user.put("number",number);
            user.put("email",email);
            user.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("form_data", String.valueOf(user));

        databaseHelper.addAccountNew(user);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();
        display_MainActivity();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup Failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String fname = _fname.getText().toString();
        String lname =_lname.getText().toString();
        String number=_number.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (fname.isEmpty() || fname.length() < 3) {
            _fname.setError("at least 3 characters");
            valid = false;
        } else {
            _fname.setError(null);
        }
        if (lname.isEmpty() || lname.length() < 3) {
            _lname.setError("at least 3 characters");
            valid = false;
        } else {
            _lname.setError(null);
        }
        if (number.isEmpty() || number.length() < 10) {
            _number.setError("at least 10 characters");
            valid = false;
        } else {
            _number.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }




        return valid;
    }


    public  void display_MainActivity(){

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

    }
}