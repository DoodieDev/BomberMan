package doodieman.bomberman.playerdata;

import lombok.Getter;

public enum PlayerStat {

    WINS("wins", 0, "§d", "vundet spil", true),
    GAMES_PLAYED("gamesplayed", 0, "§5", "spillede spil", true),
    KILLS("kills", 0, "§a", "drab", true),
    DEATHS("deaths", 0, "§c", "døde", true),
    SUICIDES("suicides", 0, "§c", "selvmord", true),
    TNTS_PLACED("tntsplaced", 0, "§e", "tnt placeret", true),
    BLOCKS_BROKEN("blocksbroken", 0, "§3", "blocks smadret", true),
    BALANCE("balance", 0, "§6", "⛃", false), //TODO
    ELO("elo", 1000, "§f", "elo", false);

    @Getter
    private final String id;
    @Getter
    private final double defaultValue;
    @Getter
    private final String colorCode;
    @Getter
    private final String fancyText;
    @Getter
    private final boolean show;

    PlayerStat(String id, double defaultValue, String colorCode, String fancyText, boolean show) {
        this.id = id;
        this.defaultValue = defaultValue;
        this.colorCode = colorCode;
        this.fancyText = fancyText;
        this.show = show;
    }

}
