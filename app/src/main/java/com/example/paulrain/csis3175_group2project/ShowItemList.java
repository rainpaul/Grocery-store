package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShowItemList extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_list);

        //For debug, we decided to keep it here since it is very convenient for debugging process
        // (since it will automatically fill in required info for other steps)
//        Customer.Customer_ID = 1;
//        Customer.User_Name = "1";
//        Customer.Store_ID = 4;
//        Customer.Store_Name = "Surrey T&T";
//        Customer.Order_Date = "20190316_111711";
//        startActivity(new Intent(ShowItemList.this, Delivery.class));

        //Set the store name base on info stored previously
        TextView txViStoreName = findViewById(R.id.txViStoreName);
        txViStoreName.append( " " + Customer.Store_Name);
        //Init the item list view
        initListView();
        Button btnProceed = findViewById(R.id.btnProceedItems);
        //Set actions chains for proceed button
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lv = findViewById(R.id.itemListView);
                EditText eTAmount;
                TextView txViItem_Name;
                int Item_ID;
                //Get current time as order date
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                Customer.Order_Date = timeStamp;
                int listLength = lv.getChildCount();
                //Verify if user has ordered any item
                boolean hasItem = false;
                for (int i = 0; i < listLength; i++)
                {
                    v = lv.getChildAt(i);
                    eTAmount = (EditText) v.findViewById(R.id.eTAmount);
                    txViItem_Name = (TextView) v.findViewById(R.id.itemName);
                    Item_ID = dbh.getItemIDFromItemName(txViItem_Name.getText().toString());

                    if (eTAmount.getText().toString().compareTo("") != 0){
                        hasItem = true;
                        int iAmount = Integer.parseInt(eTAmount.getText().toString());
                        dbh.addSale(Item_ID,Customer.Customer_ID,timeStamp,iAmount,
                                0,0,Customer.Store_ID);
                    }

                }
                dbh.close();
                if ( hasItem == false){
                    Toast.makeText(ShowItemList.this,"Please order at least 1 item to proceed!",Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(ShowItemList.this, Delivery.class));
            }
        });
    }

    private void initListView()
    {
        final String[] matrix  = { "_id","itemPic", "itemName","itemPrice","itemUnit","itemAmount" };
        final String[] columns = { "itemPic","itemName","itemPrice","itemUnit","itemAmount" };
        final int[]    layouts = { R.id.imgItem, R.id.itemName,R.id.itemPrice,R.id.itemUnit,R.id.eTAmount };
        MatrixCursor cursor = new MatrixCursor(matrix);
        dbh = new DatabaseHelper(this);
        int key = 0;
        Cursor c = dbh.getItemFromStore(Customer.Store_ID);
        if (c.getCount() > 0){
            while (c.moveToNext()){
                EditText eTAmount = findViewById(R.id.eTAmount);
                //Get the image id for each item base image name convention
                int imagID = getResources().getIdentifier(c.getString(1).replaceAll("\\s","").toLowerCase(),"drawable", this.getPackageName());
                cursor.addRow(new Object[]{key++,imagID, c.getString(1),
                        c.getString(3),c.getString(4),""});
            }
        }
        dbh.close();
        SimpleCursorAdapter data =
                new SimpleCursorAdapter(this,
                        R.layout.listview_layout,
                        cursor,
                        columns,
                        layouts);
        ListView lv = findViewById(R.id.itemListView);
        lv.setAdapter( data );
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
