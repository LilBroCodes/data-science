package org.lilbrocodes.data_science.common.recipes.processing;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lilbrocodes.data_science.common.item.AbstractPolishableItem;
import org.lilbrocodes.data_science.common.recipes.GrindingRecipe;
import org.lilbrocodes.data_science.common.registry.ModRecipes;

import java.util.List;

public class GrindstoneMethods {
    public static boolean tryUseItem(World world, BlockHitResult hitResult, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
        if (world.isClient) {
            addDustParticles(world, hitResult, state, player.getRotationVecClient(), hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite());
            return false;
        };

        ItemStack stack = player.getStackInHand(hand);
        if (player.shouldCancelInteraction() || stack.isEmpty()) return false;

        if (stack.getItem() instanceof AbstractPolishableItem polishable) {
            ItemStack result = polishable.tryPolish(stack, world, player);

            if (result == null || result.isOf(polishable.getShatteredItem(stack).getItem())) {
                player.setStackInHand(hand, result);
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1f, 1f);
            } else if (!result.isEmpty()) {
                // completed → replace with finished item
                player.setStackInHand(hand, result);
                world.playSound(null, pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1f, 1f);
            } else {
                // successful partial progress → keep same stack (already mutated)
                player.setStackInHand(hand, stack);
                world.playSound(null, pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1f, 1f);
            }

            return true;
        }

        List<GrindingRecipe> recipes = world.getRecipeManager().listAllOfType(ModRecipes.GRINDING_RECIPE_TYPE);
        for (GrindingRecipe recipe : recipes) {
            if (recipe.getInput().test(stack)) {
                if (Math.random() > recipe.getFailChance()) {
                    recipe.getOutputs().forEach(s -> {
                        ItemStack insert = s.copy();
                        Vec3d p = Vec3d.ofCenter(pos).add(0, 1, 0);
                        if (!player.giveItemStack(insert)) {
                            world.spawnEntity(new ItemEntity(world, p.x, p.y, p.z, insert));
                        }
                    });
                    world.playSound(null, pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1f, 1f);
                } else {
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                }
                stack.decrement(1);
                player.setStackInHand(hand, stack);
                return true;
            }
        }

        return false;
    }

    public static void addDustParticles(World world, BlockHitResult hitResult, BlockState state, Vec3d userRotation, Arm arm) {
        int i = arm == Arm.RIGHT ? 1 : -1;
        int j = world.getRandom().nextBetweenExclusive(7, 12);
        BlockStateParticleEffect blockStateParticleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
        Direction direction = hitResult.getSide();
        DustParticlesOffset dustParticlesOffset = DustParticlesOffset.fromSide(userRotation, direction);
        Vec3d vec3d = hitResult.getPos();

        for(int k = 0; k < j; ++k) {
            world.addParticle(blockStateParticleEffect, vec3d.x - (double)(direction == Direction.WEST ? 1.0E-6F : 0.0F), vec3d.y, vec3d.z - (double)(direction == Direction.NORTH ? 1.0E-6F : 0.0F), dustParticlesOffset.xd() * (double)i * (double)3.0F * world.getRandom().nextDouble(), (double)0.0F, dustParticlesOffset.zd() * (double)i * (double)3.0F * world.getRandom().nextDouble());
        }

    }

    record DustParticlesOffset(double xd, double yd, double zd) {
        public static DustParticlesOffset fromSide(Vec3d userRotation, Direction side) {
            return switch (side) {
                case DOWN, UP -> new DustParticlesOffset(userRotation.getZ(), 0.0F, -userRotation.getX());
                case NORTH -> new DustParticlesOffset(1.0F, 0.0F, -0.1);
                case SOUTH -> new DustParticlesOffset(-1.0F, 0.0F, 0.1);
                case WEST -> new DustParticlesOffset(-0.1, 0.0F, -1.0F);
                case EAST -> new DustParticlesOffset(0.1, 0.0F, 1.0F);
            };
        }
    }
}
