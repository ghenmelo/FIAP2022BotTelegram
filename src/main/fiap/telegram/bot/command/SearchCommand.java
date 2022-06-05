package main.fiap.telegram.bot.command;

public class SearchCommand extends Command {
    public static final SearchCommand CARD = new SearchCommand("/card", "fname");
    public static final SearchCommand TYPE = new SearchCommand("/type", "race");
    public static final SearchCommand LEVEL = new SearchCommand("/level", "level");
    public static final SearchCommand ATTRIBUTE = new SearchCommand("/attribute", "attribute");
    public static final SearchCommand ARCHETYPE = new SearchCommand("/archetype", "archetype");

    public String getSearchParam() {
        return searchParam;
    }

    private final String searchParam;

    /**
     * M�todo que busca se existe o comando que ser� executado.
     *
     * @param command comando em quest�o para ser executa
     * @param param parametro de busca
     */
    public SearchCommand(String command, String param) {
        super(command);
        this.searchParam = param;
    }
}