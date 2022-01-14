package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTM;
import com.westeroscraft.westerosctm.render.TextureWesterosCTM;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Classic 47 texture CTM - files are base image + 46 additional images, following MCPatcher/Optifile tile order for CTM
@TextureType("westeros_ctm")
public class TextureTypeWesterosCTM implements ITextureType {
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTM> makeTexture(TextureInfo info) {
      return new TextureWesterosCTM(this, info);
    }

    @Override
    public ITextureContext getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosCTM(state, world, pos, (TextureWesterosCTM) tex);
     }

    @Override
    public int getQuadsPerSide() {
        return 1;
    }

    @Override
    public int requiredTextures() {
        return 48;
    }

	@Override
	public ITextureContext getContextFromData(long data) {
		throw new UnsupportedOperationException();
	}
}
