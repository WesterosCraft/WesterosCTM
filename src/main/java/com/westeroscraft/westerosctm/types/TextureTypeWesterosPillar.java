package com.westeroscraft.westerosctm.types;

import com.westeroscraft.westerosctm.WesterosCTM;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPillar;
import com.westeroscraft.westerosctm.render.TextureWesterosPillar;

import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@TextureType("westeros_pillar")
public class TextureTypeWesterosPillar implements ITextureType {

    @Override
    public ICTMTexture<TextureTypeWesterosPillar> makeTexture(TextureInfo info) {
        return new TextureWesterosPillar(this, info);
    }
    
    @Override
    public TextureContextWesterosPillar getBlockRenderContext(BlockState state, BlockGetter world, BlockPos pos, ICTMTexture<?> tex) {
    	LocalPlayer p = Minecraft.getInstance().player;
    	if (p != null) {
    		Holder<Biome> b = p.clientLevel.getBiome(pos);
    		String n = b.unwrap().map((v) -> {
    	         return v.location().toString();
    	      }, (v) -> {
    	         return "[unregistered " + v + "]";
    	      });
    		WesterosCTM.LOGGER.info("pos=" + pos.toString() + ", biome=" +n);
    	}
        return new TextureContextWesterosPillar(world, pos, (TextureWesterosPillar) tex);
    }
    
    @Override
    public int requiredTextures() {
        return 2;
    }

    @Override
    public ITextureContext getContextFromData(long data){
		throw new UnsupportedOperationException();
    }
}
