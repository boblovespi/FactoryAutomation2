package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.sound.FASoundTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class BlockProperties
{
	public static final BlockBehaviour.Properties ROCK = BlockBehaviour.Properties.of().destroyTime(0.1f).explosionResistance(0.1f).replaceable().sound(FASoundTypes.ROCK);
	public static final BlockBehaviour.Properties LOG = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(b -> MapColor.WOOD);
}
