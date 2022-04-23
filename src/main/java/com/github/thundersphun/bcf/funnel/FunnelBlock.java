package com.github.thundersphun.bcf.funnel;

import com.github.thundersphun.bcf.Bcf;
import com.github.thundersphun.bcf.config.ConfigData;
import com.google.common.hash.Funnel;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class FunnelBlock extends BlockWithEntity {
	private final static VoxelShape SHAPE = generateShape();
	private final FunnelData data;

	public FunnelBlock(Settings settings, FunnelData data) {
		super(settings);
		this.data = data;
	}

	private static VoxelShape generateShape() {
		var foot = Block.createCuboidShape(0, 0, 0, 16, 1, 16);
		var wallX = Block.createCuboidShape(3, 1, 2, 13, 14, 14);
		var wallZ = Block.createCuboidShape(2, 1, 3, 14, 14, 13);
		var center = Block.createCuboidShape(3, 14, 3, 13, 15, 13);
		var top = Block.createCuboidShape(4, 15, 4, 12, 16, 12);

		return VoxelShapes.union(foot, wallX, wallZ, center, top).simplify();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return 0; // calculated via mixin
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);

		if (world.getBlockEntity(pos) instanceof FunnelBlockEntity blockEntity
				&& world.getBlockState(pos.down()).isAir() && blockEntity.getWaterFullness() > 0
				&& blockEntity.getLavaFullness() > 0) {
			world.setBlockState(pos.down(), Registry.BLOCK.get(this.data.getWeights().getRandom()).getDefaultState());
			blockEntity.use();
			world.updateComparators(pos, this);
			if (!world.isClient) {
				world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
				//blockEntity.sync();
			}
		}
		//world.getBlockTickScheduler().schedule(pos, this, this.data.getSpeed());
		world.getBlockTickScheduler().scheduleTick(new OrderedTick(this, pos, (long)this.data.getSpeed(), 1));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		//world.getBlockTickScheduler().schedule(pos, this, this.data.getSpeed());
		world.getBlockTickScheduler().scheduleTick(new OrderedTick(this, pos, this.data.getSpeed(), 1));
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		if (world != null) {
			super.appendTooltip(stack, world, tooltip, options);

			var id = this.data.getWeights().sort().get(0).getElement();
			var gameOptions = MinecraftClient.getInstance().options;

			tooltip.add(new TranslatableText("util.bcf.blocks_per_second", this.data.getSpeed() / 20f)
					.formatted(Formatting.GRAY));
			tooltip.add(new TranslatableText("util.bcf.best_block",
					Registry.BLOCK.get(id).getName()
							.formatted(Formatting.AQUA, Formatting.BOLD))
					.formatted(Formatting.GRAY));
			tooltip.add(new TranslatableText("util.bcf.extra_info",
					new TranslatableText(gameOptions.useKey.getBoundKeyTranslationKey())
							.formatted(Formatting.AQUA, Formatting.BOLD),
					new TranslatableText(gameOptions.useKey.getBoundKeyTranslationKey())
							.formatted(Formatting.AQUA, Formatting.BOLD)
			).formatted(Formatting.GRAY));
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.isSneaking() && hand == Hand.MAIN_HAND && world.isClient) {
			this.showPossibleOutputs(world, pos, player);
		}
		if (!player.isSneaking() && world.getBlockEntity(pos) instanceof FunnelBlockEntity blockEntity) {
			if (attemptFillFrom(blockEntity, world, pos, player, hand)) {
				return ActionResult.SUCCESS;
			}
			var id = Registry.ITEM.getId(player.getStackInHand(hand).getItem());
			if (ConfigData.LAVA_RESULT.containsKey(id) || ConfigData.WATER_RESULT.containsKey(id)) {
				return ActionResult.FAIL;
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	private void showPossibleOutputs(World world, BlockPos pos, PlayerEntity player) {
		if (world.getBlockEntity(pos) instanceof FunnelBlockEntity funnel) {
			if (world.getTime() - funnel.getInteractionTime() > 20) {
				var text = new TranslatableText("util.bcf.funnel_output");
				this.data.getWeights().stream().forEach(e -> text.append(new LiteralText("\n  "))
						.append(Registry.BLOCK.get(e).getName()));

				player.sendMessage(text, false);
				funnel.setInteractionTime(world.getTime());
			}
		}
	}

	private boolean attemptFillFrom(FunnelBlockEntity blockEntity, World world, BlockPos pos, PlayerEntity player, Hand hand) {
		var hasFilledFunnel = false;
		var replacementStack = new ItemStack(Items.AIR);
		var stat = new Identifier("");

		var itemStack = player.getStackInHand(hand);
		var itemId = Registry.ITEM.getId(itemStack.getItem());

		if (canFillLava(blockEntity, itemId)) {
			blockEntity.fillLava();

			hasFilledFunnel = true;
			replacementStack = new ItemStack(Registry.ITEM.get(ConfigData.LAVA_RESULT.get(itemId)));
			stat = Bcf.STAT_FILL_LAVA;
		} else if (canFillWater(blockEntity, itemId)) {
			blockEntity.fillWater();

			hasFilledFunnel = true;
			replacementStack = new ItemStack(Registry.ITEM.get(ConfigData.WATER_RESULT.get(itemId)));
			stat = Bcf.STAT_FILL_WATER;
		}

		if (hasFilledFunnel) {
			world.updateComparators(pos, this);
			if (!world.isClient) {
				//blockEntity.sync();
			}

			player.setStackInHand(hand, ItemUsage.exchangeStack(itemStack, player, replacementStack));
			player.increaseStat(Stats.CUSTOM.getOrCreateStat(stat, StatFormatter.DEFAULT), 1);
			return true;
		}

		return false;
	}

	private boolean canFillWater(FunnelBlockEntity blockEntity, Identifier id) {
		return ConfigData.WATER_RESULT.containsKey(id) && blockEntity.getWaterFullness() == 0;
	}

	private boolean canFillLava(FunnelBlockEntity blockEntity, Identifier id) {
		return ConfigData.LAVA_RESULT.containsKey(id) && blockEntity.getLavaFullness() == 0;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FunnelBlockEntity(pos, state, this.data.getLavaUses(), this.data.getWaterUses());
	}
}
