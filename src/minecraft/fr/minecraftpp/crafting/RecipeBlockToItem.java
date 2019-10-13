package fr.minecraftpp.crafting;

import fr.minecraftpp.anotation.Mod;
import fr.minecraftpp.item.tool.IToolMaterial;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

@Mod("Minecraftpp")
public class RecipeBlockToItem extends GenericRecipe {
	
	private Block material;
	private Item recipeResult;
	
	public RecipeBlockToItem(Block material, Item result) {
		super();
		this.material = material;
		this.recipeResult = result;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int counter = 0;
        for (int i = 0; i < inv.getWidth(); i++)
        {
            for (int j = 0; j < inv.getHeight(); j++)
            {
                ItemStack itemstack = inv.getStackInRowAndColumn(i, j);

                if (!itemstack.isNotValid())
                {
                	Item item = itemstack.getItem();
                    Block block = Block.getBlockFromItem(item);

                    if (block == material)
                    {
                        counter++;
                    }
                }	
            }
        }
        if (counter == 1) 
        {
        	return true;
        }
        else 
        {
        	return false;
        }
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return new ItemStack(recipeResult, 9);
	}

	@Override
	public boolean checkIfCraftingMatrixSizeIsCorrect(int craftingMatrixWidth, int craftingMatrixHeight) {
		return craftingMatrixHeight >= 3 && craftingMatrixWidth >= 3;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(recipeResult, 9);
	}

	@Override
    public String getRecipeGroup()
    {
        return recipeResult.getItemStackDisplayName(new ItemStack(recipeResult));
    }
	
	@Override
	public NonNullList<Ingredient> getListOfIngredients() {
		return NonNullList.<Ingredient>getInstanceFilledWith(1, Ingredient.getIngredientFromItemStack(new ItemStack(material)));
	}

	@Override
	public String getRecipePath() {
		return "from" + material.getLocalizedName() + "To" + recipeResult.getItemStackDisplayName(new ItemStack(recipeResult));
	}

}