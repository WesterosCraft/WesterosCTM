package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.TextureWesterosCond;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import team.chisel.ctm.api.texture.ITextureContext;

public class TextureContextWesterosCond implements ITextureContext {
	public static final long COND_REMAPPED_MASK = 0x100L;
	public static final int COND_REMAPPED_SHIFT = 8;
	public static final long COND_ROWOUT_MASK = 0x0F0L;
	public static final int COND_ROWOUT_SHIFT = 4;
	public static final long COND_COLOUT_MASK = 0x00FL;
	public static final int COND_COLOUT_SHIFT = 0;
	
    private long compressedData; // 
    
    public TextureContextWesterosCond(BlockGetter world, BlockPos pos, TextureWesterosCond tex) {
        compressedData = tex.resolveCond(0, 0, 0, world, pos);
    }

    public final boolean getIsRemapped() { return (compressedData & COND_REMAPPED_MASK) != 0; }
    public final int getColOut() { return (int)((compressedData & COND_ROWOUT_MASK) >> COND_ROWOUT_SHIFT); }
    public final int getRowOut() { return (int)((compressedData & COND_COLOUT_MASK) >> COND_COLOUT_SHIFT); }
    
    @Override
    public long getCompressedData(){
        return this.compressedData;
    }
}
