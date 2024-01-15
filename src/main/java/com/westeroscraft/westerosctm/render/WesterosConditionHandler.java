package com.westeroscraft.westerosctm.render;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
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
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.westeroscraft.westerosctm.ctx.TextureContextCommon;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosCTM;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosHorizontal;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPattern;
import com.westeroscraft.westerosctm.ctx.TextureContextWesterosPillar;
import com.westeroscraft.westerosctm.render.TextureWesterosCommon.ConnectionCheck;

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
    
    public static final String TYPE_SIMPLE = "simple";
    public static final String TYPE_HORIZONTAL = "horizontal";
    public static final String TYPE_PATTERN = "pattern";
    public static final String TYPE_VERTICAL = "vertical";
    public static final String TYPE_CTM = "ctm";
    public static final String TYPE_CTM_PATTERN = "ctm+pattern";
    public static final String TYPE_RANDOM = "random";
    public static final String TYPE_EDGES_FULL = "edges-full";
    public static final String TYPE_OVERLAY = "overlay";
    public static final String TYPE_NULL = "null";	// null quad
    
    public static final String[] TYPES = new String[] { TYPE_HORIZONTAL, TYPE_PATTERN, TYPE_VERTICAL, TYPE_CTM, TYPE_CTM_PATTERN, TYPE_RANDOM,
    		TYPE_EDGES_FULL, TYPE_NULL, TYPE_OVERLAY };
    
    private static final ThreadLocal<Random> rand = ThreadLocal.withInitial(() -> new Random());

    private static class CondRule {
    	SrcTexture[] source = null;	// If defined, only apply rule to source textures with given texture index, column, row
    	String[] biomeNames = null;	// If defined, only apply rule to locations matching one of the biomes
    	int yPosMin = Integer.MIN_VALUE;	// If defined, only apply rule if pos.getY() >= yPosMin
    	int yPosMax = Integer.MAX_VALUE;	// If defined, pnly apply rule if pos.getY() <= yPosMax
    	Boolean isFancy = null;		// If defined, only match if client is running fancy mode
    	int rowOut = OUT_EQ_SRC, colOut = OUT_EQ_SRC;		// column, row for texture to be substituted (or origin of pattern)
    	int patWidth = 1, patHeight = 1;	// width and height of pattern with 0,0 at patRow, patCol
    	int patRow = 0, patCol = 0;		// origin of pattern, for pattern and ctm+pattern pattern grid
    	int weights[] = null;	// Weights for type=random (row*width + col]
    	int weightTotal;
    	int rndOffX = 0, rndOffY = 0, rndOffZ = 0;	// position offset for random (for sake of consistency between adjacent blocks)
    	boolean rndSameAllSides = false;
    	String type = TYPE_SIMPLE;
    	CondRule[] conds = null;	// If nested rules
    	
    	private ConnectionCheck connCheck;

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
    		if ((isFancy != null) && (isFancy.booleanValue() != ItemBlockRenderTypes.renderCutout)) {
    			return false;
    		}
    		return true;
    	}
    };
    
    private final CondRule[] conds;
    
    private CondRule parseRule(JsonObject crec, ConnectionCheck parentConnCheck, List<ConnectionCheck> connectionChecks, String defType,
    		int cWidth, int cHeight) {
    	CondRule crule = new CondRule();
        crule.type = defType;	// Assume default type matching parent
    	if (crec.has("sources")) {
            Preconditions.checkArgument(crec.get("sources").isJsonArray(), "sources must be an array!");
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
    	if (crec.has("isFancy")) {
            Preconditions.checkArgument(crec.get("isFancy").isJsonPrimitive() && crec.get("isFancy").getAsJsonPrimitive().isBoolean(), "isFancy must be a boolean");    		
            crule.isFancy = crec.get("isFancy").getAsBoolean();
    	}
    	if (crec.has("type")) {
            Preconditions.checkArgument(crec.get("type").isJsonPrimitive() && crec.get("type").getAsJsonPrimitive().isString(), "type must be a string!");
    		crule.type = crec.get("type").getAsString();
    		int idx;
    		for (idx = 0; (idx < TYPES.length) && (!TYPES[idx].equals(crule.type)); idx++) {}
            Preconditions.checkArgument((idx < TYPES.length), "type = " + crule.type + " is not supported!");
    	}
    	if (crec.has("rowOut")) {
            Preconditions.checkArgument(crec.get("rowOut").isJsonPrimitive() && crec.get("rowOut").getAsJsonPrimitive().isNumber(), "rowOut must be a number!");
    		crule.rowOut = crec.get("rowOut").getAsInt();
    		Preconditions.checkArgument(crule.rowOut < cHeight, "rowOut must be below condHeight");
    	}
    	if (crec.has("colOut")) {
            Preconditions.checkArgument(crec.get("colOut").isJsonPrimitive() && crec.get("colOut").getAsJsonPrimitive().isNumber(), "colOut must be a number!");
    		crule.colOut = crec.get("colOut").getAsInt();
    		Preconditions.checkArgument(crule.colOut < cWidth, "colOut must be below condWidth");
    	}	
        if (crule.type.equals(TYPE_CTM) || crule.type.equals(TYPE_CTM_PATTERN) || crule.type.equals(TYPE_VERTICAL) || crule.type.equals(TYPE_HORIZONTAL) ||
        		crule.type.equals(TYPE_EDGES_FULL) || crule.type.equals(TYPE_OVERLAY)) {
        	if (crec.has("ctmRow")) {
                Preconditions.checkArgument(crec.get("ctmRow").isJsonPrimitive() && crec.get("ctmRow").getAsJsonPrimitive().isNumber(), "ctmRow must be a number!");
        		crule.rowOut = crec.get("ctmRow").getAsInt();
        		Preconditions.checkArgument(crule.rowOut < cHeight, "ctmRow must be below condHeight");
        	}
        	if (crec.has("ctmCol")) {
                Preconditions.checkArgument(crec.get("ctmCol").isJsonPrimitive() && crec.get("ctmCol").getAsJsonPrimitive().isNumber(), "ctmCol must be a number!");
        		crule.colOut = crec.get("ctmCol").getAsInt();
        		Preconditions.checkArgument(crule.colOut < cWidth, "ctmCol must be below condWidth");
        	}	
            if (crule.type.equals(TYPE_CTM) || crule.type.equals(TYPE_CTM_PATTERN)) { // 12x4
        		Preconditions.checkArgument((crule.rowOut + 4) <= cHeight, "ctmRow+4 must be below condHeight");
        		Preconditions.checkArgument((crule.colOut + 12) <= cWidth, "ctmCol+12 must be below condWidth");            	
            }
            else if (crule.type.equals(TYPE_VERTICAL) || crule.type.equals(TYPE_HORIZONTAL)) { // 2x2
        		Preconditions.checkArgument((crule.rowOut + 2) <= cHeight, "ctmRow+2 must be below condHeight");
        		Preconditions.checkArgument((crule.colOut + 2) <= cWidth, "ctmCol+2 must be below condWidth");            	            	
            }
            else if (crule.type.equals(TYPE_EDGES_FULL)) { // 4x4
        		Preconditions.checkArgument((crule.rowOut + 4) <= cHeight, "ctmRow+4 must be below condHeight");
        		Preconditions.checkArgument((crule.colOut + 4) <= cWidth, "ctmCol+4 must be below condWidth");            	
            }
            else if (crule.type.equals(TYPE_OVERLAY)) {	// 7x3
        		Preconditions.checkArgument((crule.rowOut + 3) <= cHeight, "ctmRow+3 must be below condHeight");
        		Preconditions.checkArgument((crule.colOut + 7) <= cWidth, "ctmCol+7 must be below condWidth");            	
            }
        }
        if (crule.type.equals(TYPE_RANDOM)) {
        	if (crec.has("rndRow")) {
                Preconditions.checkArgument(crec.get("rndRow").isJsonPrimitive() && crec.get("rndRow").getAsJsonPrimitive().isNumber(), "rndRow must be a number!");
        		crule.patRow = crec.get("rndRow").getAsInt();
        		Preconditions.checkArgument(crule.patRow < cHeight, "rndRow must be below condHeight");
        	}
        	if (crec.has("rndCol")) {
                Preconditions.checkArgument(crec.get("rndCol").isJsonPrimitive() && crec.get("rndCol").getAsJsonPrimitive().isNumber(), "rndCol must be a number!");
        		crule.patCol = crec.get("rndCol").getAsInt();
        		Preconditions.checkArgument(crule.patCol < cWidth, "rndCol must be below condWidth");
        	}
        	if (crec.has("rndWidth")) {
                Preconditions.checkArgument(crec.get("rndWidth").isJsonPrimitive() && crec.get("rndWidth").getAsJsonPrimitive().isNumber(), "rndWidth must be a number!");
        		crule.patWidth = crec.get("rndWidth").getAsInt();
        		Preconditions.checkArgument((crule.patCol + crule.patWidth) <= cWidth, "rndRow+rndWidth must be less than or equal to condWidth");
        	}
        	if (crec.has("rndHeight")) {
                Preconditions.checkArgument(crec.get("rndHeight").isJsonPrimitive() && crec.get("rndHeight").getAsJsonPrimitive().isNumber(), "rndHeight must be a number!");
        		crule.patHeight = crec.get("rndHeight").getAsInt();
        		Preconditions.checkArgument((crule.patRow + crule.patHeight) <= cHeight, "rndRow+rndHeight must be less than or equal to condHeight");
        	}	
        	if (crec.has("rndOffX")) {
                Preconditions.checkArgument(crec.get("rndOffX").isJsonPrimitive() && crec.get("rndOffX").getAsJsonPrimitive().isNumber(), "rndOffX must be a number!");
        		crule.rndOffX = crec.get("rndOffX").getAsInt();
        	}
        	if (crec.has("rndOffY")) {
                Preconditions.checkArgument(crec.get("rndOffY").isJsonPrimitive() && crec.get("rndOffY").getAsJsonPrimitive().isNumber(), "rndOffY must be a number!");
        		crule.rndOffY = crec.get("rndOffY").getAsInt();
        	}
        	if (crec.has("rndOffZ")) {
                Preconditions.checkArgument(crec.get("rndOffZ").isJsonPrimitive() && crec.get("rndOffZ").getAsJsonPrimitive().isNumber(), "rndOffZ must be a number!");
        		crule.rndOffZ = crec.get("rndOffZ").getAsInt();
        	}
        	if (crec.has("rndSameAllSides")) {
        		Preconditions.checkArgument(crec.get("rndSameAllSides").isJsonPrimitive() && crec.get("rndSameAllSides").getAsJsonPrimitive().isBoolean(), "rndSameAllSides must be a boolean!");
        		crule.rndSameAllSides = crec.get("rndSameAllSides").getAsBoolean();
        	}
            Preconditions.checkArgument((crule.patHeight * crule.patWidth) > 0, "patternWidth and patternHeight must be nonzero!");
        	crule.weights = new int[crule.patHeight * crule.patWidth];
        	Arrays.fill(crule.weights, 1);
        	if (crec.has("weights")) {
                Preconditions.checkArgument(crec.get("weights").isJsonArray(), "weights must be an array!");
                JsonArray blist = crec.getAsJsonArray("weights");
                for (int i = 0; i < crule.weights.length && i < blist.size(); i++) {
                	JsonElement elem = blist.get(i);
                    Preconditions.checkArgument(elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isNumber(), "weights must be numbers!");
                	crule.weights[i] = elem.getAsInt();
                }
        	}
        	crule.weightTotal = 0;
        	for (int i = 0; i < crule.weights.length; i++) {
        		crule.weightTotal += crule.weights[i];
        	}
    	}
        if (crule.type.equals(TYPE_PATTERN) || (crule.type.equals(TYPE_CTM_PATTERN))) {
        	if (crec.has("patRow")) {
                Preconditions.checkArgument(crec.get("patRow").isJsonPrimitive() && crec.get("patRow").getAsJsonPrimitive().isNumber(), "patRow must be a number!");
        		crule.patRow = crec.get("patRow").getAsInt();
        		Preconditions.checkArgument(crule.patRow < cHeight, "patRow must be below condHeight");
        	}
        	if (crec.has("patCol")) {
                Preconditions.checkArgument(crec.get("patCol").isJsonPrimitive() && crec.get("patCol").getAsJsonPrimitive().isNumber(), "patCol must be a number!");
        		crule.patCol = crec.get("patCol").getAsInt();
        		Preconditions.checkArgument(crule.patCol < cWidth, "patCol must be below condWidth");
        	}
        	if (crec.has("patWidth")) {
                Preconditions.checkArgument(crec.get("patWidth").isJsonPrimitive() && crec.get("patWidth").getAsJsonPrimitive().isNumber(), "patWidth must be a number!");
        		crule.patWidth = crec.get("patWidth").getAsInt();
        		Preconditions.checkArgument((crule.patCol + crule.patWidth) <= cWidth, "patRow+patWidth must be less than or equal to condWidth");
        	}
        	if (crec.has("patHeight")) {
                Preconditions.checkArgument(crec.get("patHeight").isJsonPrimitive() && crec.get("patHeight").getAsJsonPrimitive().isNumber(), "patHeight must be a number!");
        		crule.patHeight = crec.get("patHeight").getAsInt();
        		Preconditions.checkArgument((crule.patRow + crule.patHeight) <= cHeight, "patRow+patHeight must be less than or equal to condHeight");
        	}	
            Preconditions.checkArgument((crule.patHeight * crule.patWidth > 0), "patWidth and patHeight must be nonzero!");
        }
    	boolean newConn = false;
    	boolean ignoreStates = false;
    	BiPredicate<Direction, BlockState> connCheck = null;
    	String connect_to_tag = null;
			String connect_to_state = null;
    	if (crec.has("ignore_states")) {
            Preconditions.checkArgument(crec.get("ignore_states").isJsonPrimitive() && crec.get("ignore_states").getAsJsonPrimitive().isBoolean(), "ignore_states must be a boolean");    		
            ignoreStates = crec.get("ignore_states").getAsBoolean();    		
            newConn = true;
    	}
    	if (crec.has("connect_to")) {
    		connCheck = TextureWesterosCommon.predicateParser.parse(crec.get("connect_to"));
            newConn = true;
    	}
    	if (crec.has("connect_to_tag")) {
    		connect_to_tag = crec.get("connect_to_tag").getAsString();
            newConn = true;
    	}
			if (crec.has("connect_to_state")) {
				connect_to_state = crec.get("connect_to_state").getAsString();
						newConn = true;
			}
    	if (newConn) {	// If new settings, assign new index
    		crule.connCheck = new ConnectionCheck(connectionChecks.size(), ignoreStates, connCheck, connect_to_tag, connect_to_state);
    		connectionChecks.add(crule.connCheck);
    	}
    	else {
    		crule.connCheck = parentConnCheck;
    	}
    	// If any nested rules
    	if (crec.has("conds")) {
            Preconditions.checkArgument(crec.get("conds").isJsonArray(), "conds must be an array!");
            JsonArray clist = crec.getAsJsonArray("conds");
            crule.conds = new CondRule[clist.size()];
            int ruleidx = 0;
            for (JsonElement rec : clist) {
            	JsonObject crec2 = rec.getAsJsonObject();
            	CondRule crule2 = parseRule(crec2, crule.connCheck, connectionChecks, defType, cWidth, cHeight);                	
            	crule.conds[ruleidx] = crule2;
            	ruleidx++;
            }
    	}
    	return crule;
    }
    
    public int connectionCount = 1;	// Always assume at least top level
    
    public WesterosConditionHandler(TextureInfo info, int condIndex, List<ConnectionCheck> connectionChecks, String defType, int defWidth, int defHeight) {
        int cWidth = defWidth;
        int cHeight = defHeight;
        CondRule[] crules = null;
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
                	CondRule crule = parseRule(crec, connectionChecks.get(0), connectionChecks, defType, cWidth, cHeight);                	
                	crules[ruleidx] = crule;
                	ruleidx++;
                }
            }
        }
        // Set dimensions of condition map
        this.condHeight = cHeight;
        this.condWidth = cWidth;
        this.condIndex = condIndex;
        this.conds = (crules == null) ? new CondRule[0] : crules;        
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
    // @param ctmConnBits - connection bits (if computed at top level, else null)
    public int resolveCond(final int txtIdx, final int txtRow, final int txtCol, BlockGetter world, BlockPos pos, String biomeName, ITextureWesterosCompactedIndex tex,
		Direction dir, long[] ctmConnBits) {
    	CondRule[] rules = this.conds;	// Start at top
    	// Default is to be unchanged
    	int txtOut = txtIdx;
		int rowOut = txtRow;
		int colOut = txtCol;
		BlockState state = null;
		while (rules != null) {
			boolean matched = false;
	    	// Find matching rule, if any
	    	for (int i = 0; (!matched) && (i < rules.length); i++) {
	    		if (rules[i].isMatch(txtOut, rowOut, colOut, biomeName, pos)) {
	    			CondRule r = rules[i];
	    			// We matched (at least almost all the time)
	    			matched = true;
	    			// If pattern to apply, apply it
	    			if (TYPE_PATTERN.equals(r.type)) {
	    				int rowcol = TextureContextWesterosPattern.getPatternRowCol(pos.getX(), pos.getY(), pos.getZ(), dir, r.patHeight, r.patWidth);
	    				rowOut = TextureWesterosCommon.getRow(rowcol) + r.patRow;
	    				colOut = TextureWesterosCommon.getCol(rowcol) + r.patCol;
		    			txtOut = condIndex;
	    			}
	    			else if (TYPE_RANDOM.equals(r.type)) {
	    				Random rnd = rand.get();
						if (r.rndOffX == 0 && r.rndOffY == 0 && r.rndOffZ == 0) {
				            rnd.setSeed(Mth.getSeed(pos) + (r.rndSameAllSides ? 0 : dir.ordinal()));
	    				}
	    				else {	// Apply offset, if nonzero
				            rnd.setSeed(Mth.getSeed(pos.getX() + r.rndOffX, pos.getY() + r.rndOffY, pos.getZ() + r.rndOffZ) + (r.rndSameAllSides ? 0 : dir.ordinal()));
	    				}
			            rnd.nextBoolean();
	    				int w = rnd.nextInt(r.weightTotal);
	    				int rowcol = 0;
	    				for (int idx = 0; idx < r.weights.length; idx++) {
	    					w = w - r.weights[idx];
	    					if (w < 0) break;
	    					rowcol++;
	    				}
	    				rowOut = (rowcol / r.patWidth) + r.patRow;
	    				colOut = (rowcol % r.patWidth) + r.patCol;
		    			txtOut = condIndex;
	    			}
	    			else if (TYPE_HORIZONTAL.equals(r.type)) {	// If horizontal pattern
	    		        if (state == null) state = world.getBlockState(pos);
	    		        ConnectionCheck cc = r.connCheck;
	    		    	boolean northConn = cc.connectTo(state, world.getBlockState(NORTH.transform(pos)), Direction.NORTH);
	    		    	boolean southConn = cc.connectTo(state, world.getBlockState(SOUTH.transform(pos)), Direction.SOUTH);
	    		    	boolean eastConn = cc.connectTo(state, world.getBlockState(EAST.transform(pos)), Direction.EAST);
	    		    	boolean westConn = cc.connectTo(state, world.getBlockState(WEST.transform(pos)), Direction.WEST);
	    				int rowcol = TextureContextWesterosHorizontal.getHorizontalRowCol(northConn, southConn, eastConn, westConn, dir);
	    				//WesterosCTM.LOGGER.info(String.format("getHorizontalRowCol(%b, %b, %b, %b, %s) = %08x",
	    				//		northConn, southConn, eastConn, westConn, dir.toString(), rowcol));
	    				rowOut = TextureWesterosCommon.getRow(rowcol) + r.rowOut;
	    				colOut = TextureWesterosCommon.getCol(rowcol) + r.colOut;    				
		    			txtOut = condIndex;
	    			}
	    			else if (TYPE_VERTICAL.equals(r.type)) {	// If vertical pattern
	    		        if (state == null) state = world.getBlockState(pos);
	    		        ConnectionCheck cc = r.connCheck;
	    		    	boolean upConn = cc.connectTo(state, world.getBlockState(UP.transform(pos)), Direction.UP);
	    		    	boolean downConn = cc.connectTo(state, world.getBlockState(DOWN.transform(pos)), Direction.DOWN);
	    				int rowcol = TextureContextWesterosPillar.getPillarRowCol(upConn, downConn, Axis.Y, dir);
	    				rowOut = TextureWesterosCommon.getRow(rowcol) + r.rowOut;
	    				colOut = TextureWesterosCommon.getCol(rowcol) + r.colOut;    				
		    			txtOut = condIndex;
	    			}
	    			else if (TYPE_CTM.equals(r.type)) {	// If CTM pattern (12 x 4)
	    				int ccidx = r.connCheck.connIndex;
	    				// If not computed, compute connection bits
	    				if (ctmConnBits == null) {
	    					ctmConnBits = TextureContextWesterosCTM.buildCTMConnectionBits(world, pos, tex.getConnectionChecks());
	    				}
	    				// Get sprite index
	    				int spriteIndex = TextureContextWesterosCTM.getSpriteIndex(ctmConnBits[ccidx], dir);
	    				rowOut = (spriteIndex / 12) + r.rowOut;
	    				colOut = (spriteIndex % 12) + r.colOut;    				
		    			txtOut = condIndex;
	    			}
	    			else if (TYPE_CTM_PATTERN.equals(r.type)) {	// If CTM pattern (12 x 4) + pattern (patternWidth, patternHeight)
	    				int ccidx = r.connCheck.connIndex;
	    				// If not computed, compute connection bits
	    				if (ctmConnBits == null) {
	    					ctmConnBits = TextureContextWesterosCTM.buildCTMConnectionBits(world, pos, tex.getConnectionChecks());
	    				}
	    				// Get sprite index
	    				int spriteIndex = TextureContextWesterosCTM.getSpriteIndex(ctmConnBits[ccidx], dir);
	    				if (spriteIndex == TextureContextWesterosCTM.MIDDLE_TILE_INDEX) {
	    			    	// Get pattern index
	    			    	int rowcol = TextureContextWesterosPattern.getPatternRowCol(pos.getX(), pos.getY(), pos.getZ(), 
	    			    			dir, r.patHeight, r.patWidth);
	    			    	rowOut = TextureWesterosCommon.getRow(rowcol) + r.patRow;
	    			    	colOut = TextureWesterosCommon.getCol(rowcol) + r.patCol;
	    				}
	    				else {
	    					rowOut = (spriteIndex / 12) + r.rowOut;
	    					colOut = (spriteIndex % 12) + r.colOut;    		
	    				}
		    			txtOut = condIndex;
	    			}
	    			else if (TYPE_EDGES_FULL.equals(r.type)) {	// If edges full pattern (4 x 4, with original fallthrough)
	    				int ccidx = r.connCheck.connIndex;
	    				// If not computed, compute connection bits
	    				if (ctmConnBits == null) {
	    					ctmConnBits = TextureContextWesterosCTM.buildCTMConnectionBits(world, pos, tex.getConnectionChecks());
	    				}
	    				// Get sprite index
	    				int spriteIndex = TextureContextWesterosCTM.getFullEdgeIndex(ctmConnBits[ccidx], dir);
	    				if (spriteIndex >= 0) {	// Only if remapped
	    					rowOut = (spriteIndex / 4) + r.rowOut;
	    					colOut = (spriteIndex % 4) + r.colOut;   
	    	    			txtOut = condIndex;
	    				}
	    				else {	// If sprite, fall through to next rule
	    					matched = false;
	    				}
	    			}
	    			else if (TYPE_OVERLAY.equals(r.type)) {	// If overlay (17 textures, 7 x 3 pattern - as in Optifine)
	    				int ccidx = r.connCheck.connIndex;
	    				// If not computed, compute connection bits
	    				if (ctmConnBits == null) {
	    					ctmConnBits = TextureContextWesterosCTM.buildCTMConnectionBits(world, pos, tex.getConnectionChecks());
	    				}
	    				// Get sprite index
	    				int spriteIndex = TextureContextWesterosCTM.getOverlayIndex(ctmConnBits[ccidx], dir);
	    				if (spriteIndex >= 0) {	// Only if remapped
	    					rowOut = (spriteIndex / 7) + r.rowOut;
	    					colOut = (spriteIndex % 7) + r.colOut;   
	    	    			txtOut = condIndex;
	    				}
	    				else {	// If sprite, fall through to next rule
	    					matched = false;
	    				}
	    			}
	    			else if (TYPE_NULL.equals(r.type)) {	// If null
	    				// Return null quad - no nesting supported beyond this
	    				return TextureContextCommon.COMPACTED_INDEX_NULL_QUAD;
	    			}
	    			else {
		    			// Map to new texture and location
		    			rowOut = (r.rowOut == OUT_EQ_SRC) ? rowOut : r.rowOut;
		    			colOut = (r.colOut == OUT_EQ_SRC) ? colOut : r.colOut;
		    			txtOut = condIndex;
	    			}
	    			// If matched, check out nested rules
	    			if (matched) {
	    				rules = r.conds;
	    			}
	    		}
	    	}
	    	// If no match, we're done
	    	if (!matched) {
	    		rules = null;
	    	}
		}
		// Return index for matching texture
		return tex.getCompactedIndexFromTextureRowColumn(txtOut, rowOut, colOut);
    }
}
