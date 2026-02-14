package com.example.easy_command.client;

import com.example.easy_command.CommandScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ClientSetup.OPEN_GUI_KEY.isDown()) {
            Minecraft.getInstance().setScreen(new CommandScreen());
        }
    }
}
