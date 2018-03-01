package space.mooksemoes.chocolatearmoury;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import space.mooksemoes.chocolatearmoury.item.ChocolateItems;
import space.mooksemoes.chocolatearmoury.proxy.CommonProxy;

@Mod(modid = ChocolateArmoury.MODID, name = ChocolateArmoury.NAME, version = ChocolateArmoury.VERSION)
public class ChocolateArmoury {
	public static final String MODID = "chocolatearmoury";
	public static final String NAME = "Chocolate Armoury";
	public static final String VERSION = "1.0.0";

	@SidedProxy(clientSide = "space.mooksemoes.chocolatearmoury.proxy.ClientProxy", serverSide = "space.mooksemoes.chocolatearmoury.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ChocolateEvents());
		ChocolateItems.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenders();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
