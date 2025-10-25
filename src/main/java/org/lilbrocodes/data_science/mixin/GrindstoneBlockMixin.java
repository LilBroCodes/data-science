package org.lilbrocodes.data_science.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lilbrocodes.data_science.common.recipes.processing.GrindstoneMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneBlock.class)
public class GrindstoneBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void dataScience$tryGrindItem(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (GrindstoneMethods.tryUseItem(world, hit, state, pos, player, hand)) cir.setReturnValue(ActionResult.SUCCESS);
    }
}
