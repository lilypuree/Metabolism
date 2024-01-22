package lilypuree.metabolism.mixin;

import lilypuree.metabolism.core.Metabolism;
import lilypuree.metabolism.core.metabolite.Metabolite;
import lilypuree.metabolism.core.metabolite.Metabolites;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public class ItemMixin {
    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;canEat(Z)Z"))
    private boolean onCanEat(Player instance, boolean pCanAlwaysEat, Level level, Player player, InteractionHand hand) {
        if (player.getAbilities().invulnerable || pCanAlwaysEat)
            return true;
        else {
            ItemStack itemStack = instance.getItemInHand(hand);
            Metabolism metabolism = Metabolism.get(instance);
            Metabolite metabolite = Metabolites.getMetabolite(itemStack, instance);
            return metabolism.canEat(metabolite);
        }
    }
}
