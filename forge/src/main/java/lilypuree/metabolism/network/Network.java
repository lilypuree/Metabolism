package lilypuree.metabolism.network;

import lilypuree.metabolism.Constants;
import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.client.ClientMetabolites;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {
    private static final ResourceLocation NAME = new ResourceLocation(Constants.MOD_ID, "network");
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            NAME, () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 1;
        channel.messageBuilder(MetabolitesPacket.class, id++)
                .decoder(MetabolitesPacket::fromBytes)
                .encoder(MetabolitesPacket::toBytes)
                .consumerMainThread((msg, ctx)-> ClientMetabolites.setClientMetabolites(msg))
                .add();

        channel.messageBuilder(ClientSyncMessage.class, id++)
                .decoder(ClientSyncMessage::fromBytes)
                .encoder(ClientSyncMessage::toBytes)
                .consumerMainThread((msg, ctx)->ClientHandler.handleSyncMessage(msg))
                .add();

        channel.messageBuilder(ProgressSyncMessage.class, id++)
                .decoder(ProgressSyncMessage::fromBytes)
                .encoder(ProgressSyncMessage::toBytes)
                .consumerMainThread((msg, ctx)->ClientHandler.handleSyncProgress(msg))
                .add();

        channel.messageBuilder(ResultSyncMessage.class, id++)
                .decoder(ResultSyncMessage::fromBytes)
                .encoder(ResultSyncMessage::toBytes)
                .consumerMainThread((msg, ctx)->ClientHandler.handleSyncResult(msg))
                .add();
    }
}
