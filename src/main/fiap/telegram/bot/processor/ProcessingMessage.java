package main.fiap.telegram.bot.processor;

import java.io.IOException;

import main.fiap.telegram.bot.enums.Searches;
import main.ygo.api.YgoProDeckApi;

public class ProcessingMessage {

	public static String processing(String mensagem) throws IOException {

			if(mensagem.isEmpty()) {
//				TODO
			}
		
			if (mensagem.startsWith(Searches.CARD.getCommand())) {
				String parameter = mensagem.substring(Searches.CARD.getCommand().length())
												.replaceAll("@ygotcgbot", "")
												.trim();
				
				return YgoProDeckApi.search(Searches.CARD.getSearchParam(), parameter);
				
			} else if (mensagem.startsWith(Searches.TYPE.getCommand())) {
				String parameter = mensagem.substring(Searches.TYPE.getCommand().length())
												.replaceAll("@ygotcgbot", "")
												.trim();
				
				return YgoProDeckApi.search(Searches.TYPE.getSearchParam(), parameter);
				
			} else if (mensagem.startsWith(Searches.ARCHETYPE.getCommand())) {
				String parameter = mensagem.substring(Searches.ARCHETYPE.getCommand().length())
												.replaceAll("@ygotcgbot", "")
												.trim();
				
				return YgoProDeckApi.search(Searches.ARCHETYPE.getSearchParam(), parameter);
				
			} else if (mensagem.startsWith(Searches.ATTRIBUTE.getCommand())) {
				String parameter = mensagem.substring(Searches.ATTRIBUTE.getCommand().length())
												.replaceAll("@ygotcgbot", "")
												.trim();
				
				return YgoProDeckApi.search(Searches.ATTRIBUTE.getSearchParam(), parameter);
				
			} else if (mensagem.startsWith(Searches.LEVEL.getCommand())) {
				String parameter = mensagem.substring(Searches.LEVEL.getCommand().length())
												.replaceAll("@ygotcgbot", "")
												.trim();
				return YgoProDeckApi.search(Searches.LEVEL.getSearchParam(), parameter);
				
			} else {
				return "Os unicos paramentros aceitos pelo bot são '/card, /type, /level, /attribute ou /archetype'";
			}

	}
}
