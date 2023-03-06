package com.westeroscraft.westerosctm.ctx;

import com.westeroscraft.westerosctm.render.TextureWesterosCond;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import team.chisel.ctm.api.texture.ITextureContext;

public class TextureContextWesterosCond implements ITextureContext {
	
    private long compressedData; // 
    
    public TextureContextWesterosCond(BlockGetter world, BlockPos pos, TextureWesterosCond tex) {
        compressedData = tex.handler.resolveCond(0, 0, 0, world, pos);
    }
    
    @Override
    public long getCompressedData(){
        return this.compressedData;
    }
}
