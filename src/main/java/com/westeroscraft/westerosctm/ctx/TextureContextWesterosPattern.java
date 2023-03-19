package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.TextureWesterosPattern;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

public class TextureContextWesterosPattern extends TextureContextCommon {
	
	private static final int getPatternIndex(int coord, int dim) {
		return ((coord % dim) + dim) % dim;
	}
	// Returns row and column in compactedDim format
	public static final int getPatternRowCol(int x, int y, int z, Direction dir, int theight, int twidth) {
		switch (dir) {
			case DOWN:
				return TextureWesterosCommon.makeRowCol(
						getPatternIndex(-z, theight),
						getPatternIndex(x, twidth));
			case UP:
				return TextureWesterosCommon.makeRowCol(
		        		getPatternIndex(z, theight),
		        		getPatternIndex(x, twidth));
			case NORTH:
				return TextureWesterosCommon.makeRowCol(
						getPatternIndex(-y, theight),
						getPatternIndex(-x, twidth));
			case SOUTH:
				return TextureWesterosCommon.makeRowCol(
						getPatternIndex(-y, theight),
						getPatternIndex(x, twidth));
			case WEST:
				return TextureWesterosCommon.makeRowCol(
						getPatternIndex(-y, theight),
						getPatternIndex(z, twidth));
			case EAST:
				return TextureWesterosCommon.makeRowCol(
						getPatternIndex(-y, theight),
						getPatternIndex(-z, twidth));
		}
		return 0;
	}

    public TextureContextWesterosPattern(BlockGetter world, BlockPos pos, TextureWesterosPattern<?> tex) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        // Get biome name, if needed
        String biomeName = null;
        ConnectedBits cbits = null;
    	if (tex.handler != null) {
			biomeName = getBiomeName(pos);
			cbits = new ConnectedBits();
    	}
        // Pattern dimensions
        int twidth = TextureWesterosCommon.getWidth(tex.compactedDims[0]);
        int theight = TextureWesterosCommon.getHeight(tex.compactedDims[0]);
        for (Direction dir : Direction.values()) {
        	int rowcol = getPatternRowCol(x, y, z, dir, theight, twidth);
        	int cidx = getTextureIndex(0, TextureWesterosCommon.getHeight(rowcol), TextureWesterosCommon.getWidth(rowcol),
    			tex, world, pos, biomeName, dir, cbits);
            this.setCompactedIndexByDirection(dir, cidx);
        }
    }    
}
