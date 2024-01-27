package lilypuree.metabolism.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import lilypuree.metabolism.client.ClientHandler;
import lilypuree.metabolism.client.gui.MetabolismDisplayHandler;
import lilypuree.metabolism.client.gui.WarmthDisplayHandler;
import lilypuree.metabolism.config.Config;
import lilypuree.metabolism.core.Metabolism;
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

    @WrapOperation(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    public int onGetVehicleHealth(Gui instance, LivingEntity vehicle, Operation<Integer> original) {
        return 20;
    }

    @Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getMaxAirSupply()I"))
    public void onRenderPlayerHealth(GuiGraphics graphics, CallbackInfo ci, @Local(index = 22) LocalIntRef x, @Local(index = 18) LocalIntRef rightY) {
        x.set(this.getVehicleMaxHearts(this.getPlayerVehicleWithHealth()));
        if (x.get() == 0) {
            int rightHeight = this.screenHeight - rightY.get() - 10;
            rightHeight = WarmthDisplayHandler.INSTANCE.renderWarmth(this.minecraft, this.screenWidth, this.screenHeight, graphics, rightHeight);
            rightY.set(screenHeight - rightHeight - 10);
        }

        if (Config.CLIENT.metabolismOverlayShow()) {
            Metabolism metabolism = ClientHandler.getClientMetabolism(this.minecraft);
            MetabolismDisplayHandler.INSTANCE.render(graphics, this.minecraft, metabolism);
        }
    }


}
