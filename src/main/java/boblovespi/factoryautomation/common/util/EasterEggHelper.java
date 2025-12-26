package boblovespi.factoryautomation.common.util;

import net.minecraft.world.level.block.NoteBlock;

import java.util.Calendar;

public class EasterEggHelper
{
	private static final Calendar calendar = Calendar.getInstance();
	private static final int[] christmasSong = new int[] {
		0 // put song here
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
