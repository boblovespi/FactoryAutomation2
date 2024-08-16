package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.client.gui.StoneFoundryMenu;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.StoneCrucibleBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.Metal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StoneCrucible extends Block implements EntityBlock
{
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape BOUNDING_BOX = Block.box(2, 0, 2, 14, 16, 14);

	public StoneCrucible(Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(MULTIBLOCK_COMPLETE, false).setValue(FACING, Direction.NORTH));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult pHitResult)
	{
		if (!level.isClientSide)
		{
			var be = level.getBlockEntity(pos, FABETypes.STONE_CRUCIBLE_TYPE.get()).orElseThrow();
			if (state.getValue(MULTIBLOCK_COMPLETE))
			{
				var metal = Metal.fromStack(player.getMainHandItem());
				if (metal != Metal.UNKNOWN)
				{
					var stack = player.getMainHandItem().consumeAndReturn(1, player);
					be.addMetal(metal, Form.fromStack(stack).amount());
				}
				else if (player.getMainHandItem().is(Items.COAL))
				{
					be.addCoal(player.getMainHandItem().consumeAndReturn(1, player));
				}
				else
				{ // TODO: temporary; replace with casting vessel
					player.openMenu(state.getMenuProvider(level, pos));
					/*var stack = be.pour();
					ItemHelper.putItemsInInventoryOrDropAt(player, stack, level, pos.getCenter());*/
				}
			}
			else if (Multiblocks.STONE_CRUCIBLE.isValid(level, pos, state.getValue(FACING)))
			{
				Multiblocks.STONE_CRUCIBLE.build(level, pos, state.getValue(FACING));
				level.setBlock(pos, state.setValue(MULTIBLOCK_COMPLETE, true), 2);
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.STONE_CRUCIBLE_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new StoneCrucibleBE(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide || !pState.getValue(MULTIBLOCK_COMPLETE))
			return null;
		return ITickable.makeTicker(FABETypes.STONE_CRUCIBLE_TYPE.get(), beType);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MULTIBLOCK_COMPLETE, FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getCounterClockWise());
	}

	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos)
	{
		return new SimpleMenuProvider((a, b, c) -> new StoneFoundryMenu(a, b,pLevel.getBlockEntity(pPos, FABETypes.STONE_CRUCIBLE_TYPE.get()).orElseThrow().getInv(), ContainerLevelAccess.create(pLevel, pPos)), Component.literal("REPLACE ME"));
	}
}
