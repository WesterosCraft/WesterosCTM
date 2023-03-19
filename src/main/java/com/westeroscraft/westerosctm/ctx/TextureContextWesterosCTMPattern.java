package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

public class TextureContextWesterosCTMPattern extends TextureContextWesterosCTMSingle {
    
    public TextureContextWesterosCTMPattern(BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex) {
    	super(world, pos, tex);
    }
    @Override
    public void handleCenterTexture(int spridx, Direction dir, BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex,
		String biomeName, ConnectedBits ctmConnBits) {
    	// Get pattern index
    	int rowcol = TextureContextWesterosPattern.getPatternRowCol(pos.getX(), pos.getY(), pos.getZ(), 
    			dir, TextureWesterosCommon.getHeight(tex.compactedDims[2]), TextureWesterosCommon.getWidth(tex.compactedDims[2]));
    	// Apply conditional, if needed
    	int cidx = getTextureIndex(2, TextureWesterosCommon.getRow(rowcol), TextureWesterosCommon.getCol(rowcol),
    			tex, world, pos, biomeName, dir, ctmConnBits);
		this.setCompactedIndexByDirection(dir, cidx);    	
    }
}