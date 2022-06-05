package main.fiap.telegram.bot.command;

import java.util.ArrayList;

public class HelpCommand extends Command{
    public static final HelpCommand HELP = new HelpCommand("/help");

    protected HelpCommand(String command) {
        super(command);
    }

    /**
     * Método que cria o retorno do comando de /help.
     *
     * @return Arrays com ajuda
     */
    public ArrayList<String> help() {
        ArrayList<String> results = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("/card <card name> - Draw a random card with card name");
        sb.append(System.lineSeparator());
        sb.append("/type <type> - Draw a random card with type");
        sb.append(System.lineSeparator());
        sb.append("/level <level/rank/link> - Draw a random card with level/rank/link");
        sb.append(System.lineSeparator());
        sb.append("/attribute <attribute> - Draw a random card with attribute");
        sb.append(System.lineSeparator());
        sb.append("/archetype <archetype> - Draw a random card from a archetype");
        sb.append(System.lineSeparator());
        sb.append("/play - Draw or redraw a card, then compare with another player card if someone draw");
        sb.append(System.lineSeparator());
        sb.append("/help - Commands");
        results.add(sb.toString());
        return results;
    }
}
