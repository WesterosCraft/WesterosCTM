package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTM;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.WesterosConditionHandler;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Classic 47 texture CTM - files are base image + 46 additional images, following MCPatcher/Optifile tile order for CTM
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_ctm")
public class TextureTypeWesterosCTM implements ITextureType {
	protected static final int compactedDims[];
	static {
		compactedDims = new int[48]; // base plus 47 image classic CTM
		for (int i = 0; i < 48; i++) {
			compactedDims[i] = TextureWesterosCommon.makeDim(1, 1, i);
		}
	}
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTM> makeTexture(TextureInfo info) {
      return new TextureWesterosCommon<TextureTypeWesterosCTM>(this, info, compactedDims, false, WesterosConditionHandler.TYPE_CTM, 12, 4);
    }

    @Override
    public ITextureContext getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosCTM(world, pos, (TextureWesterosCommon<?>) tex);
     }

    @Override
    public int getQuadsPerSide() {
        return 1;
    }

    @Override
    public int requiredTextures() {
        return 48;
    }

	@Override
	public ITextureContext getContextFromData(long data) {
		throw new UnsupportedOperationException();
	}
}
