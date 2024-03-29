package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.WesterosConditionHandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Classic 47 texture CTM - files are base image + a 12 x 4 atlas image + conditional texture, following MCPatcher/Optifile tile order for CTM
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_ctm_single_cond")
public class TextureTypeWesterosCTMSingleCond extends TextureTypeWesterosCTMSingle {
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTM> makeTexture(TextureInfo info) {
      return new TextureWesterosCommon<TextureTypeWesterosCTM>(this, info, TextureTypeWesterosCTMSingle.compactedDims, true, WesterosConditionHandler.TYPE_CTM, 12, 4);
    }
    @Override
    public int requiredTextures() {
        return 3;
    }
}
