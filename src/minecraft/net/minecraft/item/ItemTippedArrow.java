package net.minecraft.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemTippedArrow extends ItemArrow
{
	@Override
	public ItemStack getAsStack()
	{
		return PotionUtils.addPotionToItemStack(super.getAsStack(), PotionTypes.POISON);
	}

	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
	{
		EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
		entitytippedarrow.setPotionEffect(stack);
		return entitytippedarrow;
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye
	 * returns 16 items)
	 */
	@Override
	public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab)
	{
		if (this.func_194125_a(itemIn))
		{
			for (PotionType potiontype : PotionType.REGISTRY)
			{
				if (!potiontype.getEffects().isEmpty())
				{
					tab.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potiontype));
				}
			}
		}
	}

	/**
	 * allows items to add custom lines of information to the mouseover
	 * description
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced)
	{
		PotionUtils.addPotionTooltip(stack, tooltip, 0.125F);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("tipped_arrow.effect."));
	}
}
