# WesterosCTM

This is a client mod for Forge 1.16.5 that provides additional CTM types for ConnectedTexturesMod.

This mod will not function without ConnectedTexturesMod 1.16.5 installed.

Note: all examples below can be found, along with functional templates and images and examples of all 'stock' ConnectedTexturesMod types,
at https://github.com/WesterosCraft/WesterosBlocks/tree/1.16.5/src/main/resources/assets/westerosblocks/textures/block/ctm 

# Additional types
## type='westeros_ctm'

This type provides support for classic 47 image CTM.  This logically works akin to the standard 'ctm' type, but rather than being based on
the 5 image compact CTM, this depends upon having 47 images listed in the "textures" array.  For example,

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_ctm",
    "layer": "SOLID",
    "textures": [
      "westerosblocks:block/ctm/westerosctm/ctm0",
      "westerosblocks:block/ctm/westerosctm/ctm1",
      "westerosblocks:block/ctm/westerosctm/ctm2",
      "westerosblocks:block/ctm/westerosctm/ctm3",
      "westerosblocks:block/ctm/westerosctm/ctm4",
      "westerosblocks:block/ctm/westerosctm/ctm5",
      "westerosblocks:block/ctm/westerosctm/ctm6",
      "westerosblocks:block/ctm/westerosctm/ctm7",
      "westerosblocks:block/ctm/westerosctm/ctm8",
      "westerosblocks:block/ctm/westerosctm/ctm9",
      "westerosblocks:block/ctm/westerosctm/ctm10",
      "westerosblocks:block/ctm/westerosctm/ctm11",
      "westerosblocks:block/ctm/westerosctm/ctm12",
      "westerosblocks:block/ctm/westerosctm/ctm13",
      "westerosblocks:block/ctm/westerosctm/ctm14",
      "westerosblocks:block/ctm/westerosctm/ctm15",
      "westerosblocks:block/ctm/westerosctm/ctm16",
      "westerosblocks:block/ctm/westerosctm/ctm17",
      "westerosblocks:block/ctm/westerosctm/ctm18",
      "westerosblocks:block/ctm/westerosctm/ctm19",
      "westerosblocks:block/ctm/westerosctm/ctm20",
      "westerosblocks:block/ctm/westerosctm/ctm21",
      "westerosblocks:block/ctm/westerosctm/ctm22",
      "westerosblocks:block/ctm/westerosctm/ctm23",
      "westerosblocks:block/ctm/westerosctm/ctm24",
      "westerosblocks:block/ctm/westerosctm/ctm25",
      "westerosblocks:block/ctm/westerosctm/ctm26",
      "westerosblocks:block/ctm/westerosctm/ctm27",
      "westerosblocks:block/ctm/westerosctm/ctm28",
      "westerosblocks:block/ctm/westerosctm/ctm29",
      "westerosblocks:block/ctm/westerosctm/ctm30",
      "westerosblocks:block/ctm/westerosctm/ctm31",
      "westerosblocks:block/ctm/westerosctm/ctm32",
      "westerosblocks:block/ctm/westerosctm/ctm33",
      "westerosblocks:block/ctm/westerosctm/ctm34",
      "westerosblocks:block/ctm/westerosctm/ctm35",
      "westerosblocks:block/ctm/westerosctm/ctm36",
      "westerosblocks:block/ctm/westerosctm/ctm37",
      "westerosblocks:block/ctm/westerosctm/ctm38",
      "westerosblocks:block/ctm/westerosctm/ctm39",
      "westerosblocks:block/ctm/westerosctm/ctm40",
      "westerosblocks:block/ctm/westerosctm/ctm41",
      "westerosblocks:block/ctm/westerosctm/ctm42",
      "westerosblocks:block/ctm/westerosctm/ctm43",
      "westerosblocks:block/ctm/westerosctm/ctm44",
      "westerosblocks:block/ctm/westerosctm/ctm45",
      "westerosblocks:block/ctm/westerosctm/ctm46"
    ],
    "extra": {
    }
  }
}
</code>

## type='westeros_ctm_single'

This is the same as 'westeros_ctm' in that it implements 47 texture classic CTM, except that this version relies on one additional image in 'textures', corresponding to the 12 columns and 4 rows template image for CTM (such as found at https://preview.redd.it/i22o64bmavb31.png?auto=webp&s=814a72b4ef512b75592bb87cb5f721afbcbe0be8 ).  For example, 

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_ctm_single",
    "layer": "SOLID",
    "textures": [
      "westerosblocks:block/ctm/westerosctmsingle_template"
    ],
    "extra": {
    }
  }
}
</code>

## type='westeros_h+v'

This is the same as the classic 'horizontal+vertical' CTM from MCPatcher/Optifine.  It requires the base image to be a 7 x 1
image teamplate (as shown by the first image found at https://i.imgur.com/8LZj1Hy.png).

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_h+v",
    "layer": "SOLID",
    "textures": [],
    "extra": {
    }
  }
}
</code>

## type='westeros_v+h'

This is the same as the classic 'vertical+horizontal' CTM from MCPatcher/Optifine.  It requires the base image to be a 7 x 1 
image template (as shown by the second image found at https://i.imgur.com/8LZj1Hy.png).

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_v+h",
    "layer": "SOLID",
    "textures": [],
    "extra": {
    }
  }
}
</code>

## type='westeros_ctm+pattern'

This is a layered combination of 'westeros_ctm_single' for all tiles but the 'middle' CTM tile (index 26 - no connection on any sides or corners).
For the middle tile, the image is controlled by the second texture template, whose format corresponds to the ConnectedTexturesMod 'pattern'
type.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_ctm+pattern",
    "layer": "SOLID",
    "textures": [
      "westerosblocks:block/ctm/westerosctmpattern_ctmtemplate",
      "westerosblocks:block/ctm/westerosctmpattern_pattern_3x2"
    ],
    "extra": {
      "width": 3,
      "height": 2    
    }
  }
}
</code>

