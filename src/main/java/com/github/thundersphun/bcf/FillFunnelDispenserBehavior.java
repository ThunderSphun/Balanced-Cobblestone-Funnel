package com.github.thundersphun.bcf;

import com.github.thundersphun.bcf.config.ConfigData;
import com.github.thundersphun.bcf.config.ConfigLoader;
import com.github.thundersphun.bcf.funnel.FunnelBlock;
import com.github.thundersphun.bcf.funnel.FunnelBlockEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.registry.Registry;

public class FillFunnelDispenserBehavior implements DispenserBehavior {
	private static final DispenserBehavior DEFAULT_BEHAVIOR = new ItemDispenserBehavior();

	private final Item output;
	private final DispenserBehavior fallback;

	public FillFunnelDispenserBehavior(Item output, DispenserBehavior fallback) {
		this.output = output;
		this.fallback = fallback;
	}

	public FillFunnelDispenserBehavior(Item output) {
		this(output, DEFAULT_BEHAVIOR);
	}

	@Override
	public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
		var pos = pointer.getPos();
		var facing = pointer.getBlockState().get(DispenserBlock.FACING);
		var offset = pos.offset(facing);
		var world = pointer.getWorld();
		if (world.getBlockState(offset).getBlock() instanceof FunnelBlock) {
			var blockEntity = world.getBlockEntity(offset);
			if (blockEntity instanceof FunnelBlockEntity funnelBlockEntity) {
				var itemId = Registry.ITEM.getId(stack.getItem());
				if (ConfigData.LAVA_RESULT.containsKey(itemId) || ConfigData.WATER_RESULT.containsKey(itemId)) {
					if (funnelBlockEntity.attemptFill(stack)) {
						//funnelBlockEntity.sync();
						funnelBlockEntity.toUpdatePacket();
						stack.decrement(1);
						if (stack.isEmpty()) {
							return new ItemStack(this.output);
						} else {
							if (((DispenserBlockEntity) pointer.getBlockEntity()).addToFirstFreeSlot(new ItemStack(this.output)) < 0) {
								DEFAULT_BEHAVIOR.dispense(pointer, new ItemStack(this.output));
							}
							return stack;
						}
					}
				}
			}
		}

		return this.fallback.dispense(pointer, stack);
	}
}
