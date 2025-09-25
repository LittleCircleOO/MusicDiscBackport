package com.littlecircleoo.musicdiscbackport;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALLOW_TEARS_DISC_LOOT = BUILDER
            .comment("Determines whether Tears music disc can be dropped when a ghast dies from a player-deflected fireball")
            .define("AllowTearsDiscLoot", true);

    public static final ModConfigSpec.BooleanValue ALLOW_LAVA_CHICKEN_DISC_LOOT = BUILDER
            .comment("Determines whether Lava Chicken music disc can be dropped when a chicken jockey is killed by the player")
            .define("AllowLavaChickenDiscLoot", true);

    static final ModConfigSpec SPEC = BUILDER.build();

}
