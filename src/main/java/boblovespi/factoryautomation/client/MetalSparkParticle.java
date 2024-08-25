package boblovespi.factoryautomation.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class MetalSparkParticle extends TextureSheetParticle
{
	private final SpriteSet spriteSet;
	private float temp;

	protected MetalSparkParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSet)
	{
		super(pLevel, pX, pY, pZ, 0, 0, 0);
		gravity = 0.6f;
		xd *= 0.8;
		zd *= 0.8;
		yd = random.nextFloat() * 0.15 + 0.08;
		this.spriteSet = spriteSet;
		setSpriteFromAge(spriteSet);
		temp = 1600 + random.nextInt(200);
		quadSize = 1 / 48f;
		lifetime = 60 + random.nextInt(40);
	}

	@Override
	public void tick()
	{
		setSpriteFromAge(spriteSet);
		rCol = red(temp);
		gCol = green(temp);
		bCol = blue(temp);
		temp *= 0.99f;
		temp -= 10f;
		if (temp < 300)
			temp = 300;

		super.tick();
	}

	@Override
	public float getQuadSize(float pScaleFactor)
	{
		var f = ((float) age + pScaleFactor) / (float) lifetime;
		return quadSize * (1 - f / 1.5f);
	}

	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public int getLightColor(float pPartialTick)
	{
		int i = super.getLightColor(pPartialTick);
		int k = i >> 16 & 0xFF;
		return 240 | k << 16;
	}

	private float red(float temp)
	{
		return 1f;
	}

	private float green(float temp)
	{
		if (temp > 900f)
			return (0.5f / 900f) * temp;
		else if (temp > 450f)
			return (0.4f / 450f) * temp - 0.3f;
		else
			return (-0.9f / 450f) * temp + 1f;
	}

	private float blue(float temp)
	{
		if (temp > 1350f)
			return (0.8f / 450) * temp - 2.4f;
		else if (temp > 450f)
			return 0f;
		else
			return (-1f / 450f) * temp + 1f;
	}

	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;

		public Provider(SpriteSet spriteSet)
		{
			this.spriteSet = spriteSet;
		}

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed)
		{
			return new MetalSparkParticle(pLevel, pX, pY, pZ, spriteSet);
		}
	}
}
