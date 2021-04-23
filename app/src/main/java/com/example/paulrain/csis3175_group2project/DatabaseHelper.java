package com.example.paulrain.csis3175_group2project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    final static String DATABASE_NAME = "NKM_Grocery.db";
    final static int DATABASE_VERSION = 1;
    final static String TABLE1_NAME = "Customer_table";
    final static String T1COL1 = "Customer_ID";
    final static String T1COL2 = "User_Name";
    final static String T1COL3 = "Password";
    final static String T1COL4 = "Address";
    final static String T1COL5 = "Postal_Code";
    final static String T1COL6 = "First_Name";
    final static String T1COL7 = "Last_Name";
    final static String T1COL8 = "Phone_Number";
    final static String T1COL9 = "Discount_Amount";

    final static String TABLE2_NAME = "Store_table";
    final static String T2COL1 = "Store_ID";
    final static String T2COL2 = "Store_Name";
    final static String T2COL3 = "Postal_Code";
    final static String T2COL4 = "Phone_Number";
    final static String T2COL5 = "Address";
    final static String T2COL6 = "Email";

    final static String TABLE3_NAME = "Item_table";
    final static String T3COL1 = "Item_ID";
    final static String T3COL2 = "Item_Name";
    final static String T3COL3 = "Item_Description";
    final static String T3COL4 = "Item_Price";
    final static String T3COL5 = "Item_Unit";
    final static String T3COL6 = "Store_ID";

    final static String TABLE4_NAME = "Sale_table";
    final static String T4COL1 = "Item_ID";
    final static String T4COL2 = "Customer_ID";
    final static String T4COL3 = "Order_Date";
    final static String T4COL4 = "Amount";
    final static String T4COL5 = "Discount";
    final static String T4COL6 = "DeliveryFee";
    final static String T4COL7 = "Store_ID";

    final static String TABLE5_NAME = "Email";
    final static String T5COL1 = "Value";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_table1 = "CREATE TABLE " + TABLE1_NAME + "(" +
                T1COL1 + " INTEGER PRIMARY KEY, " +
                T1COL2 + " TEXT," + T1COL3 + " TEXT," +
                T1COL4 + " TEXT," + T1COL5 + " TEXT," +
                T1COL6 + " TEXT," + T1COL7 + " TEXT," +
                T1COL8 + " TEXT," + T1COL9 + " TEXT" +
                ")";
        String query_table2 = "CREATE TABLE " + TABLE2_NAME + "(" +
                T2COL1 + " INTEGER PRIMARY KEY, " +
                T2COL2 + " TEXT, " +
                T2COL3 + " TEXT, " +
                T2COL4 + " TEXT, " +
                T2COL5 + " TEXT, " +
                T2COL6 + " TEXT" +
                ") ";
        String query_table3 = "CREATE TABLE " + TABLE3_NAME + "(" +
                T3COL1 + " INTEGER, " +
                T3COL2 + " TEXT," +
                T3COL3 + " TEXT, "+
                T3COL4 + " TEXT, "+
                T3COL5 + " TEXT, "+
                T3COL6 + " INTEGER, "+
                "CONSTRAINT FK_StoreItem FOREIGN KEY( "+ T3COL6 + ") REFERENCES " + TABLE2_NAME  + "(" + T2COL1 + ")" +
                ") ";

        String query_table4 = "CREATE TABLE " + TABLE4_NAME + "(" +
                T4COL1 + " INTEGER, " +
                T4COL2 + " INTEGER, " +
                T4COL3 + " TEXT, " +
                T4COL4 + " INTEGER, " +
                T4COL5 + " INTEGER, " +
                T4COL6 + " INTEGER, " +
                T4COL7 + " INTEGER, " +
                "CONSTRAINT FK_ItemSale FOREIGN KEY( " + T4COL1 + ") REFERENCES " + TABLE3_NAME + "(" + T3COL1 + "), " +
                "CONSTRAINT FK_CustomerSale FOREIGN KEY( " + T4COL2 + ") REFERENCES " + TABLE1_NAME + "(" + T1COL1 + ")," +
                "CONSTRAINT FK_StoreSale FOREIGN KEY( " + T4COL7 + ") REFERENCES " + TABLE2_NAME + "(" + T2COL1 + ") " +
                ")";

        String query_table5 = "CREATE TABLE " + TABLE5_NAME + "(" +
                T5COL1 + " TEXT "+
                ")";

        db.execSQL(query_table1);
        db.execSQL(query_table2);
        db.execSQL(query_table3);
        db.execSQL(query_table4);
        db.execSQL(query_table5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE4_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE5_NAME);
        onCreate(db);
    }

    //Add new customer
    public boolean addCustomer (String User_Name, String Password,
                                String Address, String Postal_Code,
                                 String First_Name, String Last_Name,
                                 String Phone_Number, String Discount_Amount ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T1COL2,User_Name);
        values.put(T1COL3,Password);
        values.put(T1COL4,Address);
        values.put(T1COL5,Postal_Code);
        values.put(T1COL6,First_Name);
        values.put(T1COL7,Last_Name);
        values.put(T1COL8,Phone_Number);
        values.put(T1COL9,Discount_Amount);

        long r = sqLiteDatabase.insert(TABLE1_NAME, null, values);
        if (r>0){
            return true;
        }
        else {
            return  false;
        }
    }

    //Select the whole Customer Table
    public Cursor viewCustomerTable(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * from " + TABLE1_NAME;
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    //Select the selected user based on user name
    public Cursor viewSelectedUserName (String User_Name){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * from " + TABLE1_NAME +
                " WHERE " + T1COL2 + " = '" + User_Name + "'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    //Verify if the new user name is duplicated with existing user name or not
    public boolean authentication (String User_Name, String Password){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = viewSelectedUserName(User_Name);
        if (c.getCount() > 0){
            String query = "SELECT * from " + TABLE1_NAME +
                    " WHERE " + T1COL2 + " = '" + User_Name +
                    "' AND " +
                    T1COL3 + " = '" + Password + "'" ;
            Cursor d = sqLiteDatabase.rawQuery(query,null);
            if (d.getCount() > 0){
                return true;
            }
            return false;
        }
        return false;
    }

    //Delete customer when we need to modify customer info
    public boolean deleteCustomer (String User_Name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE1_NAME," User_Name = ? ", new String[]{User_Name});
        if (result > 0){
            return true;
        }
        else {
            return false;
        }
    }

    //Update customer info
    public boolean updateCustomer (int CusID, String UserName, String Password, String Address,
                                   String PostalCode, String FirstName, String LastName,
                                   String PhoneNumber, String Discount_Amount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T1COL3,Password);
        values.put(T1COL4,Address);
        values.put(T1COL5,PostalCode);
        values.put(T1COL6,FirstName);
        values.put(T1COL7,LastName);
        values.put(T1COL8,PhoneNumber);
        values.put(T1COL9,Discount_Amount);
        int result = sqLiteDatabase.update(TABLE1_NAME,values," Customer_ID = ? and User_Name = ? ",new String[]{Integer.toString(CusID),
        UserName});
        if (result > 0){
            return true;
        }
        else{
            return false;
        }
    }

    //Get how many discount times the customer have left base on customer ID
    public String getDiscountFromCusID (int CusID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT " + T1COL9 + " FROM " + TABLE1_NAME + " WHERE " + T1COL1 + " = " + CusID;
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if (c.getCount() > 0){
            while (c.moveToNext()){
                return c.getString(0);
            }
        }
        return "Not Found";

    }

    //Update discount time for customer base on customer ID
    public boolean updateCusDiscount (int Cus_ID,String updateDiscount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String Discount = getDiscountFromCusID(Cus_ID);
        int iDiscount = Integer.parseInt(Discount);
        int iUpdateDiscount = Integer.parseInt(updateDiscount);
        iDiscount = iDiscount + iUpdateDiscount;
        values.put(T1COL9,Integer.toString(iDiscount));
        int update = sqLiteDatabase.update(TABLE1_NAME,values," Customer_ID = ? ",
                new String[]{Integer.toString(Cus_ID)});
        if (update > 0){
            return  true;
        }
        else {
            return false;
        }
    }

    //Add new store
    public boolean addStore (String Store_Name, String Postal_Code, String Phone_Number, String Address,
                             String Email){
        ContentValues values = new ContentValues();
        values.put(T2COL2,Store_Name);
        values.put(T2COL3,Postal_Code);
        values.put(T2COL4,Phone_Number);
        values.put(T2COL5,Address);
        values.put(T2COL6,Email);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long insert = sqLiteDatabase.insert(TABLE2_NAME, null, values);
        if (insert > 0){
            return true;
        }
        else {
            return false;
        }
    }

    //Init the stores
    public void initStore(){
        Cursor c = getAllStores();
        if (c.getCount() > 0){
            return;
        }
        boolean V3Y2W1 = addStore("Real Canadian Superstore Lougheed", "V3Y2W1", "6044604319", "19800 Lougheed Hwy #201, Pitt Meadows, BC",
                "superstoreLougheed@gmail.com");
        boolean V3T2W1 = addStore("Surrey T&T", "V3T2W1", "6045803168", "3000 Central City, 10153 King George Blvd, Surrey, BC",
                "surreyTandT@gmail.com");
        boolean V3R1N3 = addStore("Walmart Surrey Guildford Supercentre", "V3R1N3", "6045811932", "1000 Guildford Town Centre, Surrey, BC",
                "walmartGuildFord@gmail.com");
        boolean V3M1C4 = addStore("Save-On-Foods New Westminster", "V3M1C4", "6045491225", "1025 Columbia St, New Westminster, BC",
                "saveonfoodNewWest@gmail.com");
        boolean V5V4G7 = addStore("Dean's No Frills", "V5V4G7", "18669876453", "4508 Fraser St, Vancouver, BC",
                "nofrillVan@gmail.com");
        boolean V6B1V4 = addStore("Costco Wholesale Vancouver", "V6B1V4", "6046225050", "605 Expo Blvd, Vancouver, BC",
                "costcoVan@gmail.com");
        boolean V5Z2E2 = addStore("Safeway King Edward", "V5Z2E2", "6047330073", "990 W King Edward Ave, Vancouver, BC",
                "safewayKing@gmail.com");
        boolean V5X0A7 = addStore("Canadian Tire - Vancouver, SW Marine", "V5X0A7", "6043361086", "8277 Ontario St, Vancouver, BC",
                "canatireMarine@gmail.com");
        boolean V6X1S3 = addStore("Dollarama Richmond", "V6X1S3", "6042331045", "9751 Bridgeport Rd, Richmond, BC",
                "dollaramaRichmond@gmail.com");
    }

    //Verify store base on its postal code
    public Cursor verifyStore(String Postal_Code){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * from " + TABLE2_NAME +
                " WHERE " + T2COL3 + " = '" + Postal_Code + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    //Get 1 or 2 stores georgraphical near the current store base on store ID
    public Cursor get2StoresNear (int StoreID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = getAllStores();
        int count = c.getCount();
        int iPrev = StoreID - 1;
        int iNext = StoreID + 1;
        String query = "";
        if (iPrev > 0 && iNext <= c.getCount() ){
            query = "SELECT * from " + TABLE2_NAME +
                " WHERE " + T2COL1 + " IN(" +  iPrev + "," + iNext   + ")";
        }
        else if (iPrev <= 0){
            query = "SELECT * from " + TABLE2_NAME +
                    " WHERE " + T2COL1 + " IN(" + iNext +")";
        }
        else  if (iNext > c.getCount()){
            query = "SELECT * from " + TABLE2_NAME +
                    " WHERE " + T2COL1 + " IN(" + iPrev + ")";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    //Query all stores
    public Cursor getAllStores (){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * from " + TABLE2_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        return cursor;
    }

    //Try to find store based on input address from user
    public int verifyStoreAddress (String Address){
        Cursor c = getAllStores();
        if (c.getCount() > 0){
            while (c.moveToNext()){
                boolean contains = c.getString(4).contains(Address);
                if (contains){
                    return c.getInt(0);
                }
            }
        }
        return 0;
    }

    //Get store from its ID
    public Cursor getStoreFromID (int ID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE2_NAME + " WHERE " + T2COL1 + " = " + ID;
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    //Add new item
    public boolean addItem(int Item_ID, String Item_Name, String Item_Description, String Item_Price , String Item_Unit, int Store_ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T3COL1,Item_ID);
        values.put(T3COL2,Item_Name);
        values.put(T3COL3,Item_Description);
        values.put(T3COL4,Item_Price);
        values.put(T3COL5,Item_Unit);
        values.put(T3COL6,Store_ID);
        long insert = sqLiteDatabase.insert(TABLE3_NAME, null, values);
        if (insert > 0){
            return true;
        }
        else{
            return false;
        }
    }

    //Init items for 9 stores, there are some common and specified items
    public void initItem(){
        Cursor c = getItemFromStore(1);
        if (c.getCount() > 0){
            return;
        }
        addItem(1,"Apple", "Apple Mexico, import from Cancun","$3","lbs",
                1);
        addItem(2,"Orange", "Orange Nagpur","$2","lbs",
                1);
        addItem(3,"Banana", "Banana Vietnam","$1","lbs",
                1);
        addItem(4,"Plum", "Plum US","$4","lbs",
                1);

        addItem(5,"Rambutan", "Rambutan Cambodia","$5","lbs",
                2);
        addItem(6,"Durian", "Durian Taiwan","$6","lbs",
                2);
        addItem(7,"Dragon fruit", "Dragon fruit Laos","$7","lbs",
                2);
        addItem(8,"Jackfruit", "Jackfruit Myanmar","$7","lbs",
                2);

        addItem(9,"Angus beef", "Angus beef Canadian","$9","lbs",
                3);
        addItem(10,"Lamb", "Lamb shoulder New Zealand","$10","lbs",
                3);
        addItem(11,"Chicken", "Whole Chicken Wings","$6","lbs",
                3);
        addItem(12,"Pork", "Pork Shoulder Boneless","$8","lbs",
                3);

        addItem(13,"Pens", "Zebra Z-Grip Flight Pens, pack 25","$8","pack",
                4);
        addItem(14,"Pencils", "Classmate HB Pencils Pack of 144","$60","pack",
                4);
        addItem(15,"Erasers", "Staedtler Mars Plastic Erasers Pack of 20","$8","pack",
                4);
        addItem(16,"Notebooks", "Blueline Notebook Pack of 5","$9","pack",
                4);

        addItem(17,"Glue gun", "Glue Gun Crafts","$3","item",
                5);
        addItem(18,"Elastics", "Elastics (Assorted colours)","$1","pack",
                5);
        addItem(19,"Stapler", "Stapler Set (Assorted colours)","$4","pack",
                5);
        addItem(20,"Clips", "Multi-Purpose Clips (Assorted colours)","$2","pack",
                5);

        //Duplicated items for Store 1
        addItem(5,"Rambutan", "Rambutan Cambodia","$5","lbs",
                1);
        addItem(6,"Durian", "Durian Taiwan","$6","lbs",
                1);
        addItem(7,"Dragon fruit", "Dragon fruit Laos","$7","lbs",
                1);
        addItem(8,"Jackfruit", "Jackfruit Myanmar","$7","lbs",
                1);

        addItem(9,"Angus beef", "Angus beef Canadian","$9","lbs",
                1);
        addItem(10,"Lamb", "Lamb shoulder New Zealand","$10","lbs",
                1);
        addItem(11,"Chicken", "Whole Chicken Wings","$6","lbs",
                1);
        addItem(12,"Pork", "Pork Shoulder Boneless","$8","lbs",
                1);

        //Duplicate items for Store 2
        addItem(13,"Pens", "Zebra Z-Grip Flight Pens, pack 25","$8","pack",
                2);
        addItem(14,"Pencils", "Classmate HB Pencils Pack of 144","$60","pack",
                2);
        addItem(15,"Erasers", "Staedtler Mars Plastic Erasers Pack of 20","$8","pack",
                2);
        addItem(16,"Notebooks", "Blueline Notebook Pack of 5","$9","pack",
                2);

        addItem(17,"Glue gun", "Glue Gun Crafts","$3","item",
                2);
        addItem(18,"Elastics", "Elastics (Assorted colours)","$1","pack",
                2);
        addItem(19,"Stapler", "Stapler Set (Assorted colours)","$4","pack",
                2);
        addItem(20,"Clips", "Multi-Purpose Clips (Assorted colours)","$2","pack",
                2);

        //Duplicate items for Store 3
        addItem(1,"Apple", "Apple Mexico, import from Cancun","$3","lbs",
                3);
        addItem(2,"Orange", "Orange Nagpur","$2","lbs",
                3);
        addItem(3,"Banana", "Banana Vietnam","$1","lbs",
                3);
        addItem(4,"Plum", "Plum US","$4","lbs",
                3);
        addItem(17,"Glue gun", "Glue Gun Crafts","$3","item",
                3);
        addItem(18,"Elastics", "Elastics (Assorted colours)","$1","pack",
                3);
        addItem(19,"Stapler", "Stapler Set (Assorted colours)","$4","pack",
                3);
        addItem(20,"Clips", "Multi-Purpose Clips (Assorted colours)","$2","pack",
                3);

        //Duplicate items for Store 4
        addItem(1,"Apple", "Apple Mexico, import from Cancun","$3","lbs",
                4);
        addItem(2,"Orange", "Orange Nagpur","$2","lbs",
                4);
        addItem(3,"Banana", "Banana Vietnam","$1","lbs",
                4);
        addItem(4,"Plum", "Plum US","$4","lbs",
                4);

        addItem(5,"Rambutan", "Rambutan Cambodia","$5","lbs",
                4);
        addItem(6,"Durian", "Durian Taiwan","$6","lbs",
                4);
        addItem(7,"Dragon fruit", "Dragon fruit Laos","$7","lbs",
                4);
        addItem(8,"Jackfruit", "Jackfruit Myanmar","$7","lbs",
                4);


        //Duplicate items for Store 5
        addItem(5,"Rambutan", "Rambutan Cambodia","$5","lbs",
                5);
        addItem(6,"Durian", "Durian Taiwan","$6","lbs",
                5);
        addItem(7,"Dragon fruit", "Dragon fruit Laos","$7","lbs",
                5);
        addItem(8,"Jackfruit", "Jackfruit Myanmar","$7","lbs",
                5);

        addItem(9,"Angus beef", "Angus beef Canadian","$9","lbs",
                5);
        addItem(10,"Lamb", "Lamb shoulder New Zealand","$10","lbs",
                5);
        addItem(11,"Chicken", "Whole Chicken Wings","$6","lbs",
                5);
        addItem(12,"Pork", "Pork Shoulder Boneless","$8","lbs",
                5);

        //Items for store 6
        addItem(15,"Erasers", "Staedtler Mars Plastic Erasers Pack of 20","$8","pack",
                6);
        addItem(16,"Notebooks", "Blueline Notebook Pack of 5","$9","pack",
                6);
        addItem(5,"Rambutan", "Rambutan Cambodia","$5","lbs",
                6);
        addItem(6,"Durian", "Durian Taiwan","$6","lbs",
                6);
        addItem(3,"Banana", "Banana Vietnam","$1","lbs",
                6);
        addItem(4,"Plum", "Plum US","$4","lbs",
                6);
        addItem(18,"Elastics", "Elastics (Assorted colours)","$1","pack",
                6);
        addItem(19,"Stapler", "Stapler Set (Assorted colours)","$4","pack",
                6);
        addItem(9,"Angus beef", "Angus beef Canadian","$9","lbs",
                6);
        addItem(12,"Pork", "Pork Shoulder Boneless","$8","lbs",
                6);

        //Item for store 7
        addItem(8,"Jackfruit", "Jackfruit Myanmar","$7","lbs",
                7);
        addItem(9,"Angus beef", "Angus beef Canadian","$9","lbs",
                7);
        addItem(1,"Apple", "Apple Mexico, import from Cancun","$3","lbs",
                7);
        addItem(2,"Orange", "Orange Nagpur","$2","lbs",
                7);
        addItem(11,"Chicken", "Whole Chicken Wings","$6","lbs",
                7);
        addItem(12,"Pork", "Pork Shoulder Boneless","$8","lbs",
                7);
        addItem(7,"Dragon fruit", "Dragon fruit Laos","$7","lbs",
                7);
        addItem(15,"Erasers", "Staedtler Mars Plastic Erasers Pack of 20","$8","pack",
                7);
        addItem(16,"Notebooks", "Blueline Notebook Pack of 5","$9","pack",
                7);

        //Item for store 8
        addItem(19,"Stapler", "Stapler Set (Assorted colours)","$4","pack",
                8);
        addItem(9,"Angus beef", "Angus beef Canadian","$9","lbs",
                8);
        addItem(2,"Orange", "Orange Nagpur","$2","lbs",
                8);
        addItem(3,"Banana", "Banana Vietnam","$1","lbs",
                8);
        addItem(7,"Dragon fruit", "Dragon fruit Laos","$7","lbs",
                8);
        addItem(8,"Jackfruit", "Jackfruit Myanmar","$7","lbs",
                8);
        addItem(11,"Chicken", "Whole Chicken Wings","$6","lbs",
                8);
        addItem(12,"Pork", "Pork Shoulder Boneless","$8","lbs",
                8);

        //Item for store 9
        addItem(8,"Jackfruit", "Jackfruit Myanmar","$7","lbs",
                9);
        addItem(15,"Erasers", "Staedtler Mars Plastic Erasers Pack of 20","$8","pack",
                9);
        addItem(6,"Durian", "Durian Taiwan","$6","lbs",
                9);
        addItem(7,"Dragon fruit", "Dragon fruit Laos","$7","lbs",
                9);
        addItem(18,"Elastics", "Elastics (Assorted colours)","$1","pack",
                9);
        addItem(19,"Stapler", "Stapler Set (Assorted colours)","$4","pack",
                9);
        addItem(9,"Angus beef", "Angus beef Canadian","$9","lbs",
                9);


    }

    //Get item from store based on store ID
    public Cursor getItemFromStore(int Store_ID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE3_NAME +
                " WHERE " + T3COL6 + " = " + Store_ID ;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        return  cursor;
    }

    //Get item id from item name, it is used for adding a new sale
    public int getItemIDFromItemName(String Item_Name){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT " + T3COL1 + " FROM " + TABLE3_NAME +
                " WHERE " + T3COL2 + " = " + "'" + Item_Name + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if (cursor.getCount() > 0){
            while(cursor.moveToNext()){
                return cursor.getInt(0);
            }
        }
        return 0;
    }

    //Get item from Item ID & Store ID
    public Cursor getItemFromIDAndStoreID (int Item_ID, int StoreID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE3_NAME +
                " WHERE " + T3COL1 + " = " + Item_ID +
                " AND " + T3COL6 + " = " + StoreID;
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return  c;
    }

    //Add new sale
    public boolean addSale(int Item_ID, int Customer_ID, String Order_Date, int Amount,
                           int Discount, int DeliFee, int Store_ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T4COL1,Item_ID);
        values.put(T4COL2,Customer_ID);
        values.put(T4COL3,Order_Date);
        values.put(T4COL4,Amount);
        values.put(T4COL5,Discount);
        values.put(T4COL6,DeliFee);
        values.put(T4COL7,Store_ID);
        long result = sqLiteDatabase.insert(TABLE4_NAME,null,values);
        if (result > 0){
            return  true;
        }
        else {
            return false;
        }
    }

    //Get sale form customer ID, order date and store ID, this is used for Order Complete
    public Cursor getSaleFromCusIDOrderDateStoreID (int Customer_ID, String Order_Date,
                                                    int Store_ID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT " + T4COL1 + "," + T4COL4 + "," + T4COL5 + "," + T4COL6  +
                " FROM " + TABLE4_NAME +
                " WHERE " + T4COL2 + " = " + Customer_ID +
                " AND " + T4COL3 + " ='" + Order_Date + "'" +
                " AND " + T4COL7 + " = " + Store_ID;
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    //Get sale from customer ID and order date, this is used for Show Order History Detail
    public Cursor getSaleFromCusIDOrderDate(int Cus_ID, String OrderDate){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT " + T4COL1 + "," + T4COL4 + "," + T4COL5 + "," + T4COL6 + "," + T4COL7 +
                        " FROM " + TABLE4_NAME +
                        " WHERE " + T4COL2 + " = " + Cus_ID +
                        " AND " + T4COL3 + " ='" + OrderDate + "'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    //Get every order date from customer ID
    public Cursor getOrderDateFromCusID (int Customer_ID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT DISTINCT " + T4COL3 +
                " FROM " + TABLE4_NAME +
                " WHERE " + T4COL2 + " =" + Customer_ID ;
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    //Update discount property for each item based on customer ID, order date and store ID
    public boolean updateSaleDiscount (int CusID, String orderDate,int Store_ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor c = getSaleFromCusIDOrderDateStoreID(CusID,orderDate,Store_ID);
        if (c.getCount() > 0){
            while (c.moveToNext()){
                String currentDiscount = c.getString(2);
                int iCurDis = Integer.parseInt(currentDiscount);
                iCurDis++;
                values.put(T4COL5,Integer.toString(iCurDis));
                int update = sqLiteDatabase.update(TABLE4_NAME, values,
                        " Customer_ID = ? AND Order_Date = ? AND Store_ID = ? ",
                        new String[]{Integer.toString(CusID), orderDate,Integer.toString(Store_ID)});
                if (update > 0){
                    return true;
                }
                else {
                    return false;
                }

            }
        }
        return false;
    }

    //Update delivery fee property for each item base on customer ID, order date and store id
    public boolean updateSaleDeliFee (int CusID, String orderDate,int Store_ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor c = getSaleFromCusIDOrderDateStoreID(CusID,orderDate,Store_ID);
        if (c.getCount() > 0){
            while (c.moveToNext()){
                int test = c.getInt(3);
                if (c.getInt(3) == 0){
                    values.put(T4COL6,"1");
                    int update = sqLiteDatabase.update(TABLE4_NAME,values,
                            " Customer_ID = ? AND Order_Date = ? AND Store_ID = ? " ,
                            new String[]{Integer.toString(CusID),orderDate,Integer.toString(Store_ID)});
                    if (update > 0){
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    //Add new email address to the email table, it is used for tracking if the email is invited or not
    public boolean addEmail (String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T5COL1,email);
        long result = sqLiteDatabase.insert(TABLE5_NAME,null,values);
        if (result > 0){
            return true;
        }
        else {
            return false;
        }
    }

    //Verify if the input email is in existing database or not
    public boolean verifyEmail (String email){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE5_NAME + " WHERE " + T5COL1 + " ='" + email + "'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if (c.getCount() > 0){
            return false;
        }
        else {
            return true;
        }
    }
}
