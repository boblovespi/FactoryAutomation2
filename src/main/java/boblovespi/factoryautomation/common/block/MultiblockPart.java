package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.MultiblockPartBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

public class MultiblockPart extends Block implements EntityBlock
{
	public MultiblockPart(Properties p)
	{
		super(p);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new MultiblockPartBE(pPos, pState);
	}

	@Override
	protected void onRemove(BlockState pState, Level level, BlockPos pos, BlockState pNewState, boolean pMovedByPiston)
	{
		level.getBlockEntity(pos, FABETypes.MULTIBLOCK_PART_TYPE.get()).ifPresent(MultiblockPartBE::onDestroy);
		super.onRemove(pState, level, pos, pNewState, pMovedByPiston);
	}

	@Override
	protected VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos)
	{
		return Shapes.empty();
	}

	@Override
	public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity)
	{
		return super.getSoundType(level.getBlockEntity(pos, FABETypes.MULTIBLOCK_PART_TYPE.get()).map(MultiblockPartBE::getMultiblockState).orElse(state), level, pos, entity);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState pState, Player player)
	{
		var optional = level.getBlockEntity(pos, FABETypes.MULTIBLOCK_PART_TYPE.get());
		if (player.isCreative() || optional.isEmpty())
			return super.playerWillDestroy(level, pos, pState, player);
		var storedState = optional.get().getMultiblockState();
		if (storedState.canHarvestBlock(level, pos, player))
			Block.dropResources(storedState, level, pos, null, player, player.getMainHandItem());
		return super.playerWillDestroy(level, pos, pState, player);
	}

	@Override
	protected RenderShape getRenderShape(BlockState pState)
	{
		return RenderShape.INVISIBLE;
	}

	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player)
	{
		return level.getBlockEntity(pos, FABETypes.MULTIBLOCK_PART_TYPE.get()).map(b -> b.getMultiblockState().canHarvestBlock(level, pos, player)).orElse(false);
	}

	@Override
	public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemStack tool)
	{
		return super.getExpDrop(level.getBlockEntity(pos, FABETypes.MULTIBLOCK_PART_TYPE.get()).map(MultiblockPartBE::getMultiblockState).orElse(state), level, pos, blockEntity, breaker, tool);
	}

	@Override
	protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos)
	{
		var optional = level.getBlockEntity(pos, FABETypes.MULTIBLOCK_PART_TYPE.get());
		if (optional.isEmpty())
			return super.getDestroyProgress(state, player, level, pos);
		var be = optional.get();
		float f = be.getMultiblockState().getDestroySpeed(level, pos);
		if (f == -1.0F)
			return 0.0F;
		else
		{
			int i = EventHooks.doPlayerHarvestCheck(player, be.getMultiblockState(), level, pos) ? 30 : 100;
			return player.getDigSpeed(be.getMultiblockState(), pos) / f / (float) i;
		}
	}

	@Override
	protected void spawnDestroyParticles(Level level, Player pPlayer, BlockPos pos, BlockState pState)
	{
		super.spawnDestroyParticles(level, pPlayer, pos, level.getBlockEntity(pos, FABETypes.MULTIBLOCK_PART_TYPE.get()).map(MultiblockPartBE::getMultiblockState).orElse(pState));
	}
}
