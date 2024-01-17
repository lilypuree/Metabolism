package lilypuree.metabolism.network;

import lilypuree.metabolism.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ClientSyncMessage implements IMessage {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "client_sync");

    public float heat;
    public float warmth;
    public float food;
    public float hydration;

    public ClientSyncMessage() {
    }

    public ClientSyncMessage(float heat, float warmth, float food, float hydration) {
        this.heat = heat;
        this.warmth = warmth;
        this.food = food;
        this.hydration = hydration;
    }

    public static ClientSyncMessage fromBytes(FriendlyByteBuf buf) {
        ClientSyncMessage msg = new ClientSyncMessage();
        msg.heat = buf.readFloat();
        msg.warmth = buf.readFloat();
        msg.food = buf.readFloat();
        msg.hydration = buf.readFloat();
        return msg;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(heat);
        buf.writeFloat(warmth);
        buf.writeFloat(food);
        buf.writeFloat(hydration);
    }
}
