package com.westeroscraft.westerosctm.render;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface ConnectToFunction {
    public boolean apply(BlockState from, BlockState to, Direction dir);
}

