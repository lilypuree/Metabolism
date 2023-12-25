package lilypuree.metabolism;

import com.mojang.logging.LogUtils;
import lilypuree.metabolism.compat.AppleSkinEventHandler;
import lilypuree.metabolism.data.Environment;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.data.Metabolites;
import lilypuree.metabolism.network.MetabolitesPacket;
import lilypuree.metabolism.network.Network;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.slf4j.Logger;

@Mod(MetabolismMod.MOD_ID)
public class MetabolismMod {
    public static final String MOD_ID = "metabolism";
    public static final Logger LOGGER = LogUtils.getLogger();


    public MetabolismMod() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgebus = MinecraftForge.EVENT_BUS;
        Registration.LOOT_CONDITIONS.register(modbus);

        Config.register();
        Network.init();

        modbus.addListener(this::reloadConfig);
        modbus.addListener(this::clientInit);
        forgebus.addListener(this::addListener);
        forgebus.addListener(this::syncMetabolites);
    }

    private void clientInit(final FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("appleskin")) {
            MinecraftForge.EVENT_BUS.register(new AppleSkinEventHandler());
        }
    }

    private void reloadConfig(ModConfigEvent.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.CLIENT)
            Config.CLIENT.reload();
        else if (event.getConfig().getType() == ModConfig.Type.COMMON)
            Config.SERVER.reload();
    }

    private void addListener(AddReloadListenerEvent event) {
        event.addListener(new Environment());
        event.addListener(new Metabolites());
    }

    private void syncMetabolites(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            Network.channel.sendTo(new MetabolitesPacket(Metabolites.getMetaboliteMap()), event.getPlayer().connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        } else {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                Network.channel.sendTo(new MetabolitesPacket(Metabolites.getMetaboliteMap()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }

    public static ResourceLocation getID(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
