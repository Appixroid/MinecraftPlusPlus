package net.minecraft.entity.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import fr.minecraftpp.inventory.EntityArmorSlot;
import fr.minecraftpp.inventory.EntityEquipmentSlot;
import fr.minecraftpp.inventory.EntityHandSlot;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityArmorStand extends EntityLivingBase
{
	private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
	private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
	private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
	private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
	private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
	private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
	public static final DataParameter<Byte> STATUS = EntityDataManager.<Byte>createKey(EntityArmorStand.class, DataSerializers.BYTE);
	public static final DataParameter<Rotations> HEAD_ROTATION = EntityDataManager.<Rotations>createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> BODY_ROTATION = EntityDataManager.<Rotations>createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> LEFT_ARM_ROTATION = EntityDataManager.<Rotations>createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> RIGHT_ARM_ROTATION = EntityDataManager.<Rotations>createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> LEFT_LEG_ROTATION = EntityDataManager.<Rotations>createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
	public static final DataParameter<Rotations> RIGHT_LEG_ROTATION = EntityDataManager.<Rotations>createKey(EntityArmorStand.class, DataSerializers.ROTATIONS);
	private static final Predicate<Entity> IS_RIDEABLE_MINECART = new Predicate<Entity>()
	{
		@Override
		public boolean apply(@Nullable Entity p_apply_1_)
		{
			return p_apply_1_ instanceof EntityMinecart && ((EntityMinecart) p_apply_1_).getType() == EntityMinecart.Type.RIDEABLE;
		}
	};
	private final NonNullList<ItemStack> handItems;
	private final NonNullList<ItemStack> armorItems;
	private boolean canInteract;

	/**
	 * After punching the stand, the cooldown before you can punch it again
	 * without breaking it.
	 */
	public long punchCooldown;
	private int disabledSlots;
	private boolean wasMarker;
	private Rotations headRotation;
	private Rotations bodyRotation;
	private Rotations leftArmRotation;
	private Rotations rightArmRotation;
	private Rotations leftLegRotation;
	private Rotations rightLegRotation;

	public EntityArmorStand(World worldIn)
	{
		super(worldIn);
		this.handItems = NonNullList.<ItemStack>getInstanceFilledWith(2, ItemStack.EMPTY_ITEM_STACK);
		this.armorItems = NonNullList.<ItemStack>getInstanceFilledWith(4, ItemStack.EMPTY_ITEM_STACK);
		this.headRotation = DEFAULT_HEAD_ROTATION;
		this.bodyRotation = DEFAULT_BODY_ROTATION;
		this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
		this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
		this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
		this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
		this.noClip = this.hasNoGravity();
		this.setSize(0.5F, 1.975F);
	}

	public EntityArmorStand(World worldIn, double posX, double posY, double posZ)
	{
		this(worldIn);
		this.setPosition(posX, posY, posZ);
	}

	/**
	 * Sets the width and height of the entity.
	 */
	@Override
	protected final void setSize(float width, float height)
	{
		double d0 = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		float f = this.hasMarker() ? 0.0F : (this.isChild() ? 0.5F : 1.0F);
		super.setSize(width * f, height * f);
		this.setPosition(d0, d1, d2);
	}

	/**
	 * Returns whether the entity is in a server world
	 */
	@Override
	public boolean isServerWorld()
	{
		return super.isServerWorld() && !this.hasNoGravity();
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(STATUS, Byte.valueOf((byte) 0));
		this.dataManager.register(HEAD_ROTATION, DEFAULT_HEAD_ROTATION);
		this.dataManager.register(BODY_ROTATION, DEFAULT_BODY_ROTATION);
		this.dataManager.register(LEFT_ARM_ROTATION, DEFAULT_LEFTARM_ROTATION);
		this.dataManager.register(RIGHT_ARM_ROTATION, DEFAULT_RIGHTARM_ROTATION);
		this.dataManager.register(LEFT_LEG_ROTATION, DEFAULT_LEFTLEG_ROTATION);
		this.dataManager.register(RIGHT_LEG_ROTATION, DEFAULT_RIGHTLEG_ROTATION);
	}

	@Override
	public Iterable<ItemStack> getHeldEquipment()
	{
		return this.handItems;
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList()
	{
		return this.armorItems;
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot)
	{
		if (slot instanceof EntityHandSlot)
		{
			return this.handItems.get(slot.getIndex());
		}
		else if (slot instanceof EntityArmorSlot)
		{
			return this.armorItems.get(slot.getIndex());
		}
		else
		{
			return ItemStack.EMPTY_ITEM_STACK;
		}
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack stack)
	{
		if (slot instanceof EntityHandSlot)
		{
			this.playEquipSound(stack);
			this.handItems.set(slot.getIndex(), stack);
		}
		else if (slot instanceof EntityArmorSlot)
		{
			this.playEquipSound(stack);
			this.armorItems.set(slot.getIndex(), stack);
		}
	}

	@Override
	public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn)
	{
		EntityEquipmentSlot entityequipmentslot;

		if (inventorySlot == 98)
		{
			entityequipmentslot = EntityHandSlot.MAINHAND;
		}
		else if (inventorySlot == 99)
		{
			entityequipmentslot = EntityHandSlot.OFFHAND;
		}
		else if (inventorySlot == 100 + EntityArmorSlot.HEAD.getIndex())
		{
			entityequipmentslot = EntityArmorSlot.HEAD;
		}
		else if (inventorySlot == 100 + EntityArmorSlot.CHEST.getIndex())
		{
			entityequipmentslot = EntityArmorSlot.CHEST;
		}
		else if (inventorySlot == 100 + EntityArmorSlot.LEGS.getIndex())
		{
			entityequipmentslot = EntityArmorSlot.LEGS;
		}
		else
		{
			if (inventorySlot != 100 + EntityArmorSlot.FEET.getIndex())
			{
				return false;
			}

			entityequipmentslot = EntityArmorSlot.FEET;
		}

		if (!itemStackIn.isNotValid() && !EntityLiving.isItemStackInSlot(entityequipmentslot, itemStackIn) && entityequipmentslot != EntityArmorSlot.HEAD)
		{
			return false;
		}
		else
		{
			this.setItemStackToSlot(entityequipmentslot, itemStackIn);
			return true;
		}
	}

	public static void registerFixesArmorStand(DataFixer fixer)
	{
		fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityArmorStand.class, new String[] { "ArmorItems", "HandItems" }));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		NBTTagList nbttaglist = new NBTTagList();

		for (ItemStack itemstack : this.armorItems)
		{
			NBTTagCompound nbttagcompound = new NBTTagCompound();

			if (!itemstack.isNotValid())
			{
				itemstack.writeToNBT(nbttagcompound);
			}

			nbttaglist.appendTag(nbttagcompound);
		}

		compound.setTag("ArmorItems", nbttaglist);
		NBTTagList nbttaglist1 = new NBTTagList();

		for (ItemStack itemstack1 : this.handItems)
		{
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();

			if (!itemstack1.isNotValid())
			{
				itemstack1.writeToNBT(nbttagcompound1);
			}

			nbttaglist1.appendTag(nbttagcompound1);
		}

		compound.setTag("HandItems", nbttaglist1);
		compound.setBoolean("Invisible", this.isInvisible());
		compound.setBoolean("Small", this.isSmall());
		compound.setBoolean("ShowArms", this.getShowArms());
		compound.setInteger("DisabledSlots", this.disabledSlots);
		compound.setBoolean("NoBasePlate", this.hasNoBasePlate());

		if (this.hasMarker())
		{
			compound.setBoolean("Marker", this.hasMarker());
		}

		compound.setTag("Pose", this.readPoseFromNBT());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);

		if (compound.hasKey("ArmorItems", 9))
		{
			NBTTagList nbttaglist = compound.getTagList("ArmorItems", 10);

			for (int i = 0; i < this.armorItems.size(); ++i)
			{
				this.armorItems.set(i, new ItemStack(nbttaglist.getCompoundTagAt(i)));
			}
		}

		if (compound.hasKey("HandItems", 9))
		{
			NBTTagList nbttaglist1 = compound.getTagList("HandItems", 10);

			for (int j = 0; j < this.handItems.size(); ++j)
			{
				this.handItems.set(j, new ItemStack(nbttaglist1.getCompoundTagAt(j)));
			}
		}

		this.setInvisible(compound.getBoolean("Invisible"));
		this.setSmall(compound.getBoolean("Small"));
		this.setShowArms(compound.getBoolean("ShowArms"));
		this.disabledSlots = compound.getInteger("DisabledSlots");
		this.setNoBasePlate(compound.getBoolean("NoBasePlate"));
		this.setMarker(compound.getBoolean("Marker"));
		this.wasMarker = !this.hasMarker();
		this.noClip = this.hasNoGravity();
		NBTTagCompound nbttagcompound = compound.getCompoundTag("Pose");
		this.writePoseToNBT(nbttagcompound);
	}

	/**
	 * Saves the pose to an NBTTagCompound.
	 */
	private void writePoseToNBT(NBTTagCompound tagCompound)
	{
		NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);
		this.setHeadRotation(nbttaglist.hasNoTags() ? DEFAULT_HEAD_ROTATION : new Rotations(nbttaglist));
		NBTTagList nbttaglist1 = tagCompound.getTagList("Body", 5);
		this.setBodyRotation(nbttaglist1.hasNoTags() ? DEFAULT_BODY_ROTATION : new Rotations(nbttaglist1));
		NBTTagList nbttaglist2 = tagCompound.getTagList("LeftArm", 5);
		this.setLeftArmRotation(nbttaglist2.hasNoTags() ? DEFAULT_LEFTARM_ROTATION : new Rotations(nbttaglist2));
		NBTTagList nbttaglist3 = tagCompound.getTagList("RightArm", 5);
		this.setRightArmRotation(nbttaglist3.hasNoTags() ? DEFAULT_RIGHTARM_ROTATION : new Rotations(nbttaglist3));
		NBTTagList nbttaglist4 = tagCompound.getTagList("LeftLeg", 5);
		this.setLeftLegRotation(nbttaglist4.hasNoTags() ? DEFAULT_LEFTLEG_ROTATION : new Rotations(nbttaglist4));
		NBTTagList nbttaglist5 = tagCompound.getTagList("RightLeg", 5);
		this.setRightLegRotation(nbttaglist5.hasNoTags() ? DEFAULT_RIGHTLEG_ROTATION : new Rotations(nbttaglist5));
	}

	private NBTTagCompound readPoseFromNBT()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();

		if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation))
		{
			nbttagcompound.setTag("Head", this.headRotation.writeToNBT());
		}

		if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation))
		{
			nbttagcompound.setTag("Body", this.bodyRotation.writeToNBT());
		}

		if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation))
		{
			nbttagcompound.setTag("LeftArm", this.leftArmRotation.writeToNBT());
		}

		if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation))
		{
			nbttagcompound.setTag("RightArm", this.rightArmRotation.writeToNBT());
		}

		if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation))
		{
			nbttagcompound.setTag("LeftLeg", this.leftLegRotation.writeToNBT());
		}

		if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation))
		{
			nbttagcompound.setTag("RightLeg", this.rightLegRotation.writeToNBT());
		}

		return nbttagcompound;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entityIn)
	{
	}

	@Override
	protected void collideWithNearbyEntities()
	{
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), IS_RIDEABLE_MINECART);

		for (int i = 0; i < list.size(); ++i)
		{
			Entity entity = list.get(i);

			if (this.getDistanceSqToEntity(entity) <= 0.2D)
			{
				entity.applyEntityCollision(this);
			}
		}
	}

	/**
	 * Applies the given player interaction to this Entity.
	 */
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand stack)
	{
		ItemStack itemstack = player.getHeldItem(stack);

		if (!this.hasMarker() && itemstack.getItem() != Items.NAME_TAG)
		{
			if (!this.world.isRemote && !player.isSpectator())
			{
				EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);

				if (itemstack.isNotValid())
				{
					EntityEquipmentSlot entityequipmentslot1 = this.func_190772_a(vec);
					EntityEquipmentSlot entityequipmentslot2 = this.isDisabled(entityequipmentslot1) ? entityequipmentslot : entityequipmentslot1;

					if (this.func_190630_a(entityequipmentslot2))
					{
						this.swapItem(player, entityequipmentslot2, itemstack, stack);
					}
				}
				else
				{
					if (this.isDisabled(entityequipmentslot))
					{
						return EnumActionResult.FAIL;
					}

					if (entityequipmentslot instanceof EntityHandSlot && !this.getShowArms())
					{
						return EnumActionResult.FAIL;
					}

					this.swapItem(player, entityequipmentslot, itemstack, stack);
				}

				return EnumActionResult.SUCCESS;
			}
			else
			{
				return EnumActionResult.SUCCESS;
			}
		}
		else
		{
			return EnumActionResult.PASS;
		}
	}

	protected EntityEquipmentSlot func_190772_a(Vec3d p_190772_1_)
	{
		EntityEquipmentSlot entityequipmentslot = EntityHandSlot.MAINHAND;
		boolean flag = this.isSmall();
		double d0 = flag ? p_190772_1_.yCoord * 2.0D : p_190772_1_.yCoord;
		EntityEquipmentSlot entityequipmentslot1 = EntityArmorSlot.FEET;

		if (d0 >= 0.1D && d0 < 0.1D + (flag ? 0.8D : 0.45D) && this.func_190630_a(entityequipmentslot1))
		{
			entityequipmentslot = EntityArmorSlot.FEET;
		}
		else if (d0 >= 0.9D + (flag ? 0.3D : 0.0D) && d0 < 0.9D + (flag ? 1.0D : 0.7D) && this.func_190630_a(EntityArmorSlot.CHEST))
		{
			entityequipmentslot = EntityArmorSlot.CHEST;
		}
		else if (d0 >= 0.4D && d0 < 0.4D + (flag ? 1.0D : 0.8D) && this.func_190630_a(EntityArmorSlot.LEGS))
		{
			entityequipmentslot = EntityArmorSlot.LEGS;
		}
		else if (d0 >= 1.6D && this.func_190630_a(EntityArmorSlot.HEAD))
		{
			entityequipmentslot = EntityArmorSlot.HEAD;
		}

		return entityequipmentslot;
	}

	private boolean isDisabled(EntityEquipmentSlot slotIn)
	{
		return (this.disabledSlots & 1 << slotIn.getSlotIndex()) != 0;
	}

	private void swapItem(EntityPlayer player, EntityEquipmentSlot p_184795_2_, ItemStack p_184795_3_, EnumHand hand)
	{
		ItemStack itemstack = this.getItemStackFromSlot(p_184795_2_);

		if (itemstack.isNotValid() || (this.disabledSlots & 1 << p_184795_2_.getSlotIndex() + 8) == 0)
		{
			if (!itemstack.isNotValid() || (this.disabledSlots & 1 << p_184795_2_.getSlotIndex() + 16) == 0)
			{
				if (player.capabilities.isCreativeMode && itemstack.isNotValid() && !p_184795_3_.isNotValid())
				{
					ItemStack itemstack2 = p_184795_3_.copy();
					itemstack2.setStackSize(1);
					this.setItemStackToSlot(p_184795_2_, itemstack2);
				}
				else if (!p_184795_3_.isNotValid() && p_184795_3_.getStackSize() > 1)
				{
					if (itemstack.isNotValid())
					{
						ItemStack itemstack1 = p_184795_3_.copy();
						itemstack1.setStackSize(1);
						this.setItemStackToSlot(p_184795_2_, itemstack1);
						p_184795_3_.decreaseStackSize(1);
					}
				}
				else
				{
					this.setItemStackToSlot(p_184795_2_, p_184795_3_);
					player.setHeldItem(hand, itemstack);
				}
			}
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (!this.world.isRemote && !this.isDead)
		{
			if (DamageSource.outOfWorld.equals(source))
			{
				this.setDead();
				return false;
			}
			else if (!this.isEntityInvulnerable(source) && !this.canInteract && !this.hasMarker())
			{
				if (source.isExplosion())
				{
					this.dropContents();
					this.setDead();
					return false;
				}
				else if (DamageSource.inFire.equals(source))
				{
					if (this.isBurning())
					{
						this.damageArmorStand(0.15F);
					}
					else
					{
						this.setFire(5);
					}

					return false;
				}
				else if (DamageSource.onFire.equals(source) && this.getHealth() > 0.5F)
				{
					this.damageArmorStand(4.0F);
					return false;
				}
				else
				{
					boolean flag = "arrow".equals(source.getDamageType());
					boolean flag1 = "player".equals(source.getDamageType());

					if (!flag1 && !flag)
					{
						return false;
					}
					else
					{
						if (source.getSourceOfDamage() instanceof EntityArrow)
						{
							source.getSourceOfDamage().setDead();
						}

						if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer) source.getEntity()).capabilities.allowEdit)
						{
							return false;
						}
						else if (source.isCreativePlayer())
						{
							this.func_190773_I();
							this.playParticles();
							this.setDead();
							return false;
						}
						else
						{
							long i = this.world.getTotalWorldTime();

							if (i - this.punchCooldown > 5L && !flag)
							{
								this.world.setEntityState(this, (byte) 32);
								this.punchCooldown = i;
							}
							else
							{
								this.dropBlock();
								this.playParticles();
								this.setDead();
							}

							return false;
						}
					}
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	public void handleStatusUpdate(byte id)
	{
		if (id == 32)
		{
			if (this.world.isRemote)
			{
				this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ARMORSTAND_HIT, this.getSoundCategory(), 0.3F, 1.0F, false);
				this.punchCooldown = this.world.getTotalWorldTime();
			}
		}
		else
		{
			super.handleStatusUpdate(id);
		}
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

		if (Double.isNaN(d0) || d0 == 0.0D)
		{
			d0 = 4.0D;
		}

		d0 = d0 * 64.0D;
		return distance < d0 * d0;
	}

	private void playParticles()
	{
		if (this.world instanceof WorldServer)
		{
			((WorldServer) this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + this.height / 1.5D, this.posZ, 10, (double) (this.width / 4.0F), (double) (this.height / 4.0F), (double) (this.width / 4.0F), 0.05D, Block.getStateId(Blocks.PLANKS.getDefaultState()));
		}
	}

	private void damageArmorStand(float damage)
	{
		float f = this.getHealth();
		f = f - damage;

		if (f <= 0.5F)
		{
			this.dropContents();
			this.setDead();
		}
		else
		{
			this.setHealth(f);
		}
	}

	private void dropBlock()
	{
		Block.spawnAsEntity(this.world, new BlockPos(this), new ItemStack(Items.ARMOR_STAND));
		this.dropContents();
	}

	private void dropContents()
	{
		this.func_190773_I();

		for (int i = 0; i < this.handItems.size(); ++i)
		{
			ItemStack itemstack = this.handItems.get(i);

			if (!itemstack.isNotValid())
			{
				Block.spawnAsEntity(this.world, (new BlockPos(this)).up(), itemstack);
				this.handItems.set(i, ItemStack.EMPTY_ITEM_STACK);
			}
		}

		for (int j = 0; j < this.armorItems.size(); ++j)
		{
			ItemStack itemstack1 = this.armorItems.get(j);

			if (!itemstack1.isNotValid())
			{
				Block.spawnAsEntity(this.world, (new BlockPos(this)).up(), itemstack1);
				this.armorItems.set(j, ItemStack.EMPTY_ITEM_STACK);
			}
		}
	}

	private void func_190773_I()
	{
		this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ARMORSTAND_BREAK, this.getSoundCategory(), 1.0F, 1.0F);
	}

	@Override
	protected float updateDistance(float p_110146_1_, float p_110146_2_)
	{
		this.prevRenderYawOffset = this.prevRotationYaw;
		this.renderYawOffset = this.rotationYaw;
		return 0.0F;
	}

	@Override
	public float getEyeHeight()
	{
		return this.isChild() ? this.height * 0.5F : this.height * 0.9F;
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	@Override
	public double getYOffset()
	{
		return this.hasMarker() ? 0.0D : 0.10000000149011612D;
	}

	@Override
	public void func_191986_a(float p_191986_1_, float p_191986_2_, float p_191986_3_)
	{
		if (!this.hasNoGravity())
		{
			super.func_191986_a(p_191986_1_, p_191986_2_, p_191986_3_);
		}
	}

	/**
	 * Set the render yaw offset
	 */
	@Override
	public void setRenderYawOffset(float offset)
	{
		this.prevRenderYawOffset = this.prevRotationYaw = offset;
		this.prevRotationYawHead = this.rotationYawHead = offset;
	}

	/**
	 * Sets the head's yaw rotation of the entity.
	 */
	@Override
	public void setRotationYawHead(float rotation)
	{
		this.prevRenderYawOffset = this.prevRotationYaw = rotation;
		this.prevRotationYawHead = this.rotationYawHead = rotation;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		Rotations rotations = this.dataManager.get(HEAD_ROTATION);

		if (!this.headRotation.equals(rotations))
		{
			this.setHeadRotation(rotations);
		}

		Rotations rotations1 = this.dataManager.get(BODY_ROTATION);

		if (!this.bodyRotation.equals(rotations1))
		{
			this.setBodyRotation(rotations1);
		}

		Rotations rotations2 = this.dataManager.get(LEFT_ARM_ROTATION);

		if (!this.leftArmRotation.equals(rotations2))
		{
			this.setLeftArmRotation(rotations2);
		}

		Rotations rotations3 = this.dataManager.get(RIGHT_ARM_ROTATION);

		if (!this.rightArmRotation.equals(rotations3))
		{
			this.setRightArmRotation(rotations3);
		}

		Rotations rotations4 = this.dataManager.get(LEFT_LEG_ROTATION);

		if (!this.leftLegRotation.equals(rotations4))
		{
			this.setLeftLegRotation(rotations4);
		}

		Rotations rotations5 = this.dataManager.get(RIGHT_LEG_ROTATION);

		if (!this.rightLegRotation.equals(rotations5))
		{
			this.setRightLegRotation(rotations5);
		}

		boolean flag = this.hasMarker();

		if (this.wasMarker != flag)
		{
			this.updateBoundingBox(flag);
			this.preventEntitySpawning = !flag;
			this.wasMarker = flag;
		}
	}

	private void updateBoundingBox(boolean p_181550_1_)
	{
		if (p_181550_1_)
		{
			this.setSize(0.0F, 0.0F);
		}
		else
		{
			this.setSize(0.5F, 1.975F);
		}
	}

	/**
	 * Clears potion metadata values if the entity has no potion effects.
	 * Otherwise, updates potion effect color, ambience, and invisibility
	 * metadata values
	 */
	@Override
	protected void updatePotionMetadata()
	{
		this.setInvisible(this.canInteract);
	}

	@Override
	public void setInvisible(boolean invisible)
	{
		this.canInteract = invisible;
		super.setInvisible(invisible);
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	@Override
	public boolean isChild()
	{
		return this.isSmall();
	}

	/**
	 * Called by the /kill command.
	 */
	@Override
	public void onKillCommand()
	{
		this.setDead();
	}

	@Override
	public boolean isImmuneToExplosions()
	{
		return this.isInvisible();
	}

	@Override
	public EnumPushReaction getPushReaction()
	{
		return this.hasMarker() ? EnumPushReaction.IGNORE : super.getPushReaction();
	}

	private void setSmall(boolean small)
	{
		this.dataManager.set(STATUS, Byte.valueOf(this.setBit(this.dataManager.get(STATUS).byteValue(), 1, small)));
		this.setSize(0.5F, 1.975F);
	}

	public boolean isSmall()
	{
		return (this.dataManager.get(STATUS).byteValue() & 1) != 0;
	}

	private void setShowArms(boolean showArms)
	{
		this.dataManager.set(STATUS, Byte.valueOf(this.setBit(this.dataManager.get(STATUS).byteValue(), 4, showArms)));
	}

	public boolean getShowArms()
	{
		return (this.dataManager.get(STATUS).byteValue() & 4) != 0;
	}

	private void setNoBasePlate(boolean noBasePlate)
	{
		this.dataManager.set(STATUS, Byte.valueOf(this.setBit(this.dataManager.get(STATUS).byteValue(), 8, noBasePlate)));
	}

	public boolean hasNoBasePlate()
	{
		return (this.dataManager.get(STATUS).byteValue() & 8) != 0;
	}

	/**
	 * Marker defines where if true, the size is 0 and will not be rendered or
	 * intractable.
	 */
	private void setMarker(boolean marker)
	{
		this.dataManager.set(STATUS, Byte.valueOf(this.setBit(this.dataManager.get(STATUS).byteValue(), 16, marker)));
		this.setSize(0.5F, 1.975F);
	}

	/**
	 * Gets whether the armor stand has marker enabled. If true, the armor
	 * stand's bounding box is set to zero and cannot be interacted with.
	 */
	public boolean hasMarker()
	{
		return (this.dataManager.get(STATUS).byteValue() & 16) != 0;
	}

	private byte setBit(byte p_184797_1_, int p_184797_2_, boolean p_184797_3_)
	{
		if (p_184797_3_)
		{
			p_184797_1_ = (byte) (p_184797_1_ | p_184797_2_);
		}
		else
		{
			p_184797_1_ = (byte) (p_184797_1_ & ~p_184797_2_);
		}

		return p_184797_1_;
	}

	public void setHeadRotation(Rotations vec)
	{
		this.headRotation = vec;
		this.dataManager.set(HEAD_ROTATION, vec);
	}

	public void setBodyRotation(Rotations vec)
	{
		this.bodyRotation = vec;
		this.dataManager.set(BODY_ROTATION, vec);
	}

	public void setLeftArmRotation(Rotations vec)
	{
		this.leftArmRotation = vec;
		this.dataManager.set(LEFT_ARM_ROTATION, vec);
	}

	public void setRightArmRotation(Rotations vec)
	{
		this.rightArmRotation = vec;
		this.dataManager.set(RIGHT_ARM_ROTATION, vec);
	}

	public void setLeftLegRotation(Rotations vec)
	{
		this.leftLegRotation = vec;
		this.dataManager.set(LEFT_LEG_ROTATION, vec);
	}

	public void setRightLegRotation(Rotations vec)
	{
		this.rightLegRotation = vec;
		this.dataManager.set(RIGHT_LEG_ROTATION, vec);
	}

	public Rotations getHeadRotation()
	{
		return this.headRotation;
	}

	public Rotations getBodyRotation()
	{
		return this.bodyRotation;
	}

	public Rotations getLeftArmRotation()
	{
		return this.leftArmRotation;
	}

	public Rotations getRightArmRotation()
	{
		return this.rightArmRotation;
	}

	public Rotations getLeftLegRotation()
	{
		return this.leftLegRotation;
	}

	public Rotations getRightLegRotation()
	{
		return this.rightLegRotation;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return super.canBeCollidedWith() && !this.hasMarker();
	}

	@Override
	public EnumHandSide getPrimaryHand()
	{
		return EnumHandSide.RIGHT;
	}

	@Override
	protected SoundEvent getFallSound(int heightIn)
	{
		return SoundEvents.ENTITY_ARMORSTAND_FALL;
	}

	@Override
	@Nullable
	protected SoundEvent getHurtSound(DamageSource p_184601_1_)
	{
		return SoundEvents.ENTITY_ARMORSTAND_HIT;
	}

	@Override
	@Nullable
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_ARMORSTAND_BREAK;
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt)
	{
	}

	/**
	 * Returns false if the entity is an armor stand. Returns true for all other
	 * entity living bases.
	 */
	@Override
	public boolean canBeHitWithPotion()
	{
		return false;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		if (STATUS.equals(key))
		{
			this.setSize(0.5F, 1.975F);
		}

		super.notifyDataManagerChange(key);
	}

	@Override
	public boolean func_190631_cK()
	{
		return false;
	}
}
