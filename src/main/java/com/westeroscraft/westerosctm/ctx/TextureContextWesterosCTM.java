package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.ITextureWesterosCompactedIndex;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;

public class TextureContextWesterosCTM  extends TextureContextCommon {
	public static final int MIDDLE_TILE_INDEX = 26;	// Index of tile with connections on all 8 directions

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
    // Mapping of connection bits for each side [by face direction ordinal][bit - 0: left, 1:up-left, 2:up, 3:up-right, 4:right, 5:down-right, 6:down, 7:down-left\
    public static final int[][] connToDir = new int[][] {
    	// Down (left=-X, up=+Z)
    	new int[] { getCTMConenctionBit(-1, 0, 0), getCTMConenctionBit(-1, 0, 1), getCTMConenctionBit(0, 0, 1),
    			getCTMConenctionBit(1, 0, 1), getCTMConenctionBit(1, 0, 0), getCTMConenctionBit(1, 0, -1),
    			getCTMConenctionBit(0, 0, -1), getCTMConenctionBit(-1, 0, -1) },
    	// Up (left=-X, up=-Z)
    	new int[] { getCTMConenctionBit(-1, 0, 0), getCTMConenctionBit(-1, 0, -1), getCTMConenctionBit(0, 0, -1),
    			getCTMConenctionBit(1, 0, -1), getCTMConenctionBit(1, 0, 0), getCTMConenctionBit(1, 0, 1),
    			getCTMConenctionBit(0, 0, 1), getCTMConenctionBit(-1, 0, 1) },
    	// North (left=+X, up=+Y)
    	new int[] { getCTMConenctionBit(1, 0, 0), getCTMConenctionBit(1, 1, 0), getCTMConenctionBit(0, 1, 0),
    			getCTMConenctionBit(-1, 1, 0), getCTMConenctionBit(-1, 0, 0), getCTMConenctionBit(-1, -1, 0),
    			getCTMConenctionBit(0, -1, 0), getCTMConenctionBit(1, -1, 0) },
    	// South (left=-X, up=+Y)
    	new int[] { getCTMConenctionBit(-1, 0, 0), getCTMConenctionBit(-1, 1, 0), getCTMConenctionBit(0, 1, 0),
    			getCTMConenctionBit(1, 1, 0), getCTMConenctionBit(1, 0, 0), getCTMConenctionBit(1, -1, 0),
    			getCTMConenctionBit(0, -1, 0), getCTMConenctionBit(-1, -1, 0) },
    	// West (left=-Z, up=+Y)
    	new int[] { getCTMConenctionBit(0, 0, -1), getCTMConenctionBit(0, 1, -1), getCTMConenctionBit(0, 1, 0),
    			getCTMConenctionBit(0, 1, 1), getCTMConenctionBit(0, 0, 1), getCTMConenctionBit(0, -1, 1),
    			getCTMConenctionBit(0, -1, 0), getCTMConenctionBit(0, -1, -1) },    	
    	// East (left=+Z, up=+Y)
    	new int[] { getCTMConenctionBit(0, 0, 1), getCTMConenctionBit(0, 1, 1), getCTMConenctionBit(0, 1, 0),
    			getCTMConenctionBit(0, 1, -1), getCTMConenctionBit(0, 0, -1), getCTMConenctionBit(0, -1, -1),
    			getCTMConenctionBit(0, -1, 0), getCTMConenctionBit(0, -1, 1) }  	
    };
    		
    public static int getSpriteIndex(long connBits, Direction dir) {
    	int [] conns = connToDir[dir.ordinal()];	// Get mappings for this side
        int index = (((connBits & (1L << conns[0])) != 0) ? 1 : 0) +
        		(((connBits & (1L << conns[7])) != 0) ? 2 : 0) +
        		(((connBits & (1L << conns[6])) != 0) ? 4 : 0) +
        		(((connBits & (1L << conns[5])) != 0) ? 8 : 0) +
        		(((connBits & (1L << conns[4])) != 0) ? 16 : 0) +
        		(((connBits & (1L << conns[3])) != 0) ? 32 : 0) +
        		(((connBits & (1L << conns[2])) != 0) ? 64 : 0) +
        		(((connBits & (1L << conns[1])) != 0) ? 128 : 0);
        return neighborMapCtm[index];    	
    }
    
    public static final int getCTMConenctionBit(int dx, int dy, int dz) {
    	int v = (dx + 1) + (3 * (dy + 1)) + (9 * (dz + 1));
    	return v;
    }
    // Bits are N = (dX + 1) + 3*(dY + 1) + 9*(dZ + 1)
    public static long buildCTMConnectionBits(BlockGetter world, BlockPos pos, ITextureWesterosCompactedIndex tex) {
    	BlockState state = world.getBlockState(pos);
    	long flags = 0;
    	for (Direction dir : Direction.values()) {
    		BlockPos p = pos.relative(dir);
        	// Get connection for primary direction
        	if (tex.connectTo(state, world.getBlockState(p), dir)) {
        		flags |= (1L << getCTMConenctionBit(dir.getStepX(), dir.getStepY(), dir.getStepZ()));
        	}
    		if (dir.getAxis() != Axis.Y) {	// If side direction, check diagonals too (up and down)
    	    	if (tex.connectTo(state, world.getBlockState(p.above()), dir)) {
    	    		flags |= (1L << getCTMConenctionBit(dir.getStepX(), Direction.UP.getStepY(), dir.getStepZ()));
    	    	}
    	    	if (tex.connectTo(state, world.getBlockState(p.below()), dir)) {
    	    		flags |= (1L << getCTMConenctionBit(dir.getStepX(), Direction.DOWN.getStepY(), dir.getStepZ()));
    	    	}
    		}
	    	// If north/south, check horizontal diagonals too
    		if (dir.getAxis() == Axis.Z) {
    	    	if (tex.connectTo(state, world.getBlockState(p.east()), dir)) {
    	    		flags |= (1L << getCTMConenctionBit(Direction.EAST.getStepX(), 0, dir.getStepZ()));
    	    	}
    	    	if (tex.connectTo(state, world.getBlockState(p.west()), dir)) {
    	    		flags |= (1L << getCTMConenctionBit(Direction.WEST.getStepX(), 0, dir.getStepZ()));
    	    	}    			
    		}
    	}    	
    	return flags;
    }

    // Get row of sprite
    public int getSpriteRow(int spridx) {
    	return 0;
    }
    // Get col of sprite
    public int getSpriteCol(int spridx) {
    	return 0;
    }
    // Get texture index of sprite
    public int getSpriteTxtIdx(int spridx) {
    	return spridx + 1;	// Base image not used
    }
    
    public TextureContextWesterosCTM(BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex) {
    	long flags = buildCTMConnectionBits(world, pos, tex);
    	String biomeName = null;
    	if (tex.handler != null) {
    		biomeName = this.getBiomeName(pos);
    	}
        for (Direction dir : Direction.values()) {
        	int spridx = getSpriteIndex(flags, dir);	// Get sprite index
        	if (spridx == MIDDLE_TILE_INDEX) {
        		this.handleCenterTexture(spridx, dir, world, pos, tex, biomeName, flags);
        	}
        	else {
        		// Do any conditional mapping, if needed
            	int cidx = getTextureIndex(getSpriteTxtIdx(spridx), getSpriteRow(spridx), getSpriteCol(spridx),
            			tex, world, pos, biomeName, dir, flags);
        		this.setCompactedIndexByDirection(dir, cidx);
        	}
        }
    }    
    
    public void handleCenterTexture(int spridx, Direction dir, BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex,
    		String biomeName, long ctmConnBits) {
    	int cidx = getTextureIndex(getSpriteTxtIdx(spridx), getSpriteRow(spridx), getSpriteCol(spridx),
    			tex, world, pos, biomeName, dir, ctmConnBits);
		this.setCompactedIndexByDirection(dir, cidx);    	
    }
}