package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;

public class PowerShaftBE extends FABE implements IClientTickable, IPowerChainElement
{
	private final MechanicalManager manager;
	private float rot;
	@Nullable
	private Direction inputSide;
	@Nullable
	private IPowerChainElement source;
	@Nullable
	private BlockPos sourcePos;

	public PowerShaftBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.POWER_SHAFT_TYPE.get(), pPos, pBlockState);
		manager = new MechanicalManager("mech", Function.identity(), Function.identity(), this::updateInputs);
		inputSide = null;
		source = null;
		sourcePos = null;
	}

	public void updateInputs()
	{
		setChangedAndUpdateClient();
		if (inputSide != null)
		{
			var be = level.getBlockEntity(worldPosition.relative(inputSide.getOpposite()));
			if (be instanceof IPowerChainElement pce)
				pce.setSource(source == null ? this : source, inputSide);
			else
			{
				var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(inputSide.getOpposite()), null, be, inputSide);
				if (cap != null)
					cap.update(source == null ? manager : source.getManager());
			}
		}
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.save(tag);
		if (inputSide != null)
			tag.putInt("inputSide", inputSide.get3DDataValue());
		if (source != null)
			tag.put("sourcePos", NbtUtils.writeBlockPos(source.getPos()));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.load(tag);
		if (tag.contains("inputSide"))
			inputSide = Direction.from3DDataValue(tag.getInt("inputSide"));
		if (tag.contains("sourcePos"))
			NbtUtils.readBlockPos(tag, "sourcePos").ifPresent(p -> sourcePos = p);
		else
			sourcePos = null;
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		save(tag, registries);
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		load(tag, registries);
		if (level != null && level.isClientSide)
		{
			if (sourcePos != null)
			{
				var be = level.getBlockEntity(sourcePos);
				if (be instanceof IPowerChainElement pce)
					source = pce;
			}
			else
				source = null;
		}
	}

	@Override
	public void onDestroy()
	{
		if (inputSide == null)
			return;
		var be = level.getBlockEntity(worldPosition.relative(inputSide.getOpposite()));
		if (be instanceof IPowerChainElement pce)
			pce.notifyBroken(inputSide);
		else
		{
			var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(inputSide.getOpposite()), null, be, inputSide);
			if (cap != null)
				cap.update(MechanicalManager.ZERO);
		}
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		if (level != null && sourcePos != null)
		{
			var be = level.getBlockEntity(sourcePos);
			if (be instanceof IPowerChainElement pce)
				source = pce;
		}
	}

	@Nullable
	public IMechanicalInput input(Direction dir)
	{
		if (dir == inputSide || (dir.getAxis() == getBlockState().getValue(PowerShaft.AXIS) && inputSide == null))
		{
			if (inputSide == null)
			{
				inputSide = dir;
				invalidateCapabilities();
			}
			return manager;
		}
		return null;
	}

	@Nullable
	public IMechanicalOutput output(Direction dir)
	{
		if (dir.getOpposite() == inputSide)
			return source == null ? manager : source.getManager();
		return null;
	}

	public float getRenderRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		if (source != null)
			return source.getRotation(delta);
		return (rot + delta * (float) (Math.toDegrees(manager.getSpeed()) / 20)) % 360;
	}

	@Override
	public void clientTick()
	{
		rot += (float) (Math.toDegrees(manager.getSpeed()) / 20);
		rot %= 360;
	}

	@Override
	public IPowerChainElement setSource(IPowerChainElement source, Direction dir)
	{
		this.source = source;
		inputSide = dir;
		var cap = level.getCapability(MechanicalCapability.OUTPUT, worldPosition.relative(inputSide.getOpposite()), inputSide);
		if (cap != null)
			level.destroyBlock(worldPosition, true);
		else
		{
			updateInputs();
			invalidateCapabilities();
		}
		return this;
	}

	@Override
	public void notifyBroken(Direction brokenSide)
	{
		if (brokenSide != inputSide)
			return;
		source = null;
		var be = level.getBlockEntity(worldPosition.relative(this.inputSide.getOpposite()));
		if (be instanceof IPowerChainElement pce)
			pce.notifyBroken(brokenSide);
		this.inputSide = null;
		invalidateCapabilities();
		setChangedAndUpdateClient();
	}

	@Override
	public float getRotation(float delta)
	{
		if (source == null)
			return (rot + delta * (float) (Math.toDegrees(manager.getSpeed()) / 20)) % 360;
		return 0;
	}

	@Override
	public MechanicalManager getManager()
	{
		return manager;
	}

	@Override
	public BlockPos getPos()
	{
		return worldPosition;
	}
}
