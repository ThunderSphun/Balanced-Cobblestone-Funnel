package com.github.thundersphun.bcf.config;

import com.github.thundersphun.bcf.Bcf;
import com.github.thundersphun.bcf.funnel.FunnelData;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class ConfigGenerator {
	private ConfigGenerator() {
	}

	static void generateConfig() {
		Bcf.LOGGER.info("Generating Default Config");

		ConfigData.LAVA_RESULT.put(Registry.ITEM.getId(Items.LAVA_BUCKET), Registry.ITEM.getId(Items.BUCKET));
		ConfigData.WATER_RESULT.put(Registry.ITEM.getId(Items.WATER_BUCKET), Registry.ITEM.getId(Items.BUCKET));

		var simple = new FunnelData(60, -1, -1);
		var nether = new FunnelData(60, -1, 50);
		var overworldStones = new FunnelData(100, 250, 500);
		var netherStones = new FunnelData(100, 250, 125);
		var overworldSurface = new FunnelData(100, 1000, 1000);
		var cobblestone = new FunnelData(60, 100, -1);
		var copper = new FunnelData(30, 50, -1);
		var iron = new FunnelData(60, 100, -1);
		var gold = new FunnelData(45, 25, -1);
		var diamond = new FunnelData(30, 10, -1);
		var deepslate = new FunnelData(30, 10, 50);
		var netherite = new FunnelData(20, 5, 80);

		simple.getWeights().add(Registry.BLOCK.getId(Blocks.COBBLESTONE), 1);

		nether.getWeights().add(Registry.BLOCK.getId(Blocks.NETHERRACK), 4);
		nether.getWeights().add(Registry.BLOCK.getId(Blocks.NETHER_QUARTZ_ORE), 1);
		nether.getWeights().add(Registry.BLOCK.getId(Blocks.NETHER_GOLD_ORE), 1);

		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.COBBLESTONE), 14);
		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.STONE), 10);
		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.GRANITE), 8);
		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.DIORITE), 8);
		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.ANDESITE), 8);
		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.CALCITE), 1);
		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.TUFF), 2);
		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.DEEPSLATE), 4);
		overworldStones.getWeights().add(Registry.BLOCK.getId(Blocks.DRIPSTONE_BLOCK), 3);

		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.NETHERRACK), 4);
		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.CRIMSON_NYLIUM), 2);
		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.WARPED_NYLIUM), 2);
		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.SOUL_SAND), 2);
		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.SOUL_SOIL), 2);
		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.BASALT), 4);
		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.BLACKSTONE), 3);
		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.GILDED_BLACKSTONE), 1);
		netherStones.getWeights().add(Registry.BLOCK.getId(Blocks.GLOWSTONE), 2);

		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.CLAY), 5);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.GRAVEL), 8);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.RED_SAND), 2);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.SAND), 10);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.ROOTED_DIRT), 3);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.PODZOL), 6);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.COARSE_DIRT), 4);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.DIRT), 12);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.GRASS_BLOCK), 10);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.SNOW_BLOCK), 5);
		overworldSurface.getWeights().add(Registry.BLOCK.getId(Blocks.ICE), 3);

		cobblestone.getWeights().add(Registry.BLOCK.getId(Blocks.COBBLESTONE), 4);
		cobblestone.getWeights().add(Registry.BLOCK.getId(Blocks.STONE), 4);
		cobblestone.getWeights().add(Registry.BLOCK.getId(Blocks.COAL_ORE), 2);
		cobblestone.getWeights().add(Registry.BLOCK.getId(Blocks.COPPER_ORE), 1);
		cobblestone.getWeights().add(Registry.BLOCK.getId(Blocks.IRON_ORE), 1);

		copper.getWeights().add(Registry.BLOCK.getId(Blocks.COBBLESTONE), 6);
		copper.getWeights().add(Registry.BLOCK.getId(Blocks.STONE), 6);
		copper.getWeights().add(Registry.BLOCK.getId(Blocks.COAL_ORE), 4);
		copper.getWeights().add(Registry.BLOCK.getId(Blocks.COPPER_ORE), 3);
		copper.getWeights().add(Registry.BLOCK.getId(Blocks.IRON_ORE), 2);
		copper.getWeights().add(Registry.BLOCK.getId(Blocks.REDSTONE_ORE), 1);
		copper.getWeights().add(Registry.BLOCK.getId(Blocks.GOLD_ORE), 1);

		iron.getWeights().add(Registry.BLOCK.getId(Blocks.COBBLESTONE), 6);
		iron.getWeights().add(Registry.BLOCK.getId(Blocks.STONE), 6);
		iron.getWeights().add(Registry.BLOCK.getId(Blocks.COAL_ORE), 4);
		iron.getWeights().add(Registry.BLOCK.getId(Blocks.COPPER_ORE), 2);
		iron.getWeights().add(Registry.BLOCK.getId(Blocks.IRON_ORE), 3);
		iron.getWeights().add(Registry.BLOCK.getId(Blocks.REDSTONE_ORE), 1);
		iron.getWeights().add(Registry.BLOCK.getId(Blocks.GOLD_ORE), 1);

		gold.getWeights().add(Registry.BLOCK.getId(Blocks.COBBLESTONE), 10);
		gold.getWeights().add(Registry.BLOCK.getId(Blocks.STONE), 10);
		gold.getWeights().add(Registry.BLOCK.getId(Blocks.COAL_ORE), 6);
		gold.getWeights().add(Registry.BLOCK.getId(Blocks.COPPER_ORE), 4);
		gold.getWeights().add(Registry.BLOCK.getId(Blocks.IRON_ORE), 4);
		gold.getWeights().add(Registry.BLOCK.getId(Blocks.REDSTONE_ORE), 2);
		gold.getWeights().add(Registry.BLOCK.getId(Blocks.GOLD_ORE), 3);
		gold.getWeights().add(Registry.BLOCK.getId(Blocks.DIAMOND_ORE), 1);

		diamond.getWeights().add(Registry.BLOCK.getId(Blocks.COBBLESTONE), 5);
		diamond.getWeights().add(Registry.BLOCK.getId(Blocks.STONE), 5);
		diamond.getWeights().add(Registry.BLOCK.getId(Blocks.COAL_ORE), 3);
		diamond.getWeights().add(Registry.BLOCK.getId(Blocks.COPPER_ORE), 2);
		diamond.getWeights().add(Registry.BLOCK.getId(Blocks.IRON_ORE), 2);
		diamond.getWeights().add(Registry.BLOCK.getId(Blocks.REDSTONE_ORE), 2);
		diamond.getWeights().add(Registry.BLOCK.getId(Blocks.GOLD_ORE), 2);
		diamond.getWeights().add(Registry.BLOCK.getId(Blocks.DIAMOND_ORE), 2);

		deepslate.getWeights().add(Registry.BLOCK.getId(Blocks.COBBLED_DEEPSLATE), 5);
		deepslate.getWeights().add(Registry.BLOCK.getId(Blocks.DEEPSLATE), 5);
		deepslate.getWeights().add(Registry.BLOCK.getId(Blocks.DEEPSLATE_COAL_ORE), 3);
		deepslate.getWeights().add(Registry.BLOCK.getId(Blocks.DEEPSLATE_COPPER_ORE), 2);
		deepslate.getWeights().add(Registry.BLOCK.getId(Blocks.DEEPSLATE_IRON_ORE), 2);
		deepslate.getWeights().add(Registry.BLOCK.getId(Blocks.DEEPSLATE_REDSTONE_ORE), 2);
		deepslate.getWeights().add(Registry.BLOCK.getId(Blocks.DEEPSLATE_GOLD_ORE), 2);
		deepslate.getWeights().add(Registry.BLOCK.getId(Blocks.DEEPSLATE_DIAMOND_ORE), 2);

		netherite.getWeights().add(Registry.BLOCK.getId(Blocks.NETHER_GOLD_ORE), 12);
		netherite.getWeights().add(Registry.BLOCK.getId(Blocks.DIAMOND_ORE), 4);
		netherite.getWeights().add(Registry.BLOCK.getId(Blocks.ANCIENT_DEBRIS), 1);

		ConfigData.FUNNEL_TYPES.put("simple_funnel", simple);
		ConfigData.FUNNEL_TYPES.put("nether_funnel", nether);
		ConfigData.FUNNEL_TYPES.put("overworld_stones_funnel", overworldStones);
		ConfigData.FUNNEL_TYPES.put("nether_stones_funnel", netherStones);
		ConfigData.FUNNEL_TYPES.put("overworld_surface_funnel", overworldSurface);
		ConfigData.FUNNEL_TYPES.put("cobblestone_funnel", cobblestone);
		ConfigData.FUNNEL_TYPES.put("copper_funnel", copper);
		ConfigData.FUNNEL_TYPES.put("iron_funnel", iron);
		ConfigData.FUNNEL_TYPES.put("gold_funnel", gold);
		ConfigData.FUNNEL_TYPES.put("diamond_funnel", diamond);
		ConfigData.FUNNEL_TYPES.put("deepslate_funnel", deepslate);
		ConfigData.FUNNEL_TYPES.put("netherite_funnel", netherite);
	}
}
