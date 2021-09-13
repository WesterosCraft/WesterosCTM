# WesterosCTM

This is a client mod for Forge 1.16.5 that provides additional CTM types for ConnectedTextureMod.

This mod will not function without ConnectedTextureMod 1.16.5 installed.

# Additional types
## type='westerosctm'

This type provides support for classic 47 image CTM.  This logically works akin to the standard 'ctm' type, but rather than being based on
the 5 image compact CTM, this depends upon having the base image (as CTM image 0), along with 46 additional images listed in the     "textures" array.  For example,

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westerosctm",
    "layer": "SOLID",
    "textures": [
      "westerosblocks:block/wood_black/145",
      "westerosblocks:block/wood_black/146",
      "westerosblocks:block/wood_black/147",
      "westerosblocks:block/wood_black/148",
      "westerosblocks:block/wood_black/149",
      "westerosblocks:block/wood_black/150",
      "westerosblocks:block/wood_black/151",
      "westerosblocks:block/wood_black/152",
      "westerosblocks:block/wood_black/153",
      "westerosblocks:block/wood_black/154",
      "westerosblocks:block/wood_black/155",
      "westerosblocks:block/wood_black/156",
      "westerosblocks:block/wood_black/157",
      "westerosblocks:block/wood_black/158",
      "westerosblocks:block/wood_black/159",
      "westerosblocks:block/wood_black/160",
      "westerosblocks:block/wood_black/161",
      "westerosblocks:block/wood_black/162",
      "westerosblocks:block/wood_black/163",
      "westerosblocks:block/wood_black/164",
      "westerosblocks:block/wood_black/165",
      "westerosblocks:block/wood_black/166",
      "westerosblocks:block/wood_black/167",
      "westerosblocks:block/wood_black/168",
      "westerosblocks:block/wood_black/169",
      "westerosblocks:block/wood_black/170",
      "westerosblocks:block/wood_black/171",
      "westerosblocks:block/wood_black/172",
      "westerosblocks:block/wood_black/173",
      "westerosblocks:block/wood_black/174",
      "westerosblocks:block/wood_black/175",
      "westerosblocks:block/wood_black/176",
      "westerosblocks:block/wood_black/177",
      "westerosblocks:block/wood_black/178",
      "westerosblocks:block/wood_black/179",
      "westerosblocks:block/wood_black/180",
      "westerosblocks:block/wood_black/181",
      "westerosblocks:block/wood_black/182",
      "westerosblocks:block/wood_black/183",
      "westerosblocks:block/wood_black/184",
      "westerosblocks:block/wood_black/185",
      "westerosblocks:block/wood_black/186",
      "westerosblocks:block/wood_black/187",
      "westerosblocks:block/wood_black/188",
      "westerosblocks:block/wood_black/189",
      "westerosblocks:block/wood_black/190"
    ]
  }
}
</code>

## type='westerosctmsingle'

This is the same as 'westerosctm' in that it implements 47 texture classic CTM, except that this version relies on one additional image in 'textures', corresponding to the 12 columns and 4 rows template image for CTM (such as found at https://preview.redd.it/i22o64bmavb31.png?auto=webp&s=814a72b4ef512b75592bb87cb5f721afbcbe0be8 ).  For example, 

<code>
{
  "ctm": {
    "ctm_version": 1,
    "type": "westerosctmsingle",
    "layer": "SOLID",
    "textures": [
      "westerosblocks:block/ctm_test"
    ]
  }
}
</code>


