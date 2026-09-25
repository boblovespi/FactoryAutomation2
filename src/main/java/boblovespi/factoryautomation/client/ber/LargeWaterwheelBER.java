package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.mechanical.LargeWaterwheel;
import boblovespi.factoryautomation.common.blockentity.mechanical.LargeWaterwheelBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LargeWaterwheelBER extends GeoBlockRenderer<LargeWaterwheelBE>
{
	public LargeWaterwheelBER(BlockEntityRendererProvider.Context context)
	{
		super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("large_waterwheel")));
	}

	@Override
	public boolean shouldRender(LargeWaterwheelBE blockEntity, Vec3 cameraPos)
	{
		return super.shouldRender(blockEntity, cameraPos) && blockEntity.getBlockState().getValue(LargeWaterwheel.MULTIBLOCK_COMPLETE);
	}

	@Override
	public AABB getRenderBoundingBox(LargeWaterwheelBE blockEntity)
	{
		var dir = blockEntity.getBlockState().getValue(LargeWaterwheel.FACING).getClockWise();
		return AABB.encapsulatingFullBlocks(blockEntity.getBlockPos().below(2).relative(dir, 2), blockEntity.getBlockPos().above(2).relative(dir, -2));
	}
}
