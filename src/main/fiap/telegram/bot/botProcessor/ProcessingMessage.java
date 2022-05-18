package main.fiap.telegram.bot.botProcessor;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import main.fiap.telegram.bot.utils.FormatUtils;

import java.util.Arrays;

public class ProcessingMessage {

    private static final String START_BOT_LISTEM = "/ifoodBot ";

    public static String processing(String mensagem) {
        if (mensagem.startsWith(START_BOT_LISTEM)) {
            String subsStringMessage =
                    mensagem.substring(START_BOT_LISTEM.length());

            if (subsStringMessage.toLowerCase().contains("comida")) {
                return "Ta com fome? Vem de IiiiiFoood";
            } else {
                return "Ainda não entendo essa frase";
            }
        }
        return "";
    }

    public static String processingNewMembers(Message mensagem) {
        String name = Arrays.stream(mensagem.newChatMembers()).findFirst().map(User::firstName).get();
        name = FormatUtils.removeAccents(name).toLowerCase();

        if (name.contains("guilherme")) {
            return "Olá Guilherme seu Gênio";
        } else if (name.contains("gabriel")) {
            return "Olá Gabriel, vai um drink? Melhor não";
        } else if (name.contains("patricia")) {
            return "Olá Tami, Itadakimasu";
        } else if (name.contains("matheus")) {
            return "Olá Matheus, vava hoje a noite?";
        } else if (name.contains("wellington")) {
            return "Olá Welligton, obrigado por me apoiar pra ser em java";
        } else if (name.contains("gustavo")) {
            return "Olá Gustavo, seu traidor, traíra!";
        } else {
            return "Bem vindo, invasor";
        }
    }
}
