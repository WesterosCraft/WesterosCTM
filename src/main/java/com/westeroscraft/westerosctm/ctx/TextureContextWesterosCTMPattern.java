package com.westeroscraft.westerosctm.ctx;

import java.util.EnumMap;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.westeroscraft.westerosctm.render.TextureWesterosCTMPattern;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.client.util.CTMLogic;
import net.minecraft.core.Direction.Axis;
import team.chisel.ctm.client.texture.ctx.TextureContextGrid.Point2i;
import team.chisel.ctm.client.util.FaceOffset;

public class TextureContextWesterosCTMPattern implements ITextureContext {
    
	protected final TextureWesterosCTMPattern tex;
	
    private EnumMap<Direction, CTMLogic> ctmData = new EnumMap<>(Direction.class);
    private final EnumMap<Direction, Point2i> textureCoords = new EnumMap<>(Direction.class);    

    private long data;

    public TextureContextWesterosCTMPattern(@Nonnull BlockState state, BlockGetter world, BlockPos pos, TextureWesterosCTMPattern tex) {
    	this.tex = tex;

        // Since we can only return a long, we must limit to 10 bits of data per face = 60 bits
        Preconditions.checkArgument((tex.xSize * tex.ySize) < 1024, "V* Texture size too large for texture %s", tex.getParticle());

        for (@Nonnull Direction side : Direction.values()) {
            BlockPos modifiedPosition = (tex.xOffset == 0 && tex.yOffset == 0) ? 
        		pos : 
    			pos.offset(FaceOffset.getBlockPosOffsetFromFaceOffset(side, tex.xOffset, tex.yOffset));
            Point2i coords = calculateTextureCoord(modifiedPosition, tex.xSize, tex.ySize, side);
            textureCoords.put(side, coords);
        }

        for (Direction face : Direction.values()) {
            CTMLogic ctm = createCTM(state);
            ctm.createSubmapIndices(world, pos, face);
            ctmData.put(face, ctm);
            int idx = tex.getSpriteIndex(ctm);
            if (idx == tex.MIDDLE_TILE_INDEX) {	// If middle, use texture index (plus 48, to non-overlap with CTM indexes)
            	Point2i p = getTextureCoords(face);
            	this.data |= (p.getX() + (p.getY() * tex.xSize) + 48) << (face.ordinal() * 10);
            }
            else {
            	this.data |= idx << (face.ordinal() * 10);
            }
        }
    }

    protected Point2i calculateTextureCoord(BlockPos pos, int w, int h, Direction side) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int tx, ty;

        // Calculate submap x/y from x/y/z by ignoring the direction which the side is offset on
        // Negate the y coordinate when calculating non-vertical directions, otherwise it is reverse order
        if (side.getAxis().isVertical()) {
            // DOWN || UP
            tx = x % w;
            ty = (side.getStepY() * z + 1) % h;
        } else if (side.getAxis() == Axis.Z) {
            // NORTH || SOUTH
            tx = x % w;
            ty = -y % h;
        } else {
            // WEST || EAST
            tx = (z + 1) % w;
            ty = -y % h;
        }

        // Reverse x order for north and east
        if (side == Direction.NORTH || side == Direction.EAST) {
            tx = (w - tx - 1) % w;
        }

        // Remainder can produce negative values, so wrap around
        if (tx < 0) {
            tx += w;
        }
        if (ty < 0) {
            ty += h;
        }
        
        return new Point2i(tx, ty);
    }
    
    public Point2i getTextureCoords(Direction side) {
    	return textureCoords.get(side);
    }

    protected CTMLogic createCTM(@Nonnull BlockState state) {
        CTMLogic ret = CTMLogic.getInstance()
                .ignoreStates(tex.getIgnoreStates())
                .stateComparator(tex::connectTo);
        ret.disableObscuredFaceCheck = tex.getConnectInside();
        return ret;
    }

    public CTMLogic getCTM(Direction face) {
        return ctmData.get(face);
    }

    @Override
    public long getCompressedData(){
        return this.data;
    }
}