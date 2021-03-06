package net.minecraft.enchantment;

import fr.minecraftpp.inventory.EntityEquipmentSlot;

public class EnchantmentFishingSpeed extends Enchantment
{
	protected EnchantmentFishingSpeed(Enchantment.EnchantmentRarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot... slots)
	{
		super(rarityIn, typeIn, slots);
		this.setName("fishingSpeed");
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment
	 * level passed.
	 */
	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 15 + (enchantmentLevel - 1) * 9;
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment
	 * level passed.
	 */
	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel()
	{
		return 3;
	}
}
