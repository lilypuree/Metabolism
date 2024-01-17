package lilypuree.metabolism.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {
    
    @Mutable
    @Accessor(value = "maxStackSize")
    void setMaxStackSize(int maxStackSize);
}
