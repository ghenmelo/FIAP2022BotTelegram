package main.fiap.telegram.bot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.ygo.tcg.Card;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    public String name;
    public Long id;
    public Card card;
}
