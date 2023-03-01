package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPillar;
import com.westeroscraft.westerosctm.render.TextureWesterosPillar;
import com.westeroscraft.westerosctm.render.TextureWesterosVertical;

import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextPillar;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

@TextureType("westeros_vertical")
public class TextureTypeWesterosVertical implements ITextureType {

    @Override
    public ICTMTexture<TextureTypeWesterosVertical> makeTexture(TextureInfo info) {
        return new TextureWesterosVertical(this, info);
    }
    
    @Override
    public TextureContextWesterosPillar getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosPillar(world, pos);
    }
    
    @Override
    public int requiredTextures() {
        return 1;
    }

    @Override
    public ITextureContext getContextFromData(long data){
        return new TextureContextPillar(data);
    }
}
