package doodieman.bomberman.ranking.enums;

import lombok.Getter;

public enum Rank {

    SILVER1("Sølv I", "§7", 1000),
    SILVER2("Sølv II", "§7", 1050),
    SILVER3("Sølv III", "§7", 1100),
    SILVER4("Sølv IV", "§7", 1150),
    GULD1("Guld I", "§e", 1250),
    GULD2("Guld II", "§e", 1350),
    GULD3("Guld III", "§e", 1450),
    GULD4("Guld IV", "§e", 1550),
    DIAMANT1("Diamant I", "§b", 1950),
    DIAMANT2("Diamant II", "§b", 2300),
    DIAMANT3("Diamant III", "§b", 2650),
    DIAMANT4("Diamant IV", "§b", 3000),
    RUBIN1("Rubin I", "§5", 4500),
    RUBIN2("Rubin II", "§5", 5000),
    RUBIN3("Rubin III", "§5", 5500),
    RUBIN4("Rubin IV", "§5", 6000),
    BOMBEMESTER("BombeMester", "§c", 8000);

    @Getter
    private final String name;
    @Getter
    private final String color;
    @Getter
    private final double elo;

    Rank(String name, String color, double elo) {
        this.name = name;
        this.color = color;
        this.elo = elo;
    }

    public String getPrefix() {
        return color + name;
    }

    public static Rank getRank(double elo) {
        Rank selected = values()[0];
        for (Rank rank : values()) {
            double currentValue = rank.getElo();
            if (elo >= currentValue)
                selected = rank;
            else
                break;
        }
        return selected;
    }

}
