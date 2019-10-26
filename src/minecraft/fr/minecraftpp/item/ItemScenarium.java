package fr.minecraftpp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemScenarium extends ItemFood
{

	public ItemScenarium()
	{
		super(10, 1.2F, true);

		this.setUnlocalizedName("scenarium");
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.setRarity(Rarity.EPIC);

		TileEntityBeacon.paymentItems.add(this);
	}

	@Override
	public int getBurnTime()
	{
		return 1600;
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return LighterUse.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean canSetFire()
	{
		return true;
	}

	@Override
	public boolean allowEnchanting()
	{
		return true;
	}
}
