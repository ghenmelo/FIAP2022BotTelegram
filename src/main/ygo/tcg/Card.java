package main.ygo.tcg;

public class Card {
    public enum CardType {
        NORMAL_MONSTER,
        EFFECT_MONSTER,
        SYNCHRO_MONSTER,
        XYZ_MONSTER,
        PENDULUM_MONSTER,
        LINK_MONSTER,
        SPELL,
        TRAP
    }

    public CardType cardType;
    public int attack;
    public int defense;
    public String imageUrl;
    public String name;
    public String description;
    public String type;
    public String attribute;
    public String level;
    public int pendulumScale;
    public boolean tuner;
    public String archetype;


    public Card(String imageUrl, String name, String description) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.type = "";
        this.attribute = "";
        this.level = "";
        this.tuner = false;
    }

    public void setNormalMonster(int attack, int defense, String type, String attribute, int level) {
        this.cardType = CardType.NORMAL_MONSTER;
        this.attack = attack;
        this.defense = defense;
        this.type = type;
        this.attribute = attribute;
        this.level = "Level " + level;
    }

    public void setEffectMonster(int attack, int defense, String type, String attribute, int level) {
        this.setNormalMonster(attack, defense, type, attribute, level);
        this.cardType = CardType.EFFECT_MONSTER;
    }

    public void setSynchroMonster() {
        this.cardType = CardType.SYNCHRO_MONSTER;
    }

    public void setXyzMonster(int rank) {
        this.level = "Rank " + rank;
        this.cardType = CardType.XYZ_MONSTER;
    }

    public void setPendulumMonster(int pendulumScale) {
        this.level = this.level + " / Scale " + pendulumScale;
        this.pendulumScale = pendulumScale;
        this.cardType = CardType.PENDULUM_MONSTER;
    }

    public void setLinkMonster(int link) {
        this.level = "Link " + link;
        this.cardType = CardType.LINK_MONSTER;
    }

    public void setTuner(boolean tuner) {
        this.tuner = tuner;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public void setSpell(String type) {
        this.cardType = CardType.SPELL;
        this.type = type;
    }

    public void setTrap(String type) {
        this.cardType = CardType.TRAP;
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*" + this.name + "*");
        sb.append(System.lineSeparator());

        if (!this.type.isEmpty()) {
            sb.append(this.type);
            if(this.attribute.isEmpty())
                sb.append(System.lineSeparator());
        }
        if (!this.attribute.isEmpty()) {
            sb.append(" " + this.attribute);
            sb.append(System.lineSeparator());
        }
        if (!this.level.isEmpty()) {
            if(tuner)
                sb.append("Tuner ");
            sb.append(this.level);
            sb.append(System.lineSeparator());
        }
        switch (this.cardType) {
            case NORMAL_MONSTER:
            case EFFECT_MONSTER:
            case SYNCHRO_MONSTER:
            case XYZ_MONSTER:
            case PENDULUM_MONSTER:
                sb.append("Attack *" + this.attack + "* / Defense *" + this.defense + "*");
                sb.append(System.lineSeparator());
                break;
            case LINK_MONSTER:
                sb.append("Attack *" + this.attack + "*");
                sb.append(System.lineSeparator());
                break;
        }
        sb.append(System.lineSeparator());

        sb.append("_" + this.description + "_");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());

        sb.append(this.imageUrl);
        return sb.toString();
    }
}
