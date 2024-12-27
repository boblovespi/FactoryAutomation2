package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Gearbox;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.blockentity.mechanical.GearboxBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

public class GearboxBER implements BlockEntityRenderer<GearboxBE>
{
	private final BlockRenderDispatcher blockRenderer;
	private final ItemRenderer itemRenderer;
	private final BlockState[] shaftStates;

	public GearboxBER(BlockEntityRendererProvider.Context pContext)
	{
		blockRenderer = pContext.getBlockRenderDispatcher();
		itemRenderer = pContext.getItemRenderer();
		shaftStates = new BlockState[Direction.Axis.VALUES.length];
	}

	@Override
	public void render(GearboxBE be, float delta, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay)
	{
		var facing = be.getBlockState().getValue(Gearbox.FACING);
		var axis = facing.getAxis();
		if (shaftStates[axis.ordinal()] == null)
			shaftStates[axis.ordinal()] = FABlocks.WOOD_POWER_SHAFT.get().defaultBlockState().setValue(PowerShaft.AXIS, axis);
		var shaft = shaftStates[axis.ordinal()];
		var dir = facing.getAxisDirection().getStep();
		stack.pushPose();
		{
			stack.translate(0.5 - axis.choose(0.45, 0, 0), 0.5 - axis.choose(0, 0.45, 0), 0.5 - axis.choose(0, 0, 0.45));
			stack.scale((float) axis.choose(0.9, 0.5, 0.5), (float) axis.choose(0.5, 0.9, 0.5), (float) axis.choose(0.5, 0.5, 0.9));
			blockRenderer.renderBatched(shaft, be.getBlockPos(), be.getLevel(), stack, bufferSource.getBuffer(RenderType.SOLID), false, RandomSource.create(42));
		}
		stack.popPose();
		stack.pushPose();
		{
			stack.translate(0.5, 0.5, 0.5);
			switch (facing)
			{
				case DOWN ->
				{
					stack.mulPose(BERUtils.quatFromAngleAxis(180, 0, 1, 0));
					stack.mulPose(BERUtils.quatFromAngleAxis(90, 1, 0, 0));
				}
				case UP ->
				{
					stack.mulPose(BERUtils.quatFromAngleAxis(180, 0, 1, 0));
					stack.mulPose(BERUtils.quatFromAngleAxis(-90, 1, 0, 0));
				}
				case NORTH -> stack.mulPose(BERUtils.quatFromAngleAxis(180, 0, 1, 0));
				case SOUTH ->
				{
				}
				case WEST -> stack.mulPose(BERUtils.quatFromAngleAxis(270, 0, 1, 0));
				case EAST -> stack.mulPose(BERUtils.quatFromAngleAxis(90, 0, 1, 0));
			}
			stack.translate(0, 0, 0.3);
			stack.pushPose();
			{
				stack.pushPose();
				{
					stack.scale(0.36f, 0.36f, 1);
					stack.mulPose(BERUtils.quatFromAngleAxis(dir * be.getRenderOutRot(delta), 0, 0, 1));
					itemRenderer.renderStatic(be.getInputGear(), ItemDisplayContext.NONE, light, overlay, stack, bufferSource, be.getLevel(), (int) be.getBlockPos().asLong());
				}
				stack.popPose();
				var scale = be.getInputScale();
				stack.scale(2 * 0.36f * scale, 2 * 0.36f * scale, 1);
				stack.translate(0, 0, -0.6);
				stack.mulPose(BERUtils.quatFromAngleAxis(dir * be.getRenderInRot(delta), 0, 0, 1));
				itemRenderer.renderStatic(be.getInputGear(), ItemDisplayContext.NONE, light, overlay, stack, bufferSource, be.getLevel(), (int) be.getBlockPos().asLong());
			}
			stack.popPose();
			stack.translate(0, 0.3, 0);
			stack.mulPose(BERUtils.quatFromAngleAxis(22.5f, 0, 0, 1));
			stack.pushPose();
			{
				stack.scale(0.36f, 0.36f, 1);
				stack.mulPose(BERUtils.quatFromAngleAxis(-dir * be.getRenderOutRot(delta), 0, 0, 1));
				itemRenderer.renderStatic(be.getOutputGear(), ItemDisplayContext.NONE, light, overlay, stack, bufferSource, be.getLevel(), (int) be.getBlockPos().asLong());
				var scale = be.getOutputScale();
				stack.scale(2 * scale, 2 * scale, 1);
				stack.translate(0, 0, -0.6);
				itemRenderer.renderStatic(be.getOutputGear(), ItemDisplayContext.NONE, light, overlay, stack, bufferSource, be.getLevel(), (int) be.getBlockPos().asLong());
			}
			stack.popPose();
		}
		stack.popPose();
		stack.pushPose();
		{
			stack.translate(0.5 - axis.choose(0.45, 0, 0), 0.5 - axis.choose(0, 0.45, 0), 0.5 - axis.choose(0, 0, 0.45));
			if (axis.isHorizontal())
				stack.translate(0, 0.3, 0);
			else if (facing == Direction.UP)
				stack.translate(0, 0, 0.3);
			else
				stack.translate(0, 0, -0.3);
			stack.mulPose(BERUtils.quatFromAngleAxis(dir * 22.5f - dir * be.getRenderOutRot(delta), axis.choose(1, 0, 0), axis.choose(0, 1, 0),
					axis.choose(0, 0, 1)));
			stack.scale((float) axis.choose(0.9, 0.5, 0.5), (float) axis.choose(0.5, 0.9, 0.5), (float) axis.choose(0.5, 0.5, 0.9));
			blockRenderer.renderBatched(shaft, be.getBlockPos(), be.getLevel(), stack, bufferSource.getBuffer(RenderType.SOLID), false, RandomSource.create(42));
		}
		stack.popPose();
	}
}
