package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTMPattern;
import com.westeroscraft.westerosctm.render.TextureWesterosCTMPattern;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Combination of classic 47 texture CTM - files are base image + a 12 x 4 atlas image, following MCPatcher/Optifile tile order for CTM - and
// 'pattern' ctm (width x height pattern in second extra image
@TextureType("westerosctmpattern")
public class TextureTypeWesterosCTMPattern extends TextureTypeWesterosCTMSingle {
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTM> makeTexture(TextureInfo info) {
		return new TextureWesterosCTMPattern(this, info);
	}
    @Override
    public ITextureContext getBlockRenderContext(BlockState state, IBlockReader world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosCTMPattern(state, world, pos, (TextureWesterosCTMPattern) tex);
     }

    @Override
    public int requiredTextures() {
        return 3;
    }
}
