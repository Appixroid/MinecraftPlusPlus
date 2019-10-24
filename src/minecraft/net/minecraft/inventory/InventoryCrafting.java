package net.minecraft.inventory;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryCrafting implements IInventory
{
	private final NonNullList<ItemStack> stackList;

	/** the width of the crafting inventory */
	private final int inventoryWidth;
	private final int inventoryHeight;

	/**
	 * Class containing the callbacks for the events on_GUIClosed and
	 * on_CraftMaxtrixChanged.
	 */
	private final Container eventHandler;

	public InventoryCrafting(Container eventHandlerIn, int width, int height)
	{
		this.stackList = NonNullList.<ItemStack>getInstanceFilledWith(width * height, ItemStack.EMPTY_ITEM_STACK);
		this.eventHandler = eventHandlerIn;
		this.inventoryWidth = width;
		this.inventoryHeight = height;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return this.stackList.size();
	}

	@Override
	public boolean isStackNotValid()
	{
		for (ItemStack itemstack : this.stackList)
		{
			if (!itemstack.isNotValid())
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index >= this.getSizeInventory() ? ItemStack.EMPTY_ITEM_STACK : (ItemStack) this.stackList.get(index);
	}

	/**
	 * Gets the ItemStack in the slot specified.
	 */
	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		return row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight ? this.getStackInSlot(row + column * this.inventoryWidth) : ItemStack.EMPTY_ITEM_STACK;
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName()
	{
		return "container.crafting";
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's
	 * username in chat
	 */
	@Override
	public ITextComponent getDisplayName()
	{
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.stackList, index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and
	 * returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);

		if (!itemstack.isNotValid())
		{
			this.eventHandler.onCraftMatrixChanged(this);
		}

		return itemstack;
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stackList.set(index, stack);
		this.eventHandler.onCraftMatrixChanged(this);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved
	 * to disk later - the game won't think it hasn't changed and skip it.
	 */
	@Override
	public void markDirty()
	{
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with
	 * Container
	 */
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot. For guis use Slot.isItemValid
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		this.stackList.clear();
	}

	public int getHeight()
	{
		return this.inventoryHeight;
	}

	public int getWidth()
	{
		return this.inventoryWidth;
	}

	public void func_194018_a(RecipeItemHelper p_194018_1_)
	{
		for (ItemStack itemstack : this.stackList)
		{
			p_194018_1_.func_194112_a(itemstack);
		}
	}
}
