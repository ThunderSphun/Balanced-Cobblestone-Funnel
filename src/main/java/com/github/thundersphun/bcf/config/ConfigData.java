package com.github.thundersphun.bcf.config;

import com.github.thundersphun.bcf.funnel.FunnelData;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ConfigData {
	public static final Map<Identifier, Identifier> LAVA_RESULT = new HashMap<>();
	public static final Map<Identifier, Identifier> WATER_RESULT = new HashMap<>();
	public static final Map<String, FunnelData> FUNNEL_TYPES = new HashMap<>();
	static boolean AUTOFILL = false;

	public static boolean getAutoFill() {
		return AUTOFILL;
	}
}
