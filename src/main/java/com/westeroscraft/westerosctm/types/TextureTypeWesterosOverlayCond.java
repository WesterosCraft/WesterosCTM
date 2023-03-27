package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCond;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Overlay conditional texture - same as westeros_cond, except no quad is added if no conditional substitutions are done (versus the base texture)
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_overlay_cond")
public class TextureTypeWesterosOverlayCond implements ITextureType {
	private static final int compactedDims[] = { TextureWesterosCommon.makeDim(1, 1, 0) };
	@Override
    public ICTMTexture<? extends TextureTypeWesterosOverlayCond> makeTexture(TextureInfo info) {
      //return new TextureWesterosCond(this, info, 1);
		return new TextureWesterosCommon<TextureTypeWesterosOverlayCond>(this, info, compactedDims, true, true);
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
        return 2;
    }

	@Override
	public ITextureContext getContextFromData(long data) {
		throw new UnsupportedOperationException();
	}
}
