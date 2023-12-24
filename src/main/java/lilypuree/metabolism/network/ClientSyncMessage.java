package lilypuree.metabolism.network;

import net.minecraft.network.FriendlyByteBuf;

public class ClientSyncMessage {
    public float heat;
    public float warmth;
    public float food;
    public float energy;
    
    public ClientSyncMessage(){}

    public ClientSyncMessage(float heat, float warmth, float food, float energy) {
        this.heat = heat;
        this.warmth = warmth;
        this.food = food;
        this.energy = energy;
    }
    
    public static ClientSyncMessage fromBytes(FriendlyByteBuf buf){
        ClientSyncMessage msg = new ClientSyncMessage();
        msg.heat = buf.readFloat();
        msg.warmth = buf.readFloat();
        msg.food = buf.readFloat();
        msg.energy = buf.readFloat();
        return msg;
    }
    
    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(heat);
        buf.writeFloat(warmth);
        buf.writeFloat(food);
        buf.writeFloat(energy);
    }
}
