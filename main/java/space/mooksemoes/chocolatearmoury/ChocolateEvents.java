package space.mooksemoes.chocolatearmoury;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import space.mooksemoes.chocolatearmoury.item.ChocolateSword;

public class ChocolateEvents {
	@SubscribeEvent
	public boolean playerAttack(AttackEntityEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		if (target.canBeAttackedWithItem()) {
			if (!target.hitByEntity(player)) {
				float f = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				float f1;

				if (target instanceof EntityLivingBase) {
					f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(),
							((EntityLivingBase) target).getCreatureAttribute());
				} else {
					f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(),
							EnumCreatureAttribute.UNDEFINED);
				}

				float f2 = player.getCooledAttackStrength(0.5F);
				f = f * (0.2F + f2 * f2 * 0.8F);
				f1 = f1 * f2;
				player.resetCooldown();

				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = f2 > 0.9F;
					boolean flag1 = false;
					int i = 0;
					i = i + EnchantmentHelper.getKnockbackModifier(player);

					if (player.isSprinting() && flag) {
						player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
								SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
						++i;
						flag1 = true;
					}

					boolean flag2 = flag && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder()
							&& !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding()
							&& target instanceof EntityLivingBase;
					flag2 = flag2 && !player.isSprinting();

					net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks
							.getCriticalHit(player, target, flag2, flag2 ? 1.5F : 1.0F);
					flag2 = hitResult != null;
					if (flag2) {
						f *= hitResult.getDamageModifier();
					}

					f = f + f1;
					boolean flag3 = false;
					double d0 = (double) (player.distanceWalkedModified - player.prevDistanceWalkedModified);

					if (flag && !flag2 && !flag1 && player.onGround && d0 < (double) player.getAIMoveSpeed()) {
						ItemStack itemstack = player.getHeldItem(EnumHand.MAIN_HAND);
						Item item = itemstack.getItem();
						if ((item instanceof ItemSword && !(item instanceof ChocolateSword))
								|| (item instanceof ChocolateSword && ((ChocolateSword) item).getCanSweep())) {
							flag3 = true;
						}
					}

					float f4 = 0.0F;
					boolean flag4 = false;
					int j = EnchantmentHelper.getFireAspectModifier(player);

					if (target instanceof EntityLivingBase) {
						f4 = ((EntityLivingBase) target).getHealth();

						if (j > 0 && !target.isBurning()) {
							flag4 = true;
							target.setFire(1);
						}
					}

					double d1 = target.motionX;
					double d2 = target.motionY;
					double d3 = target.motionZ;
					boolean flag5 = target.attackEntityFrom(DamageSource.causePlayerDamage(player), f);

					if (flag5) {
						if (i > 0) {
							if (target instanceof EntityLivingBase) {
								((EntityLivingBase) target).knockBack(player, (float) i * 0.5F,
										(double) MathHelper.sin(player.rotationYaw * 0.017453292F),
										(double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
							} else {
								target.addVelocity(
										(double) (-MathHelper.sin(player.rotationYaw * 0.017453292F) * (float) i
												* 0.5F),
										0.1D, (double) (MathHelper.cos(player.rotationYaw * 0.017453292F) * (float) i
												* 0.5F));
							}

							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}

						if (flag3) {
							float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * f;

							for (EntityLivingBase entitylivingbase : player.world.getEntitiesWithinAABB(
									EntityLivingBase.class, target.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
								if (entitylivingbase != player && entitylivingbase != target
										&& !player.isOnSameTeam(entitylivingbase)
										&& player.getDistanceSq(entitylivingbase) < 9.0D) {
									entitylivingbase.knockBack(player, 0.4F,
											(double) MathHelper.sin(player.rotationYaw * 0.017453292F),
											(double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
									entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage(player), f3);
								}
							}

							player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
									SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
							player.spawnSweepParticles();
						}

						if (target instanceof EntityPlayerMP && target.velocityChanged) {
							((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
							target.velocityChanged = false;
							target.motionX = d1;
							target.motionY = d2;
							target.motionZ = d3;
						}

						if (flag2) {
							player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
									SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
							player.onCriticalHit(target);
						}

						if (!flag2 && !flag3) {
							if (flag) {
								player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
										SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
							} else {
								player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
										SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
							}
						}

						if (f1 > 0.0F) {
							player.onEnchantmentCritical(target);
						}

						player.setLastAttackedEntity(target);

						if (target instanceof EntityLivingBase) {
							EnchantmentHelper.applyThornEnchantments((EntityLivingBase) target, player);
						}

						EnchantmentHelper.applyArthropodEnchantments(player, target);
						ItemStack itemstack1 = player.getHeldItemMainhand();
						Entity entity = target;

						if (target instanceof MultiPartEntityPart) {
							IEntityMultiPart ientitymultipart = ((MultiPartEntityPart) target).parent;

							if (ientitymultipart instanceof EntityLivingBase) {
								entity = (EntityLivingBase) ientitymultipart;
							}
						}

						if (!itemstack1.isEmpty() && entity instanceof EntityLivingBase) {
							ItemStack beforeHitCopy = itemstack1.copy();
							itemstack1.hitEntity((EntityLivingBase) entity, player);

							if (itemstack1.isEmpty()) {
								net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy,
										EnumHand.MAIN_HAND);
								player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
							}
						}

						if (target instanceof EntityLivingBase) {
							float f5 = f4 - ((EntityLivingBase) target).getHealth();
							player.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));

							if (j > 0) {
								target.setFire(j * 4);
							}

							if (player.world instanceof WorldServer && f5 > 2.0F) {
								int k = (int) ((double) f5 * 0.5D);
								((WorldServer) player.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR,
										target.posX, target.posY + (double) (target.height * 0.5F), target.posZ, k,
										0.1D, 0.0D, 0.1D, 0.2D);
							}
						}

						player.addExhaustion(0.1F);
					} else {
						player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ,
								SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);

						if (flag4) {
							target.extinguish();
						}
					}
				}
			}
		}
		return false;
	}
}
