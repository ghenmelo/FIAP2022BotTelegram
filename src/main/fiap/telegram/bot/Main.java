package main.fiap.telegram.bot;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import main.fiap.telegram.bot.botProcessor.ProcessingMessage;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Main {

	private static final String PROPERTIES_NAME = "app.properties";
	private static final String TOKEN = "TELEGRAM_TOKEN";

	public static void main(String[] args) throws ConfigurationException {

		PropertiesConfiguration config = new PropertiesConfiguration();
		config.load(PROPERTIES_NAME);
		config.getString(TOKEN);

		TelegramBot bot = new TelegramBot(config.getString(TOKEN));

		// Objeto responsavel por receber as mensagens.
		GetUpdatesResponse updatesResponse;

		// Objeto responsavel por gerenciar o envio de respostas.
		SendResponse sendResponse;

		// Objeto responsavel por gerenciar o envio de acoes do chat.
		BaseResponse baseResponse;

		// Controle de off-set, isto e, a partir deste ID sera lido as mensagens
		// pendentes na fila.
		int m = 0;

		// Loop infinito pode ser alterado por algum timer de intervalo curto.
		while (true) {
			// Executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial).
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// Lista de mensagens.
			List<Update> updates = updatesResponse.updates();

			// Analise de cada acao da mensagem.
			for (Update update : updates) {

				// Atualizacao do off-set.
				m = update.updateId() + 1;

				System.out.println("Recebendo mensagem: " + update.message().text());

				String responseMessage = "";

				if (update.message().text() != null ) {
					responseMessage = ProcessingMessage.processing(update.message().text());
				} else if (update.message().newChatMembers() != null) {
					responseMessage = ProcessingMessage.processingNewMembers(update.message());
				}

				if (!responseMessage.isEmpty()) {
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
	}
}
