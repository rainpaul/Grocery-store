package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class InviteFriends extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        dbh = new DatabaseHelper(this);
        Button btnInvite = findViewById(R.id.btnInviteFriends);
        //Set actions for invite new email button
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eTEmail = findViewById(R.id.eTInviteEmail);
                String sEmail = eTEmail.getText().toString();
                if (sEmail.compareTo("") == 0){
                    Toast.makeText(InviteFriends.this,"Please fill in required field!",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    //Verify the new email
                    boolean isEmailValid = isValidEmailAddress(sEmail);
                    if (isEmailValid){
                        boolean bIsQualified = dbh.verifyEmail(sEmail);
                        if (bIsQualified){
                            boolean addEmail = dbh.addEmail(sEmail);
                            if (addEmail){
                                dbh.updateCusDiscount(Customer.Customer_ID,"1");
                                Toast.makeText(InviteFriends.this,"Add email successfully!",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(InviteFriends.this,"Add email unsuccessfully!",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(InviteFriends.this,"This email is already invited, please try another one!",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(InviteFriends.this,"Email format is not valid, please double check!",Toast.LENGTH_LONG).show();
                    }
                }
            dbh.close();
            }

        });
    }

    //Verify email format
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    //4 menu buttons
    public void editAcc(View view){
        startActivity(new Intent(this,EditAccountActivity.class));
    }

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
