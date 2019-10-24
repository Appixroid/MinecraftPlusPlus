package net.minecraft.util.datafix.fixes;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class EntityHealth implements IFixableData
{
	private static final Set<String> ENTITY_LIST = Sets.newHashSet("ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie");

	@Override
	public int getFixVersion()
	{
		return 109;
	}

	@Override
	public NBTTagCompound fixTagCompound(NBTTagCompound compound)
	{
		if (ENTITY_LIST.contains(compound.getString("id")))
		{
			float f;

			if (compound.hasKey("HealF", 99))
			{
				f = compound.getFloat("HealF");
				compound.removeTag("HealF");
			}
			else
			{
				if (!compound.hasKey("Health", 99))
				{
					return compound;
				}

				f = compound.getFloat("Health");
			}

			compound.setFloat("Health", f);
		}

		return compound;
	}
}
