package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.StoneCastingVesselBE;
import boblovespi.factoryautomation.common.util.Form;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StoneCastingVessel extends Block implements EntityBlock
{
	public static final EnumProperty<CastingVesselStates> MOLD = EnumProperty.create("mold", CastingVesselStates.class);
	private static final VoxelShape BOUNDING_BOX_NO_SAND = Shapes.or(Block.box(1, 0, 1, 15, 1, 15), Block.box(0, 0, 1, 1, 8, 15),
			Block.box(15, 0, 1, 16, 8, 15), Block.box(1, 0, 0, 15, 8, 1), Block.box(1, 0, 15, 15, 8, 16));
	private static final VoxelShape BOUNDING_BOX_SAND = Shapes.or(BOUNDING_BOX_NO_SAND, Block.box(1, 0, 1, 15, 7, 15));

	public StoneCastingVessel(Properties p)
	{
		super(p);
		registerDefaultState(defaultBlockState().setValue(MOLD, CastingVesselStates.EMPTY));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MOLD);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		if (state.getValue(MOLD) == CastingVesselStates.EMPTY)
			return BOUNDING_BOX_NO_SAND;
		return BOUNDING_BOX_SAND;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new StoneCastingVesselBE(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide || pState.getValue(MOLD) == CastingVesselStates.EMPTY || pState.getValue(MOLD) == CastingVesselStates.SAND)
			return null;
		return ITickable.makeTicker(FABETypes.STONE_CASTING_VESSEL_TYPE.get(), beType);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHitResult)
	{
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;
		if (stack.is(FABlocks.GREEN_SAND.asItem()) && state.getValue(MOLD) == CastingVesselStates.EMPTY)
			level.setBlockAndUpdate(pos, state.setValue(MOLD, CastingVesselStates.SAND));
		else
			level.getBlockEntity(pos, FABETypes.STONE_CASTING_VESSEL_TYPE.get()).ifPresent(b -> b.takeItem(player));
		return ItemInteractionResult.CONSUME;
	}

	public enum CastingVesselStates implements StringRepresentable
	{
		EMPTY(Form.NONE),
		SAND(Form.NONE),
		INGOT(Form.INGOT),
		NUGGET(Form.NUGGET),
		SHEET(Form.SHEET),
		// COIN(Form.COIN),
		ROD(Form.ROD),
		GEAR(Form.GEAR);
		public final Form metalForm;

		CastingVesselStates(Form metalForm)
		{
			this.metalForm = metalForm;
		}

		@Override
		public String getSerializedName()
		{
			return name().toLowerCase();
		}

		@Override
		public String toString()
		{
			return getSerializedName();
		}
	}
}
