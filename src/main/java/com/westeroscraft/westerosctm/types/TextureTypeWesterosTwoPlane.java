package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.render.TextureWesterosTwoPlane;

import net.minecraft.core.Direction;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.type.TextureTypeCTM;;

// Vertical+Horizontal or Horizontal+Vertical:  base texture is 1 x 7 tile map, corresponding to 
// order of images from OptiFine/MCPatch v+h and h+v tile list, respectively.
//   See https://i.imgur.com/8LZj1Hy.png for layouts
public class TextureTypeWesterosTwoPlane extends TextureTypeCTM {
	
    @TextureType("westeros_v+h")
    public static final TextureTypeWesterosTwoPlane V_H = new TextureTypeWesterosTwoPlane(Direction.Plane.VERTICAL);
    @TextureType("westeros_h+v")
    public static final TextureTypeWesterosTwoPlane H_V = new TextureTypeWesterosTwoPlane(Direction.Plane.HORIZONTAL);

    public final Direction.Plane plane;

    public TextureTypeWesterosTwoPlane(Direction.Plane p) {
    	plane = p;
    }
    
	@Override
    public ICTMTexture<? extends TextureTypeWesterosTwoPlane> makeTexture(TextureInfo info) {
      return new TextureWesterosTwoPlane(this, info);
    }

    @Override
    public int requiredTextures() {
        return 1;
    }

	@Override
	public ITextureContext getContextFromData(long data) {
		throw new UnsupportedOperationException();
	}
}
