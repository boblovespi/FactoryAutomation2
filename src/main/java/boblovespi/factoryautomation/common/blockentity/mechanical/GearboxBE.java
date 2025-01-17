package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.util.GearMaterial;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class GearboxBE extends FABE implements ITickable, IClientTickable
{
	private final MechanicalManager manager;
	private final float damageChance;
	private ItemStack inputStack;
	private ItemStack outputStack;
	private GearMaterial inputGear;
	private GearMaterial outputGear;
	private int counter;
	private float inRot;
	private float outRot;

	public GearboxBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.GEARBOX_TYPE.get(), pPos, pBlockState);
		manager = new MechanicalManager("mech", this::multSpeed, this::multTorque, this::updateInputs);
		inputStack = ItemStack.EMPTY;
		outputStack = ItemStack.EMPTY;
		inputGear = GearMaterial.NONE;
		outputGear = GearMaterial.NONE;
		if (pBlockState.getBlock() instanceof Gearbox gearbox)
			damageChance = gearbox.damageChance;
		else
			throw new RuntimeException("Gearbox block entities must be for gearbox block?!?!?");
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.save(tag);
		tag.put("inputStack", inputStack.saveOptional(registries));
		tag.put("outputStack", outputStack.saveOptional(registries));
		tag.putInt("counter", counter);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.load(tag);
		inputStack = ItemStack.parseOptional(registries, tag.getCompound("inputStack"));
		inputGear = GearMaterial.fromStack(inputStack);
		outputStack = ItemStack.parseOptional(registries, tag.getCompound("outputStack"));
		outputGear = GearMaterial.fromStack(outputStack);
		counter = tag.getInt("counter");
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
		ItemHelper.dropItem(level, worldPosition.getCenter(), inputStack);
		ItemHelper.dropItem(level, worldPosition.getCenter(), outputStack);
		var facing = getBlockState().getValue(Gearbox.FACING);
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(facing), null, null, facing.getOpposite());
		if (cap != null)
			cap.update(MechanicalManager.ZERO);
	}

	@Override
	public void clientTick()
	{
		inRot += (float) (Math.toDegrees(manager.getInputSpeed()) / 20);
		inRot %= 360;
		outRot += (float) (Math.toDegrees(manager.getSpeed()) / 20);
		outRot %= 360;
	}

	@Override
	public void tick()
	{
		if (inputGear != GearMaterial.NONE && outputGear != GearMaterial.NONE && manager.getSpeed() > 0)
		{
			counter++;
			if (counter >= 100)
			{
				counter = 0;
				if (damageChance >= 0.999 || level.random.nextFloat() <= damageChance)
				{
					inputStack.hurtAndBreak(1, (ServerLevel) level, null, u -> inputGear = GearMaterial.NONE);
					outputStack.hurtAndBreak(1, (ServerLevel) level, null, u -> outputGear = GearMaterial.NONE);
				}
				updateInputs();
			}
		}
	}

	public void updateInputs()
	{
		setChangedAndUpdateClient();
		var facing = getBlockState().getValue(Gearbox.FACING);
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(facing), null, null, facing.getOpposite());
		if (cap != null)
			cap.update(manager);
	}

	public void takeOrPlace(ItemStack stack, Player player)
	{
		// put input, put output, take output, take input
		var gear = GearMaterial.fromStack(stack);
		var changed = false;
		if (gear != GearMaterial.NONE && inputStack.isEmpty())
		{
			inputStack = stack.split(1);
			inputGear = gear;
			changed = true;
		}
		else if (gear != GearMaterial.NONE && outputStack.isEmpty())
		{
			outputStack = stack.split(1);
			outputGear = gear;
			changed = true;
		}
		else if (!outputStack.isEmpty())
		{
			ItemHelper.putItemsInInventoryOrDropAt(player, outputStack, level, Vec3.atLowerCornerWithOffset(worldPosition, 0.5f, 7 / 16f, 0.5f));
			outputStack = ItemStack.EMPTY;
			outputGear = GearMaterial.NONE;
			changed = true;
		}
		else if (!inputStack.isEmpty())
		{
			ItemHelper.putItemsInInventoryOrDropAt(player, inputStack, level, Vec3.atLowerCornerWithOffset(worldPosition, 0.5f, 7 / 16f, 0.5f));
			inputStack = ItemStack.EMPTY;
			inputGear = GearMaterial.NONE;
			changed = true;
		}
		if (changed)
			updateInputs();
	}

	private float multSpeed(float in)
	{
		if (inputGear == GearMaterial.NONE || outputGear == GearMaterial.NONE)
			return 0;
		return in * inputGear.getScaleFactor() / outputGear.getScaleFactor();
	}

	private float multTorque(float in)
	{
		if (inputGear == GearMaterial.NONE || outputGear == GearMaterial.NONE)
			return 0;
		return in * outputGear.getScaleFactor() / inputGear.getScaleFactor();
	}

	@Nullable
	public IMechanicalOutput output(Direction direction)
	{
		return direction == getBlockState().getValue(Gearbox.FACING) ? manager : null;
	}

	@Nullable
	public IMechanicalInput input(Direction direction)
	{
		return direction.getOpposite() == getBlockState().getValue(Gearbox.FACING) ? manager : null;
	}

	public ItemStack getInputGear()
	{
		if (level.isClientSide)
			return inputStack;
		return ItemStack.EMPTY;
	}

	public ItemStack getOutputGear()
	{
		if (level.isClientSide)
			return outputStack;
		return ItemStack.EMPTY;
	}

	public float getInputScale()
	{
		if (level.isClientSide)
			return inputGear == GearMaterial.NONE ? 1 : inputGear.getScaleFactor() / (inputGear.getScaleFactor() + outputGear.getScaleFactor());
		return 1;
	}

	public float getOutputScale()
	{
		if (level.isClientSide)
			return outputGear == GearMaterial.NONE ? 1 : outputGear.getScaleFactor() / (inputGear.getScaleFactor() + outputGear.getScaleFactor());
		return 1;
	}

	public float getRenderInRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (inRot + delta * (float) (Math.toDegrees(manager.getInputSpeed()) / 20)) % 360;
	}

	public float getRenderOutRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (outRot + delta * (float) (Math.toDegrees(manager.getSpeed()) / 20)) % 360;
	}

	public float getInputRatio()
	{
		if (!level.isClientSide)
			return 1;
		return inputGear.getScaleFactor();
	}

	public float getOutputRatio()
	{
		if (!level.isClientSide)
			return 1;
		return outputGear.getScaleFactor();
	}
}
