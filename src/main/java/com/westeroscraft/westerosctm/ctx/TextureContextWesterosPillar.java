package com.westeroscraft.westerosctm.ctx;

import static team.chisel.ctm.client.util.ConnectionLocations.DOWN;
import static team.chisel.ctm.client.util.ConnectionLocations.EAST;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH;
import static team.chisel.ctm.client.util.ConnectionLocations.UP;
import static team.chisel.ctm.client.util.ConnectionLocations.WEST;

import java.util.Optional;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.TextureWesterosPattern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TextureContextWesterosPillar extends TextureContextCommon {
	
    public TextureContextWesterosPillar(BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex,
		boolean vertOnly) {
        BlockState state = world.getBlockState(pos);
        Axis axisVal = Axis.Y;
        if (!vertOnly) {
	        Optional<Axis> axis = state.getOptionalValue(RotatedPillarBlock.AXIS);
	        if (axis.isPresent()) {
	        	axisVal = axis.get();
	        }
        }
        // Get up/down connections, based on orientation
        boolean upConn = false, downConn = false;
        if (axisVal == Axis.Y) {
        	upConn = tex.connectTo(state, world.getBlockState(UP.transform(pos)), Direction.UP);
        	downConn = tex.connectTo(state, world.getBlockState(DOWN.transform(pos)), Direction.DOWN);
        }
        else if (axisVal == Axis.X) {
        	upConn = tex.connectTo(state, world.getBlockState(EAST.transform(pos)), Direction.EAST);
        	downConn = tex.connectTo(state, world.getBlockState(WEST.transform(pos)), Direction.WEST);
        }
        else {
        	upConn = tex.connectTo(state, world.getBlockState(NORTH.transform(pos)), Direction.NORTH);
        	downConn = tex.connectTo(state, world.getBlockState(SOUTH.transform(pos)), Direction.SOUTH);       	        	
        }
        // Compute patch for sides
        int row = 0, col = 0;
        if (upConn) {
        	if (downConn) {
        		row = 1; col = 0;
        	}
        	else {
        		row = 1; col = 1;
        	}
        }
        else {
        	if (downConn) {
        		row = 0; col = 1;
        	}
        	else {
        		row = 0; col = 0;
        	}
        }    	
    	String biomeName = null;
    	if (tex.handler != null) {
        	biomeName = getBiomeName(pos);    		
    	}
        // Get index to be used for end caps (0, 0)
    	for (Direction dir : Direction.values()) {
    		int compactedIndex;
            if (axisVal != dir.getAxis()) {
        		compactedIndex = this.getTextureIndex(0, row, col, tex, pos, biomeName, dir);
            }
            else {
        		compactedIndex = this.getTextureIndex(0, 0, 0, tex, pos, biomeName, dir);
            }
    		this.setCompactedIndexByDirection(dir, compactedIndex);
    	}
    }    
}
