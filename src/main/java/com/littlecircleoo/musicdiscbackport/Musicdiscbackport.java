package com.littlecircleoo.musicdiscbackport;

import com.littlecircleoo.musicdiscbackport.items.DiscBackportItems;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import static com.littlecircleoo.musicdiscbackport.items.DiscBackportItems.ITEMS;
import static com.littlecircleoo.musicdiscbackport.jukebox.DiscBackportJukeboxSongs.JUKEBOX_SONGS;
import static com.littlecircleoo.musicdiscbackport.sound.DiscBackportSoundEvents.SOUND_EVENTS;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Musicdiscbackport.MODID)
public class Musicdiscbackport {

    public static final String MODID = "musicdiscbackport";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("musicdiscbackport", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.musicdiscbackport")).icon(() -> DiscBackportItems.MUSIC_DISC_LAVA_CHICKEN.get().getDefaultInstance()).displayItems((parameters, output) -> {
        output.accept(DiscBackportItems.MUSIC_DISC_TEARS.get());
        output.accept(DiscBackportItems.MUSIC_DISC_LAVA_CHICKEN.get());
    }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Musicdiscbackport(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so sound_events get registered
        SOUND_EVENTS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so jukebox_songs get registered
        JUKEBOX_SONGS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Musicdiscbackport) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        NeoForge.EVENT_BUS.addListener(this::lootTableSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[MusicDiscBackport] Loaded!");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) event.accept(DiscBackportItems.MUSIC_DISC_TEARS);
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) event.accept(DiscBackportItems.MUSIC_DISC_LAVA_CHICKEN);
    }

    private void lootTableSetup(final LootTableLoadEvent event) {
        ResourceKey<LootTable> ghastLoot = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/ghast"));
        ResourceKey<LootTable> zombieLoot = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/zombie"));

        boolean allow_tears_disc_loot = Config.ALLOW_TEARS_DISC_LOOT.get();
        boolean allow_lava_chicken_disc_loot = Config.ALLOW_LAVA_CHICKEN_DISC_LOOT.get();

        if (allow_tears_disc_loot && event.getKey() == ghastLoot) {
            LOGGER.info("[MusicDiscBackport] Modifying ghast loot table for tears");
            LootPool.Builder pool =
                    LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(DiscBackportItems.MUSIC_DISC_TEARS))
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                            .when(DamageSourceCondition.hasDamageSource(
                                    DamageSourcePredicate.Builder.damageType()
                                            .tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
                                            .direct(EntityPredicate.Builder.entity().of(EntityType.FIREBALL))))
                            .when(LootItemKilledByPlayerCondition.killedByPlayer());
            event.getTable().addPool(pool.build());
        }
        if (allow_lava_chicken_disc_loot && event.getKey() == zombieLoot) {
            LOGGER.info("[MusicDiscBackport] Modifying zombie loot table for lava chicken");
            LootPool.Builder pool =
                    LootPool.lootPool()
                            .add(LootItem.lootTableItem(DiscBackportItems.MUSIC_DISC_LAVA_CHICKEN))
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            .when(LootItemEntityPropertyCondition.hasProperties(
                                    LootContext.EntityTarget.THIS,
                                    EntityPredicate.Builder.entity()
                                            .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(true))
                                            .vehicle(EntityPredicate.Builder.entity().of(EntityType.CHICKEN))));
            event.getTable().addPool(pool.build());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[MusicDiscBackport] Loading configuration");
        LOGGER.info("[MusicDiscBackport] Tears >> {}", Config.ALLOW_TEARS_DISC_LOOT.get() ? "ENABLED" : "DISABLED");
        LOGGER.info("[MusicDiscBackport] LavaChicken >> {}", Config.ALLOW_TEARS_DISC_LOOT.get() ? "ENABLED" : "DISABLED");
    }
}
