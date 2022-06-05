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

    /**
     * Método responsável por criar o objeto CARD, validando e atribuindo
     * os dados com valores válidos.
     *
     * @param cardObject JSONObject da carta da requisicao
     * @return retorna uma carta
     * @throws IOException
     */
    protected static Card getCard(JSONObject cardObject) throws JSONException {
        String imageUrl = cardObject.getJSONArray("card_images")
                .getJSONObject(0)
                .getString("image_url");

        String type = cardObject.getString("type");
        String name = cardObject.getString("name");
        String description = cardObject.getString("desc");

        Card card = new Card(imageUrl, name, description);

        int atk = 0, def = 0, lvl = 0, scale = 0;
        String race = "", attr = "", archetype = "";
        boolean tuner = false;
        if(cardObject.has("atk"))
            atk = cardObject.getInt("atk");
        if(cardObject.has("def"))
            def = cardObject.getInt("def");
        if(cardObject.has("level"))
            lvl = cardObject.getInt("level");
        if(cardObject.has("linkval"))
            lvl = cardObject.getInt("linkval");
        if(cardObject.has("scale"))
            scale = cardObject.getInt("scale");
        if(cardObject.has("race"))
            race = cardObject.getString("race");
        if(cardObject.has("archetype"))
            archetype = cardObject.getString("archetype");
        if(cardObject.has("attribute"))
            attr = cardObject.getString("attribute");
        if (type.contains("Tuner"))
            tuner = true;


        if (type.contains("Monster")) {
            if(type.contains("Normal"))
                card.setNormalMonster(atk, def, race, attr, lvl);
            else
                card.setEffectMonster(atk, def, race, attr, lvl);

            if(type.contains("Synchro"))
                card.setSynchroMonster();

            if(type.contains("Xyz"))
                card.setXyzMonster(lvl);

            if(type.contains("Pendulum"))
                card.setPendulumMonster(scale);

            if(type.contains("Link"))
                card.setLinkMonster(lvl);

            if(!archetype.isEmpty())
                card.setArchetype(archetype);

            card.setTuner(tuner);
        } else if (type.contains("Spell")) {
            card.setSpell(race);
        } else if (type.contains("Trap")) {
            card.setTrap(race);
        }
        return card;
    }

    /**
     * Método responsável por validar e ler o body recebido da API do YuGiOh.
     *
     * @param bodyString Body da requisição
     * @return retorna uma lista de cartas
     * @throws IOException
     */
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

    /**
     * Método responsável por buscar TODAS as cartas da API do YuGiOh.
     *
     * @return retorna uma lista de cartas
     * @throws IOException
     */
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

    /**
     * Método responsável por buscar as cartas pelo filtro do comando na API do YuGiOh.
     *
     * @param key chave do comando
     * @param value valor do comando
     * @return retorna uma lista de cartas
     * @throws IOException
     */
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