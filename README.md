# WesterosCTM

This is a client mod for Forge 1.18.2 that provides additional CTM types for ConnectedTexturesMod.

This mod will not function without ConnectedTexturesMod 1.18.2 installed.

Note: all examples below can be found, along with functional templates and images and examples of all 'stock' ConnectedTexturesMod types,
at https://github.com/WesterosCraft/WesterosBlocks/tree/1.18.2/src/main/resources/assets/westerosblocks/textures/block/ctm 

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

## type='westeros_ctm_ctm'

Combines 'westeros_cond' function with 'westeros_ctm' function.  For example,

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_ctm_cond",
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
      "westerosblocks:block/ctm/westerosctm/ctm46",
	  "westerosblocks:block/forest_dirt/dirt0
    ],
    "extra": {
       "condWidth": 1,
       "condHeight": 2,
       "conds": [
          {
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 0,
            "colOut": 0
          },
          {
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
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

## type='westeros_ctm_single_cond'

Combines 'westeros_cond' function with 'westeros_ctm_single'.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_ctm_single_cond",
    "layer": "SOLID",
    "textures": [
      "westerosblocks:block/ctm/westerosctmsingle_template",
	  "westerosblocks:block/forest_dirt/dirt0
    ],
    "extra": {
       "condWidth": 1,
       "condHeight": 2,
       "conds": [
          {
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 0,
            "colOut": 0
          },
          {
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
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

## type='westeros_ctm+pattern_cond'

This is a layered combination of 'westeros_ctm_single' for all tiles but the 'middle' CTM tile (index 26 - no connection on any sides or corners).
For the middle tile, the image is controlled by the second texture template, whose format corresponds to the ConnectedTexturesMod 'pattern'
type.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_ctm+pattern_cond",
    "layer": "SOLID",
    "textures": [
      "westerosblocks:block/ctm/westerosctmpattern_ctmtemplate",
      "westerosblocks:block/ctm/westerosctmpattern_pattern_3x2",
	  "westerosblocks:block/forest_dirt/dirt0
    ],
    "extra": {
      "width": 3,
      "height": 2,
       "condWidth": 1,
       "condHeight": 2,
       "conds": [
          {
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 0,
            "colOut": 0
          },
          {
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
    }
  }
}
</code>

## type='westeros_horizontal'

This is an alternative to the 'ctm_horizontal' method.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_horizontal",
    "layer": "SOLID",
    "textures": [
    ],
    "extra": {
    }
  }
}

## type='westeros_horizontal_cond'

This is 'westeros_cond' method with the 'westeros_horizontal' method

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_horizontal_cond",
    "layer": "SOLID",
    "textures": [
    ],
    "extra": {
       "condWidth": 1,
       "condHeight": 2,
       "conds": [
          {
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 0,
            "colOut": 0
          },
          {
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
    }
  }
}

</code>
## type='westeros_vertical'

This is an alternative to the 'ctm_vertical' method that handles crossed and rotated models better than
the standard one (particularly for stacked plants).

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_vertical",
    "layer": "SOLID",
    "textures": [
    ],
    "extra": {
    }
  }
}
</code>

## type='westeros_vertical_cond'

This adds the 'westeros_cond' features to the 'westeros_vertical' function.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_vertical",
    "layer": "SOLID",
    "textures": [
       "westerosblocks:block/forest_dirt/dirt0
    ],
    "extra": {
       "condWidth": 1,
       "condHeight": 2,
       "conds": [
          {
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 0,
            "colOut": 0
          },
          {
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
    }
  }
}
</code>

## type='westeros_cond'

This is a simple use of the conditional substitution support, allowing a single base image to be replaced with 1 or more
alternative images from a provided 'condWidth'x'condHeight' texture images when a provided conditional rule is matched.
Conditional rules allow conditions based on matching one of a list of biomes and/or the Y coordinate of the block being
within a provided value range.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_cond",
    "layer": "SOLID",
    "textures": [
       "westerosblocks:block/forest_dirt/dirt0
    ],
    "extra": {
       "condWidth": 1,
       "condHeight": 2,
       "conds": [
          {
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 0,
            "colOut": 0
          },
          {
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
    }
  }
}
</code>

## type='westeros_single_cond'

This is a simple use of the conditional substitution support, where the substitution image is the base image, and the
default texture used corresponds to the row=0, col=0 texture from that image.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_single_cond",
    "layer": "SOLID",
    "textures": [
    ],
    "extra": {
       "condWidth": 2,
       "condHeight": 2,
       "conds": [
          {
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 1,
            "colOut": 
          },
          {
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
    }
  }
}
</code>


## type='westeros_pillar'

This is an alternative to the 'pillar' method that specifically leverages the AXIS property
to reliably determine the orientation of RotatedPillarBlock subclasses (such as logs and pillars). If used
on a block that lacks the AXIS property, the block will be treated as being upright (axis=y), connecting vertically.
It also expects only the base texture as a 2x2 map, so is more consistent with "ctm_vertical" than "pillar".

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_pillar",
    "layer": "SOLID",
    "textures": [
    ],
    "extra": {
    }
  }
}
</code>

## type='westeros_pillar_cond'

This adds the 'westeros_cond' features to the 'westeros_pillar' function.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_pillar_cond",
    "layer": "SOLID",
    "textures": [
       "westerosblocks:block/forest_dirt/dirt0
    ],
    "extra": {
       "condWidth": 1,
       "condHeight": 2,
       "conds": [
          {
            "sources": [ { "index": 0, "row": 1, "col": 0 } ],
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 0,
            "colOut": 0
          },
          {
            "sources": [ { "index": 0, "row": 1, "col": 0 } ],
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
    }
  }
}
</code>


## type='westeros_pattern'

This is an alternative to the 'pattern' method.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "pattern",
    "layer": "SOLID",
    "textures": [
    ],
    "extra": {
       "width": 3,
       "height": 3
    }
  }
}
</code>

## type='westeros_pattern_cond'

This adds the 'westeros_cond' features to the 'westeros_pattern' function.

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westeros_pattern_cond",
    "layer": "SOLID",
    "textures": [
    ],
    "extra": {
       "width": 3,
       "height": 3,
       "condWidth": 1,
       "condHeight": 2,
       "conds": [
          {
            "biomeNames": [  
                "minecraft:forest",
     	        "minecraft:flower_forest",
        	    "minecraft:dark_forest",
            	"minecraft:jungle" ],
            "rowOut": 0,
            "colOut": 0
          },
          {
            "yPosMin": 200,
            "yPosMax": 383,
            "rowOut": 0,
            "colOut": 1
          }
     	]       
    }
  }
}
</code>

# connect_to, ignoreState Support
The following of these methods supports the "extra" data settings for "connect_to" and "ignoreState", which
allow for connections to non-identical block states to be considered:

- westeros_ctm
- westeros_ctm_cond
- westeros_ctm_single
- westeros_ctm_single_cond
- westeros_h+v
- westeros_v+h
- westeros_ctm+pattern
- westeros_ctm+pattern_cond
- westeros_horizontal
- westeros_horizontal_cond
- westeros_pillar
- westeros_pillar_cond
- westeros_vertical
- westeros_vertical_cond

# Conditional override support
This is a new feature to enable substitution of textures with alternate textures, based on matching provided
rules sensitive to biome and/or Y coordinate ranges.  The syntax for these settings, which are provided in the
"extra" section, is as follows:

- "condWidth": number of textures wide the provided substitution texture image is - if undefined, 1 is assumed
- "condHeight": number of textures high the provided substitution texture image is - if undefined, 1 is assumed
- "conds": An array of substitution rules.  Each rule is an object with the following fields
   - sources: an optional array of source texture coordinates: each of these objects is formated as follows:
      - index: index number of the input texture - if not defined, any index will match
      - row: base zero row in the input texture - if not defined, any row will match
      - col: base zero column in the input texture - if not defined, any column will match
     If sources is not defined, the rule may match any source texture coordinate.  If provided, the rule only applies
     if the source texture is matches the provided coordinate.  For a westeros_cond, the single source texture would be
     { "index": 0, "row": 0, "col" 0 }, although no sources condition is needed for this case.
   - "biomeList": an optional list that, if defined, provides the fully qualified names for the biomes for which the rule
     will match.  If not in "modid:biomename" form, "minecraft:biomename" is assumed.  If not provided, rule may match any biome.
   - "yPosMin": if specified, minimum Y coordinate for a block in order for the rule to match (Yblock >= yPosMin).  If not specified,
     no lower bound is assumed.
   - "yPosMax": if specified, maximum Y coordinate for a block in order for the rule to match (Yblock <= yPosMax).  If not specified,
     no upper bound is assumed.
   - "type": Tyoe of mapping (if not defined, default is 'simple'):
      - "simple": Simple mapping - matching texture is replaced with texture at row rowOut, column colOut from substitution texture image.
         - rowOut: Specifies row of texture from substitution texture image to use in place of matching texture.  If not defined,
           source "row" is used.
         - colOut: Specifies column of texture from substitution texture image to use in place of matching texture.  If not defined,
           source "col" is used.
      - "pattern": Match mapped to a texture from a repeating pattern grid found at patRow, patCol with dimensions patWidth, patHeight.
         - patRow: row of top left corner of pattern grid in subsitution texture image.  If not defined, 0 is assumed
         - patCol: column of top left corner of pattern grid in subsitution texture image.  If not defined, 0 is assumed
         - patWidth: number of columns in the pattern grid.  If not defined, 1 is assumed
         - patHeight: number of rows in the pattern grod.  If not defined, 1 is assumed
      - "ctm": Match mapped to a CTM grid found at ctmRow, ctmCol - width is 12, and height is 4
         - ctmRow: row of top left corner of CTM grid in subsitution texture image.  If not defined, 0 is assumed
         - ctmCol: column of top left corner of CTM grid in subsitution texture image.  If not defined, 0 is assumed
      - "vertical": Match mapped to a vertically connection grid found at ctmRow, ctmCol - width is 2, and height is 2
         - ctmRow: row of top left corner of vertical connection grid in subsitution texture image.  If not defined, 0 is assumed
         - ctmCol: column of top left corner of vertical connection grid in subsitution texture image.  If not defined, 0 is assumed
      - "horizontal": Match mapped to a horizontally connection grid found at ctmRow, ctmCol - width is 2, and height is 2
         - ctmRow: row of top left corner of horizontal connection grid in subsitution texture image.  If not defined, 0 is assumed
         - ctmCol: column of top left corner of horizontal connection grid in subsitution texture image.  If not defined, 0 is assumed
      - "ctm+pattern": Matches mapped using "ctm" pattern, except middle texture (26 - row 2, col 2 of CTM grid) which is mapped using
         "pattern".  Parameters defined are same as those for "ctm" and those for "pattern".
   - "conds": If defined, provides a set of nested rules that will be considered to further map the texture of the containing rule.
      Nested rules are only considered when the owning rule has matched, and is evaluated with the mapped texture as the source
      texture for matching the nested rules.

On CTMs supporting this feature, the substitution texture image is provided as one additional file added to the 'textures' array
(that is, one additional texture file, beyond whatever the given CTM would otherwise expect).

For each use of a texture with the conditional substitution configured, the source texture is determined as usual for the given
type of CTM (for westeros_cond, this is just the base texture - index=0, row=0, col=0).  Then, the rules are evaluated for the
block, in order from first to last.  The first matching rule will result in the texture being replaced with the row=rowOut, col=colOut
texture from the substitution texture image.  If no rule matches, the source texture is uses as normal.

The following CTMs support conditional substitution images:
- westeros_cond
- westeros_single_cond
- westeros_vertical_cond
- westeros_pillar_cond
- westeros_pattern_cond
- westeros_horizontal_cond
- westeros_ctm_cond
- westeros_ctm_single_cond
- westeros_ctm+pattern_cond

