package space.mooksemoes.chocolatearmoury.proxy;

import space.mooksemoes.chocolatearmoury.item.ChocolateItems;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenders() {
		ChocolateItems.registerRenders();
	}
}
