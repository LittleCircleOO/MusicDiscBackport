package com.littlecircleoo.musicdiscbackport.jukebox;

import com.littlecircleoo.musicdiscbackport.sound.DiscBackportSoundEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.littlecircleoo.musicdiscbackport.Musicdiscbackport.MODID;

public class DiscBackportJukeboxSongs {
    public static final DeferredRegister<JukeboxSong> JUKEBOX_SONGS = DeferredRegister.create(Registries.JUKEBOX_SONG, MODID);

    public static final DeferredHolder<JukeboxSong, JukeboxSong> TEARS = JUKEBOX_SONGS.register("tears", () -> new JukeboxSong(DiscBackportSoundEvents.MUSIC_DISC_TEARS, Component.translatable("jukebox_song.musicdiscbackport.tears"), 175, 10));
    public static final DeferredHolder<JukeboxSong, JukeboxSong> LAVA_CHICKEN = JUKEBOX_SONGS.register("lava_chicken", () -> new JukeboxSong(DiscBackportSoundEvents.MUSIC_DISC_LAVA_CHICKEN, Component.translatable("jukebox_song.musicdiscbackport.lava_chicken"), 134, 9));

    //public static final ResourceKey<JukeboxSong> TEARS = registerKey("tears");
    //public static final ResourceKey<JukeboxSong> LAVA_CHICKEN = registerKey("lava_chicken");

    //private static ResourceKey<JukeboxSong> registerKey(String name) {
    //    return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(MODID, name));
    //}
}
