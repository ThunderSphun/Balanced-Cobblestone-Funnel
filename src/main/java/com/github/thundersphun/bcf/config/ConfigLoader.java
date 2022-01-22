package com.github.thundersphun.bcf.config;

import com.github.thundersphun.bcf.Bcf;
import com.github.thundersphun.bcf.config.json.JsonMissingKeyException;
import com.github.thundersphun.bcf.config.json.JsonUtil;
import com.github.thundersphun.bcf.funnel.FunnelData;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigLoader {
	static final File FILE = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "bcf.json").toFile();

	private ConfigLoader() {
	}

	public static void loadJson() {
		try (var reader = new FileReader(FILE)) {
			Bcf.LOGGER.info("Loaded config");

			var config = new Gson().fromJson(reader, JsonObject.class);

			fillResultMap(ConfigData.LAVA_RESULT, config.getAsJsonArray("lava_input"), config.getAsJsonArray("lava_output"));
			fillResultMap(ConfigData.WATER_RESULT, config.getAsJsonArray("water_input"), config.getAsJsonArray("water_output"));
			ConfigData.AUTOFILL = JsonUtil.getBoolean(config, "autofill_infinite_fluid_tank");
			config.getAsJsonObject("funnels").entrySet().forEach(e -> {
				try {
					ConfigData.FUNNEL_TYPES.put(e.getKey() + "_funnel", new FunnelData(e.getValue().getAsJsonObject()));
				} catch (JsonMissingKeyException ex) {
					ex.printStackTrace();
				}
			});
		} catch (FileNotFoundException e) {
			ConfigGenerator.generateConfig();
			ConfigSaver.saveConfig();
		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}

	private static void fillResultMap(Map<Identifier, Identifier> map, JsonArray inputKeys, JsonArray outputValues) {
		map.clear();
		if (inputKeys.size() != outputValues.size()) {
			throw new JsonSyntaxException("input and output should have the same size");
		}

		var regex = "([a-z0-9_.-]+:)?[a-z0-9_.-]+";
		for (int i = 0; i < inputKeys.size(); i++) {
			var input = inputKeys.get(i).getAsString();
			var output = outputValues.get(i).getAsString();
			if (input.matches(regex) && output.matches(regex)) {
				map.put(new Identifier(input), new Identifier(output));
			} else {
				Bcf.LOGGER.error(String.format("invalid input (%s) and/or output (%s)", input, output));
			}
		}
	}
}
