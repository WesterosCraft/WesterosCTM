package com.westeroscraft.westerosctm.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westeroscraft.westerosctm.ctx.TextureContextCommon;

public class TextureWesterosCommon<T extends ITextureType> extends AbstractTexture<T>
	implements ITextureWesterosCompactedIndex {    
    public final WesterosConditionHandler handler;
    public int compactedDims[];	// (width << 24) | (height << 16) | lastoffset
    
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
    // Compacted row/col encoding
    public static final int getCol(int dim) { return (dim & COMPACT_WIDTH_MASK) >> COMPACT_WIDTH_SHIFT; }
    public static final int getRow(int dim) { return (dim & COMPACT_HEIGHT_MASK) >> COMPACT_HEIGHT_SHIFT; }
    public static final int makeRowCol(int row, int col) {
    	return (col << COMPACT_WIDTH_SHIFT) + (row << COMPACT_HEIGHT_SHIFT);
    }
    public static final BlockstatePredicateParser predicateParser = new BlockstatePredicateParser();

    
	public static class ConnectionCheck implements ITextureWesterosConnectTo {
		private final boolean ignoreStates;
		private final TagKey<Block> connTag;
		private final String connState;
		public final int connIndex;
		private ConnectToFunction func;
		@Nullable
		private final BiPredicate<Direction, BlockState> connectionChecks;
		public ConnectionCheck(int connIndex, boolean ignore, BiPredicate<Direction, BlockState> conncheck, String connTag, String connState) {
			this.connIndex = connIndex;
			this.ignoreStates = ignore;
			this.connectionChecks = conncheck;
			this.connState = connState;
			TagKey<Block> ct = null;
			if (connTag != null) {
				String[] parts = connTag.split(":");
				ResourceLocation loc;
				if (parts.length == 2) {
					loc = new ResourceLocation(parts[0], parts[1]);
				}
				else {
					loc = new ResourceLocation("minecraft", parts[0]);
				}
				ct = BlockTags.create(loc);
			}
			this.connTag = ct;
			if (connectionChecks != null) {
				func = (from, to, dir) -> connectionChecks.test(dir, to);
			}
			else if (connTag != null) {
				func = (from, to, dir) -> from.is(ConnectionCheck.this.connTag) && to.is(ConnectionCheck.this.connTag);
			}
			else if (connState != null) {
				func = (from, to, dir) -> {
					Property<?> p = from.getBlock().getStateDefinition().getProperty(this.connState);
					if (p == null || !from.hasProperty(p) || !to.hasProperty(p))
						return false;
					return (from.getBlock() == to.getBlock() && from.getValue(p).equals(to.getValue(p)));
				};
			}
			else if (this.ignoreStates) {
				func = (from, to, dir) -> from.getBlock() == to.getBlock();
			}
			else {
				func = (from, to, dir) -> from == to;
			}
		}
		@Override
	    public boolean connectTo(BlockState from, BlockState to, Direction dir) {
	        try {
	        	return func.apply(from, to, dir);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }    
	}
	
	public List<ConnectionCheck> connectionChecks = new ArrayList<ConnectionCheck>();
	
	// Get connection checks
	@Override
	public List<TextureWesterosCommon.ConnectionCheck> getConnectionChecks() {
		return connectionChecks;
	}

    public TextureWesterosCommon(T type, TextureInfo info, final int[] compactedDims, boolean conds, String defType, int defWidth, int defHeight) {
        super(type, info);
        // Set up base connection check
        Optional<JsonObject> infoobj = info.getInfo();
        boolean ignoreStates = infoobj.flatMap(obj -> ParseUtils.getBoolean(obj, "ignore_states")).orElse(false);
        String connect_to_tag = infoobj.flatMap(obj -> getString(obj, "connect_to_tag")).orElse(null);
				String connect_to_state = infoobj.flatMap(obj -> getString(obj, "connect_to_state")).orElse(null);
        BiPredicate<Direction, BlockState> connChecks = info.getInfo().map(obj -> predicateParser.parse(obj.get("connect_to"))).orElse(null);
        // Add as base connection check
        connectionChecks.add(new ConnectionCheck(0, ignoreStates, connChecks, connect_to_tag, connect_to_state));
        
        if (conds) {
        	this.handler = new WesterosConditionHandler(info, compactedDims.length, connectionChecks, defType, defWidth, defHeight);
        }
        else {
        	this.handler = null;
        }
        computeCompactedDims(compactedDims, conds);
    }
    
    protected void computeCompactedDims(final int[] compactedDims, boolean conds) {
        if (conds) {
        	int prevTxtCount = compactedDims.length;
        	this.compactedDims = Arrays.copyOf(compactedDims, prevTxtCount + 1);
        	int lastend = (prevTxtCount == 0) ? 0 : getLastOffset(compactedDims[prevTxtCount-1]);
        	this.compactedDims[prevTxtCount] = makeDim(handler.condWidth, handler.condHeight, lastend);
        }
        else {
        	this.compactedDims = compactedDims;
        }    	
    }
        
    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, ITextureContext context, int quadGoal) {
    	BakedQuad bq = null;
        if (context == null) {
            // Default to unmodified base image
    		bq = defaultTexture(quad, context);
        }
        else {
        	bq = getQuad(quad, context);
        }
        return (bq == null) ? Lists.newArrayList() : Lists.newArrayList(bq);
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
        // If null quad, drop it
        if (compactedIndex == TextureContextCommon.COMPACTED_INDEX_NULL_QUAD) {
            //WesterosCTM.LOGGER.info("compactedIndex=" + compactedIndex);
    		return null;        	
        }
        //WesterosCTM.LOGGER.info("compactedIndex=" + compactedIndex);
        int txtIdx = this.getTextureIndexFromCompacted(compactedIndex);	// Get texture index
        //if (txtIdx < 0) {
        //    WesterosCTM.LOGGER.info(String.format("compactedIndex=%x, txtIdx=%d", compactedIndex, txtIdx));        	
        //    for (int i = 0; i < compactedDims.length; i++) {
        //    	WesterosCTM.LOGGER.info(String.format("compactedDims[%d]=%x", i, compactedDims[i]));        
        //    }
        //}
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
	

	public static Optional<String> getString(JsonElement element) {
		if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
			return Optional.of(element.getAsString());
		}
		return Optional.empty();
	}

	public static Optional<String> getString(JsonObject object, String memberName) {
		if (object.has(memberName)) {
			return getString(object.get(memberName));
		}
		return Optional.empty();
	}

}
