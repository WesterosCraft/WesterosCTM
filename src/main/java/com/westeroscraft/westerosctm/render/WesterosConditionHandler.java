package com.westeroscraft.westerosctm.render;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import team.chisel.ctm.api.util.TextureInfo;

import static team.chisel.ctm.client.util.ConnectionLocations.EAST;
import static team.chisel.ctm.client.util.ConnectionLocations.NORTH;
import static team.chisel.ctm.client.util.ConnectionLocations.SOUTH;
import static team.chisel.ctm.client.util.ConnectionLocations.WEST;
import static team.chisel.ctm.client.util.ConnectionLocations.UP;
import static team.chisel.ctm.client.util.ConnectionLocations.DOWN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westeroscraft.westerosctm.WesterosCTM;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosHorizontal;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPattern;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPillar;

public class WesterosConditionHandler {
    public final int condWidth;
    public final int condHeight;
    public final int condIndex;
    
    private static class SrcTexture {
    	int index = MATCH_ANY;
    	int row = MATCH_ANY;
    	int col = MATCH_ANY;
    };
    private static final int MATCH_ANY = -1;
    private static final int OUT_EQ_SRC = -1;
    
    public static final String TYPE_HORIZONTAL = "horizontal";
    public static final String TYPE_PATTERN = "pattern";
    public static final String TYPE_VERTICAL = "vertical";
    
    public static final String[] TYPES = new String[] { TYPE_HORIZONTAL, TYPE_PATTERN, TYPE_VERTICAL };
    
    private static class CondRule {
    	SrcTexture[] source = null;	// If defined, only apply rule to source textures with given texture index, column, row
    	String[] biomeNames = null;	// If defined, only apply rule to locations matching one of the biomes
    	int yPosMin = Integer.MIN_VALUE;	// If defined, only apply rule if pos.getY() >= yPosMin
    	int yPosMax = Integer.MAX_VALUE;	// If defined, pnly apply rule if pos.getY() <= yPosMax
    	int rowOut = OUT_EQ_SRC, colOut = OUT_EQ_SRC;		// column, row for texture to be substituted (or origin of pattern)
    	int patWidth = 0, patHeight = 0;	// If nonzero, width and height of pattern with 0,0 at rowOut, colOut
    	String type = null;		// If defined, function tyoe for output - roOut, colOut is origin of mapping specific to type (e.g. 2x2 for 'horizontal' or 'vertical')
    	boolean isMatch(int txtIdx, int txtRow, int txtCol, String biomename, BlockPos pos) {
    		int y = pos.getY();
    		if ((y < yPosMin) || (y > yPosMax)) return false;
    		if (source != null) {
    			boolean match = false;
    			for (int i = 0; (i < source.length) && (!match); i++) {
    				// Match if equal or rule didn't specify value
    				match = ((source[i].index == txtIdx) || (source[i].index == MATCH_ANY)) && 
    						((source[i].row == txtRow) || (source[i].row == MATCH_ANY)) && 
    						((source[i].col == txtCol) || (source[i].col == MATCH_ANY));
    			}
    			if (!match) return false;
    		}
    		if (biomeNames != null) {
    			boolean match = false;
    			for (int i = 0; (i < biomeNames.length) && (!match); i++) {
    				match = biomeNames[i].equals(biomename);
    			}
    			if (!match) return false;
    		}
    		return true;
    	}
    };
    
    private final CondRule[] rules;
    
    public WesterosConditionHandler(TextureInfo info, int condIndex) {
        int cWidth = 1;	// Default to 1 x 1
        int cHeight = 1;
        CondRule[] crules = new CondRule[0];
        if (info.getInfo().isPresent()) {
            JsonObject object = info.getInfo().get();
            if (object.has("condWidth")) {
                Preconditions.checkArgument(object.get("condWidth").isJsonPrimitive() && object.get("condWidth").getAsJsonPrimitive().isNumber(), "condWidth must be a number!");
                cWidth = object.get("condWidth").getAsInt();
            }
            if (object.has("condHeight")) {
                Preconditions.checkArgument(object.get("condHeight").isJsonPrimitive() && object.get("condHeight").getAsJsonPrimitive().isNumber(), "condHeight must be a number!");
                cHeight = object.get("condHeight").getAsInt();
            }
            if (object.has("conds")) {
                Preconditions.checkArgument(object.get("conds").isJsonArray(), "conds must be an array!");
                JsonArray clist = object.getAsJsonArray("conds");
                crules = new CondRule[clist.size()];
                int ruleidx = 0;
                for (JsonElement rec : clist) {
                	JsonObject crec = rec.getAsJsonObject();
                	CondRule crule = new CondRule();
                	crules[ruleidx] = crule;
                	ruleidx++;
                	if (crec.has("sources")) {
                        Preconditions.checkArgument(crec.get("source").isJsonArray(), "source must be an array!");
                        JsonArray srclist = crec.getAsJsonArray("sources");
                        crule.source = new SrcTexture[srclist.size()];
                        int i = 0;
                        for (JsonElement srec : srclist) {
                        	JsonObject srcrec = srec.getAsJsonObject();
                        	SrcTexture stxt = new SrcTexture();
                        	if (srcrec.has("index")) {
                        		Preconditions.checkArgument(srcrec.get("index").isJsonPrimitive() && srcrec.get("index").getAsJsonPrimitive().isNumber(), "index must be a number!");
                            	stxt.index = srcrec.get("index").getAsInt();
                        	}
                        	if (srcrec.has("row")) {
                        		Preconditions.checkArgument(srcrec.get("row").isJsonPrimitive() && srcrec.get("row").getAsJsonPrimitive().isNumber(), "row must be a number!");
                            	stxt.row = srcrec.get("row").getAsInt();
                        	}
                        	if (srcrec.has("col")) {
                        		Preconditions.checkArgument(srcrec.get("col").isJsonPrimitive() && srcrec.get("col").getAsJsonPrimitive().isNumber(), "col must be a number!");
                        		stxt.col = srcrec.get("col").getAsInt();
                        	}
                        	crule.source[i] = stxt;
                        	i++;
                        }
                	}
                	if (crec.has("biomeNames")) {
                        Preconditions.checkArgument(crec.get("biomeNames").isJsonArray(), "biomeNames must be an array!");
                        JsonArray blist = crec.getAsJsonArray("biomeNames");
                		crule.biomeNames = new String[blist.size()];
                		int i = 0;
                		for (JsonElement biome : blist) {
                			String bstr = biome.getAsString();
                			if (bstr.indexOf(':') < 0) { bstr = "minecraft:" + bstr; }
                			crule.biomeNames[i] = bstr;
                			i++;
                		}
                	}
                	if (crec.has("yPosMin")) {
                        Preconditions.checkArgument(crec.get("yPosMin").isJsonPrimitive() && crec.get("yPosMin").getAsJsonPrimitive().isNumber(), "yPosMin must be a number!");
                		crule.yPosMin = crec.get("yPosMin").getAsInt();
                	}
                	if (crec.has("yPosMax")) {
                        Preconditions.checkArgument(crec.get("yPosMax").isJsonPrimitive() && crec.get("yPosMax").getAsJsonPrimitive().isNumber(), "yPosMax must be a number!");
                		crule.yPosMax = crec.get("yPosMax").getAsInt();
                	}	
                	if (crec.has("rowOut")) {
                        Preconditions.checkArgument(crec.get("rowOut").isJsonPrimitive() && crec.get("rowOut").getAsJsonPrimitive().isNumber(), "rowOut must be a number!");
                		crule.rowOut = crec.get("rowOut").getAsInt();
                	}
                	if (crec.has("colOut")) {
                        Preconditions.checkArgument(crec.get("colOut").isJsonPrimitive() && crec.get("colOut").getAsJsonPrimitive().isNumber(), "colOut must be a number!");
                		crule.colOut = crec.get("colOut").getAsInt();
                	}	
                	if (crec.has("patternWidth")) {
                        Preconditions.checkArgument(crec.get("patternWidth").isJsonPrimitive() && crec.get("patternWidth").getAsJsonPrimitive().isNumber(), "patternWidth must be a number!");
                		crule.patWidth = crec.get("patternWidth").getAsInt();
                		crule.type = TYPE_PATTERN;
                	}
                	if (crec.has("patternHeight")) {
                        Preconditions.checkArgument(crec.get("patternHeight").isJsonPrimitive() && crec.get("patternHeight").getAsJsonPrimitive().isNumber(), "patternHeight must be a number!");
                		crule.patHeight = crec.get("patternHeight").getAsInt();
                		crule.type = TYPE_PATTERN;
                	}	
                	if (crec.has("type")) {
                        Preconditions.checkArgument(crec.get("type").isJsonPrimitive() && crec.get("type").getAsJsonPrimitive().isString(), "type must be a string!");
                		crule.type = crec.get("type").getAsString();
                		int idx;
                		for (idx = 0; (idx < TYPES.length) && (!TYPES[idx].equals(crule.type)); idx++) {}
                        Preconditions.checkArgument((idx < TYPES.length), "type = " + crule.type + " is not supported!");
                	}
                }
            }
        }
        // Set dimensions of condition map
        this.condHeight = cHeight;
        this.condWidth = cWidth;
        this.condIndex = condIndex;
        this.rules = crules;
    }
    
    // Resolve condition
    // @param txtIdx - index of source texture (0=base texture, etc)
    // @param txtRow - vertical offset of match in source texture (e.g. 0-3 for CTM, etc)
    // @param txtCol - horizontal offset of match in source texture (e.g. 0-11 for CTM, etc)
    // @param world - world for test
    // @param pos - position in world
    // @param biomeName - biome at location
    // @param tex - texture 
    // @param dir - direction of face
    public int resolveCond(int txtIdx, int txtRow, int txtCol, BlockGetter world, BlockPos pos, String biomeName, ITextureWesterosCompactedIndex tex, Direction dir) {
    	// Find matching rule, if any
    	for (int i = 0; i < rules.length; i++) {
    		if (rules[i].isMatch(txtIdx, txtRow, txtRow, biomeName, pos)) {
    			CondRule r = rules[i];
    			int rowOut = r.rowOut;
    			int colOut = r.colOut;
    			// If equivalence map, copy row/col indexes
    			if (rowOut == OUT_EQ_SRC) rowOut = txtRow;
    			if (colOut == OUT_EQ_SRC) colOut = txtCol;
    			// If pattern to apply, apply it
    			if (TYPE_PATTERN.equals(r.type) && ((r.patWidth > 1) || (r.patHeight > 1))) {
    				int rowcol = TextureContextWesterosPattern.getPatternRowCol(pos.getX(), pos.getY(), pos.getZ(), dir, r.patWidth, r.patHeight);
    				rowOut = TextureWesterosCommon.getRow(rowcol) + r.rowOut;
    				colOut = TextureWesterosCommon.getCol(rowcol) + r.colOut;
    			}
    			else if (TYPE_HORIZONTAL.equals(r.type)) {	// If horizontal pattern
    		        BlockState state = world.getBlockState(pos);
    		    	boolean northConn = tex.connectTo(state, world.getBlockState(NORTH.transform(pos)), Direction.NORTH);
    		    	boolean southConn = tex.connectTo(state, world.getBlockState(SOUTH.transform(pos)), Direction.SOUTH);
    		    	boolean eastConn = tex.connectTo(state, world.getBlockState(EAST.transform(pos)), Direction.EAST);
    		    	boolean westConn = tex.connectTo(state, world.getBlockState(WEST.transform(pos)), Direction.WEST);
    				int rowcol = TextureContextWesterosHorizontal.getHorizontalRowCol(northConn, southConn, eastConn, westConn, dir);
    				//WesterosCTM.LOGGER.info(String.format("getHorizontalRowCol(%b, %b, %b, %b, %s) = %08x",
    				//		northConn, southConn, eastConn, westConn, dir.toString(), rowcol));
    				rowOut = TextureWesterosCommon.getRow(rowcol) + r.rowOut;
    				colOut = TextureWesterosCommon.getCol(rowcol) + r.colOut;    				
    			}
    			else if (TYPE_VERTICAL.equals(r.type)) {	// If vertical pattern
    		        BlockState state = world.getBlockState(pos);
    		    	boolean upConn = tex.connectTo(state, world.getBlockState(UP.transform(pos)), Direction.UP);
    		    	boolean downConn = tex.connectTo(state, world.getBlockState(DOWN.transform(pos)), Direction.DOWN);
    				int rowcol = TextureContextWesterosPillar.getPillarRowCol(upConn, downConn, Axis.Y, dir);
    				rowOut = TextureWesterosCommon.getRow(rowcol) + r.rowOut;
    				colOut = TextureWesterosCommon.getCol(rowcol) + r.colOut;    				
    			}
    			// Return index for corresponding texture
    			return tex.getCompactedIndexFromTextureRowColumn(condIndex, rowOut, colOut);
    		}
    	}
		// Return index for existing texture
    	return tex.getCompactedIndexFromTextureRowColumn(txtIdx, txtRow, txtCol);
    }
}
