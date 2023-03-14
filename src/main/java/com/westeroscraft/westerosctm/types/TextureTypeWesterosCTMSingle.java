package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTMSingle;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Classic 47 texture CTM - files are base image + a 12 x 4 atlas image, following MCPatcher/Optifile tile order for CTM
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_ctm_single")
public class TextureTypeWesterosCTMSingle extends TextureTypeWesterosCTM {
	protected static final int compactedDims[] = new int[] { TextureWesterosCommon.makeDim(1, 1, 0), TextureWesterosCommon.makeDim(12, 4, 1) };
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTM> makeTexture(TextureInfo info) {
      return new TextureWesterosCommon<TextureTypeWesterosCTM>(this, info, compactedDims, false);
    }

    @Override
    public ITextureContext getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosCTMSingle(world, pos, (TextureWesterosCommon<?>) tex);
     }

    @Override
    public int requiredTextures() {
        return 2;
    }
}
