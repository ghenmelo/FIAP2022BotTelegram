package main.fiap.telegram.bot.command;

import main.fiap.telegram.bot.models.Player;
import main.ygo.tcg.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlayCommand extends Command {

    public static final PlayCommand PLAY = new PlayCommand("/play");
    private HashMap<Long, ArrayList<Player>> games;

    /**
     * Construtor que chama o início do duelo
     *
     * @param command commando em questão
     */
    public PlayCommand(String command) {
        super(command);
        this.games = new HashMap<>();
    }

    /**
     * Método que cria a mensagem do duelo, apresentando a carta de cada jogador.
     *
     * @param redraw flat pra se precisar jogar de novo a carta
     * @param playerName nome do jogador
     * @param card carta em questão
     * @return retorna a mensagem do duelo
     */
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

    /**
     * Método que cria o corpo do duelo que será enviado por chat
     *
     * @param one player 1
     * @param two player 2
     * @return retonna o texto do duelo
     */
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

    /**
     * Método que executa o comando de play, onde é feito um duelo fictício com
     * a máquina através de uma carta aleatória para cada. Ganha a carta normal
     * com maior poder.
     *
     * @param chat chat
     * @param playerId id do player
     * @param playerName nome do player
     * @param cardPool possíveis cartas
     * @return retorna o resultado do combate
     */
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
