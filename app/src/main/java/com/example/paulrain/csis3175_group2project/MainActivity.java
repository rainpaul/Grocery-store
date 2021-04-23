package com.example.paulrain.csis3175_group2project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //For debug, we decided to keep it here since it is very convenient for debugging process
        // (since it will automatically fill in required info for other steps)
//        Customer.Customer_ID = 1;
//        Customer.User_Name = "1";
//        startActivity(new Intent(MainActivity.this,EnterPostalCode.class));

        //Reset new user when Log out
        Customer.reset();
        dbh = new DatabaseHelper(this);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        //Actions for Sign in button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eTAcc = findViewById(R.id.txtAcc);
                final String sAcc = eTAcc.getText().toString();
                EditText eTPass = findViewById(R.id.txtPassword);
                String sPass = eTPass.getText().toString();
                if ( sAcc.compareTo("") != 0 && sPass.compareTo("") != 0){
                    //encrypt the provided password to compare with the password in DB
                    final String secretKey = "secretkey";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sPass = AES.encrypt(sPass, secretKey) ;
                    }
                    boolean c  = dbh.authentication(sAcc,sPass);
                    if (c == true){
                        Customer.User_Name = sAcc;
                        Cursor cursor = dbh.viewSelectedUserName(sAcc);
                        if (cursor.getCount() > 0){
                            while (cursor.moveToNext()){
                                Customer.Customer_ID = cursor.getInt(0);
                            }
                        }
                        //Init store for next step
                        dbh.initStore();
                        dbh.close();
                        startActivity(new Intent(MainActivity.this,EnterPostalCode.class));
                    }
                    else{
                        dbh.close();
                        Toast.makeText(MainActivity.this,"User name and/or password does not exist, please try again!",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Please fill in required fields!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Create new account
    public void createAcc(View view) {
        RadioButton radSignIn = findViewById(R.id.radLogin);
        radSignIn.setChecked(true);
        startActivity( new Intent( MainActivity.this,CreateAccountActivity.class));
    }

    public void editAccount(View view) {
        startActivity(new Intent(MainActivity.this,EditAccountActivity.class));
    }
}
