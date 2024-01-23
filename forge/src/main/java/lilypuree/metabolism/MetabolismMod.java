package lilypuree.metabolism;

import lilypuree.metabolism.client.ClientEventHandler;
import lilypuree.metabolism.command.MetabolismCommand;
import lilypuree.metabolism.compat.AppleSkinEventHandler;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.core.environment.Environment;
import lilypuree.metabolism.core.metabolite.Metabolites;
import lilypuree.metabolism.network.Network;
import lilypuree.metabolism.platform.ForgePlatformHelper;
import lilypuree.metabolism.registration.Registration;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Constants.MOD_ID)
public class MetabolismMod {
    public static ForgeConfigSpec CLIENT_SPEC;
    public static ForgeConfigSpec SERVER_SPEC;

    public MetabolismMod() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgebus = MinecraftForge.EVENT_BUS;
        Registration.init();
        Config.init();
        Network.init();
        ForgePlatformHelper.registries.values().forEach(reg -> reg.register(modbus));
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);

        modbus.addListener(this::reloadConfig);
        modbus.addListener(this::clientInit);
        modbus.addListener(this::commonSetup);
        forgebus.addListener(this::addListener);
        forgebus.addListener(this::syncMetabolites);
        forgebus.addListener(this::registerCommands);

        if (FMLEnvironment.dist == Dist.CLIENT)
            modbus.addListener(ClientEventHandler::onToolTipRegister);
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

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addListener(AddReloadListenerEvent event) {
        event.addListener(new Environment());
        event.addListener(new Metabolites());
    }

    private void syncMetabolites(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            Metabolites.syncMetabolites(event.getPlayer(), false);
        } else {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                Metabolites.syncMetabolites(player, false);
            }
        }
    }

    public void registerCommands(RegisterCommandsEvent event) {
        MetabolismCommand.register(event.getDispatcher());
    }
}
