package lilypuree.metabolism.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import lilypuree.metabolism.client.gui.WarmthDisplayHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    protected abstract int getVehicleMaxHearts(LivingEntity vehicle);

    @Shadow
    protected abstract LivingEntity getPlayerVehicleWithHealth();

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    public int onGetVehicleHealth(Gui gui, LivingEntity entity) {
        return 0;
    }

    @Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getMaxAirSupply()I"))
    public void onRenderPlayerHealth(GuiGraphics graphics, CallbackInfo ci, @Local(index = 22) LocalIntRef x, @Local(index = 18) LocalIntRef rightHeight) {
        x.set(this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth()));

        int i = WarmthDisplayHandler.INSTANCE.renderHealth(this.minecraft, this.screenWidth, this.screenHeight, graphics, rightHeight.get());
        rightHeight.set(i);
    }


}
