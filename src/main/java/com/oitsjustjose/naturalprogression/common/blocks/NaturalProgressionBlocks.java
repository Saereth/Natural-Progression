package com.oitsjustjose.naturalprogression.common.blocks;

import java.util.ArrayList;

import com.oitsjustjose.naturalprogression.common.items.PebbleItem;
import com.oitsjustjose.naturalprogression.common.utils.Constants;
import com.oitsjustjose.naturalprogression.common.utils.NaturalProgressionGroup;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class NaturalProgressionBlocks
{
    private static ArrayList<Block> modBlocks = new ArrayList<>();

    public static Block stonePebble;
    public static Block andesitePebble;
    public static Block dioritePebble;
    public static Block granitePebble;
    public static Block sandstonePebble;

    public static Block cobbledAndesite;
    public static Block cobbledDiorite;
    public static Block cobbledGranite;
    public static Block cobbledSandstone;

    public static void registerBlocks(final RegistryEvent.Register<Block> blockRegistryEvent)
    {
        stonePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "stone_pebble"));
        blockRegistryEvent.getRegistry().register(stonePebble);
        modBlocks.add(stonePebble);

        andesitePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "andesite_pebble"));
        blockRegistryEvent.getRegistry().register(andesitePebble);
        modBlocks.add(andesitePebble);

        dioritePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "diorite_pebble"));
        blockRegistryEvent.getRegistry().register(dioritePebble);
        modBlocks.add(dioritePebble);

        granitePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "granite_pebble"));
        blockRegistryEvent.getRegistry().register(granitePebble);
        modBlocks.add(granitePebble);

        sandstonePebble = new PebbleBlock().setRegistryName(new ResourceLocation(Constants.MODID, "sandstone_pebble"));
        blockRegistryEvent.getRegistry().register(sandstonePebble);
        modBlocks.add(sandstonePebble);

        Block.Properties cobbleProps = Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F);

        cobbledAndesite = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_andesite"));
        blockRegistryEvent.getRegistry().register(cobbledAndesite);
        modBlocks.add(cobbledAndesite);

        cobbledDiorite = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_diorite"));
        blockRegistryEvent.getRegistry().register(cobbledDiorite);
        modBlocks.add(cobbledDiorite);

        cobbledGranite = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_granite"));
        blockRegistryEvent.getRegistry().register(cobbledGranite);
        modBlocks.add(cobbledGranite);

        cobbledSandstone = new Block(cobbleProps)
                .setRegistryName(new ResourceLocation(Constants.MODID, "cobbled_sandstone"));
        blockRegistryEvent.getRegistry().register(cobbledSandstone);
        modBlocks.add(cobbledSandstone);
    }

    public static void registerBlockItems(final RegistryEvent.Register<Item> itemRegistryEvent)
    {
        for (Block block : modBlocks)
        {
            // Ignore pebble blocks - pebble item will represent them
            if (block instanceof PebbleBlock)
            {
                Item iBlock = new PebbleItem(block).setRegistryName(block.getRegistryName());
                itemRegistryEvent.getRegistry().register(iBlock);
            }
            else
            {
                Item iBlock = new BlockItem(block, new Item.Properties().group(NaturalProgressionGroup.getInstance()))
                        .setRegistryName(block.getRegistryName());
                itemRegistryEvent.getRegistry().register(iBlock);
            }
        }
    }

}