package lilypuree.metabolism.network;

import com.google.common.collect.ImmutableMap;
import lilypuree.metabolism.client.ClientMetabolites;
import lilypuree.metabolism.metabolite.Metabolite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.function.Supplier;

public record MetabolitesPacket(Map<Item, Metabolite> metaboliteMap) {
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(metaboliteMap.size());
        metaboliteMap.forEach((item, metabolite) -> {
            buffer.writeResourceLocation(ForgeRegistries.ITEMS.getKey(item));
            buffer.writeJsonWithCodec(Metabolite.CODEC, metabolite);
        });
    }

    public static MetabolitesPacket decode(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        if (size > 0) {
            ImmutableMap.Builder<Item, Metabolite> builder = new ImmutableMap.Builder<>();
            for (int i = 0; i < size; i++) {
                ResourceLocation key = buffer.readResourceLocation();
                Metabolite metabolite = buffer.readJsonWithCodec(Metabolite.CODEC);
                builder.put(ForgeRegistries.ITEMS.getValue(key), metabolite);
            }
            return new MetabolitesPacket(builder.build());
        } else return new MetabolitesPacket(ImmutableMap.of());
    }

    public static void handle(MetabolitesPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ClientMetabolites.setClientMetabolites(packet.metaboliteMap);
    }
}
