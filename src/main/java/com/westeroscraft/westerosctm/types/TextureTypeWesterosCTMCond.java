package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.WesterosConditionHandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Classic 47 texture CTM - files are base image + 46 additional images + conditional texture, following MCPatcher/Optifile tile order for CTM
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_ctm_cond")
public class TextureTypeWesterosCTMCond extends TextureTypeWesterosCTM {
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTMCond> makeTexture(TextureInfo info) {
      return new TextureWesterosCommon<TextureTypeWesterosCTMCond>(this, info, compactedDims, true, WesterosConditionHandler.TYPE_CTM, 12, 4);
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
