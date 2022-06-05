package main.fiap.telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import main.fiap.telegram.bot.command.PlayCommand;
import main.fiap.telegram.bot.command.SearchCommand;
import main.fiap.telegram.bot.processor.Processor;
import main.ygo.api.YgoProDeckApi;
import main.ygo.tcg.Card;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class YgoBotMain {

    private static final String PROPERTIES_NAME = "application.properties";
    private static final String TOKEN = "TELEGRAM_TOKEN";
    private static int messageOffset = 0;

    public static void main(String[] args) throws ConfigurationException, InterruptedException, IOException {
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.load(PROPERTIES_NAME);

        String token = config.getString(TOKEN);
        TelegramBot bot = new TelegramBot(token);
        Processor processor = new Processor();

        GetUpdatesResponse updatesResponse = null;
        SendResponse sendResponse = null;

        YgoProDeckApi.getAll();
        Random random = new Random();

        while (true) {
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(messageOffset));

            List<Update> updates = updatesResponse.updates();

            if (!updatesResponse.isOk()) {
                System.out.println("Error code: " + updatesResponse.errorCode());
                System.out.println("Error description: " + updatesResponse.description());
            } else {
                for (Update update : updates) {
                    messageOffset = update.updateId() + 1;
                    try {
                        String message = "";
                        ArrayList<String> responses = new ArrayList<>();
                        Long chatId = update.message().chat().id();
                        Long userId = update.message().from().id();
                        String userName = update.message().from().username();

                        message = update.message().text();
                        System.out.println("Receiving message: " + update.message().text());

                        Processor.Execution execution = processor.process(message);

                        if (execution.command instanceof SearchCommand command) {
                            ArrayList<Card> cards = YgoProDeckApi.search(command.getSearchParam(), execution.args);
                            responses.add(cards.get(random.nextInt(0, cards.size())).toString());
                        } else if (execution.command instanceof PlayCommand command) {
                            command.play(chatId, userId, userName, YgoProDeckApi.cards);
                        }

                        for (String response : responses)
                            sendResponse = bot.execute(new SendMessage(chatId, response));
                        if (!sendResponse.isOk()) {
                            System.out.println("Error sending message: " + sendResponse.toString());
                        }


                    } catch (Exception e) {
                        System.out.println("Get message exception " + e.getMessage());
                    }
                }
            }
            Thread.sleep(1000);
        }
    }
}
