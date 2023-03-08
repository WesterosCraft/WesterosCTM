package com.westeroscraft.westerosctm.render;

import net.minecraft.core.BlockPos;
import team.chisel.ctm.api.util.TextureInfo;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    
    private static class CondRule {
    	SrcTexture[] source = null;	// If defined, only apply rule to source textures with given texture index, column, row
    	String[] biomeNames = null;	// If defined, only apply rule to locations matching one of the biomes
    	int yPosMin = Integer.MIN_VALUE;	// If defined, only apply rule if pos.getY() >= yPosMin
    	int yPosMax = Integer.MAX_VALUE;	// If defined, pnly apply rule if pos.getY() <= yPosMax
    	int rowOut = OUT_EQ_SRC, colOut = OUT_EQ_SRC;		// column, row for texture to be substituted
    	
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
    public int resolveCond(int txtIdx, int txtRow, int txtCol, BlockPos pos, String biomeName, ITextureWesterosCompactedIndex tex) {
    	// Find matching rule, if any
    	for (int i = 0; i < rules.length; i++) {
    		if (rules[i].isMatch(txtIdx, txtRow, txtRow, biomeName, pos)) {
    			int rowOut = rules[i].rowOut;
    			int colOut = rules[i].colOut;
    			// If equivalence map, copy row/col indexes
    			if (rowOut == OUT_EQ_SRC) rowOut = txtRow;
    			if (colOut == OUT_EQ_SRC) colOut = txtCol;
    			// Return index for corresponding texture
    			return tex.getCompactedIndexFromTextureRowColumn(condIndex, rowOut, colOut);
    		}
    	}
		// Return index for existing texture
    	return tex.getCompactedIndexFromTextureRowColumn(txtIdx, txtRow, txtCol);
    }
}
