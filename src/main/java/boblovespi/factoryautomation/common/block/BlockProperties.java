package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.sound.FASoundTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlockProperties
{
	public static final BlockBehaviour.Properties ROCK = BlockBehaviour.Properties.of().strength(0.1f).replaceable().sound(FASoundTypes.ROCK);
	public static final BlockBehaviour.Properties LOG = LOG(MapColor.WOOD);
	public static final BlockBehaviour.Properties WOOD_MACHINE = BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.WOOD).mapColor(MapColor.WOOD);
	public static final BlockBehaviour.Properties WOOD_MACHINE_NO_OCCLUSION = BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.WOOD).mapColor(MapColor.WOOD).noOcclusion();

	public static BlockBehaviour.Properties LOG(MapColor c)
	{
		return BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.WOOD).ignitedByLava().mapColor(c);
	}

	public static final BlockBehaviour.Properties GREEN_SAND = BlockBehaviour.Properties.of().strength(0.6f).sound(SoundType.PACKED_MUD).mapColor(MapColor.GLOW_LICHEN);
	public static final BlockBehaviour.Properties CHARCOAL_PILE = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).sound(SoundType.GRAVEL).strength(0.5f)
																						   .requiresCorrectToolForDrops();
	public static final BlockBehaviour.Properties DRIED_BRICKS = BlockBehaviour.Properties.of().strength(1 ,3).sound(SoundType.PACKED_MUD).mapColor(MapColor.TERRACOTTA_ORANGE);

	public static final BlockBehaviour.Properties COBBLESTONE_MACHINE = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
																								 .requiresCorrectToolForDrops().strength(3.5F);

	public static BlockBehaviour.Properties LIGHT_METAL(MapColor c)
	{
		return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER).mapColor(c);
	}

	public static BlockBehaviour.Properties METAL(MapColor c)
	{
		return BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).mapColor(c);
	}

	public static BlockBehaviour.Properties SPACE_FRAME(BlockBehaviour.Properties p)
	{
		return p.isValidSpawn(Blocks::never).isRedstoneConductor(BlockProperties::never).isSuffocating(BlockProperties::never).isViewBlocking(BlockProperties::never).noOcclusion();
	}

	public static final BlockBehaviour.Properties ORE = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM)
																				 .requiresCorrectToolForDrops().strength(3.0F, 3.0F);

	public static BlockBehaviour.Properties RAW_ORE(MapColor c)
	{
		return BlockBehaviour.Properties.of().mapColor(c).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F);
	}

	public static final BlockBehaviour.Properties IRON_BLOOM = BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).strength(10, 6).requiresCorrectToolForDrops();

	private static boolean never(BlockState a, BlockGetter b, BlockPos c)
	{
		return false;
	}
}
