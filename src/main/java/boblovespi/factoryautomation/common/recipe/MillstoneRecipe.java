package boblovespi.factoryautomation.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class MillstoneRecipe extends SimpleRecipe<MillstoneRecipe.Input, TorqueSpeedData>
{
	private static final BuilderFactory<MillstoneRecipe, TorqueSpeedData, TorqueSpeedData.Builder<MillstoneRecipe>> BUILDER_FACTORY = new BuilderFactory<>("millstone",
			MillstoneRecipe::new, TorqueSpeedData.Builder::new);
	private final TorqueSpeedData data;

	public static Builder<MillstoneRecipe, TorqueSpeedData, TorqueSpeedData.Builder<MillstoneRecipe>> of(ItemStack stack)
	{
		return BUILDER_FACTORY.of(stack);
	}

	protected MillstoneRecipe(Ingredient input, ItemStack result, int progress, TorqueSpeedData data)
	{
		super(input, result, progress, RecipeThings.MILLSTONE_TYPE.get(), RecipeThings.MILLSTONE_SERIALIZER.get());
		this.data = data;
	}

	@Override
	protected boolean matchExtra(Input input, Level level)
	{
		return input.speed >= data.speed() && input.torque >= data.torque();
	}

	@Override
	protected TorqueSpeedData getData()
	{
		return data;
	}

	public static class Input extends SimpleRecipe.Input
	{
		private final float speed;
		private final float torque;

		public Input(ItemStack input, float speed, float torque)
		{
			super(input);
			this.speed = speed;
			this.torque = torque;
		}
	}
}
