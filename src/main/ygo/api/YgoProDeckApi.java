package main.ygo.api;

import main.ygo.tcg.Card;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class YgoProDeckApi {
    private static final String BASE_URL = "https://db.ygoprodeck.com/api/v7";
    private static final String CARD_INFO_URL = "/cardinfo.php";

    public static final ArrayList<Card> cards = new ArrayList<>();

    protected static Card getCard(JSONObject cardObject) throws JSONException {
        String imageUrl = cardObject.getJSONArray("card_images")
                .getJSONObject(0)
                .getString("image_url");

        String type = cardObject.getString("type");
        String name = cardObject.getString("name");
        String description = cardObject.getString("desc");

        Card card = new Card(imageUrl, name, description);
        if (type.contains("Monster")) {
            card.setMonster(cardObject.getInt("atk"), cardObject.getInt("def"));
        } else if (type.contains("Spell")) {
            card.setSpell();
        } else if (type.contains("Trap")) {
            card.setTrap();
        }
        return card;
    }

    protected static ArrayList<Card> getCards(String bodyString) {
        ArrayList<Card> cards = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONArray cardArray = null;
        try {
            jsonObject = new JSONObject(bodyString);
            cardArray = jsonObject.getJSONArray("data");
        } catch (JSONException e) {
            System.out.println("Error reading data from JSON");
            System.out.println(e.getMessage());
            return cards;
        }
        for (int index = 0; index < cardArray.length(); index++) {

            try {
                JSONObject cardObject = cardArray.getJSONObject(index);
                cards.add(YgoProDeckApi.getCard(cardObject));
            } catch (JSONException e) {
                System.out.println("Error reading data from JSON index: " + index);
                System.out.println(e.getMessage());
            }
        }

        return cards;
    }

    public static ArrayList<Card> getAll() throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder httpBuilder = HttpUrl.parse(BASE_URL + CARD_INFO_URL).newBuilder();

        Request request = new Request.Builder().url(httpBuilder.build()).build();
        Response response = client.newCall(request).execute();

        Headers headers = response.headers();
        ResponseBody body = response.body();
        String bodyString = body.string();

        YgoProDeckApi.cards.clear();
        YgoProDeckApi.cards.addAll(YgoProDeckApi.getCards(bodyString));

        return YgoProDeckApi.cards;
    }

    public static ArrayList<Card> search(String key, String value) throws IOException {
        if (key.isEmpty() || value.isEmpty()) {
            return cards;
        }

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder httpBuilder = HttpUrl.parse(BASE_URL + CARD_INFO_URL).newBuilder();
        Map<String, String> params = new HashMap<String, String>();
        params.put(key, value);

        for (Map.Entry<String, String> param : params.entrySet()) {
            httpBuilder.addQueryParameter(param.getKey(), param.getValue());
        }

        Request request = new Request.Builder().url(httpBuilder.build()).build();
        Response response = client.newCall(request).execute();

        Headers headers = response.headers();
        ResponseBody body = response.body();
        String bodyString = body.string();

        return YgoProDeckApi.getCards(bodyString);
    }
}
