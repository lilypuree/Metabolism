package lilypuree.metabolism.network;

import com.google.common.collect.ImmutableMap;
import lilypuree.metabolism.Constants;
import lilypuree.metabolism.core.metabolite.Metabolite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Map;

public record MetabolitesPacket(Map<Item, Metabolite> metaboliteMap) implements IMessage{
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "metabolite_sync");

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(metaboliteMap.size());
        metaboliteMap.forEach((item, metabolite) -> {
            buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(item));
            buffer.writeJsonWithCodec(Metabolite.CODEC, metabolite);
        });
    }

    public static MetabolitesPacket fromBytes(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        if (size > 0) {
            ImmutableMap.Builder<Item, Metabolite> builder = new ImmutableMap.Builder<>();
            for (int i = 0; i < size; i++) {
                ResourceLocation key = buffer.readResourceLocation();
                Metabolite metabolite = buffer.readJsonWithCodec(Metabolite.CODEC);
                builder.put(BuiltInRegistries.ITEM.get(key), metabolite);
            }
            return new MetabolitesPacket(builder.build());
        } else return new MetabolitesPacket(ImmutableMap.of());
    }
}
