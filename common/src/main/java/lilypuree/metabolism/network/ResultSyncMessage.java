package lilypuree.metabolism.network;

import lilypuree.metabolism.Constants;
import lilypuree.metabolism.core.MetabolismResult;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ResultSyncMessage(MetabolismResult result) implements IMessage {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "result_sync");

    public static ResultSyncMessage fromBytes(FriendlyByteBuf buf) {
        MetabolismResult result = MetabolismResult.values()[buf.readInt()];
        return new ResultSyncMessage(result);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(result.ordinal());
    }
}
