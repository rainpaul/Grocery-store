package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class PaymentMethod extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        //For debug, we decided to keep it here since it is very convenient for debugging process
        // (since it will automatically fill in required info for other steps)
//        Customer.Customer_ID = 1;
//        Customer.User_Name = "1";
//        Customer.Store_ID = 1;
//        Customer.Store_Name = "Surrey T&T";
//        Customer.Order_Date = "20190322_154800";
//        Customer.Delivery_Method = "Pick up";
//        startActivity(new Intent(PaymentMethod.this, OrderComplete.class));

        dbh = new DatabaseHelper(this);
        final RadioButton rBUseDiscount = findViewById(R.id.rBUseDis);
        //If user has at least 1 discount time in his account, activate the radio button
        String disAmo = dbh.getDiscountFromCusID(Customer.Customer_ID);
        if (Integer.parseInt(disAmo) <= 0){
            rBUseDiscount.setEnabled(false);
        }
        //Proceed the payment if it is not cash
        Button btnProceed = findViewById(R.id.btnProceedPayment);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eTName = findViewById(R.id.eTName);
                EditText eTNum = findViewById(R.id.eTNum);
                EditText eTCVV = findViewById(R.id.eTCVV);
                EditText eTExpire = findViewById(R.id.eTExpiredDate);
                String sName = eTName.getText().toString();
                String sNum = eTNum.getText().toString();
                String sCVV = eTCVV.getText().toString();
                String sExpire = eTExpire.getText().toString();
                //Double check input
                if (sName.compareTo("") != 0 && sNum.compareTo("") != 0 && sCVV.compareTo("") != 0 && sExpire.compareTo("") != 0){
                    //If user uses discount, apply and update it
                    if (rBUseDiscount.isChecked()){
                        Customer.Use_Discount = 1;
                        dbh.updateCusDiscount(Customer.Customer_ID,"-1");
                        dbh.updateSaleDiscount(Customer.Customer_ID,Customer.Order_Date,Customer.Store_ID);
                    }
                    Customer.Payment_Method = "Credit card";
                    startActivity(new Intent(PaymentMethod.this, OrderComplete.class));
                }
                else{
                    Toast.makeText(PaymentMethod.this, "Please fill in required fields!", Toast.LENGTH_LONG).show();
                }
            }
        });
        //If user use cash, apply & update it
        RadioButton raBuCash = findViewById(R.id.raBuCash);
        raBuCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton raBuCre = findViewById(R.id.raBuCreCar);
                raBuCre.setChecked(true);
                Customer.Payment_Method = "Cash";
                //If user uses discount, apply and update it
                if (rBUseDiscount.isChecked()){
                    Customer.Use_Discount = 1;
                    dbh.updateCusDiscount(Customer.Customer_ID,"-1");
                    dbh.updateSaleDiscount(Customer.Customer_ID,Customer.Order_Date,Customer.Store_ID);
                }
                startActivity(new Intent(PaymentMethod.this, OrderComplete.class));
            }
        });
        dbh.close();

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
