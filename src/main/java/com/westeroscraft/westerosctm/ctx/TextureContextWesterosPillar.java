package com.westeroscraft.westerosctm.ctx;

import static team.chisel.ctm.client.util.ConnectionLocations.DOWN;
import static team.chisel.ctm.client.util.ConnectionLocations.EAST;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH;
import static team.chisel.ctm.client.util.ConnectionLocations.UP;
import static team.chisel.ctm.client.util.ConnectionLocations.WEST;

import java.util.Optional;

import com.westeroscraft.westerosctm.render.TextureWesterosPillar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import team.chisel.ctm.api.texture.ITextureContext;

public class TextureContextWesterosPillar implements ITextureContext {
    public static final int AXIS_X = 1;
    public static final int AXIS_Y = 2;
    public static final int AXIS_Z = 3;
    public static final int AXIS_BITS = 0x3;
    
    public static final int CONNECT_UP = 1 << 2;
    public static final int CONNECT_DOWN = 1 << 3;
    
    
    private long compressedData; // == AXIS_BITS, CONNECT_UP, CONNECT_DOWN
    
    public TextureContextWesterosPillar(BlockGetter world, BlockPos pos, TextureWesterosPillar tex, boolean vertOnly) {
        BlockState state = world.getBlockState(pos);
        int axisValue = AXIS_Y;	// Assume Y
        if (!vertOnly) {
	        Optional<Axis> axis = state.getOptionalValue(RotatedPillarBlock.AXIS);
	        Axis a = Axis.Y;	// Default
	        if (axis.isPresent()) {
	        	a = axis.get();
	        	if (a == Axis.X) axisValue = AXIS_X;
	        	if (a == Axis.Z) axisValue = AXIS_Z;
	        }
        }
        // Get up/down connections, based on orientation
        boolean upConn = false, downConn = false;
        if (axisValue == AXIS_Y) {
        	upConn = tex.connectTo(state, world.getBlockState(UP.transform(pos)), Direction.UP);
        	downConn = tex.connectTo(state, world.getBlockState(DOWN.transform(pos)), Direction.DOWN);
        }
        else if (axisValue == AXIS_X) {
        	upConn = tex.connectTo(state, world.getBlockState(EAST.transform(pos)), Direction.EAST);
        	downConn = tex.connectTo(state, world.getBlockState(WEST.transform(pos)), Direction.WEST);
        }
        else {
        	upConn = tex.connectTo(state, world.getBlockState(NORTH.transform(pos)), Direction.NORTH);
        	downConn = tex.connectTo(state, world.getBlockState(SOUTH.transform(pos)), Direction.SOUTH);       	        	
        }
        compressedData = (axisValue & AXIS_BITS) | (upConn ? CONNECT_UP : 0) | (downConn ? CONNECT_DOWN : 0);
    }

    public int getAxis() { return (int)(compressedData & AXIS_BITS); }
    public boolean getConnectUp() { return ((compressedData & CONNECT_UP) == CONNECT_UP) ? true : false; }
    public boolean getConnectDown() { return ((compressedData & CONNECT_DOWN) == CONNECT_DOWN) ? true : false; }
    
    @Override
    public long getCompressedData(){
        return this.compressedData;
    }
}
