package janeelsmur.taxi.Utilites;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CoordinatesAPIService {

    private static final String TAG = "CoordinatesAPIService";

//    public void asyncJson(String address){
//        address = address.replace(" ", "+");
//
//        String url = "http://maps.googleapis.com/maps/api/geocode/json?address="+ address +"&sensor=true";
//
//        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject json, AjaxStatus status) {
//
//                if(json != null){
//
//                    //here you work with the response json
//                    JSONArray results = json.getJSONArray("results");
//                    Toast.makeText(context, results.getJSONObject(1).getString("formatted_address"));
//
//                }else{
//                    //ajax error, show error code
//                    Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

    public static List<Address> getAddressListFromCoordinates(Context context, double latitude, double longtitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            return geocoder.getFromLocation(latitude, longtitude, 1);
        } catch (IOException e){
            Log.d(TAG, "getAddressFromCoordinates: " + Log.getStackTraceString(e));
            return null;
        }
    }

    public static String getStringFromAddressList(List<Address> list) {
        Address address = list.get(0);
        //Если аддрес содержит 4 строки-индекса (а значит нашелся полный аддрес), то возвращаем его
        if(validateAddress(address)) {
            Log.d(TAG, "getStringFromAddressList: " + address.getAddressLine(0));
            return address.getAddressLine(0);
        }
        else return null;
    }

    private static boolean validateAddress(Address address) {
        return address.getMaxAddressLineIndex() == 4;
    }
}
