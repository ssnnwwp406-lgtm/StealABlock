package me.stealablock.rarity;

public enum Rarity {

    COMMON("Common", 5_000L),
    UNCOMMON("Uncommon", 10_000L),
    RARE("Rare", 20_000L),
    EPIC("Epic", 480_000L),
    LEGENDARY("Legendary", 900_000L),
    MYTHIC("Mythic", 1_800_000L),
    DIVINE("Divine", 5_400_000L),
    SECRET("Secret", 10_800_000L);

    private final String displayName;
    private final long interval;

    Rarity(String displayName, long interval) {
        this.displayName = displayName;
        this.interval = interval;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getInterval() {
        return interval;
    }
}
