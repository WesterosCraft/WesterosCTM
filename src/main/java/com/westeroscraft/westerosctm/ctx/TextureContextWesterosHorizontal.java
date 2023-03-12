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
	// Returns row and column in compactedDim format
	public static final int getHorizontalRowCol(boolean northConn, boolean southConn, boolean eastConn, boolean westConn,
			Direction dir) {
		switch (dir) {
			case DOWN:
				return 0;	// end cap
			case UP:
		        return 0; // end cap
			case NORTH:
				return TextureWesterosCommon.makeRowCol(
						getRowByConnection(eastConn, westConn),
						getColByConnection(eastConn, westConn));
			case SOUTH:
				return TextureWesterosCommon.makeRowCol(
						getRowByConnection(westConn, eastConn),
						getColByConnection(westConn, eastConn));
			case WEST:
				return TextureWesterosCommon.makeRowCol(
						getRowByConnection(northConn, southConn),
						getColByConnection(northConn, southConn));
			case EAST:
				return TextureWesterosCommon.makeRowCol(
						getRowByConnection(southConn, northConn),
						getColByConnection(southConn, northConn));
		}
		return 0;		
	}
			
    public TextureContextWesterosHorizontal(BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex) {
        BlockState state = world.getBlockState(pos);
        // Get side connections
    	boolean northConn = tex.connectTo(state, world.getBlockState(NORTH.transform(pos)), Direction.NORTH);
    	boolean southConn = tex.connectTo(state, world.getBlockState(SOUTH.transform(pos)), Direction.SOUTH);
    	boolean eastConn = tex.connectTo(state, world.getBlockState(EAST.transform(pos)), Direction.EAST);
    	boolean westConn = tex.connectTo(state, world.getBlockState(WEST.transform(pos)), Direction.WEST);
    	String biomeName = null;
    	if (tex.handler != null) {
        	biomeName = getBiomeName(pos);
    	}
    	for (Direction dir : Direction.values()) {
    		int rowcol = getHorizontalRowCol(northConn, southConn, eastConn, westConn, dir);
        	int idx = getTextureIndex(0, TextureWesterosCommon.getHeight(rowcol), TextureWesterosCommon.getWidth(rowcol), 
    			tex, world, pos, biomeName, Direction.UP);
        	this.setCompactedIndexByDirection(dir, idx);
    	}
    }    
}
