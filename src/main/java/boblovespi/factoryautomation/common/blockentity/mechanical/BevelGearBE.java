package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.BevelGear;
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

public class BevelGearBE extends FABE implements IClientTickable, IPowerChainElement
{
	private final MechanicalManager manager;
	private float rot;
	@Nullable
	private Direction inputSide;
	@Nullable
	private IPowerChainElement source;
	@Nullable
	private BlockPos sourcePos;
	private final float maxSpeed;
	private final float maxTorque;

	public BevelGearBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.BEVEL_GEAR_TYPE.get(), pPos, pBlockState);
		manager = new MechanicalManager("mech", Function.identity(), Function.identity(), this::updateInputs);
		inputSide = null;
		source = null;
		sourcePos = null;
		if (pBlockState.getBlock() instanceof BevelGear ps)
		{
			maxSpeed = ps.maxSpeed;
			maxTorque = ps.maxTorque;
		}
		else
			throw new RuntimeException("Bevel gear block entities must be for bevel gear block?!?!?");
	}

	public void updateInputs()
	{
		setChangedAndUpdateClient();
		if (manager.getSpeed() > maxSpeed || manager.getTorque() > maxTorque)
			level.destroyBlock(worldPosition, true);
		if (inputSide != null)
		{
			var outputSide = getOutputSide();
			var be = level.getBlockEntity(worldPosition.relative(outputSide));
			if (be instanceof IPowerChainElement pce)
				pce.setSource(source == null ? this : source, outputSide.getOpposite());
			else
			{
				var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(outputSide), null, be, outputSide.getOpposite());
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
		var outputSide = getOutputSide();
		var be = level.getBlockEntity(worldPosition.relative(outputSide));
		if (be instanceof IPowerChainElement pce)
			pce.notifyBroken(outputSide.getOpposite());
		else
		{
			var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(outputSide), null, be, outputSide.getOpposite());
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
	public IMechanicalInput input(@Nullable Direction dir)
	{
		var orientation = getBlockState().getValue(BevelGear.ORIENTATION);
		var front = orientation.front();
		var top = front.getAxis() == Direction.Axis.Y ? orientation.top() : front.getCounterClockWise();
		if (dir == inputSide ||
			((top == dir || front == dir) && inputSide == null))
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
	public IMechanicalOutput output(@Nullable Direction dir)
	{
		if (inputSide != null && dir == getOutputSide())
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
		var orientation = getBlockState().getValue(BevelGear.ORIENTATION);
		if (orientation.top() != dir && orientation.front() != dir)
			return null;
		this.source = source;
		inputSide = dir;
		var outputSide = getOutputSide();
		var cap = level.getCapability(MechanicalCapability.OUTPUT, worldPosition.relative(outputSide), outputSide.getOpposite());
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
		var outputSide = getOutputSide();
		var be = level.getBlockEntity(worldPosition.relative(outputSide));
		if (be instanceof IPowerChainElement pce)
			pce.notifyBroken(outputSide.getOpposite());
		else
			updateInputs();
		inputSide = null;
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

	private Direction getOutputSide()
	{
		assert inputSide != null : "Called getOutputSide without checking if inputSide is null";
		var orientation = getBlockState().getValue(BevelGear.ORIENTATION);
		if (inputSide.getAxis() == Direction.Axis.Y)
		{
			return orientation.top();
		}
		else
		{
			if (inputSide == orientation.front())
				return inputSide.getCounterClockWise();
			else
				return orientation.front();
		}
	}
}

