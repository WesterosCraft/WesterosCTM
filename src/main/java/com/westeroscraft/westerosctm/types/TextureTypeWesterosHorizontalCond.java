package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.WesterosConditionHandler;

import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@TextureType("westeros_horizontal_cond")
public class TextureTypeWesterosHorizontalCond extends TextureTypeWesterosHorizontal {
    @Override
    public ICTMTexture<TextureTypeWesterosHorizontal> makeTexture(TextureInfo info) {
		return new TextureWesterosCommon<TextureTypeWesterosHorizontal>(this, info, compactedDims, true, WesterosConditionHandler.TYPE_HORIZONTAL, 2, 2);
    }
    @Override
    public int requiredTextures() {
        return 2;
    }

}
