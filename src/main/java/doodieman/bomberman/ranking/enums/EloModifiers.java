package doodieman.bomberman.ranking.enums;

import lombok.Getter;

public enum EloModifiers {

    MAX_ELO_GAIN(50),
    SIDE_DIFFERENCE(10),
    ELO_LOSE_PERCENT(0.025),
    MIN_PLAYERS(10),
    KILLS_WORTH(10);

    @Getter
    private final double value;

    EloModifiers(double value) {
        this.value = value;
    }

    public static double getGain(double current_elo, int placement, int max_players, int kills) {

        //The maximum ELO the player can lose
        double max_lose_amount = current_elo * ELO_LOSE_PERCENT.value;
        //The placement percent. - The higher the better
        double placement_percent = 0.5 - (double) (placement - 1) / (max_players - 1);

        double result;
        if (placement_percent > 0 ) {
            result = ((placement_percent / 0.5) * MAX_ELO_GAIN.value) + SIDE_DIFFERENCE.value;
            result += kills * KILLS_WORTH.value;
        }

        else {
            result = ((placement_percent / 0.5) * max_lose_amount) - SIDE_DIFFERENCE.value;
            result += kills * KILLS_WORTH.value;
            result = result > 0 ? 0 : result;
        }

        double justifier = MIN_PLAYERS.value > max_players ? max_players / MIN_PLAYERS.value : 1;
        return result * justifier;
    }

}
