package com.github.thundersphun.bcf;

import com.github.thundersphun.bcf.funnel.FunnelBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.util.math.MathHelper;

public class ComparatorUtil {
	public static int getFillLevel(BlockState state, BlockEntity entity) {
		if (entity instanceof FunnelBlockEntity) {
			return state.get(ComparatorBlock.MODE) == ComparatorMode.COMPARE ?
					MathHelper.ceil(((FunnelBlockEntity) entity).getWaterFullness() * 15) :
					MathHelper.ceil(((FunnelBlockEntity) entity).getLavaFullness() * 15);
		}
		return 0;
	}
}
