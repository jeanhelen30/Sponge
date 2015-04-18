/*
 * This file is part of SpongeForge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.registry;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBanner.EnumBannerPattern;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.GameData;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameDictionary;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.attribute.Attribute;
import org.spongepowered.api.attribute.AttributeBuilder;
import org.spongepowered.api.attribute.AttributeCalculator;
import org.spongepowered.api.attribute.AttributeModifierBuilder;
import org.spongepowered.api.attribute.Operation;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tile.Banner;
import org.spongepowered.api.block.tile.CommandBlock;
import org.spongepowered.api.block.tile.Comparator;
import org.spongepowered.api.block.tile.DaylightDetector;
import org.spongepowered.api.block.tile.EnchantmentTable;
import org.spongepowered.api.block.tile.EndPortal;
import org.spongepowered.api.block.tile.EnderChest;
import org.spongepowered.api.block.tile.MobSpawner;
import org.spongepowered.api.block.tile.Note;
import org.spongepowered.api.block.tile.Sign;
import org.spongepowered.api.block.tile.Skull;
import org.spongepowered.api.block.tile.TileEntityType;
import org.spongepowered.api.block.tile.carrier.BrewingStand;
import org.spongepowered.api.block.tile.carrier.Chest;
import org.spongepowered.api.block.tile.carrier.Dispenser;
import org.spongepowered.api.block.tile.carrier.Dropper;
import org.spongepowered.api.block.tile.carrier.Furnace;
import org.spongepowered.api.block.tile.carrier.Hopper;
import org.spongepowered.api.data.DataManipulatorRegistry;
import org.spongepowered.api.data.manipulators.tileentities.BannerData;
import org.spongepowered.api.data.types.Art;
import org.spongepowered.api.data.types.Arts;
import org.spongepowered.api.data.types.BannerPatternShape;
import org.spongepowered.api.data.types.BannerPatternShapes;
import org.spongepowered.api.data.types.Career;
import org.spongepowered.api.data.types.Careers;
import org.spongepowered.api.data.types.CoalType;
import org.spongepowered.api.data.types.CoalTypes;
import org.spongepowered.api.data.types.Comparison;
import org.spongepowered.api.data.types.CookedFish;
import org.spongepowered.api.data.types.CookedFishes;
import org.spongepowered.api.data.types.DirtType;
import org.spongepowered.api.data.types.DisgusedBlockType;
import org.spongepowered.api.data.types.DyeColor;
import org.spongepowered.api.data.types.DyeColors;
import org.spongepowered.api.data.types.Fish;
import org.spongepowered.api.data.types.Fishes;
import org.spongepowered.api.data.types.GoldenApple;
import org.spongepowered.api.data.types.Hinge;
import org.spongepowered.api.data.types.HorseColor;
import org.spongepowered.api.data.types.HorseColors;
import org.spongepowered.api.data.types.HorseStyle;
import org.spongepowered.api.data.types.HorseStyles;
import org.spongepowered.api.data.types.HorseVariant;
import org.spongepowered.api.data.types.HorseVariants;
import org.spongepowered.api.data.types.NotePitch;
import org.spongepowered.api.data.types.NotePitches;
import org.spongepowered.api.data.types.OcelotType;
import org.spongepowered.api.data.types.OcelotTypes;
import org.spongepowered.api.data.types.PlantType;
import org.spongepowered.api.data.types.PortionType;
import org.spongepowered.api.data.types.PrismarineType;
import org.spongepowered.api.data.types.Profession;
import org.spongepowered.api.data.types.Professions;
import org.spongepowered.api.data.types.QuartzType;
import org.spongepowered.api.data.types.RabbitType;
import org.spongepowered.api.data.types.RabbitTypes;
import org.spongepowered.api.data.types.RailDirection;
import org.spongepowered.api.data.types.SandstoneType;
import org.spongepowered.api.data.types.SkeletonType;
import org.spongepowered.api.data.types.SkeletonTypes;
import org.spongepowered.api.data.types.SkullType;
import org.spongepowered.api.data.types.SkullTypes;
import org.spongepowered.api.data.types.SlabType;
import org.spongepowered.api.data.types.StairShape;
import org.spongepowered.api.data.types.StoneType;
import org.spongepowered.api.data.types.TreeType;
import org.spongepowered.api.data.types.WallType;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.FireworkEffectBuilder;
import org.spongepowered.api.item.FireworkShape;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.item.recipe.RecipeRegistry;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.potion.PotionEffectTypes;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.scoreboard.ScoreboardBuilder;
import org.spongepowered.api.scoreboard.TeamBuilder;
import org.spongepowered.api.scoreboard.Visibility;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.objective.ObjectiveBuilder;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.service.persistence.SerializationService;
import org.spongepowered.api.stats.BlockStatistic;
import org.spongepowered.api.stats.EntityStatistic;
import org.spongepowered.api.stats.ItemStatistic;
import org.spongepowered.api.stats.Statistic;
import org.spongepowered.api.stats.StatisticBuilder;
import org.spongepowered.api.stats.StatisticFormat;
import org.spongepowered.api.stats.StatisticGroup;
import org.spongepowered.api.stats.TeamStatistic;
import org.spongepowered.api.stats.achievement.Achievement;
import org.spongepowered.api.stats.achievement.AchievementBuilder;
import org.spongepowered.api.status.Favicon;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.selector.SelectorType;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.api.text.translation.locale.Locales;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.GeneratorType;
import org.spongepowered.api.world.GeneratorTypes;
import org.spongepowered.api.world.WorldBuilder;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.difficulty.Difficulties;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import org.spongepowered.api.world.gen.PopulatorFactory;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.Weathers;
import org.spongepowered.mod.SpongeMod;
import org.spongepowered.mod.block.meta.SpongeNotePitch;
import org.spongepowered.mod.block.meta.SpongeSkullType;
import org.spongepowered.mod.configuration.SpongeConfig;
import org.spongepowered.mod.effect.particle.SpongeParticleEffectBuilder;
import org.spongepowered.mod.effect.particle.SpongeParticleType;
import org.spongepowered.mod.effect.sound.SpongeSound;
import org.spongepowered.mod.entity.SpongeCareer;
import org.spongepowered.mod.entity.SpongeEntityConstants;
import org.spongepowered.mod.entity.SpongeEntityInteractionType;
import org.spongepowered.mod.entity.SpongeEntityMeta;
import org.spongepowered.mod.entity.SpongeEntityType;
import org.spongepowered.mod.entity.SpongeProfession;
import org.spongepowered.mod.entity.player.gamemode.SpongeGameMode;
import org.spongepowered.mod.item.SpongeCoalType;
import org.spongepowered.mod.item.SpongeFireworkBuilder;
import org.spongepowered.mod.item.SpongeItemStackBuilder;
import org.spongepowered.mod.item.merchant.SpongeTradeOfferBuilder;
import org.spongepowered.mod.potion.SpongePotionBuilder;
import org.spongepowered.mod.rotation.SpongeRotation;
import org.spongepowered.mod.service.persistence.builders.block.data.SpongePatternLayerBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeBannerBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeBrewingStandBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeChestBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeCommandBlockBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeComparatorBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeDaylightBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeDispenserBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeDropperBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeEnchantmentTableBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeEndPortalBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeEnderChestBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeFurnaceBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeHopperBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeMobSpawnerBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeNoteBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeSignBuilder;
import org.spongepowered.mod.service.persistence.builders.block.tile.SpongeSkullBuilder;
import org.spongepowered.mod.status.SpongeFavicon;
import org.spongepowered.mod.text.SpongeTextFactory;
import org.spongepowered.mod.text.chat.SpongeChatType;
import org.spongepowered.mod.text.format.SpongeTextColor;
import org.spongepowered.mod.text.format.SpongeTextStyle;
import org.spongepowered.mod.text.translation.SpongeTranslation;
import org.spongepowered.mod.weather.SpongeWeather;
import org.spongepowered.mod.world.SpongeDimensionType;
import org.spongepowered.mod.world.SpongeWorldBuilder;
import org.spongepowered.mod.world.SpongeWorldTypeEnd;
import org.spongepowered.mod.world.SpongeWorldTypeNether;
import org.spongepowered.mod.world.SpongeWorldTypeOverworld;
import org.spongepowered.mod.world.gen.WorldGeneratorRegistry;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unchecked")
@NonnullByDefault
public class SpongeModGameRegistry extends SpongeGameRegistry {
    private final Map<String, GeneratorType> generatorTypeMappings = Maps.newHashMap();
    private final List<BlockType> blockList = new ArrayList<BlockType>();
    private final List<ItemType> itemList = new ArrayList<ItemType>();
    private final Map<String, NotePitch> notePitchMappings = Maps.newHashMap();
    private final Map<String, SkullType> skullTypeMappings = Maps.newHashMap();
    private final Map<String, BannerPatternShape> bannerPatternShapeMappings = Maps.newHashMap();
    private final Map<String, BannerPatternShape> idToBannerPatternShapeMappings = Maps.newHashMap();
    private final Map<String, Fish> fishMappings = Maps.newHashMap();
    private final Map<String, CookedFish> cookedFishMappings = Maps.newHashMap();
    private final Map<String, DyeColor> dyeColorMappings = Maps.newHashMap();
    private final Map<String, EntityType> entityTypeMappings = Maps.newHashMap();
    private final Map<String, Art> artMappings = Maps.newHashMap();
    private static final ImmutableMap<String, EntityInteractionType> entityInteractionTypeMappings =
            new ImmutableMap.Builder<String, EntityInteractionType>()
                    .put("ATTACK", new SpongeEntityInteractionType("ATTACK"))
                    .put("PICK_BLOCK", new SpongeEntityInteractionType("PICK_BLOCK"))
                    .put("USE", new SpongeEntityInteractionType("USE"))
                    .build();

    {
        catalogTypeMap = ImmutableMap.<Class<? extends CatalogType>, Map<String, ? extends CatalogType>>builder()
                .putAll(catalogTypeMap)
                .put(Art.class, this.artMappings)
                .put(BannerPatternShape.class, this.bannerPatternShapeMappings)
                .put(CookedFish.class, this.cookedFishMappings)
                .put(DyeColor.class, this.dyeColorMappings)
                .put(EntityInteractionType.class, entityInteractionTypeMappings)
                .put(EntityType.class, this.entityTypeMappings)
                .put(Fish.class, this.fishMappings)
                .put(GeneratorType.class, this.generatorTypeMappings)
                .put(NotePitch.class, this.notePitchMappings)
                .put(SkullType.class, this.skullTypeMappings)
                .build();
    }

    public com.google.common.base.Optional<BlockType> getBlock(String id) {
        return com.google.common.base.Optional.fromNullable((BlockType) GameData.getBlockRegistry().getObject(id));
    }

    public com.google.common.base.Optional<ItemType> getItem(String id) {
        return com.google.common.base.Optional.fromNullable((ItemType) GameData.getItemRegistry().getObject(id));
    }

    public void setGeneratorTypes() {
        this.generatorTypeMappings.put("DEFAULT", (GeneratorType) WorldType.DEFAULT);
        this.generatorTypeMappings.put("FLAT", (GeneratorType) WorldType.FLAT);
        this.generatorTypeMappings.put("DEBUG", (GeneratorType) WorldType.DEBUG_WORLD);
        this.generatorTypeMappings.put("NETHER", (GeneratorType) new SpongeWorldTypeNether());
        this.generatorTypeMappings.put("END", (GeneratorType) new SpongeWorldTypeEnd());
        this.generatorTypeMappings.put("OVERWORLD", (GeneratorType) new SpongeWorldTypeOverworld());
        RegistryHelper.mapFields(GeneratorTypes.class, this.generatorTypeMappings);
    }

    private void setBlockTypes() {
        Iterator<ResourceLocation> iter = GameData.getBlockRegistry().getKeys().iterator();
        while (iter.hasNext()) {
            this.blockList.add(getBlock(iter.next().toString()).get());
        }

        RegistryHelper.mapFields(BlockTypes.class, new Function<String, BlockType>() {

            @Override
            public BlockType apply(String fieldName) {
                return getBlock(fieldName.toLowerCase()).get();
            }
        });
    }

    private void setItemTypes() {
        Iterator<ResourceLocation> iter = GameData.getItemRegistry().getKeys().iterator();
        while (iter.hasNext()) {
            this.itemList.add(getItem(iter.next().toString()).get());
        }

        RegistryHelper.mapFields(ItemTypes.class, new Function<String, ItemType>() {

            @Override
            public ItemType apply(String fieldName) {
                return getItem(fieldName.toLowerCase()).get();
            }
        });
    }

    private void setNotePitches() {
        RegistryHelper.mapFields(NotePitches.class, new Function<String, NotePitch>() {

            @Override
            public NotePitch apply(String input) {
                NotePitch pitch = new SpongeNotePitch((byte) SpongeModGameRegistry.this.notePitchMappings.size(), input);
                SpongeModGameRegistry.this.notePitchMappings.put(input, pitch);
                return pitch;
            }

        });
    }

    private void setSkullTypes() {
        RegistryHelper.mapFields(SkullTypes.class, new Function<String, SkullType>() {

            @Override
            public SkullType apply(String input) {
                SkullType skullType = new SpongeSkullType((byte) SpongeModGameRegistry.this.skullTypeMappings.size(), input);
                SpongeModGameRegistry.this.skullTypeMappings.put(input, skullType);
                return skullType;
            }

        });
    }

    private void setBannerPatternShapes() {
        RegistryHelper.mapFields(BannerPatternShapes.class, new Function<String, BannerPatternShape>() {

            @Override
            public BannerPatternShape apply(String input) {
                BannerPatternShape bannerPattern = BannerPatternShape.class.cast(TileEntityBanner.EnumBannerPattern.valueOf(input));
                SpongeModGameRegistry.this.bannerPatternShapeMappings.put(bannerPattern.getName(), bannerPattern);
                SpongeModGameRegistry.this.idToBannerPatternShapeMappings.put(bannerPattern.getId(), bannerPattern);
                return bannerPattern;
            }

        });
    }

    private void setFishes() {
        RegistryHelper.mapFields(Fishes.class, new Function<String, Fish>() {

            @Override
            public Fish apply(String input) {
                Fish fish = Fish.class.cast(ItemFishFood.FishType.valueOf(input));
                if (fish != null) {
                    SpongeModGameRegistry.this.fishMappings.put(fish.getId(), fish);
                    return fish;
                } else {
                    return null;
                }
            }
        });

        RegistryHelper.mapFields(CookedFishes.class, new Function<String, CookedFish>() {

            @Override
            public CookedFish apply(String input) {
                CookedFish fish = CookedFish.class.cast(ItemFishFood.FishType.valueOf(input));
                if (fish != null) {
                    SpongeModGameRegistry.this.cookedFishMappings.put(fish.getId(), fish);
                    return fish;
                } else {
                    return null;
                }
            }
        });
    }

    private void setDyeColors() {
        RegistryHelper.mapFields(DyeColors.class, new Function<String, DyeColor>() {

            @Override
            public DyeColor apply(String input) {
                DyeColor dyeColor = DyeColor.class.cast(EnumDyeColor.valueOf(input));
                SpongeModGameRegistry.this.dyeColorMappings.put(dyeColor.getName(), dyeColor);
                return dyeColor;
            }

        });
    }

    private void setEntityTypes() {
        // internal mapping of our EntityTypes to actual MC names
        this.entityTypeMappings.put("DROPPED_ITEM", newEntityTypeFromName("Item"));
        this.entityTypeMappings.put("EXPERIENCE_ORB", newEntityTypeFromName("XPOrb"));
        this.entityTypeMappings.put("LEASH_HITCH", newEntityTypeFromName("LeashKnot"));
        this.entityTypeMappings.put("PAINTING", newEntityTypeFromName("Painting"));
        this.entityTypeMappings.put("ARROW", newEntityTypeFromName("Arrow"));
        this.entityTypeMappings.put("SNOWBALL", newEntityTypeFromName("Snowball"));
        this.entityTypeMappings.put("FIREBALL", newEntityTypeFromName("LargeFireball", "Fireball"));
        this.entityTypeMappings.put("SMALL_FIREBALL", newEntityTypeFromName("SmallFireball"));
        this.entityTypeMappings.put("ENDER_PEARL", newEntityTypeFromName("ThrownEnderpearl"));
        this.entityTypeMappings.put("EYE_OF_ENDER", newEntityTypeFromName("EyeOfEnderSignal"));
        this.entityTypeMappings.put("SPLASH_POTION", newEntityTypeFromName("ThrownPotion"));
        this.entityTypeMappings.put("THROWN_EXP_BOTTLE", newEntityTypeFromName("ThrownExpBottle"));
        this.entityTypeMappings.put("ITEM_FRAME", newEntityTypeFromName("ItemFrame"));
        this.entityTypeMappings.put("WITHER_SKULL", newEntityTypeFromName("WitherSkull"));
        this.entityTypeMappings.put("PRIMED_TNT", newEntityTypeFromName("PrimedTnt"));
        this.entityTypeMappings.put("FALLING_BLOCK", newEntityTypeFromName("FallingSand"));
        this.entityTypeMappings.put("FIREWORK", newEntityTypeFromName("FireworksRocketEntity"));
        this.entityTypeMappings.put("ARMOR_STAND", newEntityTypeFromName("ArmorStand"));
        this.entityTypeMappings.put("BOAT", newEntityTypeFromName("Boat"));
        this.entityTypeMappings.put("RIDEABLE_MINECART", newEntityTypeFromName("MinecartRideable"));
        this.entityTypeMappings.put("CHESTED_MINECART", newEntityTypeFromName("MinecartChest"));
        this.entityTypeMappings.put("FURNACE_MINECART", newEntityTypeFromName("MinecartFurnace"));
        this.entityTypeMappings.put("TNT_MINECART", newEntityTypeFromName("MinecartTnt", "MinecartTNT"));
        this.entityTypeMappings.put("HOPPER_MINECART", newEntityTypeFromName("MinecartHopper"));
        this.entityTypeMappings.put("MOB_SPAWNER_MINECART", newEntityTypeFromName("MinecartSpawner"));
        this.entityTypeMappings.put("COMMANDBLOCK_MINECART", newEntityTypeFromName("MinecartCommandBlock"));
        this.entityTypeMappings.put("CREEPER", newEntityTypeFromName("Creeper"));
        this.entityTypeMappings.put("SKELETON", newEntityTypeFromName("Skeleton"));
        this.entityTypeMappings.put("SPIDER", newEntityTypeFromName("Spider"));
        this.entityTypeMappings.put("GIANT", newEntityTypeFromName("Giant"));
        this.entityTypeMappings.put("ZOMBIE", newEntityTypeFromName("Zombie"));
        this.entityTypeMappings.put("SLIME", newEntityTypeFromName("Slime"));
        this.entityTypeMappings.put("GHAST", newEntityTypeFromName("Ghast"));
        this.entityTypeMappings.put("PIG_ZOMBIE", newEntityTypeFromName("PigZombie"));
        this.entityTypeMappings.put("ENDERMAN", newEntityTypeFromName("Enderman"));
        this.entityTypeMappings.put("CAVE_SPIDER", newEntityTypeFromName("CaveSpider"));
        this.entityTypeMappings.put("SILVERFISH", newEntityTypeFromName("Silverfish"));
        this.entityTypeMappings.put("BLAZE", newEntityTypeFromName("Blaze"));
        this.entityTypeMappings.put("MAGMA_CUBE", newEntityTypeFromName("LavaSlime"));
        this.entityTypeMappings.put("ENDER_DRAGON", newEntityTypeFromName("EnderDragon"));
        this.entityTypeMappings.put("WITHER", newEntityTypeFromName("WitherBoss"));
        this.entityTypeMappings.put("BAT", newEntityTypeFromName("Bat"));
        this.entityTypeMappings.put("WITCH", newEntityTypeFromName("Witch"));
        this.entityTypeMappings.put("ENDERMITE", newEntityTypeFromName("Endermite"));
        this.entityTypeMappings.put("GUARDIAN", newEntityTypeFromName("Guardian"));
        this.entityTypeMappings.put("PIG", newEntityTypeFromName("Pig"));
        this.entityTypeMappings.put("SHEEP", newEntityTypeFromName("Sheep"));
        this.entityTypeMappings.put("COW", newEntityTypeFromName("Cow"));
        this.entityTypeMappings.put("CHICKEN", newEntityTypeFromName("Chicken"));
        this.entityTypeMappings.put("SQUID", newEntityTypeFromName("Squid"));
        this.entityTypeMappings.put("WOLF", newEntityTypeFromName("Wolf"));
        this.entityTypeMappings.put("MUSHROOM_COW", newEntityTypeFromName("MushroomCow"));
        this.entityTypeMappings.put("SNOWMAN", newEntityTypeFromName("SnowMan"));
        this.entityTypeMappings.put("OCELOT", newEntityTypeFromName("Ozelot"));
        this.entityTypeMappings.put("IRON_GOLEM", newEntityTypeFromName("VillagerGolem"));
        this.entityTypeMappings.put("HORSE", newEntityTypeFromName("EntityHorse"));
        this.entityTypeMappings.put("RABBIT", newEntityTypeFromName("Rabbit"));
        this.entityTypeMappings.put("VILLAGER", newEntityTypeFromName("Villager"));
        this.entityTypeMappings.put("ENDER_CRYSTAL", newEntityTypeFromName("EnderCrystal"));
        this.entityTypeMappings.put("EGG", new SpongeEntityType(-1, "Egg", EntityEgg.class));
        this.entityTypeMappings.put("FISHING_HOOK", new SpongeEntityType(-2, "FishingHook", EntityFishHook.class));
        this.entityTypeMappings.put("LIGHTNING", new SpongeEntityType(-3, "Lightning", EntityLightningBolt.class));
        this.entityTypeMappings.put("WEATHER", new SpongeEntityType(-4, "Weather", EntityWeatherEffect.class));
        this.entityTypeMappings.put("PLAYER", new SpongeEntityType(-5, "Player", EntityPlayerMP.class));
        this.entityTypeMappings.put("COMPLEX_PART", new SpongeEntityType(-6, "ComplexPart", EntityDragonPart.class));

        RegistryHelper.mapFields(EntityTypes.class, new Function<String, EntityType>() {

            @Override
            public EntityType apply(String fieldName) {
                if (fieldName.equals("UNKNOWN")) {
                    // TODO Something for Unknown?
                    return null;
                }
                EntityType entityType = SpongeModGameRegistry.this.entityTypeMappings.get(fieldName);
                SpongeModGameRegistry.this.entityClassToTypeMappings
                        .put(((SpongeEntityType) entityType).entityClass, (SpongeEntityType) entityType);
                SpongeModGameRegistry.this.entityIdToTypeMappings.put(((SpongeEntityType) entityType).getId(), ((SpongeEntityType) entityType));
                return entityType;
            }
        });

        RegistryHelper.mapFields(SkeletonTypes.class, SpongeEntityConstants.SKELETON_TYPES);
        RegistryHelper.mapFields(HorseColors.class, SpongeEntityConstants.HORSE_COLORS);
        RegistryHelper.mapFields(HorseVariants.class, SpongeEntityConstants.HORSE_VARIANTS);
        RegistryHelper.mapFields(HorseStyles.class, SpongeEntityConstants.HORSE_STYLES);
        RegistryHelper.mapFields(OcelotTypes.class, SpongeEntityConstants.OCELOT_TYPES);
        RegistryHelper.mapFields(RabbitTypes.class, SpongeEntityConstants.RABBIT_TYPES);
    }

    private SpongeEntityType newEntityTypeFromName(String spongeName, String mcName) {
        return new SpongeEntityType((Integer) EntityList.stringToIDMapping.get(mcName), spongeName,
                (Class<? extends Entity>) EntityList.stringToClassMapping.get(mcName));
    }

    private void setArts() {
        RegistryHelper.mapFields(Arts.class, new Function<String, Art>() {

            @Override
            public Art apply(String fieldName) {
                Art art = (Art) (Object) EntityPainting.EnumArt.valueOf(fieldName);
                SpongeModGameRegistry.this.artMappings.put(art.getName(), art);
                return art;
            }
        });
    }

    private SpongeEntityType newEntityTypeFromName(String name) {
        return newEntityTypeFromName(name, name);
    }


    @Override
    public GameDictionary getGameDictionary() {
        return SpongeGameDictionary.instance;
    }

    private void setEntityInteractionTypes() {
        RegistryHelper.mapFields(EntityInteractionTypes.class, SpongeModGameRegistry.entityInteractionTypeMappings);
    }

    @Override
    public void init() {
        super.init();
        setArts();
        setDyeColors();
        setSkullTypes();
        setNotePitches();
        setBannerPatternShapes();
        setEntityInteractionTypes();
        setGeneratorTypes();
    }

    @Override
    public void postInit() {
        super.postInit();
        setBlockTypes();
        setItemTypes();
        setEntityTypes();
        setFishes();
    }
}
