package com.westeroscraft.westerosctm.render;

import java.util.List;
import java.util.EnumSet;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.Quad;
import team.chisel.ctm.client.util.Submap;
import team.chisel.ctm.client.util.DirectionHelper;

import com.google.common.collect.Lists;
import com.westeroscraft.westerosctm.types.TextureTypeWesterosPillar;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPillar;

import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.EAST;
import static net.minecraft.core.Direction.NORTH;
import static net.minecraft.core.Direction.SOUTH;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.core.Direction.WEST;

// Most code here is modified version ot TexturePillar
public class TextureWesterosPillar extends AbstractTexture<TextureTypeWesterosPillar> {
    public TextureWesterosPillar(TextureTypeWesterosPillar type, TextureInfo info) {
        super(type, info);
    }
    //@Override
    // Patch from 1.19.2 CTM
    //protected Quad makeQuad(BakedQuad bq, ITextureContext context) {
    //    return super.makeQuad(bq, context).derotate();
    //}
    
    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, ITextureContext context, int quadGoal) {
        if (context == null) {
            if (quad.getDirection() != null && quad.getDirection().getAxis().isVertical()) {
                return Lists.newArrayList(makeQuad(quad, context).transformUVs(sprites[0]).rebake());
            }
            return Lists.newArrayList(makeQuad(quad, context).transformUVs(sprites[1], Submap.X2[0][0]).rebake());
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
        return q.transformUVs(sprites[1], uvs).rebake();
    }
}
