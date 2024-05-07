package com.example.homeservicesdemo.utils;


public class Singleton {
    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private String urlBase;
    private String urlLogin;
    private String urlVerifyOtp;
    private String urlHomePage;
    private String urlProfile;
    private String urlSearch;
    private String urlUpdate;
    private String urlMyOrders;
    private String urlBookingDetails;
    private String urlAddresslist;
    private String urlAddAddress;
    private String urlDeleteAddress;
    private String urlServices;
    private String urlSubCategory;
    private String urlRateCard;
    private String urlTimeSlot;

    private String urlConfirmOrder;
    private Singleton() {

        urlBase = "http://65.2.78.99/homeservices/";

        urlLogin = urlBase +"v1/register";

        urlVerifyOtp = urlBase +"v1/verifyOtp";

        urlHomePage = urlBase+"v1/homepage";

        urlProfile = urlBase+"v1/profile";

        urlServices = urlBase+"v1/services";

        urlSearch  = urlBase+"v1/search";

        urlSubCategory = urlBase+"v1/subcategory";

        urlUpdate = urlBase+"v1/updateprofile";

        urlMyOrders = urlBase +"v1/myorders";

        urlBookingDetails = urlBase +"v1/order_details";

        urlAddresslist = urlBase +"v1/addresslist";

        urlAddAddress = urlBase +"v1/addaddress";

        urlDeleteAddress = urlBase +"v1/deleteaddress";

        urlRateCard = urlBase + "v1/ratecard";

        urlTimeSlot =urlBase+"v1/timeslot";

        urlConfirmOrder = urlBase+"v1/confirmOrders";

    }

    public String getUrlConfirmOrder() {
        return urlConfirmOrder;
    }

    public String getUrlTimeSlot() {
        return urlTimeSlot;
    }

    public String getUrlRateCard() {
        return urlRateCard;
    }

    public String getUrlSubCategory() {
        return urlSubCategory;
    }

    public String getUrlServices() {
        return urlServices;
    }

    public String getUrlLogin() {
        return urlLogin;
    }

    public String getUrlVerifyOtp() {
        return urlVerifyOtp;
    }

    public String getUrlHomePage() {
        return urlHomePage;
    }
    public String getUrlProfile(){ return urlProfile;}
    public String getUrlSearch(){ return urlSearch;}
    public String getUrlUpdate(){ return urlUpdate;}

    public String getUrlMyOrders() {
        return urlMyOrders;
    }

    public String getUrlBookingDetails() {
        return urlBookingDetails;
    }

    public String getUrlAddresslist() {
        return urlAddresslist;
    }

    public String getUrlAddAddress() {
        return urlAddAddress;
    }

    public String getUrlDeleteAddress() {
        return urlDeleteAddress;
    }
}
