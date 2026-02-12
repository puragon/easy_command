package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CommandScreen extends Screen {
    int rows = 5;
    int columns = 4;

    int baseX;
    int baseY;
    int buttonWidth;
    int buttonHeight;

    public CommandScreen() {
        super(Component.translatable("screen.easy_command.command"));
    }

    @Override
    protected void init() {
        super.init();
        this.baseX = this.width / 4;
        this.baseY = this.height / 4;

        this.buttonWidth = (this.width / 2) / columns;
        this.buttonHeight = (this.height / 2) / rows;
        mainScreen();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        //guiGraphics.fill(x, y, x + GUI_WIDTH, y + GUI_HEIGHT, 0xFF202020);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void mainScreen() {
        this.clearWidgets();
        addDoubleWidthString("简易指令 v1.0.0", -1, 0);
        addButton("常用命令", 1, 0, this::commonScreen);
        addButton("自定义命令", 1, 1, this::customScreen);
        addButton("关于", 1, 3, this::aboutScreen);
    }

    private void commonScreen() {
        this.clearWidgets();
        addString("常用命令", -2, 1);
        addButton("主菜单", -1 ,0, this::mainScreen);

        addButton("切换生存", 0, 0, () -> sendCommandAndCloseUI("gamemode survival"));
        addButton("切换创造", 0, 1, () -> sendCommandAndCloseUI("gamemode creative"));
        addButton("切换冒险", 0, 2, () -> sendCommandAndCloseUI("gamemode adventure"));;
        addButton("切换旁观", 0, 3, () -> sendCommandAndCloseUI("gamemode spectator"));

        addButton("清理凋落物", 1, 0, () -> sendCommandAndCloseUI("kill @e[type=item]"));
        addButton("杀死自己", 1, 1, () -> sendCommandAndCloseUI("kill @s"));
        addDoubleWidthButton("杀死非玩家实体", 1, 2, () -> sendCommandAndCloseUI("kill @e[type=!player]"));

        addButton("晴天", 2, 0, () -> sendCommandAndCloseUI("weather clear"));
        addButton("雨天", 2, 1, () -> sendCommandAndCloseUI("weather rain"));
        addButton("白天", 2, 2, () -> sendCommandAndCloseUI("time set day"));
        addButton("夜晚", 2, 3, () -> sendCommandAndCloseUI("time set night"));

        addDoubleWidthButton("死亡保留物品栏：开", 3, 0, () -> sendCommandAndCloseUI("gamerule keepInventory true"));
        addDoubleWidthButton("死亡保留物品栏：关", 3, 2, () -> sendCommandAndCloseUI("gamerule keepInventory false"));
    }

    private void customScreen() {
        this.clearWidgets();
        addString("自定义命令", -2, 1);
        addButton("主菜单", -1, 0, this::mainScreen);
        addButton("设置", -1, 3, this::settingCustomScreen);

        List<CustomCommand> cmds = Config.getCommands();
        for (int i = 0; i < cmds.size(); i++) {
            CustomCommand cmd = cmds.get(i);
            addButton(cmd.name, cmd.row, cmd.column, () -> sendCommandAndCloseUI(cmd.command));
        }
    }

    private void aboutScreen() {
        this.clearWidgets();
        addString("关于", -2, 1);
        addButton("开发者", 1, 0, () -> {
            this.clearWidgets();
            addString("开发者", -2, 1);
            addButton("主菜单", -1, 0, this::mainScreen);
            addButton("返回", -1, 1, this::aboutScreen);
            addDoubleWidthString("", 1, 0);
            addDoubleWidthString("", 2, 0);
        });
        addButton("更新日志", 1, 1, () -> {
            this.clearWidgets();
            addString("更新日志", -2, 1);
            addButton("主菜单", -1, 0, this::mainScreen);
            addButton("返回", -1, 1, this::aboutScreen);
            addDoubleWidthString("v1.0.0: 第一个版本", 1, 0);
        });

    }

    private void settingCustomScreen() {
        this.clearWidgets();
        addString("自定义命令-设置", -2, 1);
        addButton("主菜单", -1, 0, this::mainScreen);
        addButton("自定义命令", -1, 1, this::customScreen);

        addButton("添加命令", 1, 0, this::addCustomScreen);
        addButton("编辑命令", 1, 1, this::editCustomScreen);
    }

    private void addCustomScreen() {
        this.clearWidgets();
        addString("添加命令", -1, 1);

        addString("名称: ", 0, -1);
        EditBox nameEdit = addEdit("名称", 0, 1);

        addString("位置(行 0-4)：", 1, -1);
        EditBox place_row = addEdit("位置(行)", 1, 1);
        place_row.setFilter(text -> {
            if (text.isEmpty()) return true;
            if (text.matches("\\d+")) {
                int number = Integer.parseInt(text);
                return number <= rows;
            }
            return false;
        });

        addString("位置(列 0-4)：", 2, -1);
        EditBox place_column = addEdit("位置(列)", 2, 1);
        place_column.setFilter(text -> {
            if (text.isEmpty()) return true;
            if (text.matches("\\d+")) {
                int number = Integer.parseInt(text);
                return number < columns;
            }
            return false;
        });

        addString("命令(无须加'/'：", 3, -1);
        EditBox commandEdit = addEdit("命令", 3, 1);

        addButton("确认", 4, 0, () -> {
            String name, command;
            int row, column;
            if (nameEdit.getValue().isEmpty()) {
                name = "Untitled";
            } else {
                name = nameEdit.getValue();
            }
            if (place_row.getValue().isEmpty()) {
                row = 0;
            } else {
                row = Integer.parseInt(place_row.getValue());
            }
            if (place_column.getValue().isEmpty()) {
                column = 0;
            } else {
                column = Integer.parseInt(place_column.getValue());
            }
            if (commandEdit.getValue().isEmpty()) {
                this.clearWidgets();
                addString("命令为空，自动取消创建", 1, 1);
                addDoubleWidthButton("点击返回", 2, 1, this::settingCustomScreen);
                return;
            }
            command = commandEdit.getValue();

            Config.addCommand(new CustomCommand(name, command, row, column));
            settingCustomScreen();
        });

        addButton("取消", 4, 2, this::settingCustomScreen);
    }

    private void editCustomScreen() {
        this.clearWidgets();
        addString("自定义命令-编辑", -2, 1);
        addButton("主菜单", -1, 0, this::mainScreen);
        addButton("自定义命令", -1, 1, this::customScreen);

        //addDoubleWidthString("目前仅支持删除命令, 点击命令删除", -2, -2);

        List<CustomCommand> cmds = Config.getCommands();
        for (int i = 0; i < cmds.size(); i++) {
            CustomCommand cmd = cmds.get(i);
            addButton(cmd.name, cmd.row, cmd.column, () -> {
                this.clearWidgets();
                addDoubleWidthString("将要删除" + cmd.name + "?", 0, 0);
                addDoubleWidthString("删除后不可恢复！", 1, 0);
                addButton("确认", 2, 0, () -> {
                    Config.removeCommand(cmd.name);
                    editCustomScreen();
                });
                addButton("取消", 2, 3, this::editCustomScreen);
            });
        }
    }

    private int getRowY(int row) {
        return baseY + row * buttonHeight;
    }

    private int getColumnX(int column) {
        return baseX + column * buttonWidth;
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void sendCommandAndCloseUI(String command) {
        sendCommand(command);
        this.minecraft.setScreen(null);
    }

    private void sendCommand(String command) {
        EasyCommandMod.NETWORK.sendToServer(new ActionRequestPacker(command));
    }

    private void addButton(String text, int row, int column, Runnable onClick) {
        int x = baseX + column * buttonWidth;
        int y = baseY + row * buttonHeight;
        addRenderableWidget(Button.builder(Component.literal(text), b -> onClick.run())
                .bounds(x, y, buttonWidth, buttonHeight)
                .build());
    }

    private void addDoubleWidthButton(String text, int row, int column, Runnable onClick) {
        addRenderableWidget(Button.builder(Component.literal(text), b -> onClick.run())
                .bounds(getColumnX(column), getRowY(row), buttonWidth * 2, buttonHeight)
                .build());
    }

    private void addString(String text, int row, int column) {
        addRenderableWidget(new StringWidget(
                getColumnX(column),
                getRowY(row),
                buttonWidth * 2,
                buttonHeight,
                Component.literal(text),
                Minecraft.getInstance().font
        ).alignCenter());
    }

    private void addDoubleWidthString(String text, int row, int column) {
        addRenderableWidget(new StringWidget(
                getColumnX(column),
                getRowY(row),
                buttonWidth * 4,
                buttonHeight,
                Component.literal(text),
                Minecraft.getInstance().font
        ).alignCenter());
    }

    private EditBox addEdit(String text, int row, int column) {
        EditBox editBox = new EditBox(Minecraft.getInstance().font,
                getColumnX(column),
                getRowY(row),
                buttonWidth * 3,
                buttonHeight,
                Component.literal(text));
        addRenderableWidget(editBox);
        return editBox;
    }
}
