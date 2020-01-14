package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIHarvestFarmland extends EntityAIMoveToBlock
{
	/** Villager that is harvesting */
	private final EntityVillager theVillager;
	private boolean hasFarmItem;
	private boolean wantsToReapStuff;
	private int currentTask;

	public EntityAIHarvestFarmland(EntityVillager theVillagerIn, double speedIn)
	{
		super(theVillagerIn, speedIn, 16);
		this.theVillager = theVillagerIn;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if (this.runDelay <= 0)
		{
			if (!this.theVillager.world.getGameRules().getBoolean("mobGriefing"))
			{
				return false;
			}

			this.currentTask = -1;
			this.hasFarmItem = this.theVillager.isFarmItemInInventory();
			this.wantsToReapStuff = this.theVillager.wantsMoreFood();
		}

		return super.shouldExecute();
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		return this.currentTask >= 0 && super.continueExecuting();
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		super.updateTask();
		this.theVillager.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, this.theVillager.getVerticalFaceSpeed());

		if (this.getIsAboveDestination())
		{
			World world = this.theVillager.world;
			BlockPos blockpos = this.destinationBlock.up();
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();

			if (this.currentTask == 0 && block instanceof BlockCrops && ((BlockCrops) block).isMaxAge(iblockstate))
			{
				world.destroyBlock(blockpos, true);
			}
			else if (this.currentTask == 1 && iblockstate.getMaterial() == Material.AIR)
			{
				InventoryBasic inventorybasic = this.theVillager.getVillagerInventory();

				for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
				{
					ItemStack itemstack = inventorybasic.getStackInSlot(i);
					boolean flag = false;

					if (!itemstack.isNotValid())
					{
						if (itemstack.getItem() == Items.getItem(Items.WHEAT_SEEDS))
						{
							world.setBlockState(blockpos, Blocks.getBlock(Blocks.WHEAT).getDefaultState(), 3);
							flag = true;
						}
						else if (itemstack.getItem() == Items.getItem(Items.POTATO))
						{
							world.setBlockState(blockpos, Blocks.getBlock(Blocks.POTATOES).getDefaultState(), 3);
							flag = true;
						}
						else if (itemstack.getItem() == Items.getItem(Items.CARROT))
						{
							world.setBlockState(blockpos, Blocks.getBlock(Blocks.CARROTS).getDefaultState(), 3);
							flag = true;
						}
						else if (itemstack.getItem() == Items.getItem(Items.BEETROOT_SEEDS))
						{
							world.setBlockState(blockpos, Blocks.getBlock(Blocks.BEETROOTS).getDefaultState(), 3);
							flag = true;
						}
					}

					if (flag)
					{
						itemstack.decreaseStackSize(1);

						if (itemstack.isNotValid())
						{
							inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY_ITEM_STACK);
						}

						break;
					}
				}
			}

			this.currentTask = -1;
			this.runDelay = 10;
		}
	}

	/**
	 * Return true to set given position as destination
	 */
	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos)
	{
		Block block = worldIn.getBlockState(pos).getBlock();

		if (block == Blocks.getBlock(Blocks.FARMLAND))
		{
			pos = pos.up();
			IBlockState iblockstate = worldIn.getBlockState(pos);
			block = iblockstate.getBlock();

			if (block instanceof BlockCrops && ((BlockCrops) block).isMaxAge(iblockstate) && this.wantsToReapStuff && (this.currentTask == 0 || this.currentTask < 0))
			{
				this.currentTask = 0;
				return true;
			}

			if (iblockstate.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0))
			{
				this.currentTask = 1;
				return true;
			}
		}

		return false;
	}
}
