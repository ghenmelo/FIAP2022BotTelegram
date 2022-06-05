package main.fiap.telegram.bot.command;

import main.ygo.tcg.Card;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static final PlayCommand play = new PlayCommand("/play");
    private HashMap<Long, ArrayList<Player>> games;

    public PlayCommand(String command) {
        super(command);
    }

    private String buildDrawMessage(boolean redraw, String playerName, Card card) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    private String buildBattleMessage(Player one, Player two) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }


    public ArrayList<String> play(Long chat, Long playerId, String playerName, ArrayList<Card> cardPool) {
        ArrayList<String> results = new ArrayList<>();
        ArrayList<Player> players = games.get(chat);

        Random random = new Random();

        Card card = cardPool.get(random.nextInt(0, cardPool.size()));

        if(players == null)
            players = new ArrayList<>();

        if(players.size() > 0) {
            Player playerOne = players.get(0);
            if(playerOne.id.equals(playerId)) {
                playerOne.card = card;
                results.add(this.buildDrawMessage(true, playerName, card));
            } else {
                Player playerTwo = new Player(playerName, playerId, card);
                results.add(this.buildDrawMessage(false, playerName, card));
                results.add(this.buildBattleMessage(playerOne, playerTwo));
                if(playerOne.card.attack > playerTwo.card.attack) {
                    results.add(playerOne.name + " ganhou!");
                } else if(playerTwo.card.attack > playerOne.card.attack) {
                    results.add(playerTwo.name + " ganhou!");
                } else {
                    results.add("Empate!");
                }
            }
        } else {
            Player player = new Player(playerName, playerId, card);
            players.add(player);
        }
        return results;
    }
}
