package com.westeroscraft.westerosctm.ctx;

import static team.chisel.ctm.client.util.ConnectionLocations.DOWN;
import static team.chisel.ctm.client.util.ConnectionLocations.EAST;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH_EAST_DOWN;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH_EAST_UP;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH_WEST_DOWN;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH_WEST_UP;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH_EAST_DOWN;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH_EAST_UP;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH_WEST_DOWN;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH_WEST_UP;
import static team.chisel.ctm.client.util.ConnectionLocations.UP;
import static team.chisel.ctm.client.util.ConnectionLocations.WEST;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ObjectArrays;
import com.westeroscraft.westerosctm.WesterosCTM;

import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.client.util.ConnectionLocations;

public class TextureContextWesterosPillar implements ITextureContext {
    public static final int AXIS_X = 1;
    public static final int AXIS_Y = 2;
    public static final int AXIS_Z = 3;
    public static final int AXIS_BITS = 0x3;
    
    public static final int CONNECT_UP = 1 << 2;
    public static final int CONNECT_DOWN = 1 << 3;
    
    
    private long compressedData; // == AXIS_BITS, CONNECT_UP, CONNECT_DOWN
    
    public TextureContextWesterosPillar(BlockGetter world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        int axisValue = AXIS_Y;	// Assume Y
        Optional<Axis> axis = state.getOptionalValue(RotatedPillarBlock.AXIS);
        Axis a = Axis.Y;	// Default
        if (axis.isPresent()) {
        	a = axis.get();
        	if (a == Axis.X) axisValue = AXIS_X;
        	if (a == Axis.Z) axisValue = AXIS_Z;
        }
        // Get up/down connections, based on orientation
        boolean upConn = false, downConn = false;
        if (axisValue == AXIS_Y) {
        	upConn = (state == world.getBlockState(UP.transform(pos)));
        	downConn = (state == world.getBlockState(DOWN.transform(pos)));
        }
        else if (axisValue == AXIS_X) {
        	upConn = (state == world.getBlockState(EAST.transform(pos)));
        	downConn = (state == world.getBlockState(WEST.transform(pos)));        	
        }
        else {
        	upConn = (state == world.getBlockState(NORTH.transform(pos)));
        	downConn = (state == world.getBlockState(SOUTH.transform(pos)));        	        	
        }
        compressedData = (axisValue & AXIS_BITS) | (upConn ? CONNECT_UP : 0) | (downConn ? CONNECT_DOWN : 0);
        WesterosCTM.LOGGER.warn(String.format("pos=%1$s, axis=%2$s,upConn=%3$s,downConn=%4$s", pos.toString(), a, upConn, downConn));
    }

    public int getAxis() { return (int)(compressedData & AXIS_BITS); }
    public boolean getConnectUp() { return ((compressedData & CONNECT_UP) == CONNECT_UP) ? true : false; }
    public boolean getConnectDown() { return ((compressedData & CONNECT_DOWN) == CONNECT_DOWN) ? true : false; }
    
    public TextureContextWesterosPillar(long data){
    	this.compressedData = data;
    }

    @Override
    public long getCompressedData(){
        return this.compressedData;
    }
}
