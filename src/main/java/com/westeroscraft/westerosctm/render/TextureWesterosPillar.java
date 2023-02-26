package com.westeroscraft.westerosctm.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.TexturePillar;
import team.chisel.ctm.client.texture.type.TextureTypePillar;
import team.chisel.ctm.client.util.Quad;

public class TextureWesterosPillar extends TexturePillar {
    public TextureWesterosPillar(TextureTypePillar type, TextureInfo info) {
        super(type, info);
    }
    @Override
    // Patch from 1.19.2 CTM
    protected Quad makeQuad(BakedQuad bq, ITextureContext context) {
        return super.makeQuad(bq, context).derotate();
    }
}
