package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.blockentity.processing.TripHammerBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class TripHammerBER extends GeoBlockRenderer<TripHammerBE> {

    private final ItemRenderer itemRenderer;

    public TripHammerBER(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("trip_hammer")));
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public boolean shouldRender(TripHammerBE blockEntity, Vec3 cameraPos) {
        if(blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).is(FABlocks.TRIP_HAMMER.get()))
            return blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getValue(StoneCrucible.MULTIBLOCK_COMPLETE);
        return false;
    }

    @Override
    public void render(TripHammerBE be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        poseStack.pushPose();
        poseStack.translate(0.5f, 16.5f/16.0f, 0.5f);
        poseStack.mulPose(BERUtils.quatFromAngleAxis(90, 1, 0, 0));
        itemRenderer.renderStatic(be.getRenderStack(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, be.getLevel(), 42);

        poseStack.popPose();

        super.render(be, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
