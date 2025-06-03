package boblovespi.factoryautomation;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = FactoryAutomation.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
	private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

	// private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = COMMON_BUILDER.comment("Whether to log the dirt block on common setup").define("logDirtBlock", true);
	//
	// private static final ModConfigSpec.IntValue MAGIC_NUMBER = COMMON_BUILDER.comment("A magic number").defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);
	//
	// public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = COMMON_BUILDER.comment("What you want the introduction message to be for the magic number")
	// 																								.define("magicNumberIntroduction", "The magic number is... ");
	//
	// // a list of strings that are treated as resource locations for items
	// private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = COMMON_BUILDER.comment("A list of items to log on common setup.")
	// 																									.defineListAllowEmpty("items", List.of("minecraft:iron_ingot"),
	// 																											Config::validateItemName);

	static final ModConfigSpec COMMON_SPEC = COMMON_BUILDER.build();

	private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
	private static final ModConfigSpec.DoubleValue PAPER_BELLOWS_BLOW_TIME = SERVER_BUILDER.comment("How long paper bellows blow for, in seconds.")
																						   .defineInRange("paper_bellows_blow_time", 20, 0.5, 120);
	private static final ModConfigSpec.DoubleValue LEATHER_BELLOWS_BLOW_TIME = SERVER_BUILDER.comment("How long leather bellows blow for at 2.4 rad/s, in seconds.")
																							 .defineInRange("leather_bellows_blow_time", 5, 0.5, 120);

	static final ModConfigSpec SERVER_SPEC = SERVER_BUILDER.build();

	// public static boolean logDirtBlock;
	// public static int magicNumber;
	// public static String magicNumberIntroduction;
	// public static Set<Item> items;

	public static int paperBellowsBlowTime;
	public static float leatherBellowsBlowTime;

	private static boolean validateItemName(final Object obj)
	{
		return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent event)
	{
		if (event instanceof ModConfigEvent.Unloading)
			return;
		switch (event.getConfig().getType())
		{
			case COMMON ->
			{
				// logDirtBlock = LOG_DIRT_BLOCK.get();
				// magicNumber = MAGIC_NUMBER.get();
				// magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();
				//
				// // convert the list of strings into a set of items
				// items = ITEM_STRINGS.get().stream().map(itemName -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemName))).collect(Collectors.toSet());
			}
			case CLIENT, STARTUP ->
			{
			}
			case SERVER ->
			{
				paperBellowsBlowTime = (int) (PAPER_BELLOWS_BLOW_TIME.get() * 20);
				leatherBellowsBlowTime = LEATHER_BELLOWS_BLOW_TIME.get().floatValue();
			}
		}
	}
}
