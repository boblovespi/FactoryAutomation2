package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.sound.FASoundTypes;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockProperties
{
	public static final BlockBehaviour.Properties ROCK = BlockBehaviour.Properties.of().destroyTime(0.1f).explosionResistance(0.1f).replaceable().sound(FASoundTypes.ROCK);
}
