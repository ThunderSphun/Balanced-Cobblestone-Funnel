package com.github.thundersphun.bcf.funnel;

import net.minecraft.util.Identifier;

import java.util.Map;

public class FunnelTank {
	private final int maxUses;
	private final Map<Identifier, Identifier> itemConverter;
	private int uses;

	public FunnelTank(int maxUses, int uses, Map<Identifier, Identifier> itemConverter) {
		this.maxUses = maxUses;
		this.uses = uses;
		this.itemConverter = itemConverter;
	}

	public int getMaxUses() {
		return this.maxUses;
	}

	public int getUses() {
		return this.uses;
	}

	public boolean canFill(Identifier id) {
		return this.itemConverter.containsKey(id) && this.fillAmount() == 0;
	}

	public float fillAmount() {
		return this.uses == -1 ? 0 :
				this.maxUses == -1 ? 1 : (float) this.uses / this.maxUses;
	}

	public void fill() {
		this.uses = this.maxUses;
		this.clamp();
	}

	public void use() {
		if (this.maxUses != -1) {
			this.uses--;
			this.clamp();
		}
	}

	public void clamp() {
		if (this.uses < 0) {
			this.uses = 0;
		}
		if (this.uses > this.maxUses) {
			this.uses = this.maxUses;
		}
	}
}
