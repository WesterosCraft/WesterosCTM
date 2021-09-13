package com.westeroscraft.westerosctm.render;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.model.BakedQuad;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.util.CTMLogic;
import team.chisel.ctm.client.util.Quad;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTM;
import com.westeroscraft.westerosctm.types.TextureTypeWesterosCTMSingle;

public class TextureWesterosCTMSingle extends TextureWesterosCTM {

    public TextureWesterosCTMSingle(TextureTypeWesterosCTMSingle type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(final BakedQuad bakedQuad, final ITextureContext context, final int quads) {
        final Quad quad = this.makeQuad(bakedQuad, context);
        final CTMLogic ctm = (context instanceof TextureContextWesterosCTM) ? ((TextureContextWesterosCTM) context).getCTM(bakedQuad.getDirection()) : null;
        final int txtidx = getSpriteIndex(ctm);
        return Collections.singletonList(quad.transformUVs(this.sprites[1], CTMSubmap.CTM12X4[txtidx]).rebake());
    }
}
