package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.TextureWesterosPattern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;

public class TextureContextWesterosPattern extends TextureContextCommon {
	
	private static final int getPatternIndex(int coord, int dim) {
		return ((coord % dim) + dim) % dim;
	}
	public static final int getPatternRow(int x, int y, int z, Direction dir, int theight) {
		switch (dir) {
			case DOWN:
				 return getPatternIndex(-z, theight);
			case UP:
		        return getPatternIndex(z, theight);
			case NORTH:
				return getPatternIndex(-y, theight);
			case SOUTH:
				return getPatternIndex(-y, theight);
			case WEST:
				return getPatternIndex(-y, theight);
			case EAST:
				return getPatternIndex(-y, theight);
		}
		return 0;
	}
	public static final int getPatternCol(int x, int y, int z, Direction dir, int twidth) {
		switch (dir) {
			case DOWN:
				 return getPatternIndex(x, twidth);
			case UP:
		        return getPatternIndex(x, twidth);
			case NORTH:
				return getPatternIndex(-x, twidth);
			case SOUTH:
				return getPatternIndex(x, twidth);
			case WEST:
				return getPatternIndex(z, twidth);
			case EAST:
				return getPatternIndex(-z, twidth);
		}
		return 0;
	}
	
    public TextureContextWesterosPattern(BlockGetter world, BlockPos pos, TextureWesterosPattern<?> tex) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        // Get biome name, if needed
        String biomeName = null;
    	if (tex.handler != null) {
			biomeName = getBiomeName(pos);
    	}
        // Pattern dimensions
        int twidth = TextureWesterosCommon.getWidth(tex.compactedDims[0]);
        int theight = TextureWesterosCommon.getHeight(tex.compactedDims[0]);
        for (Direction dir : Direction.values()) {
        	int cidx = getTextureIndex(0, getPatternRow(x, y, z, dir, theight),
    			getPatternCol(x, y, z, dir, twidth), tex, pos, biomeName, dir);
            this.setCompactedIndexByDirection(dir, cidx);
        }
    }    
}
