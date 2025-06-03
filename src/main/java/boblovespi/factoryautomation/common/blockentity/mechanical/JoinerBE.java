package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.Joiner;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class JoinerBE extends FABE implements IClientTickable
{
	private final JoinerMechManager manager;
	private final float maxSpeed;
	private final float maxTorque;
	private float rot;

	public JoinerBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.JOINER_TYPE.get(), pPos, pBlockState);
		manager = new JoinerMechManager("mech", this::updateInputs, () -> level.destroyBlock(worldPosition, true));
		if (pBlockState.getBlock() instanceof Joiner joiner)
		{
			maxSpeed = joiner.maxSpeed;
			maxTorque = joiner.maxTorque;
		}
		else
			throw new RuntimeException("Joiner block entities must be for joiner block?!?!?");
	}

	public void updateInputs()
	{
		setChangedAndUpdateClient();
		if (manager.getSpeed() > maxSpeed || manager.getTorque() > maxTorque)
			level.destroyBlock(worldPosition, true);
		else
			updateWith(manager);
	}

	private void updateWith(IMechanicalOutput output)
	{
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(getOutputDirection()), null, null, getOutputDirection().getOpposite());
		if (cap != null)
			cap.update(output);
	}

	@Nullable
	public IMechanicalOutput output(@Nullable Direction direction)
	{
		if (getBlockState().getValue(Joiner.VERTICAL))
		{
			var facing = getBlockState().getValue(Joiner.FACING);
			return facing.getAxisDirection() == Direction.AxisDirection.POSITIVE && direction == Direction.DOWN ||
				   facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE && direction == Direction.UP ? manager : null;
		}
		else
			return direction.getOpposite() == getBlockState().getValue(Joiner.FACING) ? manager : null;
	}

	@Nullable
	public IMechanicalInput input(@Nullable Direction direction)
	{
		return direction == getInputLeft() ? manager::updateLeft : direction == getInputRight() ? manager::updateRight : null;
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.save(tag);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.load(tag);
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
	}

	@Override
	public void onDestroy()
	{
		updateWith(MechanicalManager.ZERO);
	}

	public float getRenderRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (rot + delta * (float) (Math.toDegrees(manager.getSpeed()) / 20)) % 360;
	}

	@Override
	public void clientTick()
	{
		rot += (float) (Math.toDegrees(manager.getSpeed()) / 20);
		rot %= 360;
	}

	private Direction getOutputDirection()
	{
		if (getBlockState().getValue(Joiner.VERTICAL))
			return getBlockState().getValue(Joiner.FACING).getAxisDirection() == Direction.AxisDirection.POSITIVE ? Direction.DOWN : Direction.UP;
		else
			return getBlockState().getValue(Joiner.FACING);
	}

	private Direction getInputLeft()
	{
		return getBlockState().getValue(Joiner.FACING).getCounterClockWise();
	}

	private Direction getInputRight()
	{
		return getBlockState().getValue(Joiner.FACING).getClockWise();
	}

	private static class JoinerMechManager implements IMechanicalOutput
	{
		private final String nbtId;
		private final MechanicalManager.Updater onUpdate;
		private final Runnable destroyer;
		private float speed;
		private float leftTorque;
		private float rightTorque;

		public void updateLeft(IMechanicalOutput input)
		{
			var iSpeed = input.getSpeed();
			var iTorque = input.getTorque();
			if (iSpeed == 0 && iTorque == 0)
			{
				leftTorque = iTorque;
				if (rightTorque == 0)
					speed = 0;
			}
			else if (rightTorque == 0)
			{
				leftTorque = iTorque;
				speed = iSpeed;
			}
			else if (iSpeed * 0.99 <= speed && speed * 0.99 <= iSpeed)
				leftTorque = iTorque;
			else
				destroyer.run();
		}

		public void updateRight(IMechanicalOutput input)
		{
			var iSpeed = input.getSpeed();
			var iTorque = input.getTorque();
			if (iSpeed == 0 && iTorque == 0)
			{
				rightTorque = iTorque;
				if (leftTorque == 0)
					speed = 0;
			}
			else if (leftTorque == 0)
			{
				rightTorque = iTorque;
				speed = iSpeed;
			}
			else if (iSpeed * 0.99 <= speed && speed * 0.99 <= iSpeed)
				rightTorque = iTorque;
			else
				destroyer.run();
		}

		public JoinerMechManager(String nbtId, MechanicalManager.Updater onUpdate, Runnable destroyer)
		{
			this.nbtId = nbtId;
			this.onUpdate = onUpdate;
			this.destroyer = destroyer;
		}

		@Override
		public float getSpeed()
		{
			return speed;
		}

		@Override
		public float getTorque()
		{
			return leftTorque + rightTorque;
		}

		public float getInputSpeed()
		{
			return speed;
		}

		public void save(CompoundTag tag)
		{
			var nbt = new CompoundTag();
			nbt.putFloat("speed", speed);
			nbt.putFloat("leftTorque", leftTorque);
			nbt.putFloat("rightTorque", rightTorque);
			tag.put(nbtId, nbt);
		}

		public void load(CompoundTag tag)
		{
			var nbt = tag.getCompound(nbtId);
			speed = nbt.getFloat("speed");
			leftTorque = nbt.getFloat("leftTorque");
			rightTorque = nbt.getFloat("rightTorque");
		}
	}
}
