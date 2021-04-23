package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ShowOrderHistoryDetail extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_history_detail);
        dbh = new DatabaseHelper(this);
        //Get items from customer ID and order date, then we can use it to specify item ID & Store ID
        Cursor c = dbh.getSaleFromCusIDOrderDate(Customer.Customer_ID,Customer.Order_Date_History);
        ListView lv = findViewById(R.id.liViOrderHisDetail);
        TextView tVOrdHisDet = findViewById(R.id.tvOrderHisDetail);
        String OrdDatHisDet = ShowOrderHistory.stringToDate(Customer.Order_Date_History);
        tVOrdHisDet.setText("Order detail on "  + OrdDatHisDet);
        int iTotal = 0;
        final String[] matrix  = { "_id", "itemName", "itemPrice","itemAmount","itemSubTotal" };
        final String[] columns = { "itemName","itemPrice","itemAmount","itemSubTotal" };
        final int[]    layouts = { R.id.tVItemName,R.id.tVItemPrice,R.id.tVItemAmount,R.id.tvItemSub };

        MatrixCursor cursor = new MatrixCursor(matrix);
        int key = 0;

        if (c.getCount() > 0){
            while(c.moveToNext()){
                int Item_ID = c.getInt(0);
                int Store_ID = c.getInt(4);
                //Get item info from item ID and store ID
                Cursor cItem = dbh.getItemFromIDAndStoreID(Item_ID,Store_ID);
                if (cItem.getCount() > 0){
                    while (cItem.moveToNext()){
                        try{
                            String Item_Price = cItem.getString(3);
                            Item_Price = Item_Price.substring(1,Item_Price.length());
                            int iItem_Price = Integer.parseInt(Item_Price);
                            int iAmount = c.getInt(1);
                            String sAmount = Integer.toString(iAmount);
                            int iSubTotal = iItem_Price * iAmount;
                            iTotal += iSubTotal;
                            cursor.addRow(new Object[]{key++,cItem.getString(1),cItem.getString(3),
                                    c.getInt(1),"$" +iSubTotal});
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
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
        //Query sale info base on cus ID and Order Date, it is used to determine if that sale has discount
        // and/or delivery fee
        Cursor cSale = dbh.getSaleFromCusIDOrderDate(Customer.Customer_ID, Customer.Order_Date_History);
        boolean isDiscounted = false;
        boolean isDeliFee = false;

        if (cSale.getCount() > 0){
            while (cSale.moveToNext()){
                if (cSale.getString(2).compareTo("1") == 0){
                    isDiscounted = true;
                    RadioButton rDisc = findViewById(R.id.rBDisc);
                    try {
                        rDisc.setChecked(true);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (cSale.getInt(3) == 1){
                    RadioButton rDeliFee = findViewById(R.id.rBDeli);
                    isDeliFee = true;
                    rDeliFee.setChecked(true);
                }
            }
        }
        Double dTotal = Double.valueOf(iTotal);
        if (isDiscounted == true){
            dTotal *= 0.9;
        }
        if (isDeliFee == true){
            dTotal += 20;
        }
        lv.setAdapter( data );
            TextView result = findViewById(R.id.tVTotal);
        result.setText("Total amount is: $" +Double.toString(dTotal));

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
