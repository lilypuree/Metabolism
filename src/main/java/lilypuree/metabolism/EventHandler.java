package lilypuree.metabolism;

import lilypuree.metabolism.metabolism.Metabolism;
import lilypuree.metabolism.metabolite.Metabolites;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MetabolismMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack item = event.getItem();
            if (item.is(Items.POTION)) {
                if (PotionUtils.getPotion(event.getItem()) == Potions.WATER) {
                    Metabolism.get(player).eat(player, Metabolites.getMetabolites().get(Items.POTION));
                }
            }

            if (item.getItem() instanceof BowlFoodItem || item.getItem() instanceof SuspiciousStewItem) {
                event.getItem().shrink(1);
                if (event.getItem().isEmpty()) {
                    event.setResultStack(new ItemStack(Items.BOWL));
                } else {
                    if (!player.getAbilities().instabuild) {
                        ItemStack itemstack = new ItemStack(Items.BOWL);
                        if (!player.getInventory().add(itemstack)) {
                            player.drop(itemstack, false);
                        }
                    }
                    event.setResultStack(event.getItem());
                }
            }
        }
    }


}
