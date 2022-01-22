package com.github.thundersphun.bcf;

import com.github.thundersphun.bcf.config.ConfigData;
import com.github.thundersphun.bcf.config.ConfigLoader;
import com.github.thundersphun.bcf.funnel.FunnelBlock;
import com.github.thundersphun.bcf.funnel.FunnelBlockEntity;
import com.github.thundersphun.bcf.funnel.FunnelBlockEntityRenderer;
import com.github.thundersphun.bcf.funnel.FunnelData;
import com.github.thundersphun.bcf.mixin.DispenserBlockAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Bcf implements ModInitializer, ClientModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Balanced Cobblestone Funnel");
	public static final String ID = "bcf";
	private static final List<Block> BLOCKS = new ArrayList<>();
	public static BlockEntityType<FunnelBlockEntity> FUNNEL_BLOCK_ENTITY;
	public static Item TAB_ICON;
	public static ItemGroup TAB;
	public static Identifier STAT_FILL_LAVA;
	public static Identifier STAT_FILL_WATER;

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	@Override
	public void onInitialize() {
		TAB_ICON = Registry.register(Registry.ITEM, id("icon"), new Item(new FabricItemSettings().group(ItemGroup.HOTBAR).rarity(Rarity.EPIC)));

		ConfigLoader.loadJson();

		var funnels = ConfigData.FUNNEL_TYPES;

		LOGGER.info("loaded funnels: " + funnels.keySet());

		if (funnels.size() > 0) {
			TAB = FabricItemGroupBuilder.build(id("tab"), () -> new ItemStack(TAB_ICON));

			funnels.forEach((key, value) -> BLOCKS.add(this.registerFunnel(key, value)));

			FUNNEL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("funnel_block_entity"),
					FabricBlockEntityTypeBuilder.create((pos, state) -> new FunnelBlockEntity(pos, state, 0, 0),
							BLOCKS.toArray(new Block[0])).build());

			STAT_FILL_LAVA = Registry.register(Registry.CUSTOM_STAT, id("fill_with_lava"), id("fill_with_lava"));
			STAT_FILL_WATER = Registry.register(Registry.CUSTOM_STAT, id("fill_with_water"), id("fill_with_water"));

			ConfigData.LAVA_RESULT.entrySet().parallelStream().forEach(entry -> setDispenserBehavior(entry.getKey(), entry.getValue()));
			ConfigData.WATER_RESULT.entrySet().parallelStream().forEach(entry -> setDispenserBehavior(entry.getKey(), entry.getValue()));
		} else {
			LOGGER.warn("There are no funnels loaded, is this caused by a malformed config perhaps?");
		}
	}

	private void setDispenserBehavior(Identifier input, Identifier output) {
		var inputItem = Registry.ITEM.get(input);
		var outputItem = Registry.ITEM.get(output);
		var behavior = DispenserBlockAccessor.getBehavior();
		if (behavior.containsKey(inputItem)) {
			behavior.put(inputItem, new FillFunnelDispenserBehavior(outputItem, behavior.get(inputItem)));
		} else {
			behavior.put(inputItem, new FillFunnelDispenserBehavior(outputItem));
		}
	}

	private Block registerFunnel(String id, FunnelData data) {
		FunnelBlock block = Registry.register(Registry.BLOCK, id(id),
				new FunnelBlock(AbstractBlock.Settings.of(Material.STONE).ticksRandomly().hardness(1.5f)
						.resistance(0).nonOpaque(), data));

		Registry.register(Registry.ITEM, id(id), new BlockItem(block, new Item.Settings().group(TAB)));

		return block;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(FUNNEL_BLOCK_ENTITY, FunnelBlockEntityRenderer::new);

		BLOCKS.forEach(e -> BlockRenderLayerMap.INSTANCE.putBlock(e, RenderLayer.getCutout()));
	}
}
