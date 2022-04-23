package com.github.thundersphun.bcf.funnel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.BiomeKeys;

@Environment(EnvType.CLIENT)
public class FunnelBlockEntityRenderer implements BlockEntityRenderer<FunnelBlockEntity> {
	private static final float DEPTH = 3.25f / 16f;
	private static final float BOTTOM = 6f / 16f;
	private static final float TANK_HEIGHT = 6f / 16f;
	private static final float LEFT_START = 5f / 16f;
	private static final float LEFT_END = 7f / 16f;
	private static final float RIGHT_START = 9f / 16f;
	private static final float RIGHT_END = 11f / 16f;
	private static final float CENTER_CORNER_TANK = 7f / 16f;

	public FunnelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void render(FunnelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var renderer = RendererAccess.INSTANCE.getRenderer();
		var world = entity.getWorld();

		if (renderer == null || world == null) {
			return;
		}

		if (entity.getWaterFullness() > 0) {
			var waterColor = BuiltinRegistries.BIOME.get(BiomeKeys.OCEAN).getWaterColor();
			var r = (waterColor >> 0x10 & 0xff) / 256f;
			var g = (waterColor >> 0x8 & 0xff) / 256f;
			var b = (waterColor & 0xff) / 256f;

			var textures = FluidVariantRendering.getSprites(FluidVariant.of(Fluids.WATER));
			var top = entity.getWaterFullness() * TANK_HEIGHT + BOTTOM;
			this.renderTank(
					renderer, textures[1], textures[0], matrices,
					vertexConsumers, r, g, b, RIGHT_START, RIGHT_END, LEFT_START, LEFT_END, 1 - DEPTH,
					CENTER_CORNER_TANK, 1 - CENTER_CORNER_TANK, DEPTH, top, light, overlay, false);
		}
		if (entity.getLavaFullness() > 0) {
			var textures = FluidVariantRendering.getSprites(FluidVariant.of(Fluids.LAVA));
			var top = entity.getLavaFullness() * TANK_HEIGHT + BOTTOM;
			this.renderTank(
					renderer, textures[1], textures[0], matrices,
					vertexConsumers, 15, 15, 15 , LEFT_START, LEFT_END, RIGHT_START, RIGHT_END, DEPTH,
					DEPTH, CENTER_CORNER_TANK, CENTER_CORNER_TANK, top, light, overlay, true);
		}
	}

	private void renderTank(Renderer renderer, Sprite sideTexture, Sprite topTexture, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
							float r, float g, float b, float sideA1, float sideA2, float sideB1, float sideB2, float topX1,
							float topY1, float topX2, float topY2, float height, int light, int overlay, boolean emissive) {
		if (emissive) {
			light = LightmapTextureManager.pack(15, LightmapTextureManager.getSkyLightCoordinates(light));
		}
		vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(),
				getQuad(renderer, topTexture, Direction.UP, topX1, topY1,  topX2, topY2, 1 - height),
				new float[]{1.0F, 1.0F, 1.0F, 1.0F}, r, g, b, new int[]{light, light, light, light}, overlay, false);
		vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(),
				getQuad(renderer, topTexture, Direction.UP, 1 - topX1, 1 - topY1,  1 - topX2, 1 - topY2, 1 - height),
				new float[]{1.0F, 1.0F, 1.0F, 1.0F}, r, g, b, new int[]{light, light, light, light}, overlay, false);

		vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(),
				getQuad(renderer, sideTexture, Direction.NORTH, sideA1, BOTTOM, sideA2, height, DEPTH),
				new float[]{1.0F, 1.0F, 1.0F, 1.0F}, r, g, b, new int[]{light, light, light, light}, overlay, false);
		vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(),
				getQuad(renderer, sideTexture, Direction.SOUTH, sideA1, BOTTOM, sideA2, height, DEPTH),
				new float[]{1.0F, 1.0F, 1.0F, 1.0F}, r, g, b, new int[]{light, light, light, light}, overlay, false);

		vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(),
				getQuad(renderer, sideTexture, Direction.EAST, sideB1, BOTTOM, sideB2, height, DEPTH),
				new float[]{1.0F, 1.0F, 1.0F, 1.0F}, r, g, b, new int[]{light, light, light, light}, overlay, false);
		vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(),
				getQuad(renderer, sideTexture, Direction.WEST, sideB1, BOTTOM, sideB2, height, DEPTH),
				new float[]{1.0F, 1.0F, 1.0F, 1.0F}, r, g, b, new int[]{light, light, light, light}, overlay, false);
	}

	private BakedQuad getQuad(Renderer renderer, Sprite sprite, Direction direction, float x1, float y1, float x2, float y2, float z) {
		return renderer.meshBuilder().getEmitter()
				.square(direction, Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2), z)
				.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV)
				.toBakedQuad(0, sprite, false);
	}
}
