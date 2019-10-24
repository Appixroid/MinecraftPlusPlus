package net.minecraft.enchantment;

import fr.minecraftpp.inventory.EntityEquipmentSlot;

public class EnchantmentBindingCurse extends Enchantment
{
	public EnchantmentBindingCurse(Enchantment.Rarity p_i47254_1_, EntityEquipmentSlot... p_i47254_2_)
	{
		super(p_i47254_1_, EnumEnchantmentType.WEARABLE, p_i47254_2_);
		this.setName("binding_curse");
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment
	 * level passed.
	 */
	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 25;
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment
	 * level passed.
	 */
	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return 50;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel()
	{
		return 1;
	}

	@Override
	public boolean isTreasureEnchantment()
	{
		return true;
	}

	@Override
	public boolean func_190936_d()
	{
		return true;
	}
}
