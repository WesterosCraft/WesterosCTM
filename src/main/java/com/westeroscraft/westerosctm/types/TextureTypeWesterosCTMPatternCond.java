package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.render.TextureWesterosCTMPattern;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Combination of classic 47 texture CTM - files are base image + a 12 x 4 atlas image + a pattern image + a substitution image, following MCPatcher/Optifile tile order for CTM - and
// 'pattern' ctm (width x height pattern in second extra image
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_ctm+pattern_cond")
public class TextureTypeWesterosCTMPatternCond extends TextureTypeWesterosCTMPattern {
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTM> makeTexture(TextureInfo info) {
		return new TextureWesterosCTMPattern<TextureTypeWesterosCTM>(this, info, true);
	}
    @Override
    public int requiredTextures() {
        return 4;
    }
}
