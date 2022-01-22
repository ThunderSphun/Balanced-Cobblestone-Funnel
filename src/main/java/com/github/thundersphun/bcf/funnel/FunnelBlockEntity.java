package com.github.thundersphun.bcf.funnel;

import com.github.thundersphun.bcf.Bcf;
import com.github.thundersphun.bcf.config.ConfigData;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class FunnelBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	private FunnelTank lavaTank;
	private FunnelTank waterTank;
	private long interactionTime;

	public FunnelBlockEntity(BlockPos pos, BlockState state, int maxLavaUses, int maxWaterUses) {
		super(Bcf.FUNNEL_BLOCK_ENTITY, pos, state);
		this.lavaTank = new FunnelTank(maxLavaUses, ConfigData.getAutoFill() ? 0 : -1, ConfigData.LAVA_RESULT);
		this.waterTank = new FunnelTank(maxWaterUses, ConfigData.getAutoFill() ? 0 : -1, ConfigData.WATER_RESULT);
		this.interactionTime = -1;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		this.lavaTank = new FunnelTank(nbt.getInt("max_uses_lava"), nbt.getInt("lava_uses"), ConfigData.LAVA_RESULT);
		this.waterTank = new FunnelTank(nbt.getInt("max_uses_water"), nbt.getInt("water_uses"), ConfigData.WATER_RESULT);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		nbt.putInt("max_uses_lava", this.lavaTank.getMaxUses());
		nbt.putInt("lava_uses", this.lavaTank.getUses());
		nbt.putInt("max_uses_water", this.waterTank.getMaxUses());
		nbt.putInt("water_uses", this.waterTank.getUses());

		return nbt;
	}

	@Override
	public void fromClientTag(NbtCompound nbt) {
		this.readNbt(nbt);
	}

	@Override
	public NbtCompound toClientTag(NbtCompound nbt) {
		return this.writeNbt(nbt);
	}

	public boolean attemptFill(ItemStack item) {
		Identifier itemId = Registry.ITEM.getId(item.getItem());
		if (this.lavaTank.canFill(itemId)) {
			this.lavaTank.fill();
			return true;
		}
		if (this.waterTank.canFill(itemId)) {
			this.waterTank.fill();
			return true;
		}

		return false;
	}

	public void use() {
		this.lavaTank.use();
		this.waterTank.use();
	}

	public float getLavaFullness() {
		return this.lavaTank.fillAmount();
	}

	public float getWaterFullness() {
		return this.waterTank.fillAmount();
	}

	public long getInteractionTime() {
		return this.interactionTime;
	}

	public void setInteractionTime(long interactionTime) {
		this.interactionTime = interactionTime;
	}

	public void fillWater() {
		if (this.waterTank.fillAmount() == 0) {
			this.waterTank.fill();
		}
	}

	public void fillLava() {
		if (this.lavaTank.fillAmount() == 0) {
			this.lavaTank.fill();
		}
	}
}
