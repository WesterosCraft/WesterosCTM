package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTMPattern;
import com.westeroscraft.westerosctm.render.TextureWesterosCTMPattern;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;

// Combination of classic 47 texture CTM - files are base image + a 12 x 4 atlas image, following MCPatcher/Optifile tile order for CTM - and
// 'pattern' ctm (width x height pattern in second extra image
@OnlyIn(Dist.CLIENT)
@TextureType("westeros_ctm+pattern")
public class TextureTypeWesterosCTMPattern extends TextureTypeWesterosCTMSingle {
	@Override
    public ICTMTexture<? extends TextureTypeWesterosCTM> makeTexture(TextureInfo info) {
		return new TextureWesterosCTMPattern(this, info);
	}
    @Override
    public ITextureContext getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosCTMPattern(state, world, pos, (TextureWesterosCTMPattern) tex);
     }

    @Override
    public int requiredTextures() {
        return 3;
    }
}
