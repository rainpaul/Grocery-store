package com.example.paulrain.csis3175_group2project;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.util.ArrayList;

//The class to choose 2 stores based on the user input
public class ChooseNearStores extends AppCompatActivity {
    DatabaseHelper dbh;
    //3 array list to keep track of stores info, it is used when list view item clicked
    ArrayList<Integer> StoreIDList = new ArrayList<>();
    ArrayList<String> StoreNameList = new ArrayList<>();
    ArrayList<String> StoreAddressList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_near_stores);
        dbh = new DatabaseHelper(this);
        //Build list view based on user input
        final Cursor cFirstMatch = dbh.getStoreFromID(Customer.Store_ID);
        final Cursor c = dbh.get2StoresNear(Customer.Store_ID);
        //Components for List View
        String[] matrix = {"_id","StoreInfo"};
        int key = 0;
        MatrixCursor cursor = new MatrixCursor(matrix);
        //Add the 1st match to list view
        if (cFirstMatch.getCount() > 0){
            while (cFirstMatch.moveToNext()){
                cursor.addRow(new Object[]{key++,"Store name: " + cFirstMatch.getString(1) + "\n"+
                        "Postal code: " + cFirstMatch.getString(2) + "\n"+
                        "Phone number: "+ cFirstMatch.getString(3)+ "\n"+
                        "Address: " +cFirstMatch.getString(4)+ "\n"+
                        "Email: " + cFirstMatch.getString(5)});
                //Add store info into the array list to reuse when list item is clicked
                StoreIDList.add(cFirstMatch.getInt(0));
                StoreNameList.add(cFirstMatch.getString(1));
                StoreAddressList.add(cFirstMatch.getString(4));
            }
        }

        ListView lv = findViewById(R.id.lvStoresNear);
        String[] columns = {"StoreInfo"};
        int[] layout = {R.id.tVStoreInfo};
        //Add other stores
        if (c.getCount() > 0){
            while (c.moveToNext()){
                cursor.addRow(new Object[]{key++,"Store name: " + c.getString(1) + "\n"+
                                                 "Postal code: " + c.getString(2) + "\n"+
                                                 "Phone number: "+ c.getString(3)+ "\n"+
                                                 "Address: " +c.getString(4)+ "\n"+
                                                 "Email: " + c.getString(5)});
                //Add store info into the array list to reuse when list item is clicked
                StoreIDList.add(c.getInt(0));
                StoreNameList.add(c.getString(1));
                StoreAddressList.add(c.getString(4));
            }
        }
        SimpleCursorAdapter data = new SimpleCursorAdapter(this,
                R.layout.listview_choosenearstore,
                cursor,
                columns,
                layout);
        lv.setAdapter(data);
        dbh.close();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    //Set store info for next steps
                    Customer.Store_ID = StoreIDList.get(position);
                    Customer.Store_Name = StoreNameList.get(position);
                    Customer.Store_Address = StoreAddressList.get(position);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                startActivity(new Intent(ChooseNearStores.this, ShowItemList.class));
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
