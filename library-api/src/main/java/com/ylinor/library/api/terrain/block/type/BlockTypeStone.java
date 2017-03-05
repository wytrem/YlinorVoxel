package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.MapColor;
import com.ylinor.library.api.terrain.block.material.Material;

public class BlockTypeStone extends BlockType {

	protected BlockTypeStone(int id) {
		super(id, Material.ROCK);
	}

	public static enum EnumType {
	      STONE(0, MapColor.STONE, "stone", true),
	      GRANITE(1, MapColor.DIRT, "granite", true),
	      GRANITE_SMOOTH(2, MapColor.DIRT, "smooth_granite", "graniteSmooth", false),
	      DIORITE(3, MapColor.QUARTZ, "diorite", true),
	      DIORITE_SMOOTH(4, MapColor.QUARTZ, "smooth_diorite", "dioriteSmooth", false),
	      ANDESITE(5, MapColor.STONE, "andesite", true),
	      ANDESITE_SMOOTH(6, MapColor.STONE, "smooth_andesite", "andesiteSmooth", false);

	      private static final BlockTypeStone.EnumType[] META_LOOKUP = new BlockTypeStone.EnumType[values().length];
	      private final int meta;
	      private final String name;
	      private final String unlocalizedName;
	      private final MapColor mapColor;
	      private final boolean field_190913_m;

	      private EnumType(int p_i46383_3_, MapColor p_i46383_4_, String p_i46383_5_, boolean p_i46383_6_) {
	         this(p_i46383_3_, p_i46383_4_, p_i46383_5_, p_i46383_5_, p_i46383_6_);
	      }

	      private EnumType(int p_i46384_3_, MapColor p_i46384_4_, String p_i46384_5_, String p_i46384_6_, boolean p_i46384_7_) {
	         this.meta = p_i46384_3_;
	         this.name = p_i46384_5_;
	         this.unlocalizedName = p_i46384_6_;
	         this.mapColor = p_i46384_4_;
	         this.field_190913_m = p_i46384_7_;
	      }

	      public int getMetadata() {
	         return this.meta;
	      }

	      public MapColor getMapColor() {
	         return this.mapColor;
	      }

	      public String toString() {
	         return this.name;
	      }

	      public static BlockTypeStone.EnumType byMetadata(int meta) {
	         if(meta < 0 || meta >= META_LOOKUP.length) {
	            meta = 0;
	         }

	         return META_LOOKUP[meta];
	      }

	      public String getName() {
	         return this.name;
	      }

	      public String getUnlocalizedName() {
	         return this.unlocalizedName;
	      }

	      public boolean func_190912_e() {
	         return this.field_190913_m;
	      }

	      static {
	         for(BlockTypeStone.EnumType type : values()) {
	            META_LOOKUP[type.getMetadata()] = type;
	         }
	      }
	   }
}
