package lilypuree.metabolism.network;

import lilypuree.metabolism.MetabolismMod;
import lilypuree.metabolism.client.ClientHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {
    private static final ResourceLocation NAME = MetabolismMod.getID("network");
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            NAME, () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 1;
        channel.messageBuilder(MetabolitesPacket.class, id++)
                .decoder(MetabolitesPacket::decode)
                .encoder(MetabolitesPacket::encode)
                .consumerMainThread(MetabolitesPacket::handle) 
                .add();

        channel.messageBuilder(ClientSyncMessage.class, id++)
                .decoder(ClientSyncMessage::fromBytes)
                .encoder(ClientSyncMessage::toBytes)
                .consumerMainThread(ClientHandler::handleSyncMessage)
                .add();

    }
}
