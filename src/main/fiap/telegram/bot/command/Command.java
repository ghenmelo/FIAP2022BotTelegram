package main.fiap.telegram.bot.command;

import main.fiap.telegram.bot.YgoBotMain;
import java.util.Locale;

public abstract class Command {
    private final String command;

    protected Command(String command) {
        this.command = command;
    }

    public String check(String message) {
        try {
            String[] splitMessages = message.split(" ", 2);
            String command, arguments = "";
            if(splitMessages.length == 0)
                return null;
            command = splitMessages[0];
            if(splitMessages.length > 1) {
                arguments = splitMessages[1];
            }
            if (command.toLowerCase(Locale.ROOT).equals(this.command + YgoBotMain.BOT_NAME))
                return arguments;
            if(command.toLowerCase(Locale.ROOT).equals(this.command))
                return arguments;
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
