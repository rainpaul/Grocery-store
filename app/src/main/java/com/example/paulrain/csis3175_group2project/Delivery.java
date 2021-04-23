package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Delivery extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        //For debug, we decided to keep it here since it is very convenient for debugging process
        // (since it will automatically fill in required info for other steps)
//        Customer.Customer_ID = 1;
//        Customer.User_Name = "1";
//        Customer.Store_ID = 1;
//        Customer.Store_Name = "Surrey T&T";
//        Customer.Order_Date = "20190322_154800";

        dbh = new DatabaseHelper(this);
        //Radio button for picking up option
        RadioButton raBuPickup = findViewById(R.id.raBuPickup);
        //Radio button for deliver to current address
        RadioButton raBuDeliCur = findViewById(R.id.raBuDeliCurAdd);
        final RadioButton raBuDeliNew = findViewById(R.id.raBuNewAdd);
        raBuPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raBuDeliNew.setChecked(true);
                Customer.Delivery_Method = "pick up";
                dbh.close();
                startActivity( new Intent(Delivery.this, PaymentMethod.class));
            }
        });
        raBuDeliCur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raBuDeliNew.setChecked(true);
                Cursor c = dbh.viewSelectedUserName(Customer.User_Name);
                if(c.getCount() > 0){
                    while (c.moveToNext()){
                        //Set the delivery method
                        Customer.Delivery_Method = "deliver to address: " + c.getString(3);
                        //If user choose to deliver the good to his/her address, then the delivery fee $20 is applied
                        dbh.updateSaleDeliFee(Customer.Customer_ID,Customer.Order_Date,
                                Customer.Store_ID);
                    }
                }
                dbh.close();
                startActivity( new Intent(Delivery.this, PaymentMethod.class));
            }
        });
        //Function to go to next step
        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eTAdd = findViewById(R.id.eTAdd);
                EditText eTPostal = findViewById(R.id.eTPostal);
                EditText eTPhone = findViewById(R.id.eTPhone);
                String sAdd = eTAdd.getText().toString();
                String sPostal = eTPostal.getText().toString();
                String sPhone =  eTPhone.getText().toString();
                if (sAdd.compareTo("") != 0 && sPostal.compareTo("") != 0 && sPhone.compareTo("") != 0){
                    Customer.Delivery_Method = "deliver to address: " + sAdd + ".\t Postal code: " + sPostal +
                            ".\tPhone: " + sPhone;
                    //If user choose to deliver the good to new address, then the delivery fee $20 is applied
                    dbh.updateSaleDeliFee(Customer.Customer_ID,Customer.Order_Date,
                            Customer.Store_ID);
                    dbh.close();
                    startActivity(new Intent(Delivery.this, PaymentMethod.class));
                }
                else{

                    Toast.makeText(Delivery.this,"Please fill in required field!",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    //4 default buttons
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
