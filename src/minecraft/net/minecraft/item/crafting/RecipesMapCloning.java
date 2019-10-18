package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesMapCloning implements IRecipe
{
	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY_ITEM_STACK;

		for (int j = 0; j < inv.getSizeInventory(); ++j)
		{
			ItemStack itemstack1 = inv.getStackInSlot(j);

			if (!itemstack1.isNotValid())
			{
				if (itemstack1.getItem() == Items.FILLED_MAP)
				{
					if (!itemstack.isNotValid())
					{
						return false;
					}

					itemstack = itemstack1;
				}
				else
				{
					if (itemstack1.getItem() != Items.MAP)
					{
						return false;
					}

					++i;
				}
			}
		}

		return !itemstack.isNotValid() && i > 0;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		int i = 0;
		ItemStack itemstack = ItemStack.EMPTY_ITEM_STACK;

		for (int j = 0; j < inv.getSizeInventory(); ++j)
		{
			ItemStack itemstack1 = inv.getStackInSlot(j);

			if (!itemstack1.isNotValid())
			{
				if (itemstack1.getItem() == Items.FILLED_MAP)
				{
					if (!itemstack.isNotValid())
					{
						return ItemStack.EMPTY_ITEM_STACK;
					}

					itemstack = itemstack1;
				}
				else
				{
					if (itemstack1.getItem() != Items.MAP)
					{
						return ItemStack.EMPTY_ITEM_STACK;
					}

					++i;
				}
			}
		}

		if (!itemstack.isNotValid() && i >= 1)
		{
			ItemStack itemstack2 = new ItemStack(Items.FILLED_MAP, i + 1, itemstack.getMetadata());

			if (itemstack.hasDisplayName())
			{
				itemstack2.setStackDisplayName(itemstack.getDisplayName());
			}

			if (itemstack.hasTagCompound())
			{
				itemstack2.setTagCompound(itemstack.getTagCompound());
			}

			return itemstack2;
		}
		else
		{
			return ItemStack.EMPTY_ITEM_STACK;
		}
	}

	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY_ITEM_STACK;
	}

	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>getInstanceFilledWith(inv.getSizeInventory(), ItemStack.EMPTY_ITEM_STACK);

		for (int i = 0; i < nonnulllist.size(); ++i)
		{
			ItemStack itemstack = inv.getStackInSlot(i);

			if (itemstack.getItem().hasContainerItem())
			{
				nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
			}
		}

		return nonnulllist;
	}

	public boolean hideInCraftingTabs()
	{
		return true;
	}

	public boolean checkIfCraftingMatrixSizeIsCorrect(int p_194133_1_, int p_194133_2_)
	{
		return p_194133_1_ >= 3 && p_194133_2_ >= 3;
	}
}
