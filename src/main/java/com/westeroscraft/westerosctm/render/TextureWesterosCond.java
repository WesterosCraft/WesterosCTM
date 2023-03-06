package com.westeroscraft.westerosctm.render;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.Quad;
import team.chisel.ctm.client.util.Submap;

import com.google.common.collect.Lists;
import com.westeroscraft.westerosctm.types.TextureTypeWesterosCond;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCond;

public class TextureWesterosCond extends AbstractTexture<TextureTypeWesterosCond> {    
    public final WesterosConditionHandler handler;
    
    public TextureWesterosCond(TextureTypeWesterosCond type, TextureInfo info, int condIndex) {
        super(type, info);
        handler = new WesterosConditionHandler(info, condIndex);
    }
    
    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, ITextureContext context, int quadGoal) {
        if (context == null) {
            // Default to unmodified base image
    		return Lists.newArrayList(defaultTexture(quad, context));
        }
        return Lists.newArrayList(getQuad(quad, context));
    }
    
    private BakedQuad defaultTexture(BakedQuad quad, ITextureContext context) {
    	if (handler.condIndex == 0) {	// If single, row=0, col=0 is the default texture
            Quad q = makeQuad(quad, context);
        	float intervalU = 16f / handler.condWidth;
        	float intervalV = 16f / handler.condHeight;
        	ISubmap submap = new Submap(intervalU, intervalV, 0, 0);
            return q.transformUVs(sprites[handler.condIndex], submap).rebake();    		
    	}
    	else {
    		return makeQuad(quad, context).transformUVs(sprites[0]).rebake();
    	}
    }

    private BakedQuad getQuad(BakedQuad in, ITextureContext context) {
        TextureContextWesterosCond ctext = (TextureContextWesterosCond) context;
        long compressedData = ctext.getCompressedData();
        // If not remapped, return existing quad unmodified
        if (!handler.getIsRemapped(compressedData)) return defaultTexture(in, context);
        Quad q = makeQuad(in, context);
    	float intervalU = 16f / handler.condWidth;
    	float intervalV = 16f / handler.condHeight;
    	float minU = intervalU * handler.getColOut(compressedData);
    	float minV = intervalV * handler.getRowOut(compressedData);
    	ISubmap submap = new Submap(intervalU, intervalV, minU, minV);
        return q.transformUVs(sprites[handler.condIndex], submap).rebake();
    }
}
