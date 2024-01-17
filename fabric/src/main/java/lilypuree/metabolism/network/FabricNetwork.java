package lilypuree.metabolism.network;

import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.client.ClientMetabolites;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class FabricNetwork {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ClientSyncMessage.ID, (client, handler, buf, responseSender) -> {
            ClientSyncMessage msg = ClientSyncMessage.fromBytes(buf);
            client.execute(() -> {
                ClientHandler.handleSyncMessage(msg);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ProgressSyncMessage.ID, (client, handler, buf, responseSender) -> {
            ProgressSyncMessage msg = ProgressSyncMessage.fromBytes(buf);
            client.execute(() -> {
                ClientHandler.handleSyncProgress(msg);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ResultSyncMessage.ID, (client, handler, buf, responseSender) -> {
            ResultSyncMessage msg = ResultSyncMessage.fromBytes(buf);
            client.execute(() -> {
                ClientHandler.handleSyncResult(msg);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(MetabolitesPacket.ID, (client, handler, buf, responseSender) -> {
            MetabolitesPacket msg = MetabolitesPacket.fromBytes(buf);
            client.execute(()->{
                ClientMetabolites.setClientMetabolites(msg);
            });
        });
    }
}
