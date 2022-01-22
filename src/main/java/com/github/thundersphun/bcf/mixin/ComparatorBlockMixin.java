package com.github.thundersphun.bcf.mixin;

import com.github.thundersphun.bcf.ComparatorUtil;
import com.github.thundersphun.bcf.funnel.FunnelBlock;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComparatorBlock.class)
abstract class ComparatorBlockMixin extends AbstractRedstoneGateBlock implements BlockEntityProvider {
	private ComparatorBlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "getPower(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)I",
			at = @At("RETURN"), cancellable = true)
	public void getPower(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Integer> cir) {
		var direction = state.get(FACING);
		var offsetBlockPos = pos.offset(direction, 1);
		var relativeBlockState = world.getBlockState(offsetBlockPos);

		if (relativeBlockState.hasComparatorOutput() && relativeBlockState.getBlock() instanceof FunnelBlock) {
			cir.setReturnValue(ComparatorUtil.getFillLevel(state, world.getBlockEntity(offsetBlockPos)));

		} else if (cir.getReturnValue() < 15 && relativeBlockState.isSolidBlock(world, offsetBlockPos)) {
			offsetBlockPos = pos.offset(direction, 2);
			relativeBlockState = world.getBlockState(offsetBlockPos);

			if (relativeBlockState.hasComparatorOutput() && relativeBlockState.getBlock() instanceof FunnelBlock) {
				cir.setReturnValue(ComparatorUtil.getFillLevel(state, world.getBlockEntity(offsetBlockPos)));
			}
		}
	}
}
