package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//Class to create accounts
public class CreateAccountActivity extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        dbh = new DatabaseHelper(this);
        //Set action for Register
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eTUserName = findViewById(R.id.eTUserName);
                EditText eTPass = findViewById(R.id.eTPass);
                EditText eTRetyPass = findViewById(R.id.eTRePass);
                EditText eTAdd = findViewById(R.id.eTAdd);
                EditText eTPostal = findViewById(R.id.eTPostal);
                EditText eTFName = findViewById(R.id.eTFName);
                EditText eTLName = findViewById(R.id.eTLName);
                EditText eTPhone = findViewById(R.id.eTPhone);

                String sUserName = eTUserName.getText().toString();
                String sPass = eTPass.getText().toString();
                String sRetyPass = eTRetyPass.getText().toString();
                String sAdd = eTAdd.getText().toString();
                String sPostal = eTPostal.getText().toString();
                String sFName = eTFName.getText().toString();
                String sLName = eTLName.getText().toString();
                String sPhone = eTPhone.getText().toString();
                //Check input
                if (sUserName.compareTo("") != 0 && sPass.compareTo("") != 0 && sRetyPass.compareTo("") != 0 &&
                        sAdd.compareTo("") != 0 && sPostal.compareTo("") != 0 && sPhone.compareTo("") != 0 &&
                        sFName.compareTo("") != 0 && sLName.compareTo("") != 0){
                    if (sPass.compareTo(sRetyPass) != 0){
                        Toast.makeText(CreateAccountActivity.this,"Retype password does not match, please check!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    //Check if new user is duplicated with existing users
                    Cursor c = dbh.viewSelectedUserName(sUserName);
                    if(c.getCount() > 0){
                        Toast.makeText(CreateAccountActivity.this,"User name already exist, please choose another one!",Toast.LENGTH_LONG).show();
                    }
                    else{
                        final String secretKey = "secretkey";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            sPass = AES.encrypt(sPass, secretKey) ;
                        }
                        //Add new customer
                        boolean bResult = dbh.addCustomer(sUserName, sPass, sAdd, sPostal, sFName, sLName, sPhone,"0");
                        if (bResult == true){
                            dbh.close();
                            Toast.makeText(CreateAccountActivity.this,"Register completed, moving to Sign in page",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateAccountActivity.this,MainActivity.class));
                        }
                        else{
                            dbh.close();
                            Toast.makeText(CreateAccountActivity.this,"Failed to register!",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }
                else{
                    Toast.makeText(CreateAccountActivity.this,"Please fill in required fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
