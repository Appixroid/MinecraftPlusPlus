package net.minecraft.enchantment;

import fr.minecraftpp.inventory.EntityArmorSlot;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public enum EnumEnchantmentType
{
	ALL
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			for (EnumEnchantmentType enumenchantmenttype : EnumEnchantmentType.values())
			{
				if (enumenchantmenttype != EnumEnchantmentType.ALL && enumenchantmenttype.canEnchantItem(itemIn))
				{
					return true;
				}
			}

			return false;
		}
	},
	ARMOR
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemArmor;
		}
	},
	ARMOR_FEET
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemArmor && ((ItemArmor) itemIn).armorType == EntityArmorSlot.FEET;
		}
	},
	ARMOR_LEGS
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemArmor && ((ItemArmor) itemIn).armorType == EntityArmorSlot.LEGS;
		}
	},
	ARMOR_CHEST
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemArmor && ((ItemArmor) itemIn).armorType == EntityArmorSlot.CHEST;
		}
	},
	ARMOR_HEAD
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemArmor && ((ItemArmor) itemIn).armorType == EntityArmorSlot.HEAD;
		}
	},
	WEAPON
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemSword;
		}
	},
	DIGGER
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemTool;
		}
	},
	FISHING_ROD
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemFishingRod;
		}
	},
	BREAKABLE
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn.isDamageable();
		}
	},
	BOW
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			return itemIn instanceof ItemBow;
		}
	},
	WEARABLE
	{
		@Override
		public boolean canEnchantItem(Item itemIn)
		{
			boolean flag = itemIn instanceof ItemBlock && ((ItemBlock) itemIn).getBlock() instanceof BlockPumpkin;
			return itemIn instanceof ItemArmor || itemIn instanceof ItemElytra || itemIn instanceof ItemSkull || flag;
		}
	};

	private EnumEnchantmentType()
	{
	}

	/**
	 * Return true if the item passed can be enchanted by a enchantment of this
	 * type.
	 */
	public abstract boolean canEnchantItem(Item itemIn);
}
