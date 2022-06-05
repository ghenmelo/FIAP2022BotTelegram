package main.ygo.tcg;

public class Card {
    public enum Type {
        MONSTER,
        SPELL,
        TRAP
    }
    public Type type;
    public int attack;
    public int defense;
    public String imageUrl;
    public String name;
    public String description;

    public Card(String imageUrl, String name, String description) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
    }

    public void setMonster(int attack, int defense) {
        this.type = Type.MONSTER;
        this.attack = attack;
        this.defense = defense;
    }

    public void setSpell() {
        this.type = Type.SPELL;
    }

    public void setTrap() {
        this.type = Type.TRAP;
    }
}
