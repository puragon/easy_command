package com.example.easy_command;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActionRequestPacker {
    private final String command;

    public ActionRequestPacker(String command) {
        this.command = command;
    }

    public ActionRequestPacker(FriendlyByteBuf buf) {
        this.command = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.command);
    }

    public static void handle(ActionRequestPacker packer, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && !player.level().isClientSide()) {
                if (player.hasPermissions(2)) {
                    player.getServer().getCommands().performPrefixedCommand(
                            player.createCommandSourceStack(),
                            packer.command
                    );
                } else {
                    player.sendSystemMessage(Component.literal("§c需要 OP 权限！"));
                }

            }
        });
        ctx.get().setPacketHandled(true);
    }
}
