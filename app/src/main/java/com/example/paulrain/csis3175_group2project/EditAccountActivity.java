package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditAccountActivity extends AppCompatActivity {

    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //For debug, we decided to keep it here since it is very convenient for debugging process
        // (since it will automatically fill in required info for other steps)
//        Customer.Customer_ID = 1;
//        Customer.User_Name = "1";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        final String sUser_Name = Customer.User_Name;
        TextView tVUser_Name = findViewById(R.id.txtUserName);
        final EditText eTPassword = findViewById(R.id.eTPass);
        final EditText eTRePassword = findViewById(R.id.eTRePass);
        final EditText eTAddress = findViewById(R.id.eTAdd);
        final EditText eTPostalCode = findViewById(R.id.eTPostal);
        final EditText eTFirstName = findViewById(R.id.eTFName);
        final EditText eTLastName = findViewById(R.id.eTLName);
        final EditText eTPhone = findViewById(R.id.eTPhone);
        final TextView tVDiscount = findViewById(R.id.tVDiscount);
        //Set user name on the title
        tVUser_Name.setText(sUser_Name);
        dbh = new DatabaseHelper(this);
        Cursor c = dbh.viewSelectedUserName(sUser_Name);
        if (c.getCount() > 0){
            while (c.moveToNext()){
                //Decrypt the current password to show for user
                final String secretKey = "secretkey";
                String gottenPass = c.getString(2);//here we get encrypted password from database to decrypt it
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    gottenPass = AES.decrypt(gottenPass, secretKey);
                }
                eTPassword.setText(gottenPass);
                eTRePassword.setText(gottenPass);
                eTAddress.setText(c.getString(3));
                eTPostalCode.setText(c.getString(4));
                tVDiscount.setText(c.getString(8));
                eTFirstName.setText(c.getString(5));
                eTLastName.setText(c.getString(6));
                eTPhone.setText(c.getString(7));
            }
            dbh.close();
        }
        else{
            dbh.close();
        }

        Button btnEdit = findViewById(R.id.btnEditAcc);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String User_Name = Customer.User_Name;
                String sPass = eTPassword.getText().toString();
                String sRePass = eTRePassword.getText().toString();
                String sAddress = eTAddress.getText().toString();
                String sPostalCode = eTPostalCode.getText().toString();
                String sFName = eTFirstName.getText().toString();
                String sLName = eTLastName.getText().toString();
                String sPhone = eTPhone.getText().toString();
                String sDiscount = tVDiscount.getText().toString();
                //Check input before apply the edit
                if (sPass.compareTo("") != 0 && sRePass.compareTo("") != 0 &&
                        sAddress.compareTo("") != 0 && sPostalCode.compareTo("") != 0 &&
                        sFName.compareTo("") != 0 && sLName.compareTo("") != 0 &&
                        sPhone.compareTo("") != 0) {
                    if (sPass.compareTo(sRePass) != 0){
                        Toast.makeText(EditAccountActivity.this, "Retype password does not match, please check|",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        dbh.deleteCustomer(User_Name);
                        //Encrypt the new password & store it in DB
                        final String secretKey = "secretkey";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            sPass = AES.encrypt(sPass, secretKey) ;
                        }
                        dbh.addCustomer(User_Name,sPass,sAddress,sPostalCode,sFName,sLName,sPhone,sDiscount);
                        Toast.makeText(EditAccountActivity.this, "Update account successfully!",
                                Toast.LENGTH_LONG).show();
                        dbh.close();
                    }
                }
                else{
                    Toast.makeText(EditAccountActivity.this,"Please fill in required fields!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //3 other menu buttons
    public void logOut(View view){
        Customer.reset();
        startActivity(new Intent(this,MainActivity.class));
    }
    public void inviteFriends(View view){
        startActivity(new Intent(this,InviteFriends.class));
    }

    public void showOrderHistory(View view){
        startActivity(new Intent(this,ShowOrderHistory.class));
    }
}
