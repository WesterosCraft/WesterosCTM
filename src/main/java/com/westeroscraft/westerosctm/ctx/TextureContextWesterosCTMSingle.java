package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public class TextureContextWesterosCTMSingle  extends TextureContextWesterosCTM {
	public final int MIDDLE_TILE_INDEX = 26;	// Index of tile with connections on all 8 directions

    // Get row of sprite
    public int getSpriteRow(int spridx) {
    	return (spridx / 12);
    }
    // Get col of sprite
    public int getSpriteCol(int spridx) {
    	return (spridx % 12);
    }
    // Get texture index of sprite
    public int getSpriteTxtIdx(int spridx) {
    	return 1;	// Base image not used
    }
    
    public TextureContextWesterosCTMSingle(BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex) {
    	super(world, pos, tex);
    }    
}
