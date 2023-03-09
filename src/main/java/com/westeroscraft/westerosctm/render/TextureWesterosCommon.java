package com.westeroscraft.westerosctm.render;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.BlockstatePredicateParser;
import team.chisel.ctm.client.util.ParseUtils;
import team.chisel.ctm.client.util.Quad;
import team.chisel.ctm.client.util.Submap;

import com.google.common.collect.Lists;
import com.westeroscraft.westerosctm.ctx.TextureContextCommon;

public class TextureWesterosCommon<T extends ITextureType> extends AbstractTexture<T>
	implements ITextureWesterosCompactedIndex {    
    public final WesterosConditionHandler handler;
    public final int compactedDims[];	// (width << 24) | (height << 16) | lastoffset
    
    private static final int COMPACT_LASTOFF_MASK = 0xFFFF;
    private static final int COMPACT_WIDTH_MASK = 0xFF000000;
    private static final int COMPACT_WIDTH_SHIFT = 24;
    private static final int COMPACT_HEIGHT_MASK = 0x00FF0000;
    private static final int COMPACT_HEIGHT_SHIFT = 16;
    public static final int getLastOffset(int dim) { return dim & COMPACT_LASTOFF_MASK; }
    public static final int getWidth(int dim) { return (dim & COMPACT_WIDTH_MASK) >> COMPACT_WIDTH_SHIFT; }
    public static final int getHeight(int dim) { return (dim & COMPACT_HEIGHT_MASK) >> COMPACT_HEIGHT_SHIFT; }
    public static final int makeDim(int width, int height, int prevLastOff) {
    	return (width << COMPACT_WIDTH_SHIFT) + (height << COMPACT_HEIGHT_SHIFT) + 
			(prevLastOff + (width * height));
    }
    
    private static final BlockstatePredicateParser predicateParser = new BlockstatePredicateParser();

	private final Optional<Boolean> connectInside;
	public Optional<Boolean> getConnectInside() { return connectInside; }
	
	private final boolean ignoreStates;
	public boolean getIgnoreStates() { return ignoreStates; }
	
	@Nullable
	private final BiPredicate<Direction, BlockState> connectionChecks;
    
    public TextureWesterosCommon(T type, TextureInfo info, final int[] compactedDims, boolean conds) {
        super(type, info);
        this.connectInside = info.getInfo().flatMap(obj -> ParseUtils.getBoolean(obj, "connect_inside"));
        this.ignoreStates = info.getInfo().flatMap(obj -> ParseUtils.getBoolean(obj, "ignore_states")).orElse(false);
        this.connectionChecks = info.getInfo().map(obj -> predicateParser.parse(obj.get("connect_to"))).orElse(null);

        if (conds) {
        	int prevTxtCount = compactedDims.length;
        	this.handler = new WesterosConditionHandler(info, prevTxtCount);
        	this.compactedDims = Arrays.copyOf(compactedDims, prevTxtCount + 1);
        	int lastend = (prevTxtCount == 0) ? 0 : getLastOffset(compactedDims[prevTxtCount-1]);
        	this.compactedDims[prevTxtCount] = makeDim(handler.condWidth, handler.condHeight, lastend);
        }
        else {
        	this.handler = null;
        	this.compactedDims = compactedDims;
        }
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
            // Default to unmodified base image
    		return Lists.newArrayList(defaultTexture(quad, context));
        }
        return Lists.newArrayList(getQuad(quad, context));
    }
    
    private BakedQuad defaultTexture(BakedQuad quad, ITextureContext context) {
        Quad q = makeQuad(quad, context);
    	float intervalU = 16f / getWidth(compactedDims[0]);
    	float intervalV = 16f / getHeight(compactedDims[0]);
    	ISubmap submap = new Submap(intervalU, intervalV, 0, 0);
        return q.transformUVs(sprites[0], submap).rebake();    		
    }

    private BakedQuad getQuad(BakedQuad in, ITextureContext context) {
        TextureContextCommon ctext = (TextureContextCommon) context;
        int compactedIndex = ctext.getCompactedIndexByDirection(in.getDirection());
        //WesterosCTM.LOGGER.info("compactedIndex=" + compactedIndex);
        int txtIdx = this.getTextureIndexFromCompacted(compactedIndex);	// Get texture index
        int row = this.getRowFromCompactedIndex(compactedIndex, txtIdx);
        int col = this.getColumnFromCompactedIndex(compactedIndex, txtIdx);
        int width = getWidth(compactedDims[txtIdx]);
        int height = getHeight(compactedDims[txtIdx]);
        //WesterosCTM.LOGGER.info(String.format("row=%d,col=%d,width=%d,height=%d,txtIdx=%d", row, col, width, height, txtIdx));        
        Quad q = makeQuad(in, context);
    	float intervalU = 16f / width;
    	float intervalV = 16f / height;
    	float minU = intervalU * col;
    	float minV = intervalV * row;
    	ISubmap submap = new Submap(intervalU, intervalV, minU, minV);
        return q.transformUVs(sprites[txtIdx], submap).rebake();
    }
    
	// Get texture index from compacted
	public int getTextureIndexFromCompacted(int compacted) {
		for (int i = 0; i < compactedDims.length; i++) {
			if (compacted < getLastOffset(compactedDims[i])) { return i; }
		}
		return -1;
	}
	// Get row from compacted, given texture index
	public int getRowFromCompactedIndex(int compacted, int textureIdx) {
		if (textureIdx > 0) {
			compacted -= getLastOffset(compactedDims[textureIdx-1]);
		}
		return compacted / getWidth(compactedDims[textureIdx]);
	}
	// Get column from compacted, given texture index
	public int getColumnFromCompactedIndex(int compacted, int textureIdx) {
		if (textureIdx > 0) {
			compacted -= getLastOffset(compactedDims[textureIdx-1]);
		}
		return compacted % getWidth(compactedDims[textureIdx]);
	}
	// Make compacted index, given texture index, row, column
	public int getCompactedIndexFromTextureRowColumn(int textureIndex, int row, int column) {
		int compacted = 0;
		if (textureIndex > 0) {
			compacted = getLastOffset(compactedDims[textureIndex-1]);
		}
		return compacted + (row * getWidth(compactedDims[textureIndex])) + column;
	}
}
