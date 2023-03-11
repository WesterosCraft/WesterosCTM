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
	
	private int getTextureIndex(int trow, int tcol, TextureWesterosPattern<?> tex, BlockPos pos, String biomeName) {
		if (tex.handler != null) {
			return tex.handler.resolveCond(0, trow, tcol, pos, biomeName, tex);
		}
		else {
			return tex.getCompactedIndexFromTextureRowColumn(0, trow, tcol);
		}
	}
	
    public TextureContextWesterosPattern(BlockGetter world, BlockPos pos, TextureWesterosPattern<?> tex) {
        int tcol, trow, cidx;
        String biomeName = null;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        // Get biome name, if needed
    	if (tex.handler != null) {
			biomeName = getBiomeName(pos);
    	}
        // Pattern dimensions
        int twidth = TextureWesterosCommon.getWidth(tex.compactedDims[0]);
        int theight = TextureWesterosCommon.getHeight(tex.compactedDims[0]);
        // UP
        trow = getPatternIndex(z, theight);
        tcol = getPatternIndex(x, twidth);
        cidx = getTextureIndex(trow, tcol, tex, pos, biomeName);
        this.setCompactedIndexByDirection(Direction.UP, cidx);
        // DOWN
        trow = getPatternIndex(-z, theight);
        tcol = getPatternIndex(x, twidth);
        cidx = getTextureIndex(trow, tcol, tex, pos, biomeName);
        this.setCompactedIndexByDirection(Direction.DOWN, cidx);
        // NORTH
        trow = getPatternIndex(-y, theight);
        tcol = getPatternIndex(-x, twidth);
        cidx = getTextureIndex(trow, tcol, tex, pos, biomeName);
        this.setCompactedIndexByDirection(Direction.NORTH, cidx);
        // SOUTH
        trow = getPatternIndex(-y, theight);
        tcol = getPatternIndex(x, twidth);
        cidx = getTextureIndex(trow, tcol, tex, pos, biomeName);
        this.setCompactedIndexByDirection(Direction.SOUTH, cidx);
        // WEST
        trow = getPatternIndex(-y, theight);
        tcol = getPatternIndex(z, twidth);
        cidx = getTextureIndex(trow, tcol, tex, pos, biomeName);
        this.setCompactedIndexByDirection(Direction.WEST, cidx);
        // EAST
        trow = getPatternIndex(-y, theight);
        tcol = getPatternIndex(-z, twidth);
        cidx = getTextureIndex(trow, tcol, tex, pos, biomeName);
        this.setCompactedIndexByDirection(Direction.EAST, cidx);
    }    
}
