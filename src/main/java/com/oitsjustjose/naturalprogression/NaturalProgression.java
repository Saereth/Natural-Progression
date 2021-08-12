package com.oitsjustjose.naturalprogression;

import com.oitsjustjose.naturalprogression.client.ClientProxy;
import com.oitsjustjose.naturalprogression.common.CommonProxy;
import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;
import com.oitsjustjose.naturalprogression.common.config.CommonConfig;
import com.oitsjustjose.naturalprogression.common.event.BoneEvent;
import com.oitsjustjose.naturalprogression.common.event.GroundBreak;
import com.oitsjustjose.naturalprogression.common.event.StoneBreak;
import com.oitsjustjose.naturalprogression.common.event.ToolNeutering;
import com.oitsjustjose.naturalprogression.common.event.WoodBreak;
import com.oitsjustjose.naturalprogression.common.items.NaturalProgressionItems;
import com.oitsjustjose.naturalprogression.common.recipes.DamageItemRecipe;
import com.oitsjustjose.naturalprogression.common.utils.Constants;
import com.oitsjustjose.naturalprogression.common.world.feature.PebbleFeature;
import com.oitsjustjose.naturalprogression.common.world.feature.TwigFeature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MODID)
public class NaturalProgression {
    private static NaturalProgression instance;

    public Logger LOGGER = LogManager.getLogger();

    public static CommonProxy proxy =
            DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final IRecipeSerializer<DamageItemRecipe> DAMAGE_ITEM_RECIPE =
            new DamageItemRecipe.Serializer();

    public NaturalProgression() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new WoodBreak());
        MinecraftForge.EVENT_BUS.register(new StoneBreak());
        MinecraftForge.EVENT_BUS.register(new GroundBreak());
        MinecraftForge.EVENT_BUS.register(new ToolNeutering());
        MinecraftForge.EVENT_BUS.register(new BoneEvent());

        this.configSetup();
    }

    public static NaturalProgression getInstance() {
        return instance;
    }

    private void configSetup() {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG,
                FMLPaths.CONFIGDIR.get().resolve("natural-progression-common.toml"));
    }

    @SubscribeEvent
    public void onBiomesLoaded(BiomeLoadingEvent evt) {
        BiomeGenerationSettingsBuilder settings = evt.getGeneration();

        PebbleFeature p = new PebbleFeature(NoFeatureConfig.field_236558_a_);
        TwigFeature t = new TwigFeature(NoFeatureConfig.field_236558_a_);

        settings.withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
                p.withConfiguration(NoFeatureConfig.field_236559_b_));
        settings.withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
                t.withConfiguration(NoFeatureConfig.field_236559_b_));
    }

    public void setupClient(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(NaturalProgressionBlocks.twigs, RenderType.getCutout());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(
                final RegistryEvent.Register<Block> blockRegistryEvent) {
            NaturalProgressionBlocks.registerBlocks(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            NaturalProgressionBlocks.registerBlockItems(itemRegistryEvent);
            NaturalProgressionItems.registerItems(itemRegistryEvent);
        }

        @SubscribeEvent
        public static void onRegisterSerializers(
                final RegistryEvent.Register<IRecipeSerializer<?>> event) {
            event.getRegistry().register(DAMAGE_ITEM_RECIPE
                    .setRegistryName(new ResourceLocation(Constants.MODID, "damage_tools")));
        }
    }
}
