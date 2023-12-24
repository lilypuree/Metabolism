package lilypuree.metabolism.client;

import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.client.gui.DebugOverlay;
import lilypuree.metabolism.client.gui.EnergyDisplayHandler;
import lilypuree.metabolism.client.gui.WarmthDisplayHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MetabolismMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientInit {
    //Scaling Health says this works...
    static {
        MinecraftForge.EVENT_BUS.register(EnergyDisplayHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(WarmthDisplayHandler.INSTANCE);

        DebugOverlay.init();
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {

    }
}
