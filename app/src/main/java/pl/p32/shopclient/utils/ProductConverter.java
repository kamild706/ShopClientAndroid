package pl.p32.shopclient.utils;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import pl.p32.shopclient.model.Product;

public class ProductConverter {

    public static JsonObject toJson(Product product) throws JSONException {
        JsonObject gsonObject;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardinality", product.getCardinality());

        JsonParser parser = new JsonParser();
        gsonObject = (JsonObject) parser.parse((jsonObject.toString()));
        Log.d("MYAPP", gsonObject.toString());

        return gsonObject;
    }
}
