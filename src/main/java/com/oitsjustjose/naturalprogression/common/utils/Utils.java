package com.oitsjustjose.naturalprogression.common.utils;

import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.naturalprogression.NaturalProgression;
import com.oitsjustjose.naturalprogression.common.blocks.NaturalProgressionBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;

public class Utils {
    public static Block getPebbleForPos(IWorld world, BlockPos pos) {
        BlockPos search = new BlockPos(pos.getX(), world.getHeight(), pos.getZ());
        for (int y = 0; y < search.getY(); y++) {
            if (world.getBlockState(search.down(y)).getMaterial() == Material.AIR) {
                continue;
            }

            if (NaturalProgressionBlocks.blocksToPebbles.containsKey(world.getBlockState(search.down(y)).getBlock())) {
                return NaturalProgressionBlocks.blocksToPebbles.get(world.getBlockState(search.down(y)).getBlock());
            }
        }
        return NaturalProgressionBlocks.blocksToPebbles.get(Blocks.STONE);
    }

    /**
     * @param stateAtPos The BlockState at the the position
     * @param world      An IWorld instance
     * @param searchPos  The BlockPos currently be searched at
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(BlockState stateAtPos, IWorld world, BlockPos searchPos) {
        return stateAtPos.getMaterial().isLiquid() || stateAtPos.getMaterial() == Material.AIR;
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(IWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == Blocks.WATER || state.hasProperty(BlockStateProperties.WATERLOGGED);
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(IWorld world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isLiquid() && !isInWater(world, pos);
    }

    /**
     * @param world    an IWorld instance
     * @param chunkPos The chunkPos to place within
     * @return A random BlockPos within the chunkPos that is valid. Can return null
     *         if no valid location is found.
     */
    @Nullable
    public static BlockPos getPebblePos(IWorld world, ChunkPos chunkPos) {
        Random random = new Random();
        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);
        BlockPos searchPos = new BlockPos(blockPosX, world.getHeight(), blockPosZ);

        for (int i = 0; i < searchPos.getY(); i++) {
            BlockState stateAtPos = world.getBlockState(searchPos.down(i));
            BlockState stateBelow = world.getBlockState(searchPos.down(i + 1));

            if (canReplace(stateAtPos, world, searchPos)) {
                if (stateBelow.isSolid() && stateBelow.getMaterial().blocksMovement()) {
                    return searchPos.down(i);
                }
            }
        }

        return null;
    }

    public static String dimensionToString(IWorld world) {
        if (world instanceof World) {
            return Objects.requireNonNull(((World) world).getDimensionKey().getLocation().toString());
        } else if (world instanceof ServerWorld) {
            return Objects.requireNonNull(((ServerWorld) world).getDimensionKey().getLocation().toString());
        } else if (world instanceof WorldGenRegion) {
            return Objects
                    .requireNonNull(((WorldGenRegion) world).getWorld().getDimensionKey().getLocation().toString());
        }
        NaturalProgression.getInstance().LOGGER
                .warn("Utils.dimensionToString called on IWorld object that couldn't be interpreted");
        return "ERR";
    }
}