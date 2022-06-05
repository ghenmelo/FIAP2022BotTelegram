package main.fiap.telegram.bot.command;

import main.ygo.tcg.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlayCommand extends Command {

    private class Player {
        public String name;
        public Long id;
        public Card card;

        public Player(String name, Long id, Card card) {
            this.name = name;
            this.id = id;
            this.card = card;
        }
    }

    public static final PlayCommand PLAY = new PlayCommand("/play");
    private HashMap<Long, ArrayList<Player>> games;

    public PlayCommand(String command) {
        super(command);
        this.games = new HashMap<>();
    }

    private String buildDrawMessage(boolean redraw, String playerName, Card card) {
        StringBuilder sb = new StringBuilder();
        if (redraw)
            sb.append("*" + playerName + " redraw: *");
        else
            sb.append("*" + playerName + " draw: *");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(card.toString());
        return sb.toString();
    }

    private String buildBattleMessage(Player one, Player two) {
        StringBuilder sb = new StringBuilder();
        sb.append("*" + one.card.name + "* battles *" + two.card.name + "*");
        sb.append(System.lineSeparator());

        if (one.card.attack > two.card.attack) {
            sb.append("*" + one.card.name + "*'s attack " + one.card.attack + " is greater than *" + two.card.name + "*'s attack " + two.card.attack);
            sb.append(System.lineSeparator());
            sb.append("*" + one.name + "* wins!");
        } else if (two.card.attack > one.card.attack) {
            sb.append("*" + two.card.name + "*'s attack " + two.card.attack + " is greater than *" + one.card.name + "*'s attack  " + one.card.attack);
            sb.append(System.lineSeparator());
            sb.append("*" + two.name + "* wins!");
        } else {
            sb.append("*" + two.card.name + "*'s attack " + two.card.attack + " is equal to *" + one.card.name + "*'s attack  " + one.card.attack);
            sb.append(System.lineSeparator());
            sb.append("Draw!");
        }
        return sb.toString();
    }


    public ArrayList<String> play(Long chat, Long playerId, String playerName, List<Card> cardPool) {
        ArrayList<String> results = new ArrayList<>();
        ArrayList<Player> players = games.get(chat);

        Random random = new Random();

        Card card = cardPool.get(random.nextInt(cardPool.size()));

        if (players == null) {
            players = new ArrayList<>();
            games.put(chat, players);
        }

        if (players.size() > 0) {
            Player playerOne = players.get(0);
            if (playerOne.id.equals(playerId)) {
                playerOne.card = card;
                results.add(this.buildDrawMessage(true, playerName, card));
            } else {
                Player playerTwo = new Player(playerName, playerId, card);
                results.add(this.buildDrawMessage(false, playerName, card));
                results.add(this.buildBattleMessage(playerOne, playerTwo));
                players.clear();
            }
        } else {
            Player player = new Player(playerName, playerId, card);
            results.add(this.buildDrawMessage(false, playerName, card));
            players.add(player);
        }
        return results;
    }
}
