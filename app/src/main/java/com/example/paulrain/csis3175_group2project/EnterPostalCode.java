package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterPostalCode extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_postal_code);

        //For debug, we decided to keep it here since it is very convenient for debugging process
        // (since it will automatically fill in required info for other steps)
//        Customer.Customer_ID = 1;
//        Customer.User_Name = "1";
//        Customer.Store_ID = 1;
//        Customer.Store_Name = "Surrey T&T";
//        startActivity(new Intent(EnterPostalCode.this, ShowItemList.class));

        //Reset some customer info before making a new order
        Customer.resetNewOrder();
        Button btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eTPostal_Code = findViewById(R.id.eTPostalCode);
                String sPostal_Code = eTPostal_Code.getText().toString();
                if (sPostal_Code.compareTo("") == 0){
                    Toast.makeText(EnterPostalCode.this,"Please fill in required field!",Toast.LENGTH_LONG).show();
                    return;
                }
                dbh = new DatabaseHelper(EnterPostalCode.this);
                dbh.initItem();
                Cursor c = dbh.verifyStore(sPostal_Code);
                //First, try to find the store based on exact postal code
                if (c.getCount() > 0){
                    while (c.moveToNext()){
                        Customer.Store_ID = Integer.parseInt(c.getString(0));
                        Customer.Store_Name = c.getString(1);
                    }
                    dbh.close();
                    startActivity(new Intent(EnterPostalCode.this, ChooseNearStores.class));
                }
                //Second, try to find the store base on user input address
                else if (c.getCount() <= 0) {
                    int storeID = dbh.verifyStoreAddress(sPostal_Code);
                    if (storeID > 0){
                        Customer.Store_ID = storeID;
                        try{
                            Cursor cStore = dbh.getStoreFromID(Customer.Store_ID);
                            if (cStore.getCount() > 0){
                                while (c.moveToNext()){
                                    Customer.Store_Name = cStore.getString(1);
                                }
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        dbh.close();
                        startActivity(new Intent(EnterPostalCode.this,ChooseNearStores.class));
                    }
                    else{
                        dbh.close();
                        Toast.makeText(EnterPostalCode.this, "This postal code does not exist in our system", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }
        });
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
