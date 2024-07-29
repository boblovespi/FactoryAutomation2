package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.BlockProperties;
import boblovespi.factoryautomation.common.item.tool.Tools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbility;

public class Rock extends Block
{
	private static final VoxelShape BOUNDING_BOX = Block.box(3, 0, 3, 13, 5, 13);
	public final Variants variant;

	public Rock(Variants variant)
	{
		super(BlockProperties.ROCK);
		this.variant = variant;
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return BOUNDING_BOX;
	}

	@Override
	protected BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos)
	{
		return pDirection == Direction.DOWN && !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState() : pState;
	}

	@Override
	protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos)
	{
		return pLevel.getBlockState(pPos.below()).isFaceSturdy(pLevel, pPos.below(), Direction.UP);
	}

	public enum Variants implements StringRepresentable
	{
		COBBLESTONE("cobblestone"),
		STONE("stone"),
		ANDESITE("andesite"),
		DIORITE("diorite"),
		GRANITE("granite"),
		TUFF("tuff"),
		CALCITE("calcite"),
		SANDSTONE("sandstone"),
		MOSSY_COBBLESTONE("mossy_cobblestone"),
		TERRACOTTA("terracotta");

		private final String name;

		Variants(String name)
		{
			this.name = name;
		}

		@Override
		public String getSerializedName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}

		public String getRockName()
		{
			return name + "_rock";
		}
	}

	public static class Item extends BlockItem
	{
		public Item(Block pBlock, Properties pProperties)
		{
			super(pBlock, pProperties);
		}

		@Override
		public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility)
		{
			return itemAbility == Tools.MAKE_CHOPPING_BLOCK;
		}
	}
}
