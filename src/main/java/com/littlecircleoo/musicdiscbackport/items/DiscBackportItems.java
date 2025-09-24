package com.littlecircleoo.musicdiscbackport.items;

import net.minecraft.world.item.Item;
import com.littlecircleoo.musicdiscbackport.jukebox.DiscBackportJukeboxSongs;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.littlecircleoo.musicdiscbackport.Musicdiscbackport.MODID;

public class DiscBackportItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> MUSIC_DISC_TEARS = ITEMS.registerSimpleItem("music_disc_tears", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(DiscBackportJukeboxSongs.TEARS.getKey()));
    public static final DeferredItem<Item> MUSIC_DISC_LAVA_CHICKEN = ITEMS.registerSimpleItem("music_disc_lava_chicken", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(DiscBackportJukeboxSongs.LAVA_CHICKEN.getKey()));
}
