package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.mechanical.Joiner;
import boblovespi.factoryautomation.common.block.mechanical.PowerShaft;
import boblovespi.factoryautomation.common.blockentity.mechanical.JoinerBE;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.GearMaterial;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;

public class JoinerBER implements BlockEntityRenderer<JoinerBE>
{
	private final BlockRenderDispatcher blockRenderer;
	private final ItemRenderer itemRenderer;
	private final DeferredBlock<PowerShaft>[] shafts;
	private final BlockState[] shaftStates;
	private final ItemStack[] renderStacks;

	public JoinerBER(BlockEntityRendererProvider.Context pContext)
	{
		blockRenderer = pContext.getBlockRenderDispatcher();
		itemRenderer = pContext.getItemRenderer();
		shaftStates = new BlockState[Direction.Axis.VALUES.length * 2];
		renderStacks = new ItemStack[] {FAItems.GEARS.get(GearMaterial.COPPER).toStack(), FAItems.GEARS.get(GearMaterial.IRON).toStack()};
		shafts = new DeferredBlock[] {FABlocks.WOOD_POWER_SHAFT, FABlocks.IRON_POWER_SHAFT};
	}

	@Override
	public void render(JoinerBE be, float delta, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay)
	{
		var index = be.getBlockState().is(FABlocks.IRON_JOINER) ? 1 : 0;
		var isHorizontal = !be.getBlockState().getValue(Joiner.VERTICAL);
		var orientation = be.getBlockState().getValue(Joiner.FACING);
		var front = isHorizontal ? orientation : orientation.getAxisDirection() == Direction.AxisDirection.POSITIVE ? Direction.DOWN : Direction.UP;
		var side = orientation.getClockWise();
		if (shaftStates[index * 3] == null)
			shaftStates[index * 3] = shafts[index].get().defaultBlockState().setValue(PowerShaft.AXIS, Direction.Axis.Y);
		var invIn = front == Direction.DOWN || front == Direction.WEST || front == Direction.NORTH ? 1 : -1;
		var z = side == Direction.NORTH || side == Direction.SOUTH ? 1 : -1;
		var y = isHorizontal ? 1 : -1;
		var invOut = side == Direction.EAST || side == Direction.SOUTH ? 1 : -1;

		var scale = 0.28f;
		stack.pushPose();
		{
			stack.translate(0.5, 0.5, 0.5);
			stack.mulPose(front.getRotation());
			stack.translate(0, 2 / 16f, 0);
			stack.mulPose(BERUtils.quatFromAngleAxis(90, 1, 0, 0));
			stack.mulPose(BERUtils.quatFromAngleAxis(be.getRenderRot(delta), 0, 0, invIn));
			stack.scale(scale, scale, scale);
			itemRenderer.renderStatic(renderStacks[index], ItemDisplayContext.NONE, light, overlay, stack, bufferSource, be.getLevel(), 42);
		}
		stack.popPose();

		stack.pushPose();
		{
			stack.translate(0.5, 0.5, 0.5);
			stack.mulPose(side.getRotation());
			stack.translate(0, -2 / 16f * z * y, 0);
			stack.mulPose(BERUtils.quatFromAngleAxis(90, 1, 0, 0));
			stack.mulPose(BERUtils.quatFromAngleAxis(be.getRenderRot(delta) + 22.5f, 0, 0, -invOut));
			stack.scale(scale, scale, scale);
			itemRenderer.renderStatic(renderStacks[index], ItemDisplayContext.NONE, light, overlay, stack, bufferSource, be.getLevel(), 42);
		}
		stack.popPose();

		stack.pushPose();
		{
			stack.translate(0.5, 0.5, 0.5);
			stack.mulPose(front.getRotation());
			stack.mulPose(BERUtils.quatFromAngleAxis(-be.getRenderRot(delta), 0, invIn, 0));
			stack.translate(0, 2 / 16f, 0);
			stack.scale(0.5f, 6 / 16f - 2 / 16f, 0.5f);
			blockRenderer.renderBatched(shaftStates[index * 3], be.getBlockPos(), be.getLevel(), stack, bufferSource.getBuffer(RenderType.SOLID), false, RandomSource.create(42));
		}
		stack.popPose();

		stack.pushPose();
		{
			stack.translate(0.5, 0.5, 0.5);
			stack.mulPose(side.getRotation());
			stack.mulPose(BERUtils.quatFromAngleAxis(be.getRenderRot(delta), 0, invOut, 0));
			stack.translate(0, -6 / 16f, 0);
			stack.scale(0.5f, 12 / 16f, 0.5f);
			blockRenderer.renderBatched(shaftStates[index * 3], be.getBlockPos(), be.getLevel(), stack, bufferSource.getBuffer(RenderType.SOLID), false, RandomSource.create(42));
		}
		stack.popPose();
	}
}
