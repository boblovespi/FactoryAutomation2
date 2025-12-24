package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.recipe.FryingPanRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import boblovespi.factoryautomation.common.util.jade.IJadeViewable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FryingPanBE extends FABE implements ITickable, IJadeViewable
{
	private final ItemStackHandler inv;
	private final FluidTank tank;
	private final RecipeManager<FryingPanRecipe> rm;
	private boolean isCooking;

	public FryingPanBE(BlockPos pos, BlockState blockState)
	{
		super(FABETypes.FRYING_PAN_TYPE.get(), pos, blockState);
		inv = new ItemStackHandler(5);
		tank = new FluidTank(250);
		rm = new RecipeManager<>("recipe", this::isValid, this::findMatchingRecipe, this::getRecipe);
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		rm.onLoad();
	}

	public void placeItem(ItemStack stack, Player player)
	{
		if (!inv.getStackInSlot(4).isEmpty())
			return;
		else
		{
			var result = FluidUtil.tryEmptyContainerAndStow(stack, tank, null, 250, player, true);
			if (result.isSuccess())
			{
				player.setItemInHand(player.swingingArm, result.getResult());
				rm.updateRecipe();
				setChangedAndUpdateClient();
				return;
			}
			else if (stack.is(Items.POTION) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER) && tank.isEmpty())
			{
				tank.fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);
				if (!player.getAbilities().instabuild)
				{
					if (stack.getCount() == 1)
						player.setItemInHand(player.swingingArm, Items.GLASS_BOTTLE.getDefaultInstance());
					else
					{
						stack.shrink(1);
						ItemHelper.putItemsInInventoryOrDrop(player, Items.GLASS_BOTTLE.getDefaultInstance(), level);
					}
				}
				rm.updateRecipe();
				setChangedAndUpdateClient();
				return;
			}
		}
		for (int i = 0; i < 4; i++)
		{
			if (!inv.getStackInSlot(i).isEmpty())
				continue;
			inv.insertItem(i, stack.split(1), false);
			rm.updateRecipe();
			setChangedAndUpdateClient();
			return;
		}
	}

	public ItemStack takeItem(ItemStack stack, Player player)
	{
		if (rm.hasRecipe() && (rm.getRecipe().getData().plate().test(stack) || rm.getRecipe().getData().plate().isEmpty()))
		{
			stack.shrink(1);
			var newStack = inv.extractItem(4, Item.ABSOLUTE_MAX_STACK_SIZE, false);
			rm.complete();
			setChangedAndUpdateClient();
			return newStack;
		}
		else if (stack.isEmpty() && !rm.hasRecipe())
		{
			for (int i = 3; i >= 0; i--)
			{
				if (inv.getStackInSlot(i).isEmpty())
					continue;
				var newStack = inv.extractItem(i, Item.ABSOLUTE_MAX_STACK_SIZE, false);
				setChangedAndUpdateClient();
				return newStack;
			}
		}
		if (stack.isEmpty() && player.isSecondaryUseActive() && !rm.hasRecipe())
		{
			tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
			setChangedAndUpdateClient();
		}
		return ItemStack.EMPTY;
	}

	public void takeOrPlace(ItemStack stack, Player player)
	{
		if (!inv.getStackInSlot(4).isEmpty() || stack.isEmpty())
		{
			var taken = takeItem(stack, player);
			ItemHelper.putItemsInInventoryOrDropAt(player, taken, level, Vec3.atLowerCornerWithOffset(worldPosition, 0.5f, 7 / 16f, 0.5f));
		}
		else
			placeItem(stack, player);
	}

	public ItemStack getRenderResult()
	{
		if (level.isClientSide)
			return inv.getStackInSlot(4);
		return ItemStack.EMPTY;
	}

	public List<ItemStack> getRenderStacks()
	{
		if (level.isClientSide)
			return getInputStacks();
		return Collections.singletonList(ItemStack.EMPTY);
	}

	public FluidStack getRenderFluid()
	{
		return level.isClientSide ? tank.getFluid() : FluidStack.EMPTY;
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("inv", inv.serializeNBT(registries));
		tank.writeToNBT(registries, tag);
		rm.save(tag);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		tank.readFromNBT(registries, tag);
		rm.load(tag, registries);
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		save(tag, registries);
		tag.putBoolean("isCooking", rm.hasRecipe() && inv.getStackInSlot(4).isEmpty());
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		load(tag, registries);
		isCooking = tag.getBoolean("isCooking");
	}

	@Override
	public void onDestroy()
	{
		ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
	}

	@Override
	public List<ItemStack> makeViewStacks()
	{
		return inv.getStackInSlot(4).isEmpty() ? getInputStacks() : List.of(inv.getStackInSlot(4));
	}

	@Override
	public void tick()
	{
		if (rm.hasRecipe() && inv.getStackInSlot(4).isEmpty())
		{
			rm.progress();
			if (rm.isComplete())
			{
				var result = rm.getCompleted();
				var assembled = result.value().assemble(getInput(), level.registryAccess());
				for (int i = 0; i < 4; i++)
					inv.extractItem(i, 1, false);
				inv.setStackInSlot(4, assembled.copy());
				tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
				setChangedAndUpdateClient();
			}
		}
		setChanged();
	}

	private Optional<RecipeHolder<?>> getRecipe(ResourceLocation location)
	{
		return level.getRecipeManager().byKey(location);
	}

	@Nullable
	private RecipeHolder<FryingPanRecipe> findMatchingRecipe()
	{
		return level.getRecipeManager().getRecipeFor(RecipeThings.FRYING_PAN_TYPE.get(), getInput(), level).orElse(null);
	}

	private FryingPanRecipe.Input getInput()
	{
		return new FryingPanRecipe.Input(getInputStacks(), tank.getFluid());
	}

	private boolean isValid(RecipeHolder<FryingPanRecipe> recipe)
	{
		return recipe.value().matches(getInput(), level);
	}

	private List<ItemStack> getInputStacks()
	{
		return List.of(inv.getStackInSlot(0), inv.getStackInSlot(1), inv.getStackInSlot(2), inv.getStackInSlot(3));
	}

	public boolean isCooking()
	{
		return level.isClientSide && isCooking;
	}
}
