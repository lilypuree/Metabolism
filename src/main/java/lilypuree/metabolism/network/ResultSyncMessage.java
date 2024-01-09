package lilypuree.metabolism.network;

import lilypuree.metabolism.metabolism.MetabolismResult;
import net.minecraft.network.FriendlyByteBuf;

public record ResultSyncMessage(MetabolismResult result) {

    public static ResultSyncMessage fromBytes(FriendlyByteBuf buf) {
        MetabolismResult result = MetabolismResult.values()[buf.readInt()];
        return new ResultSyncMessage(result);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(result.ordinal());
    }
}
