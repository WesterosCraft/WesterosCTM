package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.render.TextureWesterosCTMSingle;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Classic 47 texture CTM - files are base image + a 12 x 4 atlas image, following MCPatcher/Optifile tile order for CTM
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_ctm_single")
public class TextureTypeWesterosCTMSingle extends TextureTypeWesterosCTM {
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTM> makeTexture(TextureInfo info) {
      return new TextureWesterosCTMSingle(this, info);
    }

    @Override
    public int requiredTextures() {
        return 2;
    }
}
