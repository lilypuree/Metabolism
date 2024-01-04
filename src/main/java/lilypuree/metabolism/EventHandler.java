package lilypuree.metabolism;

import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.metabolite.Metabolites;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MetabolismMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player && event.getItem().is(Items.POTION)) {
            if (PotionUtils.getPotion(event.getItem()) == Potions.WATER) {
                Metabolism.get(player).eat(player, Metabolites.getMetabolites().get(Items.POTION));
            }
        }
    }
}
