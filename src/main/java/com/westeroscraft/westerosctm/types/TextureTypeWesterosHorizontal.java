package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosHorizontal;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.WesterosConditionHandler;

import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@TextureType("westeros_horizontal")
public class TextureTypeWesterosHorizontal implements ITextureType {
	protected static final int compactedDims[] = { TextureWesterosCommon.makeDim(2, 2, 0) };
    @Override
    public ICTMTexture<TextureTypeWesterosHorizontal> makeTexture(TextureInfo info) {
		return new TextureWesterosCommon<TextureTypeWesterosHorizontal>(this, info, compactedDims, false, WesterosConditionHandler.TYPE_HORIZONTAL, 2, 2);
    }
    
    @Override
    public TextureContextWesterosHorizontal getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosHorizontal(world, pos, (TextureWesterosCommon<?>) tex);
    }
    
    @Override
    public int requiredTextures() {
        return 1;
    }

    @Override
    public ITextureContext getContextFromData(long data){
		throw new UnsupportedOperationException();
    }
}
