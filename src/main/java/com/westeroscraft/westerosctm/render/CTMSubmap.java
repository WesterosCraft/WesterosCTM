package com.westeroscraft.westerosctm.render;

import team.chisel.ctm.api.texture.ISubmap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

// For single 12 x 4 CTM image
public class CTMSubmap implements ISubmap {
    
    public static final ISubmap CTM12X4[];
    
    static {
    	CTM12X4 = new ISubmap[48];
    	for (int x = 0; x < 12; x++) {
    		for (int y = 0; y < 4; y++) {
    			CTM12X4[x + (12*y)] = new CTMSubmap(x, y);
    		}
    	}
    }
    
    private final float width, height;
    private final float xOffset, yOffset;

    private final SubmapNormalized normalized = new SubmapNormalized(this);

    private static final float FACTOR = 16f;
    
    public CTMSubmap(float xoff, float yoff) {
    	width = FACTOR;
    	height = FACTOR;
    	xOffset = xoff * FACTOR;
    	yOffset = yoff * FACTOR;
    }
    
    @Override
    public float getInterpolatedU(TextureAtlasSprite sprite, float u) {
        return sprite.getU(getXOffset() + u / getWidth());
    }

    @Override
    public float getInterpolatedV(TextureAtlasSprite sprite, float v) {
        return sprite.getV(getYOffset() + v / getWidth());
    }

    @Override
    public float[] toArray() {
        return new float[] { getXOffset(), getYOffset(), getXOffset() + getWidth(), getYOffset() + getHeight() };
    }

    @Override
    public SubmapNormalized normalize() {
        return normalized;
    }

    @Override
    public ISubmap relativize() {
        return this;
    }

    private static final float XFACTOR = 12 * FACTOR;
    private static final float YFACTOR = 4 * FACTOR;

    private static class SubmapNormalized implements ISubmap {

        private final ISubmap parent;

        SubmapNormalized(ISubmap p) {
        	parent = p;
        }
        
        @Override
        public float getXOffset() {
            return parent.getXOffset() / XFACTOR;
        }

        @Override
        public float getYOffset() {
            return parent.getYOffset() / YFACTOR;
        }

        @Override
        public float getWidth() {
            return parent.getWidth() / XFACTOR;
        }

        @Override
        public float getHeight() {
            return parent.getHeight() / YFACTOR;
        }

        @Override
        public ISubmap relativize() {
            return parent;
        }

        @Override
        public ISubmap normalize() {
            return this;
        }

        @Override
        public float getInterpolatedU(TextureAtlasSprite sprite, float u) {
            return parent.getInterpolatedU(sprite, u);
        }

        @Override
        public float getInterpolatedV(TextureAtlasSprite sprite, float v) {
            return parent.getInterpolatedV(sprite, v);
        }

        @Override
        public float[] toArray() {
            return parent.toArray();
        }
    }

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getXOffset() {
		return xOffset;
	}

	@Override
	public float getYOffset() {
		return yOffset;
	}
}
