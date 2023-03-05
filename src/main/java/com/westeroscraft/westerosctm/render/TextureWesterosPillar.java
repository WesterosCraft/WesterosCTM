package com.westeroscraft.westerosctm.render;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.BlockstatePredicateParser;
import team.chisel.ctm.client.util.ParseUtils;
import team.chisel.ctm.client.util.Quad;
import team.chisel.ctm.client.util.Submap;

import com.google.common.collect.Lists;
import com.westeroscraft.westerosctm.types.TextureTypeWesterosPillar;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPillar;

// Most code here is modified version ot TexturePillar
public class TextureWesterosPillar extends AbstractTexture<TextureTypeWesterosPillar> {
    private static final BlockstatePredicateParser predicateParser = new BlockstatePredicateParser();

	private final Optional<Boolean> connectInside;
	public Optional<Boolean> getConnectInside() { return connectInside; }
	
	private final boolean ignoreStates;
	public boolean getIgnoreStates() { return ignoreStates; }
	
	@Nullable
	private final BiPredicate<Direction, BlockState> connectionChecks;

    public TextureWesterosPillar(TextureTypeWesterosPillar type, TextureInfo info) {
        super(type, info);
        this.connectInside = info.getInfo().flatMap(obj -> ParseUtils.getBoolean(obj, "connect_inside"));
        this.ignoreStates = info.getInfo().flatMap(obj -> ParseUtils.getBoolean(obj, "ignore_states")).orElse(false);
        this.connectionChecks = info.getInfo().map(obj -> predicateParser.parse(obj.get("connect_to"))).orElse(null);
    }
    public boolean connectTo(BlockState from, BlockState to, Direction dir) {
        try {
            return (connectionChecks == null) ? 
            		(this.ignoreStates ? (from.getBlock() == to.getBlock()) :
        			(from == to)) :
				connectionChecks.test(dir, to);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }    
    
    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, ITextureContext context, int quadGoal) {
        if (context == null) {
            return Lists.newArrayList(makeQuad(quad, context).transformUVs(sprites[0], Submap.X2[0][0]).rebake());
        }
        return Lists.newArrayList(getQuad(quad, context));
    }

    private BakedQuad getQuad(BakedQuad in, ITextureContext context) {
        Quad q = makeQuad(in, context);
        TextureContextWesterosPillar ctx = (TextureContextWesterosPillar) context;
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
