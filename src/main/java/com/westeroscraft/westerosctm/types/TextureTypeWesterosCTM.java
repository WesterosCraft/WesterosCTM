package com.westeroscraft.westerosctm.types;

import javax.annotation.Nonnull;

import com.westeroscraft.westerosctm.render.TextureWesterosCTM;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

public enum TextureTypeWesterosCTM implements ITextureType {
    @TextureType("westrrosctm")
    INSTANCE;
    
    @Nonnull
    private static final ITextureContext EMPTY_CONTEXT = () -> 0L;

    @Override
    public ICTMTexture<TextureTypeWesterosCTM> makeTexture(TextureInfo info){
        return new TextureWesterosCTM(this, info);
    }

    @Override
    public ITextureContext getBlockRenderContext(BlockState state, IBlockReader world, BlockPos pos, ICTMTexture<?> tex){
        return EMPTY_CONTEXT;
    }

    @Override
    public ITextureContext getContextFromData(long data){
        return EMPTY_CONTEXT;
    }
}
