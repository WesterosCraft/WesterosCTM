package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.render.TextureWesterosPattern;

import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@TextureType("westeros_pattern_cond")
public class TextureTypeWesterosPatternCond extends TextureTypeWesterosPattern {
    @Override
    public ICTMTexture<TextureTypeWesterosPattern> makeTexture(TextureInfo info) {
		return new TextureWesterosPattern<TextureTypeWesterosPattern>(this, info, true);
    }
    
    @Override
    public int requiredTextures() {
        return 2;
    }
}
