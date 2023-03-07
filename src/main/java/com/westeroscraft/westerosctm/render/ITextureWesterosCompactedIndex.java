package com.westeroscraft.westerosctm.render;

// Interface for encode/decode of compacted texture indexes
public interface ITextureWesterosCompactedIndex {
	// Get texture index from compacted
	public int getTextureIndexFromCompacted(int compacted);
	// Get row from compacted, given texture index
	public int getRowFromCompactedIndex(int compacted, int textureIdx);
	// Get column from compacted, given texture index
	public int getColumnFromCompactedIndex(int compacted, int textureIdx);
	// Make compacted index, given texture index, row, column
	public int getCompactedIndexFromTextureRowColumn(int textureIndex, int row, int column);
}
