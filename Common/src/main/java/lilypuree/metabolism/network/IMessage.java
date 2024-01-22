package lilypuree.metabolism.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IMessage {
    void toBytes(FriendlyByteBuf buf);
}
