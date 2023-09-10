package doodieman.bomberman.playerdata;

import lombok.Getter;

public enum PlayerStat {

    BALANCE("balance", 0, "§6", "⛃"), //TODO
    WINS("wins", 0, "§d", "vundet spil"),
    GAMES_PLAYED("gamesplayed", 0, "§5", "spillede spil"),
    KILLS("kills", 0, "§a", "drab"),
    DEATHS("deaths", 0, "§c", "døde"),
    SUICIDES("suicides", 0, "§c", "selvmord"),
    TNTS_PLACED("tntsplaced", 0, "§e", "tnt placeret"),
    BLOCKS_BROKEN("blocksbroken", 0, "§3", "blocks smadret");

    @Getter
    private final String id;
    @Getter
    private final double defaultValue;
    @Getter
    private final String colorCode;
    @Getter
    private final String fancyText;

    PlayerStat(String id, double defaultValue, String colorCode, String fancyText) {
        this.id = id;
        this.defaultValue = defaultValue;
        this.colorCode = colorCode;
        this.fancyText = fancyText;
    }

}
