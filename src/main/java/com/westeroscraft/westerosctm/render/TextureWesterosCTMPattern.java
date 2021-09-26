package com.westeroscraft.westerosctm.render;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.model.BakedQuad;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextGrid;
import team.chisel.ctm.client.texture.ctx.TextureContextGrid.Point2i;
import team.chisel.ctm.client.util.CTMLogic;
import team.chisel.ctm.client.util.Quad;
import team.chisel.ctm.client.util.Submap;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTM;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTMPattern;
import com.westeroscraft.westerosctm.types.TextureTypeWesterosCTMPattern;

public class TextureWesterosCTMPattern extends TextureWesterosCTMSingle {

	public final int xSize;
	public final int ySize;
	public final int xOffset;
	public final int yOffset;
	
	public final int MIDDLE_TILE_INDEX = 26;	// Index of tile with connections on all 8 directions
	
    public TextureWesterosCTMPattern(TextureTypeWesterosCTMPattern type, TextureInfo info) {
        super(type, info);

        if (info.getInfo().isPresent()) {
            JsonObject object = info.getInfo().get();
            if (object.has("width") && object.has("height")) {
                Preconditions.checkArgument(object.get("width").isJsonPrimitive() && object.get("width").getAsJsonPrimitive().isNumber(), "width must be a number!");
                Preconditions.checkArgument(object.get("height").isJsonPrimitive() && object.get("height").getAsJsonPrimitive().isNumber(), "height must be a number!");

                this.xSize = object.get("width").getAsInt();
                this.ySize = object.get("height").getAsInt();

            } else if (object.has("size")) {
                Preconditions.checkArgument(object.get("size").isJsonPrimitive() && object.get("size").getAsJsonPrimitive().isNumber(), "size must be a number!");

                this.xSize = object.get("size").getAsInt();
                this.ySize = object.get("size").getAsInt();
            } else {
                xSize = ySize = 2;
            }

            if (object.has("x_offset")) {
                Preconditions.checkArgument(object.get("x_offset").isJsonPrimitive() && object.get("x_offset").getAsJsonPrimitive().isNumber(), "x_offset must be a number!");

                this.xOffset = object.get("x_offset").getAsInt();
            } else {
                this.xOffset = 0;
            }

            if (object.has("y_offset")) {
                Preconditions.checkArgument(object.get("y_offset").isJsonPrimitive() && object.get("y_offset").getAsJsonPrimitive().isNumber(), "y_offset must be a number!");

                this.yOffset = object.get("y_offset").getAsInt();
            } else {
                this.yOffset = 0;
            }
        } else {
            xOffset = yOffset = 0;
            xSize = ySize = 2;
        }

        Preconditions.checkArgument(xSize > 0 && ySize > 0, "Cannot have a dimension of 0!");
    }

    
    @Override
    public List<BakedQuad> transformQuad(final BakedQuad bakedQuad, final ITextureContext context, final int quads) {
        final Quad quad = this.makeQuad(bakedQuad, context);
        final CTMLogic ctm = (context instanceof TextureContextWesterosCTMPattern) ? ((TextureContextWesterosCTMPattern) context).getCTM(bakedQuad.getDirection()) : null;
        final int txtidx = getSpriteIndex(ctm);
        // If all-8-connected, use pattern
        if (txtidx == MIDDLE_TILE_INDEX) {
        	Point2i textureCoords = (context instanceof TextureContextWesterosCTMPattern) ? 
    			((TextureContextWesterosCTMPattern)context).getTextureCoords(bakedQuad.getDirection()) :
				new Point2i(0, 0);         
        	float intervalU = 16f / xSize;
        	float intervalV = 16f / ySize;

        	// throw new RuntimeException(index % variationSize+" and "+index/variationSize);
        	float minU = intervalU * textureCoords.getX();
        	float minV = intervalV * textureCoords.getY();

        	ISubmap submap = new Submap(intervalU, intervalV, minU, minV);

            return Collections.singletonList(quad.transformUVs(this.sprites[2], submap).rebake());
        }
        return Collections.singletonList(quad.transformUVs(this.sprites[1], CTMSubmap.CTM12X4[txtidx]).rebake());
    }
}
