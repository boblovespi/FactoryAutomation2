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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class GearboxBE extends FABE implements ITickable, IClientTickable
{
	private final MechanicalManager manager;
	private ItemStack inputStack;
	private ItemStack outputStack;
	private GearMaterial inputGear;
	private GearMaterial outputGear;

	public GearboxBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.GEARBOX_TYPE.get(), pPos, pBlockState);
		manager = new MechanicalManager("mech", this::multSpeed, this::multTorque, this::updateInputs);
		inputStack = ItemStack.EMPTY;
		outputStack = ItemStack.EMPTY;
		inputGear = GearMaterial.NONE;
		outputGear = GearMaterial.NONE;
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.save(tag);
		tag.put("inputStack", inputStack.saveOptional(registries));
		tag.put("outputStack", outputStack.saveOptional(registries));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.load(tag);
		inputStack = ItemStack.parseOptional(registries, tag.getCompound("inputStack"));
		inputGear = GearMaterial.fromStack(inputStack);
		outputStack = ItemStack.parseOptional(registries, tag.getCompound("outputStack"));
		outputGear = GearMaterial.fromStack(outputStack);
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

	}

	@Override
	public void clientTick()
	{

	}

	@Override
	public void tick()
	{

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
}