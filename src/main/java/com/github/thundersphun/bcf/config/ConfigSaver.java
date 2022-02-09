package com.github.thundersphun.bcf.config;

import com.github.thundersphun.bcf.Bcf;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;

public class ConfigSaver {
	private ConfigSaver() {
	}

	static void saveConfig() {
		Bcf.LOGGER.info("Saving Config");

		try (var writer = new FileWriter(ConfigLoader.FILE)) {
			// create
			var json = new JsonObject();
			var lavaInput = new JsonArray();
			var lavaOutput = new JsonArray();
			var waterInput = new JsonArray();
			var waterOutput = new JsonArray();
			var funnels = new JsonObject();

			// fill
			ConfigData.LAVA_RESULT.forEach((key, value) -> {
				lavaInput.add(key.toString());
				lavaOutput.add(value.toString());
			});

			ConfigData.WATER_RESULT.forEach((key, value) -> {
				waterInput.add(key.toString());
				waterOutput.add(value.toString());
			});

			ConfigData.FUNNEL_TYPES.forEach((funnel, funnelData) -> {
				// create
				var funnelJson = new JsonObject();
				var generates = new JsonArray();

				// fill
				funnelData.getWeights().foreach((id, weight) -> {
					// create
					var entry = new JsonObject();

					// add
					entry.addProperty("block", id.toString());
					entry.addProperty("weight", weight);

					// save
					generates.add(entry);
				});

				// add
				funnelJson.addProperty("speed", funnelData.getSpeed());
				funnelJson.addProperty("uses_per_lava", funnelData.getLavaUses());
				funnelJson.addProperty("uses_per_water", funnelData.getWaterUses());
				funnelJson.add("generates", generates);

				// save
				funnels.add(funnel.replace("_funnel", ""), funnelJson);
			});

			// add
			json.add("lava_input", lavaInput);
			json.add("lava_output", lavaOutput);
			json.add("water_input", waterInput);
			json.add("water_output", waterOutput);
			json.addProperty("autofill_infinite_fluid_tank", true);
			json.add("funnels", funnels);

			// save
			new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
