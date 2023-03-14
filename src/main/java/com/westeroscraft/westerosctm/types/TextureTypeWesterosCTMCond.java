package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTM;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

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

// Classic 47 texture CTM - files are base image + 46 additional images + conditional texture, following MCPatcher/Optifile tile order for CTM
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_ctm_cond")
public class TextureTypeWesterosCTMCond extends TextureTypeWesterosCTM {
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTMCond> makeTexture(TextureInfo info) {
      return new TextureWesterosCommon<TextureTypeWesterosCTMCond>(this, info, compactedDims, true);
    }
    @Override
    public int requiredTextures() {
        return 49;
    }

	@Override
	public ITextureContext getContextFromData(long data) {
		throw new UnsupportedOperationException();
	}
}
