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
import com.westeroscraft.westerosctm.types.TextureTypeWesterosVertical;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPillar;

// Most code here is modified version ot TexturePillar
public class TextureWesterosVertical extends AbstractTexture<TextureTypeWesterosVertical> {
    public TextureWesterosVertical(TextureTypeWesterosVertical type, TextureInfo info) {
        super(type, info);
    }    
    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, ITextureContext context, int quadGoal) {
        if (context == null) {
            if (quad.getDirection() != null && quad.getDirection().getAxis().isVertical()) {
                return Lists.newArrayList(makeQuad(quad, context).transformUVs(sprites[0]).rebake());
            }
            return Lists.newArrayList(makeQuad(quad, context).transformUVs(sprites[0], Submap.X2[0][0]).rebake());
        }
        return Lists.newArrayList(getQuad(quad, context));
    }

    private BakedQuad getQuad(BakedQuad in, ITextureContext context) {
        Quad q = makeQuad(in, context);
        TextureContextWesterosPillar ctx = (TextureContextWesterosPillar) context;
        int axis = ctx.getAxis();
        boolean connUp = ctx.getConnectUp();
        boolean connDown = ctx.getConnectDown();
        // Compute UV for which image to get
        ISubmap uvs;
        if (connUp) {
        	if (connDown) {
                uvs = Submap.X2[1][0];	// Use both image        		
        	}
        	else {
                uvs = Submap.X2[1][1];  // Bottom image      		
        	}
        }
        else {
        	if (connDown) {
                uvs = Submap.X2[0][1];  // Top image      		
        	}
        	else {
                uvs = Submap.X2[0][0];	// Neither        		
        	}
        }
        return q.transformUVs(sprites[0], uvs).rebake();
    }
}
