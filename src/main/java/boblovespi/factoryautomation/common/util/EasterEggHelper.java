package boblovespi.factoryautomation.common.util;

import net.minecraft.world.level.block.NoteBlock;

import java.util.Calendar;

public class EasterEggHelper
{
	private static final Calendar calendar = Calendar.getInstance();
	private static final int[] christmasSong = new int[] {
			1, 5, 8, 12, 13, 12, 10, 8,
			15, 13, 13, 12, 13, 12, 10, 8,
			6, 10, 13, 15, 17, 15, 13, 10,
			6, 9, 13, 15, 16, 15, 9, 9,
			15, 13, 13, 12, 13, 12, 10, 8,
			1, 5, 8, 12, 13, 12, 10, 8,
			20, 18, 18, 17, 18, 17, 13, 10,
			6, 9, 13, 15, 16, 15, 9, 9,
			13, 15, 12, 13, 10, 12, 9, 9,
			13, 15, 12, 13, 10, 12, 9, 9,
			8, 10, 13, 20, 18, 18, 18, 18,
			17, 15, 13, 10, 9, 15, 15, 17,
			13, 13, 13, 13
	};

	public static boolean isChristmas()
	{
		return calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26;
	}

	public static float getChristmasPitch(int idx)
	{
		return NoteBlock.getPitchFromNote(christmasSong[idx % christmasSong.length]);
	}

	public static int getChristmasLength()
	{
		return christmasSong.length;
	}
}
