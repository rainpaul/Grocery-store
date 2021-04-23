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
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ShowOrderHistory extends AppCompatActivity {
    DatabaseHelper dbh;


    // Convert 20190321_171834 to 2019 Mar 21_17:18:34
    public static String stringToDate (String Input){

        String result = "";
        int month = Integer.parseInt(Input.substring(4,6));
        //Convert month number to string
        String sMonth = new DateFormatSymbols().getShortMonths()[month-1];
        result = Input.substring(0,4) + " " + sMonth + " " +
                Input.substring(6,8) + "_" + Input.substring(9,11) + ":" + Input.substring(11,13)+ ":" +
                Input.substring(13,15);
        return result;
    }

    // Convert 2019 Mar 21_17:18:34 to 20190321_171834
    public String dateToString (String Input){
        String result = "";
        String sMonth = Input.substring(5,8);
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(sMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Convert month format from string to number with 2 digits
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        result = Input.substring(0,4) + String.format("%02d",month + 1)  +
                Input.substring(9,11) + "_" + Input.substring(12,14) + Input.substring(15,17) + Input.substring(18,20) ;

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //For debug, we decided to keep it here since it is very convenient for debugging process
        // (since it will automatically fill in required info for other steps)
//        Customer.Customer_ID = 1;
//        Customer.User_Name = "1";
//        String test = dateToString("2019 Oct 21_17:05:33");
//        Customer.Store_ID = 1;
//        EditText Postal = findViewById(R.id.eTPostalCode);
//        Postal.setText("V3R3B2");
//        Customer.Store_Name = "Surrey T&T";
//        Customer.Order_Date = "20190321_171834";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_history);
        dbh = new DatabaseHelper(this);
        final ListView lv = findViewById(R.id.liViOrderHis);
        final String[] matrix  = { "_id", "orderDate" };
        final String[] columns = { "orderDate" };
        final int[]    layouts = { R.id.txViOrderDateItem };
        MatrixCursor cursor = new MatrixCursor(matrix);
        int key = 0;
        Cursor c = dbh.getOrderDateFromCusID(Customer.Customer_ID);
        if (c.getCount() > 0){
            while (c.moveToNext()){
                //Query order dates bases on customer ID
                cursor.addRow(new Object[]{key++,stringToDate(c.getString(0))});
            }
        }
        dbh.close();
        SimpleCursorAdapter data = new SimpleCursorAdapter(this,
                R.layout.listview_orderhistory,
                cursor,
                columns,
                layouts);
        lv.setAdapter(data);
        //When item is click, set the chosen date for next step
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txOrder_Date = view.findViewById(R.id.txViOrderDateItem);
                String sOrder_Date = txOrder_Date.getText().toString();
                Customer.Order_Date_History = dateToString(sOrder_Date);
                startActivity(new Intent(ShowOrderHistory.this, ShowOrderHistoryDetail.class));
            }
        });



    }

    // 4 menu buttons
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
