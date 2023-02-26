package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.render.TextureWesterosPillar;

import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.type.TextureTypePillar;

@TextureType("westeros_pillar")
public class TextureTypeWesterosPillar extends TextureTypePillar {
    @Override
    public ICTMTexture<TextureTypePillar> makeTexture(TextureInfo info) {
    	// Used patched render subclass
        return new TextureWesterosPillar(this, info);
    }
}
