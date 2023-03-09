package com.westeroscraft.westerosctm.ctx;

import static team.chisel.ctm.client.util.ConnectionLocations.EAST;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH;
import static team.chisel.ctm.client.util.ConnectionLocations.WEST;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class TextureContextWesterosHorizontal extends TextureContextCommon {
	
	private static final int getColByConnection(boolean left, boolean right) {
		return left ? 1 : 0;
	}
	private static final int getRowByConnection(boolean left, boolean right) {
		if (left) {
			return right ? 0 : 1;
		}
		else {
			return right ? 1 : 0;
		}
	}
    public TextureContextWesterosHorizontal(BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex) {
        BlockState state = world.getBlockState(pos);
        // Get side connections
    	boolean northConn = tex.connectTo(state, world.getBlockState(NORTH.transform(pos)), Direction.NORTH);
    	boolean southConn = tex.connectTo(state, world.getBlockState(SOUTH.transform(pos)), Direction.SOUTH);
    	boolean eastConn = tex.connectTo(state, world.getBlockState(EAST.transform(pos)), Direction.EAST);
    	boolean westConn = tex.connectTo(state, world.getBlockState(WEST.transform(pos)), Direction.WEST);
    	String biomeName = null;
    	boolean hasHandler = false;
    	if (tex.handler != null) {
        	biomeName = getBiomeName(pos);
        	hasHandler = true;
    	}
    	// Set top and bottom
    	int idx = hasHandler ? tex.handler.resolveCond(0, 0, 0, pos, biomeName, tex) : 
    		tex.getCompactedIndexFromTextureRowColumn(0, 0, 0);
    	this.setCompactedIndexByDirection(Direction.UP, idx);
    	this.setCompactedIndexByDirection(Direction.DOWN, idx);
        // Compute patch for NORTH (east=left, west=right)
    	int row = getRowByConnection(eastConn, westConn), col = getColByConnection(eastConn, westConn);
    	idx = hasHandler ? tex.handler.resolveCond(0, row, col, pos, biomeName, tex) : 
    		tex.getCompactedIndexFromTextureRowColumn(0, row, col);
    	this.setCompactedIndexByDirection(Direction.NORTH, idx);
        // Compute patch for SOUTH (west=left, east=right)
    	row = getRowByConnection(westConn, eastConn); col = getColByConnection(westConn, eastConn);
    	idx = hasHandler ? tex.handler.resolveCond(0, row, col, pos, biomeName, tex) : 
    		tex.getCompactedIndexFromTextureRowColumn(0, row, col);
    	this.setCompactedIndexByDirection(Direction.SOUTH, idx);
        // Compute patch for EAST (south=left, north=right)
    	row = getRowByConnection(southConn, northConn); col = getColByConnection(southConn, northConn);
    	idx = hasHandler ? tex.handler.resolveCond(0, row, col, pos, biomeName, tex) : 
    		tex.getCompactedIndexFromTextureRowColumn(0, row, col);
    	this.setCompactedIndexByDirection(Direction.EAST, idx);
        // Compute patch for WEST (north=left, south=right)
    	row = getRowByConnection(northConn, southConn); col = getColByConnection(northConn, southConn);
    	idx = hasHandler ? tex.handler.resolveCond(0, row, col, pos, biomeName, tex) : 
    		tex.getCompactedIndexFromTextureRowColumn(0, row, col);
    	this.setCompactedIndexByDirection(Direction.WEST, idx);
    }    
}
