package space.mooksemoes.chocolatearmoury.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;

public class ChocolateSword extends ItemSword {
	private final float attackDamage;
	private final float attackSpeed;
	private final boolean canSweep;
	private final boolean twoHanded;
	private final ToolMaterial material;

	public ChocolateSword(ToolMaterial material, String name, float typeSpeed, float typeDamage, boolean canSweep,
			boolean twoHanded) {
		super(material);
		this.material = material;
		this.setUnlocalizedName(name); // hmm...
		this.setRegistryName(name); // hmm...
		this.attackSpeed = typeSpeed;
		this.attackDamage = typeDamage + material.getAttackDamage();
		this.canSweep = canSweep;
		this.twoHanded = twoHanded;
	}

	public boolean getCanSweep() {
		return canSweep;
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit
	 * damage.
	 */
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier> create();

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) this.attackSpeed, 0));
		}

		return multimap;
	}
}
