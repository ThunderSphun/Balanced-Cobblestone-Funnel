package com.github.thundersphun.bcf.config.json;

import com.google.gson.*;

public class JsonUtil {
	public static JsonElement get(JsonObject json, String key, JsonElement ifAbsent) {
		if (json.has(key)) {
			return json.get(key);
		}
		return ifAbsent;
	}

	public static int getInt(JsonObject json, String key, int ifAbsent) {
		return get(json, key, new JsonPrimitive(ifAbsent)).getAsInt();
	}

	public static float getFloat(JsonObject json, String key, float ifAbsent) {
		return get(json, key, new JsonPrimitive(ifAbsent)).getAsFloat();
	}

	public static String getString(JsonObject json, String key, String ifAbsent) {
		return get(json, key, new JsonPrimitive(ifAbsent)).getAsString();
	}

	public static JsonElement getArray(JsonObject json, String key, JsonArray ifAbsent) {
		return get(json, key, ifAbsent).getAsJsonArray();
	}

	public static boolean getBoolean(JsonObject json, String key, boolean ifAbsent) {
		return get(json, key, new JsonPrimitive(ifAbsent)).getAsBoolean();
	}

	public static int getInt(JsonObject json, String key) {
		return getInt(json, key, 0);
	}

	public static float getFloat(JsonObject json, String key) {
		return getFloat(json, key, 0f);
	}

	public static String getString(JsonObject json, String key) {
		return getString(json, key, "");
	}

	public static JsonArray getArray(JsonObject json, String key) {
		return getArray(json, key, new JsonArray()).getAsJsonArray();
	}

	public static boolean getBoolean(JsonObject json, String key) {
		return getBoolean(json, key, false);
	}

	public static int requiresInt(JsonObject json, String key) throws JsonMissingKeyException {
		if (json.has(key)) {
			return getInt(json, key);
		}
		throw new JsonMissingKeyException("missing key " + key + " in " + json);
	}

	public static float requiresFloat(JsonObject json, String key) throws JsonMissingKeyException {
		if (json.has(key)) {
			return getFloat(json, key);
		}
		throw new JsonMissingKeyException("missing key " + key + " in " + json);
	}

	public static String requiresString(JsonObject json, String key) throws JsonMissingKeyException {
		if (json.has(key)) {
			return getString(json, key);
		}
		throw new JsonMissingKeyException("missing key " + key + " in " + json);
	}

	public static JsonArray requiresArray(JsonObject json, String key) throws JsonMissingKeyException {
		if (json.has(key)) {
			return getArray(json, key);
		}
		throw new JsonMissingKeyException("missing key " + key + " in " + json);
	}

	public static boolean requiresBoolean(JsonObject json, String key) throws JsonMissingKeyException {
		if (json.has(key)) {
			return getBoolean(json, key);
		}
		throw new JsonMissingKeyException("missing key " + key + " in " + json);
	}
}
