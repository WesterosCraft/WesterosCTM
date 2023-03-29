package com.westeroscraft.westerosctm.render;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

// Interface for encode/decode of compacted texture indexes
public interface ITextureWesterosConnectTo {
	// Test for connection in direction
    public boolean connectTo(BlockState from, BlockState to, Direction dir);
}
