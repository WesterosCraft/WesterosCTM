package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import team.chisel.ctm.api.texture.ITextureContext;

// Common context base class
//   Each of 6 sides is 10 bits in the compressedData
//   So, bits for Direction d are at 1 << (d.ordinal() * 10)
//   In each case, the value for the bit field for a given side is the
//   compacted index number of the texture to be used.   The compacted index is
//   the ordinal of the texture patches associated with the texture, starting
//   with the first row of the first column of the first texture (index=0, row=0, col=0),
//   incrementing by column, then by row, then by texture.  The compacted index is
//   encoded and decoded using the functions of the ITextureWesterosCompactedIndex interface,
//   which is implemented by the TextureWesteros* texture classes.
//   The highest compacted index value (0x3FF) is reserved for a null quad (no quad)
public abstract class TextureContextCommon implements ITextureContext {
    protected long compressedData; // 
    private static final long masks[] = { 0x3FFL, 0x3FFL << 10, 0x3FFL << 20, 0x3FFL << 30, 0x3FFL << 40, 0x3FFL << 50 }; 

    public static final int COMPACTED_INDEX_NULL_QUAD = 0x3FF;
    
    public int getCompactedIndexByDirection(Direction dir) {
    	return (int)((compressedData >> (10 * dir.ordinal())) & 0x3FFL);
    }
    public void setCompactedIndexByDirection(Direction dir, int compactedIndex) {
    	int ord = dir.ordinal();
    	compressedData = (compressedData & (~masks[ord])) | (((long)compactedIndex) << (10 * ord));
    }
    public String getBiomeName(BlockPos pos) {
    	String biomeName = "";
    	// Compute biome
    	@SuppressWarnings("resource")
		LocalPlayer p = Minecraft.getInstance().player;
    	if (p != null) {
    		Holder<Biome> b = p.clientLevel.getBiome(pos);
    		biomeName = b.unwrap().map((v) -> {
    	         return v.location().toString();
    	      }, (v) -> {
    	         return "[unregistered " + v + "]";
    	      });
    	}    	
    	return biomeName;
    }
    
	protected int getTextureIndex(int tidx, int trow, int tcol, TextureWesterosCommon<?> tex, BlockGetter world, BlockPos pos, String biomeName,
		Direction dir, long[] ctmConnBits) {
		if (tex.handler != null) {
			return tex.handler.resolveCond(tidx, trow, tcol, world, pos, biomeName, tex, dir, ctmConnBits);
		}
		else {
			return tex.getCompactedIndexFromTextureRowColumn(tidx, trow, tcol);
		}
	}

    @Override
    public long getCompressedData() {
        return this.compressedData;
    }
}
