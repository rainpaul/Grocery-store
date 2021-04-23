package com.example.paulrain.csis3175_group2project;

//Class to keep track of current customer info
public class Customer {
    static String User_Name = "";
    static int Customer_ID = 0;
    static int Store_ID = 0;
    static String Store_Name = "";
    static String Store_Address = "";
    static String Order_Date = "";
    static String Order_Date_History = "";
    static int Use_Discount = 0;
    static String Delivery_Method = "";
    static String Payment_Method = "";

    //Reset for main activity
    public static void reset(){
        User_Name = "";
        Store_ID = 0;
        Store_Name = "";
        Store_Address = "";
        Customer_ID = 0;
        Order_Date = "";
        Order_Date_History = "";
        Use_Discount = 0;
        Delivery_Method = "";
        Payment_Method = "";
    }
    //Reset for new order
    public static void resetNewOrder(){
        Store_ID = 0;
        Store_Name = "";
        Store_Address = "";
        Order_Date = "";
        Order_Date_History = "";
        Use_Discount = 0;
        Delivery_Method = "";
        Payment_Method = "";
    }

}
