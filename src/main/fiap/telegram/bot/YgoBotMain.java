package main.fiap.telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import main.fiap.telegram.bot.processor.ProcessingMessage;
import main.ygo.api.YgoProDeckApi;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.IOException;
import java.util.List;

public class YgoBotMain {

    private static final String PROPERTIES_NAME = "application.properties";
    private static final String TOKEN = "TELEGRAM_TOKEN";

    public static void main(String[] args) throws ConfigurationException, InterruptedException, IOException {
        PropertiesConfiguration config = new PropertiesConfiguration();
        config.load(PROPERTIES_NAME);

        String token = config.getString(TOKEN);

        TelegramBot bot = new TelegramBot(config.getString(TOKEN));

        // Objeto responsavel por receber as mensagens.
        GetUpdatesResponse updatesResponse;

        // Objeto responsavel por gerenciar o envio de respostas.
        SendResponse sendResponse;

        // Objeto responsavel por gerenciar o envio de acoes do chat.
        BaseResponse baseResponse;

        // Controle de off-set, isto e, a partir deste ID sera lido as mensagens
        // pendentes na fila.
        int messageOffset = 0;

        // Loop infinito pode ser alterado por algum timer de intervalo curto.
        while (true) {
            // Executa comando no Telegram para obter as mensagens pendentes a partir de um
            // off-set (limite inicial).
            updatesResponse = bot.execute(new GetUpdates().limit(100).offset(messageOffset));

            List<Update> updates = updatesResponse.updates(); // Lista de mensagens.

            if (!updatesResponse.isOk()) {
                System.out.println("Error code: " + updatesResponse.errorCode());
                System.out.println("Error description: " + updatesResponse.description());
            } else {
                // Analise de cada acao da mensagem.
                for (Update update : updates) {

                    messageOffset = update.updateId() + 1; // Atualizacao do off-set.

                    String message = "";
                    try {
                    	
                    	System.out.println("Recebendo mensagem: " + update.message().text());
                    	message = update.message().text();
                    }catch (NullPointerException e) {
                    	message = "";
					}
                    
                	String responseMessage = ProcessingMessage.processing(message);

                    
//                    String responseMessage = YgoProDeckApi.search(update.message().text(), "fname");

                    System.out.println(responseMessage);

                    if (!responseMessage.isEmpty() && update.message() != null) {
                        // Envio de "Escrevendo" antes de enviar a resposta.

                        baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
      
                        // Verificacao de acao de chat foi enviada com sucesso.
                        System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());

                        // Envio da mensagem de resposta.
                        sendResponse = bot.execute(new SendMessage(update.message().chat().id(), responseMessage));

                        // Verificacao de mensagem enviada com sucesso.
                        System.out.println("Mensagem Enviada? " + sendResponse.isOk());
                    }
                }
            }
            Thread.sleep(1000);
        }
    }
}
