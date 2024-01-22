package lilypuree.metabolism.network;

import lilypuree.metabolism.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ProgressSyncMessage(float progress) implements IMessage{
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "progress_sync");
    
    public static ProgressSyncMessage fromBytes(FriendlyByteBuf buf) {
        float progress = buf.readFloat();
        return new ProgressSyncMessage(progress);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(progress);
    }
}
