package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

public class OrderComplete extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        //For debug, we decided to keep it here since it is very convenient for debugging process
        // (since it will automatically fill in required info for other steps)
//        Customer.Customer_ID = 1;
//        Customer.User_Name = "1";
//        Customer.Store_ID = 1;
//        Customer.Store_Name = "Surrey T&T";
//        Customer.Order_Date = "20190322_154800";
//        startActivity(new Intent(this,ShowOrderHistory.class));
        Button btnNewOrder = findViewById(R.id.btnNewOrder);
        //New order with the same customer info
        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderComplete.this,EnterPostalCode.class));
            }
        });


        dbh = new DatabaseHelper(this);
        //We get customer ID, order date and store ID to query every items to show it on list view
        Cursor c = dbh.getSaleFromCusIDOrderDateStoreID(Customer.Customer_ID,Customer.Order_Date,
                Customer.Store_ID);
        ListView lv = findViewById(R.id.liViOrderHisDetail);
        int iTotal = 0;
        final String[] matrix  = { "_id", "itemName", "itemPrice","itemAmount","itemSubTotal" };
        final String[] columns = { "itemName","itemPrice","itemAmount","itemSubTotal" };
        final int[]    layouts = { R.id.tVItemName,R.id.tVItemPrice,R.id.tVItemAmount,R.id.tvItemSub };
        MatrixCursor cursor = new MatrixCursor(matrix);
        int key = 0;
        if (c.getCount() > 0){
            while(c.moveToNext()){
                //Use item id to get item info
                int Item_ID = c.getInt(0);
                Cursor cItem = dbh.getItemFromIDAndStoreID(Item_ID,Customer.Store_ID);
                if (cItem.getCount() > 0){
                    while (cItem.moveToNext()){
                        String Item_Price = cItem.getString(3);
                        Item_Price = Item_Price.substring(1,Item_Price.length());
                        int iItem_Price = Integer.parseInt(Item_Price);
                        int iAmount = c.getInt(1);
                        String sAmount = Integer.toString(iAmount);
                        int iSubTotal = iItem_Price * iAmount;
                        //Add total of each item to the total of order
                        iTotal += iSubTotal;
                        cursor.addRow(new Object[]{key++,cItem.getString(1),cItem.getString(3),
                               c.getInt(1),"$" +iSubTotal});
                    }
                }
                dbh.close();
            }
        }
        SimpleCursorAdapter data =
                new SimpleCursorAdapter(this,
                        R.layout.listview_orderhistory_detail,
                        cursor,
                        columns,
                        layouts);
        lv.setAdapter( data );
        TextView result = findViewById(R.id.tVTotal);
        //Make the text view scrollable without using Scrollable View
        result.setMovementMethod(new ScrollingMovementMethod());
        //Add neccessary info into result
        String sResult = "Store address: " + Customer.Store_Address + "\n" +
                "Delivery method: " + Customer.Delivery_Method + "\n" +
                "Payment method: " + Customer.Payment_Method + "\n" +
                "Total amount is: $" + Integer.toString(iTotal) + "\n";
        //Convert total to double to deal with discount & delivery fee (if avaiable)
        Double dTotal = Double.valueOf(iTotal);
        Double discount = Double.valueOf(0);
        //If customer use 1 of their discount, apply it
        if (Customer.Use_Discount == 1){
            discount = dTotal * 0.1;
            DecimalFormat df = new DecimalFormat(".##");
            sResult += "Discount is: $" + df.format(discount)+"\n" +
                       "Total after discount is: $" + Double.toString(dTotal - discount) + "\n";
        }
        //If customer use delivery service, apply it
        if (Customer.Delivery_Method.toLowerCase().contains("deliver")){
            sResult += "Delivery fee is: 20$" + "\n";
            sResult += "Total is: $" + (dTotal -discount + 20);
        }
        result.setText(sResult);

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
