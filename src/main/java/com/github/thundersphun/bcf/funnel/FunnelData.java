package com.github.thundersphun.bcf.funnel;

import com.github.thundersphun.bcf.config.json.JsonMissingKeyException;
import com.github.thundersphun.bcf.config.json.JsonUtil;
import com.github.thundersphun.bcf.SortableWeightedList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.StringJoiner;

public final class FunnelData {
	private final int speed;
	private final int lavaUses;
	private final int waterUses;
	private final SortableWeightedList<Identifier> weights;

	public FunnelData(int speed, int lavaUses, int waterUses) {
		this.speed = speed;
		this.lavaUses = lavaUses;
		this.waterUses = waterUses;
		this.weights = new SortableWeightedList<>();
	}

	public FunnelData(JsonObject jsonFunnelData) throws JsonMissingKeyException {
		this(JsonUtil.requiresInt(jsonFunnelData, "speed"),
				JsonUtil.getInt(jsonFunnelData, "uses_per_lava", -1),
				JsonUtil.getInt(jsonFunnelData, "uses_per_water", -1));

		var regex = "([a-z0-9_.-]+:)?[a-z0-9_.-]+";
		for (JsonElement e : JsonUtil.getArray(jsonFunnelData, "generates")) {
			var id = JsonUtil.requiresString(e.getAsJsonObject(), "block");
			if (id.matches(regex)) {
				this.weights.add(new Identifier(id),
						JsonUtil.getInt(e.getAsJsonObject(), "weight", 1));
			}
		}
	}

	public int getSpeed() {
		return this.speed;
	}

	public int getLavaUses() {
		return this.lavaUses;
	}

	public int getWaterUses() {
		return this.waterUses;
	}

	public SortableWeightedList<Identifier> getWeights() {
		return this.weights;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", FunnelData.class.getSimpleName() + "[", "]")
				.add("speed=" + this.speed)
				.add("lavaUses=" + this.lavaUses)
				.add("waterUses=" + this.waterUses)
				.add("weights=" + this.weights)
				.toString();
	}
}
