package main.fiap.telegram.bot.enums;

public enum Searches {
    CARD("/card", "fname"),
    TYPE("/type", "race"),
    LEVEL("/level", "level"),
    ATTRIBUTE("/attribute", "attribute"),
    ARCHETYPE("/archetype", "archetype");

	private String command;
    private String searchParam;


    Searches(String cmd, String type) {
        this.command = cmd;
        this.searchParam = type;
    }

    public String getSearchParam() {
        return searchParam;
    }

    public String getCommand() {
        return command;
    }
}
