package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCond;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon;
import com.westeroscraft.westerosctm.render.WesterosConditionHandler;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Single texture with conditional substitutions, texture is just condWidth x condHeight map, with 0,0 as default image
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_single_cond")
public class TextureTypeWesterosSingleCond extends TextureTypeWesterosCond {
	private static final int compactedDims[] = { };

	@Override
    public ICTMTexture<? extends TextureTypeWesterosCond> makeTexture(TextureInfo info) {
		//return new TextureWesterosCond(this, info, 0);
		return new TextureWesterosCommon<TextureTypeWesterosCond>(this, info, compactedDims, true, WesterosConditionHandler.TYPE_SIMPLE, 1, 1);		
    }

    @Override
    public ITextureContext getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosCond(world, pos, (TextureWesterosCommon<?>) tex);
     }

    @Override
    public int getQuadsPerSide() {
        return 1;
    }

    @Override
    public int requiredTextures() {
        return 1;
    }

	@Override
	public ITextureContext getContextFromData(long data) {
		throw new UnsupportedOperationException();
	}
}
