package com.westeroscraft.westerosctm.render;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.util.TextureInfo;

public class TextureWesterosCTMPattern<T extends ITextureType> extends TextureWesterosCommon<T> {
	public TextureWesterosCTMPattern(T type, TextureInfo info, boolean conds) {
        super(type, info, new int[3], conds, WesterosConditionHandler.TYPE_CTM_PATTERN, 12, 4);
        int width = 2, height = 2;	// Default
        if (info.getInfo().isPresent()) {
            JsonObject object = info.getInfo().get();
            if (object.has("width") && object.has("height")) {
                Preconditions.checkArgument(object.get("width").isJsonPrimitive() && object.get("width").getAsJsonPrimitive().isNumber(), "width must be a number!");
                Preconditions.checkArgument(object.get("height").isJsonPrimitive() && object.get("height").getAsJsonPrimitive().isNumber(), "height must be a number!");
                width = object.get("width").getAsInt();
                height = object.get("height").getAsInt();

            } else if (object.has("size")) {
                Preconditions.checkArgument(object.get("size").isJsonPrimitive() && object.get("size").getAsJsonPrimitive().isNumber(), "size must be a number!");
                width = object.get("size").getAsInt();
                height = object.get("size").getAsInt();
            } else {
                width = height = 2;
            }
        }
        Preconditions.checkArgument(width > 0 && height > 0, "Cannot have a dimension of 0!");
        // Build and set compacted dims for CTM and pattern
        int[] compactedDims = new int[] { makeDim(1, 1, 0), makeDim(12, 4, 1), makeDim(width, height, 49) };
        computeCompactedDims(compactedDims, conds);
	}
	
}
