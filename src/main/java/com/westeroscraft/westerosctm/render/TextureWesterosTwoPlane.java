package com.westeroscraft.westerosctm.render;

import com.westeroscraft.westerosctm.types.TextureTypeWesterosTwoPlane;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextCTM;
import team.chisel.ctm.client.texture.render.TextureCTM;
import team.chisel.ctm.client.util.CTMLogic;
import team.chisel.ctm.client.util.Dir;
import team.chisel.ctm.client.util.Quad;
import team.chisel.ctm.client.util.Submap;

import java.util.Collections;
import java.util.List;


public class TextureWesterosTwoPlane extends TextureCTM<TextureTypeWesterosTwoPlane> {
    private final Direction.Plane plane;

    private static final float DIV7 = 16 / 7f;    
    
    // Mapping for 1x7 tilemap
    // Based on horozontal+vertcal and vertical+horizontal order, as show in https://i.imgur.com/8LZj1Hy.png
    public static final ISubmap[] T1X7 = new ISubmap[] {
        new Submap(DIV7, 16, 0, 0),
        new Submap(DIV7, 16, DIV7, 0),
        new Submap(DIV7, 16, DIV7 * 2, 0), 
        new Submap(DIV7, 16, DIV7 * 3, 0),
        new Submap(DIV7, 16, DIV7 * 4, 0),
        new Submap(DIV7, 16, DIV7 * 5, 0),
        new Submap(DIV7, 16, DIV7 * 6, 0)
    };
    
    public TextureWesterosTwoPlane(final TextureTypeWesterosTwoPlane type, final TextureInfo info) {
        super(type, info);
        this.plane = type.plane;
    }

    @Override
    public List<BakedQuad> transformQuad(final BakedQuad bakedQuad, final ITextureContext context, final int quads) {
        final Quad quad = this.makeQuad(bakedQuad, context);
        final CTMLogic logic = (context instanceof TextureContextCTM) ? ((TextureContextCTM) context).getCTM(bakedQuad.getDirection()) : null;
        return Collections.singletonList(quad.transformUVs(this.sprites[0], this.getQuad(logic)).rebake());
    }

    private ISubmap getQuad(final CTMLogic logic) {
        if (logic == null) {
            return T1X7[3];
        }
        if (this.plane == Direction.Plane.VERTICAL) {
            final boolean top = logic.connected(Dir.TOP);
            final boolean bottom = logic.connected(Dir.BOTTOM);
            if (top && (!bottom)) return T1X7[0];	// index 0 = collected up
            if (top && bottom) return T1X7[1];	// index 1 = collected up+down
            if ((!top) && bottom) return T1X7[2];	// index 2 = collected down
            // No vertical, check for horizontal
            final boolean left = logic.connected(Dir.LEFT);
            final boolean right = logic.connected(Dir.RIGHT);
            if (right && (!left)) return T1X7[4];	// Index 4 = connect right
            if (right && left) return T1X7[5];	// Index 5 = connect left and right
            if ((!right) && left) return T1X7[6];	// Index 6 = connect left
        }
        else {
            final boolean left = logic.connected(Dir.LEFT);
            final boolean right = logic.connected(Dir.RIGHT);
            if (right && (!left)) return T1X7[0];	// Index 0 = connect right
            if (right && left) return T1X7[1];	// Index 1 = connect left and right
            if ((!right) && left) return T1X7[2];	// Index 2 = connect left
            // No horizontal, check for vertical
            final boolean top = logic.connected(Dir.TOP);
            final boolean bottom = logic.connected(Dir.BOTTOM);
            if (top && (!bottom)) return T1X7[4];	// index 4 = collected up
            if (top && bottom) return T1X7[5];	// index 5 = collected up+down
            if ((!top) && bottom) return T1X7[6];	// index 6 = collected down
        }
        return T1X7[3];	// Index 3 = no connect
    }
}
