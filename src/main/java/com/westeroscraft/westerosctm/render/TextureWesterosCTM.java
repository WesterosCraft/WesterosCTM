package com.westeroscraft.westerosctm.render;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.BlockstatePredicateParser;
import team.chisel.ctm.client.util.CTMLogic;
import team.chisel.ctm.client.util.Dir;
import team.chisel.ctm.client.util.IdentityStrategy;
import team.chisel.ctm.client.util.ParseUtils;
import team.chisel.ctm.client.util.Quad;
import team.chisel.ctm.client.util.Submap;
import team.chisel.ctm.client.util.CTMLogic.StateComparisonCallback;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTM;
import com.westeroscraft.westerosctm.types.TextureTypeWesterosCTM;

import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenCustomHashMap;

public class TextureWesterosCTM extends AbstractTexture<TextureTypeWesterosCTM> {

    private static final BlockstatePredicateParser predicateParser = new BlockstatePredicateParser();

	private final Optional<Boolean> connectInside;
	public Optional<Boolean> getConnectInside() { return connectInside; }
	
	private final boolean ignoreStates;
	public boolean getIgnoreStates() { return ignoreStates; }
	
	@Nullable
	private final BiPredicate<Direction, BlockState> connectionChecks;
	
	private static final class CacheKey {
		private final BlockState from;
		private final Direction dir;
		
		CacheKey(BlockState f, Direction d) {
			from = f; dir = d;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + dir.hashCode();
			result = prime * result + System.identityHashCode(from);
			return result;
		}

		@Override
		public boolean equals(@Nullable Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey other = (CacheKey) obj;
			if (dir != other.dir)
				return false;
			if (from != other.from)
				return false;
			return true;
		}
	}

    public TextureWesterosCTM(TextureTypeWesterosCTM type, TextureInfo info) {
        super(type, info);
        this.connectInside = info.getInfo().flatMap(obj -> ParseUtils.getBoolean(obj, "connect_inside"));
        this.ignoreStates = info.getInfo().flatMap(obj -> ParseUtils.getBoolean(obj, "ignore_states")).orElse(false);
        this.connectionChecks = info.getInfo().map(obj -> predicateParser.parse(obj.get("connect_to"))).orElse(null);
    }
    
    public boolean connectTo(CTMLogic ctm, BlockState from, BlockState to, Direction dir) {
        try {
            return ((connectionChecks == null ? StateComparisonCallback.DEFAULT.connects(ctm, from, to, dir) : connectionChecks.test(dir, to)) ? 1 : 0) == 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Map texture using CTM method (bit - 0: left, 1:up-left, 2:up, 3:up-right, 4:right, 5:down-right, 6:down, 7:down-left
    private static final int[] neighborMapCtm = new int[]{
        0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15,
        1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14,
        0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15,
        1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14,
        36, 17, 36, 17, 24, 19, 24, 43, 36, 17, 36, 17, 24, 19, 24, 43,
        16, 18, 16, 18, 6, 46, 6, 21, 16, 18, 16, 18, 28, 9, 28, 22,
        36, 17, 36, 17, 24, 19, 24, 43, 36, 17, 36, 17, 24, 19, 24, 43,
        37, 40, 37, 40, 30, 8, 30, 34, 37, 40, 37, 40, 25, 23, 25, 45,
        0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15,
        1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14,
        0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15,
        1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14,
        36, 39, 36, 39, 24, 41, 24, 27, 36, 39, 36, 39, 24, 41, 24, 27,
        16, 42, 16, 42, 6, 20, 6, 10, 16, 42, 16, 42, 28, 35, 28, 44,
        36, 39, 36, 39, 24, 41, 24, 27, 36, 39, 36, 39, 24, 41, 24, 27,
        37, 38, 37, 38, 30, 11, 30, 32, 37, 38, 37, 38, 25, 33, 25, 26,
    };
    
    public int getSpriteIndex(CTMLogic logic) {
        if (logic == null) {
            return 0;
        }
        int index = (logic.connected(Dir.LEFT) ? 1 : 0) +
        		(logic.connected(Dir.BOTTOM_LEFT) ? 2 : 0) +
        		(logic.connected(Dir.BOTTOM) ? 4 : 0) +
        		(logic.connected(Dir.BOTTOM_RIGHT) ? 8 : 0) +
        		(logic.connected(Dir.RIGHT) ? 16 : 0) +
        		(logic.connected(Dir.TOP_RIGHT) ? 32 : 0) +
        		(logic.connected(Dir.TOP) ? 64 : 0) +
        		(logic.connected(Dir.TOP_LEFT) ? 128 : 0);
        return neighborMapCtm[index];    	
    }

    @Override
    public List<BakedQuad> transformQuad(final BakedQuad bakedQuad, final ITextureContext context, final int quads) {
        final Quad quad = this.makeQuad(bakedQuad, context);
        final CTMLogic ctm = (context instanceof TextureContextWesterosCTM) ? ((TextureContextWesterosCTM) context).getCTM(bakedQuad.getDirection()) : null;
        final int txtidx = getSpriteIndex(ctm);
        return Collections.singletonList(quad.transformUVs(this.sprites[txtidx+1], Submap.X1).rebake());
    }

    @Override
    protected Quad makeQuad(BakedQuad bq, ITextureContext context) {
        return super.makeQuad(bq, context).derotate();
    }
}
