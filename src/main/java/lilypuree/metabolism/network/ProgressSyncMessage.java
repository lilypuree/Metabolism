package lilypuree.metabolism.network;

import net.minecraft.network.FriendlyByteBuf;

public record ProgressSyncMessage(float progress) {

    public static ProgressSyncMessage fromBytes(FriendlyByteBuf buf) {
        float progress = buf.readFloat();
        return new ProgressSyncMessage(progress);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(progress);
    }
}
