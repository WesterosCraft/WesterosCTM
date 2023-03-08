package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPillar;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon;

import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@TextureType("westeros_vertical_cond")
public class TextureTypeWesterosVerticalCond extends TextureTypeWesterosPillarCond {
    @Override
    public TextureContextWesterosPillar getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
        return new TextureContextWesterosPillar(world, pos, (TextureWesterosCommon<?>) tex, true);
    }
}
