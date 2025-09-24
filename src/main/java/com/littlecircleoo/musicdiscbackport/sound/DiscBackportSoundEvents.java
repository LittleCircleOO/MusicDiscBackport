package com.littlecircleoo.musicdiscbackport.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.littlecircleoo.musicdiscbackport.Musicdiscbackport.MODID;

public class DiscBackportSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_TEARS = SOUND_EVENTS.register("music_disc.tears", SoundEvent::createVariableRangeEvent);
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_LAVA_CHICKEN = SOUND_EVENTS.register("music_disc.lava_chicken", SoundEvent::createVariableRangeEvent);

}
