package doodieman.bomberman.ranking.enums;

import lombok.Getter;

public enum Rank {

    SILVER1("Sølv I", "§7"),
    SILVER2("Sølv II", "§7"),
    SILVER3("Sølv III", "§7"),
    SILVER4("Sølv IV", "§7"),
    GULD1("Guld I", "§e"),
    GULD2("Guld II", "§e"),
    GULD3("Guld III", "§e"),
    GULD4("Guld IV", "§e"),
    DIAMANT1("Diamant I", "§b"),
    DIAMANT2("Diamant II", "§b"),
    DIAMANT3("Diamant III", "§b"),
    DIAMANT4("Diamant IV", "§b"),
    RUBIN1("Rubin I", "§5"),
    RUBIN2("Rubin II", "§5"),
    RUBIN3("Rubin III", "§5"),
    RUBIN4("Rubin IV", "§5"),
    BOMBEMESTER("BombeMester", "§c");

    @Getter
    private final String name;
    @Getter
    private final String color;

    Rank(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getPrefix() {
        return color + name;
    }

    public static Rank getRank(double elo) {
        int i = 1;
        Rank selected = values()[0];
        for (Rank rank : values()) {
            double currentValue = getExpectedElo(i);
            if (elo >= currentValue)
                selected = rank;
            else
                break;
            i++;
        }
        return selected;
    }

    private static double getExpectedElo(int x) {
        int lowerBound = 1;
        int upperBound = values().length;
        double lowerBoundValue = 50.0;
        double upperBoundValue = EloModifiers.HIGH_ELO.getValue() - EloModifiers.LOW_ELO.getValue();
        double exponent = Math.log(upperBoundValue / lowerBoundValue) / (upperBound - lowerBound);
        return EloModifiers.LOW_ELO.getValue() + (lowerBoundValue * Math.pow(Math.E, exponent * (x - lowerBound)));
    }


}
