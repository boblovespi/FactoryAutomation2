package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.sound.FASoundTypes;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlockProperties
{
	public static final BlockBehaviour.Properties ROCK = BlockBehaviour.Properties.of().strength(0.1f).replaceable().sound(FASoundTypes.ROCK);
	public static final BlockBehaviour.Properties LOG = LOG(MapColor.WOOD);

	public static BlockBehaviour.Properties LOG(MapColor c)
	{
		return BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.WOOD).ignitedByLava().mapColor(c);
	}

	public static final BlockBehaviour.Properties GREEN_SAND = BlockBehaviour.Properties.of().strength(0.6f).sound(SoundType.GRAVEL).mapColor(MapColor.GLOW_LICHEN);
	public static final BlockBehaviour.Properties CHARCOAL_PILE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).sound(SoundType.GRAVEL).strength(0.5f)
																						   .requiresCorrectToolForDrops();

	public static final BlockBehaviour.Properties COBBLESTONE_MACHINE = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
																								 .requiresCorrectToolForDrops().strength(3.5F);
}
