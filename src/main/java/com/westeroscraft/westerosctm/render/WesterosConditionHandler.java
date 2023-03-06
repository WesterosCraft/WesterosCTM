package com.westeroscraft.westerosctm.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import team.chisel.ctm.api.util.TextureInfo;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class WesterosConditionHandler {
	public static final long COND_REMAPPED_MASK = 0x100L;
	public static final int COND_REMAPPED_SHIFT = 8;
	public static final long COND_ROWOUT_MASK = 0x0F0L;
	public static final int COND_ROWOUT_SHIFT = 4;
	public static final long COND_COLOUT_MASK = 0x00FL;
	public static final int COND_COLOUT_SHIFT = 0;

    public final int condWidth;
    public final int condHeight;
    public final int condIndex;
    
    private static class SrcTexture {
    	int index;
    	int row;
    	int col;
    };
    private static class CondRule {
    	SrcTexture[] source = null;	// If defined, only apply rule to source textures with given texture index, column, row
    	String[] biomeNames = null;	// If defined, only apply rule to locations matching one of the biomes
    	int yPosMin = Integer.MIN_VALUE;	// If defined, only apply rule if pos.getY() >= yPosMin
    	int yPosMax = Integer.MAX_VALUE;	// If defined, pnly apply rule if pos.getY() <= yPosMax
    	int rowOut = 0, colOut = 0;		// column, row for texture to be substituted
    	
    	boolean isMatch(int txtIdx, int txtRow, int txtCol, String biomename, BlockPos pos) {
    		int y = pos.getY();
    		if ((y < yPosMin) || (y > yPosMax)) return false;
    		if (source != null) {
    			boolean match = false;
    			for (int i = 0; (i < source.length) && (!match); i++) {
    				match = (source[i].index == txtIdx) && (source[i].row == txtRow) && (source[i].col == txtCol);
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
                            Preconditions.checkArgument(srcrec.get("index").isJsonPrimitive() && srcrec.get("index").getAsJsonPrimitive().isNumber(), "index must be a number!");
                            Preconditions.checkArgument(srcrec.get("row").isJsonPrimitive() && srcrec.get("row").getAsJsonPrimitive().isNumber(), "row must be a number!");
                            Preconditions.checkArgument(srcrec.get("col").isJsonPrimitive() && srcrec.get("col").getAsJsonPrimitive().isNumber(), "col must be a number!");
                        	SrcTexture stxt = new SrcTexture();
                        	stxt.index = srcrec.get("index").getAsInt();
                        	stxt.row = srcrec.get("row").getAsInt();
                        	stxt.col = srcrec.get("col").getAsInt();
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
    // @param txtX - horizontal offset of match in source texture (e.g. 0-11 for CTM, etc)
    // @param txtY - vertical offset of match in source texture (e.g. 0-3 for CTM, etc)
    // @param world - world for test
    // @param pos - position in world
    public long resolveCond(int txtIdx, int txtX, int txtY, BlockGetter world, BlockPos pos) {
    	String biomeName = "";
    	// Compute biome
    	LocalPlayer p = Minecraft.getInstance().player;
    	if (p != null) {
    		Holder<Biome> b = p.clientLevel.getBiome(pos);
    		biomeName = b.unwrap().map((v) -> {
    	         return v.location().toString();
    	      }, (v) -> {
    	         return "[unregistered " + v + "]";
    	      });
    	}    	
    	// Find matching rule, if any
    	for (int i = 0; i < rules.length; i++) {
    		if (rules[i].isMatch(txtIdx, txtX, txtY, biomeName, pos)) {
    			// Return index for corresponding texture
    			return (1 << COND_REMAPPED_SHIFT) | 
    				(rules[i].rowOut << COND_ROWOUT_SHIFT) |
    				(rules[i].colOut << COND_COLOUT_SHIFT);
    		}
    	}
		// Return index for existing texture
    	return 0;
    }
    
    public final boolean getIsRemapped(long compressedData) { return (compressedData & COND_REMAPPED_MASK) != 0; }
    public final int getColOut(long compressedData) { return (int)((compressedData & COND_ROWOUT_MASK) >> COND_ROWOUT_SHIFT); }
    public final int getRowOut(long compressedData) { return (int)((compressedData & COND_COLOUT_MASK) >> COND_COLOUT_SHIFT); }

}
