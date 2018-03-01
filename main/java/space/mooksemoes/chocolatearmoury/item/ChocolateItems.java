package space.mooksemoes.chocolatearmoury.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import space.mooksemoes.chocolatearmoury.ChocolateArmoury;

public class ChocolateItems {
	public static final Item daggerWood = new ChocolateDagger(ToolMaterial.WOOD, "wooden_dagger");
	public static final Item daggerStone = new ChocolateDagger(ToolMaterial.STONE, "stone_dagger");
	public static final Item daggerIron = new ChocolateDagger(ToolMaterial.IRON, "iron_dagger");
	public static final Item daggerDiamond = new ChocolateDagger(ToolMaterial.DIAMOND, "diamond_dagger");
	public static final Item daggerGold = new ChocolateDagger(ToolMaterial.GOLD, "golden_dagger");

	public static final Item katanaWood = new ChocolateKatana(ToolMaterial.WOOD, "wooden_katana");
	public static final Item katanaStone = new ChocolateKatana(ToolMaterial.STONE, "stone_katana");
	public static final Item katanaIron = new ChocolateKatana(ToolMaterial.IRON, "iron_katana");
	public static final Item katanaDiamond = new ChocolateKatana(ToolMaterial.DIAMOND, "diamond_katana");
	public static final Item katanaGold = new ChocolateKatana(ToolMaterial.GOLD, "golden_katana");

	public static void register() {
		ForgeRegistries.ITEMS.register(daggerWood);
		ForgeRegistries.ITEMS.register(daggerStone);
		ForgeRegistries.ITEMS.register(daggerIron);
		ForgeRegistries.ITEMS.register(daggerDiamond);
		ForgeRegistries.ITEMS.register(daggerGold);

		ForgeRegistries.ITEMS.register(katanaWood);
		ForgeRegistries.ITEMS.register(katanaStone);
		ForgeRegistries.ITEMS.register(katanaIron);
		ForgeRegistries.ITEMS.register(katanaDiamond);
		ForgeRegistries.ITEMS.register(katanaGold);
	}

	public static void registerRenders() {
		registerRender(daggerWood);
		registerRender(daggerStone);
		registerRender(daggerIron);
		registerRender(daggerDiamond);
		registerRender(daggerGold);

		registerRender(katanaWood);
		registerRender(katanaStone);
		registerRender(katanaIron);
		registerRender(katanaDiamond);
		registerRender(katanaGold);
	}

	public static void registerRender(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(
				ChocolateArmoury.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
