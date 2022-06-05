package main.ygo.api;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class YgoProDeckApi {
    private static final String BASE_URL = "https://db.ygoprodeck.com/api/v7";
    private static final String CARD_INFO_URL = "/cardinfo.php";


    public static String search(String key, String value) throws IOException {
    	
    	if(value.isEmpty()) {
    		return "";
    	}
    	
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder httpBuilder = HttpUrl.parse(BASE_URL + CARD_INFO_URL).newBuilder();
        Map<String,String> params = new HashMap<String, String>();
        params.put(key, value);

        for(Map.Entry<String, String> param : params.entrySet()) {
            httpBuilder.addQueryParameter(param.getKey(),param.getValue());
        }
        Request request = new Request.Builder().url(httpBuilder.build()).build();
        Response response = client.newCall(request).execute();

        Headers headers = response.headers();
        ResponseBody body = response.body();
        String bodyString = body.string();

        JSONObject jsonObject = new JSONObject(bodyString);
        String name = "";
        try{
            name = jsonObject
                    .getJSONArray("data")
                    .getJSONObject(0)
                    .getJSONArray("card_images")
                    .getJSONObject(0)
                    .getString("image_url");
        }catch (JSONException e) {
        	System.out.println("Deu erro");
		}
        
        return name;
    }
}
