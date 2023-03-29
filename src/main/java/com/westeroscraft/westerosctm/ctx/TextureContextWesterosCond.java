package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

public class TextureContextWesterosCond extends TextureContextCommon {	
    public TextureContextWesterosCond(BlockGetter world, BlockPos pos, TextureWesterosCommon<?> tex) {
    	String biomeName = getBiomeName(pos);
    	// Get index to be used for each side
    	for (Direction dir : Direction.values()) {
        	int compactedIndex = tex.handler.resolveCond(0, 0, 0, world, pos, biomeName, tex, dir, null);
        	//WesterosCTM.LOGGER.info(String.format("blk: %s, dir %s, compactedIndex=%X", pos, dir.toString(), compactedIndex));
    		this.setCompactedIndexByDirection(dir, compactedIndex);
    	}
    }
}
