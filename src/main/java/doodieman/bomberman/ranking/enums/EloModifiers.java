package doodieman.bomberman.ranking.enums;

import lombok.Getter;

public enum EloModifiers {

    LOW_ELO(1000),
    HIGH_ELO(10000),

    KILL_VALUE(15),
    EXPECTED_KILLS_LOW(0.01),
    EXPECTED_KILLS_HIGH(10),

    WIN_VALUE(75),
    EXPECTED_WINS_LOW(0.01),
    EXPECTED_WINS_HIGH(1);

    @Getter
    private final double value;

    EloModifiers(double value) {
        this.value = value;
    }

    public static double getExpectedKills(double elo) {
        double x1 = LOW_ELO.value;
        double y1 = EXPECTED_KILLS_LOW.value;
        return y1 + ((elo - x1) / (HIGH_ELO.value - x1)) * (EXPECTED_KILLS_HIGH.value - y1);
    }

    public static double getExpectedWins(double elo) {
        double x1 = LOW_ELO.value;
        double y1 = EXPECTED_WINS_LOW.value;
        return y1 + ((elo - x1) / (HIGH_ELO.value - x1)) * (EXPECTED_WINS_HIGH.value - y1);
    }

}
