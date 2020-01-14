package net.minecraft.inventory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

public class ContainerBrewingStand extends Container
{
	private final IInventory tileBrewingStand;

	/** Instance of Slot. */
	private final Slot theSlot;

	/**
	 * Used to cache the brewing time to send changes to ICrafting listeners.
	 */
	private int prevBrewTime;

	/**
	 * Used to cache the fuel remaining in the brewing stand to send changes to
	 * ICrafting listeners.
	 */
	private int prevFuel;

	public ContainerBrewingStand(InventoryPlayer playerInventory, IInventory tileBrewingStandIn)
	{
		this.tileBrewingStand = tileBrewingStandIn;
		this.addSlotToContainer(new ContainerBrewingStand.Potion(tileBrewingStandIn, 0, 56, 51));
		this.addSlotToContainer(new ContainerBrewingStand.Potion(tileBrewingStandIn, 1, 79, 58));
		this.addSlotToContainer(new ContainerBrewingStand.Potion(tileBrewingStandIn, 2, 102, 51));
		this.theSlot = this.addSlotToContainer(new ContainerBrewingStand.Ingredient(tileBrewingStandIn, 3, 79, 17));
		this.addSlotToContainer(new ContainerBrewingStand.Fuel(tileBrewingStandIn, 4, 17, 17));

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileBrewingStand);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener icontainerlistener = this.listeners.get(i);

			if (this.prevBrewTime != this.tileBrewingStand.getField(0))
			{
				icontainerlistener.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
			}

			if (this.prevFuel != this.tileBrewingStand.getField(1))
			{
				icontainerlistener.sendProgressBarUpdate(this, 1, this.tileBrewingStand.getField(1));
			}
		}

		this.prevBrewTime = this.tileBrewingStand.getField(0);
		this.prevFuel = this.tileBrewingStand.getField(1);
	}

	@Override
	public void updateProgressBar(int id, int data)
	{
		this.tileBrewingStand.setField(id, data);
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileBrewingStand.isUsableByPlayer(playerIn);
	}

	/**
	 * Take a stack from the specified inventory slot.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY_ITEM_STACK;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if ((index < 0 || index > 2) && index != 3 && index != 4)
			{
				if (this.theSlot.isItemValid(itemstack1))
				{
					if (!this.mergeItemStack(itemstack1, 3, 4, false))
					{
						return ItemStack.EMPTY_ITEM_STACK;
					}
				}
				else if (ContainerBrewingStand.Potion.canHoldPotion(itemstack) && itemstack.getStackSize() == 1)
				{
					if (!this.mergeItemStack(itemstack1, 0, 3, false))
					{
						return ItemStack.EMPTY_ITEM_STACK;
					}
				}
				else if (ContainerBrewingStand.Fuel.isValidBrewingFuel(itemstack))
				{
					if (!this.mergeItemStack(itemstack1, 4, 5, false))
					{
						return ItemStack.EMPTY_ITEM_STACK;
					}
				}
				else if (index >= 5 && index < 32)
				{
					if (!this.mergeItemStack(itemstack1, 32, 41, false))
					{
						return ItemStack.EMPTY_ITEM_STACK;
					}
				}
				else if (index >= 32 && index < 41)
				{
					if (!this.mergeItemStack(itemstack1, 5, 32, false))
					{
						return ItemStack.EMPTY_ITEM_STACK;
					}
				}
				else if (!this.mergeItemStack(itemstack1, 5, 41, false))
				{
					return ItemStack.EMPTY_ITEM_STACK;
				}
			}
			else
			{
				if (!this.mergeItemStack(itemstack1, 5, 41, true))
				{
					return ItemStack.EMPTY_ITEM_STACK;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}

			if (itemstack1.isNotValid())
			{
				slot.putStack(ItemStack.EMPTY_ITEM_STACK);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getStackSize() == itemstack.getStackSize())
			{
				return ItemStack.EMPTY_ITEM_STACK;
			}

			slot.func_190901_a(playerIn, itemstack1);
		}

		return itemstack;
	}

	static class Fuel extends Slot
	{
		public Fuel(IInventory iInventoryIn, int index, int xPosition, int yPosition)
		{
			super(iInventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return isValidBrewingFuel(stack);
		}

		public static boolean isValidBrewingFuel(ItemStack itemStackIn)
		{
			return itemStackIn.getItem() == Items.getItem(Items.BLAZE_POWDER);
		}

		@Override
		public int getSlotStackLimit()
		{
			return 64;
		}
	}

	static class Ingredient extends Slot
	{
		public Ingredient(IInventory iInventoryIn, int index, int xPosition, int yPosition)
		{
			super(iInventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return PotionHelper.isReagent(stack);
		}

		@Override
		public int getSlotStackLimit()
		{
			return 64;
		}
	}

	static class Potion extends Slot
	{
		public Potion(IInventory p_i47598_1_, int p_i47598_2_, int p_i47598_3_, int p_i47598_4_)
		{
			super(p_i47598_1_, p_i47598_2_, p_i47598_3_, p_i47598_4_);
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return canHoldPotion(stack);
		}

		@Override
		public int getSlotStackLimit()
		{
			return 1;
		}

		@Override
		public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_)
		{
			PotionType potiontype = PotionUtils.getPotionFromItem(p_190901_2_);

			if (p_190901_1_ instanceof EntityPlayerMP)
			{
				CriteriaTriggers.field_192130_j.func_192173_a((EntityPlayerMP) p_190901_1_, potiontype);
			}

			super.func_190901_a(p_190901_1_, p_190901_2_);
			return p_190901_2_;
		}

		public static boolean canHoldPotion(ItemStack stack)
		{
			Item item = stack.getItem();
			return item == Items.getItem(Items.POTIONITEM) || item == Items.getItem(Items.SPLASH_POTION) || item == Items.getItem(Items.LINGERING_POTION) || item == Items.getItem(Items.GLASS_BOTTLE);
		}
	}
}
