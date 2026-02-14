package com.example.easy_command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.UUID;

public class CommandScreen extends Screen {
    // ROWS = 0, 1, 2, 3  以界面大小=4计算
    // COLUMNS = （-2, 8]
    int maxRow = 8;
    int maxColunm = 3;
    public static final int BUTTON_WIDTH = 100;
    public static final int BUTTON_HEIGHT = 20;


    int baseX;
    int baseY;


    public CommandScreen() {
        super(Component.translatable("screen.easy_command.command"));
    }

    @Override
    protected void init() {
        super.init();
        int totalContenWidth = BUTTON_WIDTH * 4;
        int totalContentHeight = BUTTON_HEIGHT * 5;
        this.baseX = (this.width - totalContenWidth) / 2;
        this.baseY = (this.height - totalContentHeight) / 4;
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
        addDoubleWidthString("简易指令 v1.1.0", -1, 0);
        addButton("常用命令", 2, 0, this::commonScreen);
        addButton("游戏规则", 3, 0, this::gameruleScreen);
        addButton("自定义命令", 2, 1, this::customScreen);
        addButton("关于", 2, 3, this::aboutScreen);
    }

    private void initNewScreen(String title) {
        this.clearWidgets();
        addString(title, -1, 1);
        addButton("主菜单", 0 ,0, this::mainScreen);
    }

    private void commonScreen() {
        initNewScreen("常用命令");

        addButton("切换生存", 1, 0, () -> sendCommandAndCloseUI("gamemode survival"));
        addButton("切换创造", 1, 1, () -> sendCommandAndCloseUI("gamemode creative"));
        addButton("切换冒险", 1, 2, () -> sendCommandAndCloseUI("gamemode adventure"));;
        addButton("切换旁观", 1, 3, () -> sendCommandAndCloseUI("gamemode spectator"));

        addButton("清理凋落物", 2, 0, () -> sendCommandAndCloseUI("kill @e[type=item]"));
        addButton("杀死自己", 2, 1, () -> sendCommandAndCloseUI("kill @s"));
        addButton("清除所有生物", 2, 2, () -> sendCommandAndCloseUI("kill @e[type=!player]"));
        addButton("清除箭矢", 2, 3, () -> sendCommandAndCloseUI("kill @e[type=arrow]"));

        addButton("晴天", 3, 0, () -> sendCommandAndCloseUI("weather clear"));
        addButton("雨天", 3, 1, () -> sendCommandAndCloseUI("weather rain"));
        addButton("白天", 3, 2, () -> sendCommandAndCloseUI("time set day"));
        addButton("夜晚", 3, 3, () -> sendCommandAndCloseUI("time set night"));

        addButton("清除效果", 4, 0, () -> sendCommandAndCloseUI("effect clear"));
        addButton("夜视", 4, 1, () -> sendCommandAndCloseUI("effect give @s minecraft:night_vision infinite"));
        addButton("抗性提升II", 4, 2, () -> sendCommandAndCloseUI("effect give @s minecraft:resistance 1800 1"));
        addButton("抗性提升V", 4, 3, () -> sendCommandAndCloseUI("effect give @s minecraft:resistance 1800 4"));
    }

    private void gameruleScreen() {
        initNewScreen("游戏规则");
        addButton("死亡保留物品栏", 1, 0, () -> gameruleTFScreen("死亡保留物品栏", "gamerule keepInventory"));
        addButton("火焰蔓延", 1, 1, () -> gameruleTFScreen("火焰蔓延", "gamerule doFireTick"));
        addButton("生物破坏", 1, 2, () -> gameruleTFScreen("生物破坏", "gamerule mobGriefing"));
        addButton("TNT爆炸", 1, 3, () -> gameruleTFScreen("TNT爆炸", "gamerule tntExplosionDropDecay"));
        addButton("溺水伤害", 2, 0, () -> gameruleTFScreen("溺水伤害", "gamerule drowningDamage"));
        addButton("摔落伤害", 2, 1, () -> gameruleTFScreen("摔落伤害", "gamerule fallDamage"));
        addButton("火焰伤害", 2, 2, () -> gameruleTFScreen("火焰伤害", "gamerule fireDamage"));
        addButton("冰冻伤害", 2, 3, () -> gameruleTFScreen("冰冻伤害", "gamerule freezeDamage"));
        addButton("生命自然恢复", 3, 0, () -> gameruleTFScreen("生命自然恢复", "gamerule naturalRegeneration"));
        addButton("立即重生", 3, 1, () -> gameruleTFScreen("立即重生", "gamerule doImmediateRespawn"));
        addButton("禁用袭击", 3, 2, () -> gameruleTFScreen("禁用袭击", "gamerule disableRaids"));
        addButton("生成生物", 3, 3, () -> gameruleTFScreen("生成生物", "gamerule doMobSpawning"));
        addButton("生成幻翼", 4, 0, () -> gameruleTFScreen("生成幻翼", "gamerule doInsomnia"));
        addButton("生成灾厄巡逻队", 4, 1, () -> gameruleTFScreen("生成灾厄巡逻队", "gamerule doPatrolSpawning"));
        addButton("生成流浪商人", 4, 2, () -> gameruleTFScreen("生成流浪商人", "gamerule doTraderSpawning"));
        addButton("生成监守者", 4, 3, () -> gameruleTFScreen("生成监守者", "gamerule doWardenSpawning"));
        //addButton("方块爆炸破坏掉落物", 5, 1, () -> gameruleTFScreen("", "gamerule blockExplosionDropDecay"));
        addButton("时间流逝", 5, 0, () -> gameruleTFScreen("时间流逝", "gamerule doDaylightCycle"));
        addButton("天气更替", 5, 1, () -> gameruleTFScreen("天气更替", "gamerule doWeatherCycle"));
        addButton("无限水", 5, 2, () -> gameruleTFScreen("无限水", "gamerule waterSourceConversion"));
        addButton("进度通知", 6, 0, () -> gameruleTFScreen("进度通知", "gamerule announceAdvancements"));
        addButton("显示死亡消息", 6, 1, () -> gameruleTFScreen("显示死亡消息", "gamerule showDeathMessages"));
        addButton("全局声音事件", 7, 0, () -> gameruleTFScreen("全局声音事件", "gamerule globalSoundEvents"));
    }

    private void gameruleTFScreen(String name, String cmd) {
        initNewScreen(name);
        addButton("返回", 0, 1, this::gameruleScreen);

        addButton("开", 2, 0, () -> sendCommandAndCloseUI(cmd + " true"));
        addButton("关", 2, 1, () -> sendCommandAndCloseUI(cmd + " false"));
    }

    private void customScreen() {
        initNewScreen("自定义命令");
        addButton("设置", 0, 3, this::settingCustomScreen);

        List<CustomCommand> cmds = Config.getCommands();
        for (int i = 0; i < cmds.size(); i++) {
            CustomCommand cmd = cmds.get(i);
            addButton(cmd.name, cmd.row, cmd.column, () -> sendCommandAndCloseUI(cmd.command));
        }
    }

    private void aboutScreen() {
        initNewScreen("关于");
        addButton("开发者", 1, 0, () -> {
            initNewScreen("开发者");
            addButton("返回", 0, 1, this::aboutScreen);
            addDoubleWidthString("bilibili: puragon", 3, 0);
            addDoubleWidthString("github: puragon", 4, 0);
        });
        addButton("更新日志", 1, 1, () -> {
            initNewScreen("更新日志");
            addButton("返回", 0, 1, this::aboutScreen);
            addDoubleWidthString("v1.0.0: 第一个版本", 3, 0);
            addDoubleWidthString("v1.0.1: 调整界面显示，细化游戏规则相关命令", 4, 0);
            addDoubleWidthString("v1.1.0: 完成自定义指令编辑功能, 添加更多常用指令", 5, 0);
        });

    }

    private void settingCustomScreen() {
        initNewScreen("自定义命令-设置");
        addButton("自定义命令", 0, 1, this::customScreen);

        addButton("添加命令", 1, 0, this::addCustomScreen);
        addButton("编辑命令", 1, 1, this::editCustomScreen);
    }

    private void addCustomScreen() {
        editCustomCommand(UUID.randomUUID(), "", "", "", "", false);
    }

    private void editCustomCommand(UUID default_uuid, String default_name, String default_row, String default_column, String default_command, Boolean editor) {
        initNewScreen("添加命令");

        addString("UUID (无需更改): ", 1, -1);
        EditBox uuidEdit = addEdit("uuid", 1, 1);
        uuidEdit.setMaxLength(36);
        uuidEdit.setValue(default_uuid.toString());

        addString("名称: ", 2, -1);
        EditBox nameEdit = addEdit("名称", 2, 1);
        nameEdit.setValue(default_name);

        addString("位置(行 0-8)：", 3, -1);
        EditBox place_row = addEdit("位置(行)", 3, 1);
        place_row.setFilter(text -> {
            if (text.isEmpty()) return true;
            if (text.matches("\\d+")) {
                int number = Integer.parseInt(text);
                return number <= maxRow;
            }
            return false;
        });
        place_row.setValue(default_row);

        addString("位置(列 0-3)：", 4, -1);
        EditBox place_column = addEdit("位置(列)", 4, 1);
        place_column.setFilter(text -> {
            if (text.isEmpty()) return true;
            if (text.matches("\\d+")) {
                int number = Integer.parseInt(text);
                return number < maxColunm;
            }
            return false;
        });
        place_column.setValue(default_column);

        addString("命令(无须加'/')：", 5, -1);
        EditBox commandEdit = addEdit("命令", 5, 1);
        commandEdit.setMaxLength(256);
        commandEdit.setValue(default_command);

        addButton("确认", 6, 0, () -> {
            UUID uuid;
            String name, command;
            int row, column;
            if (uuidEdit.getValue().isEmpty()) {
                uuid = UUID.randomUUID();
            } else {
                try {
                    uuid = UUID.fromString(uuidEdit.getValue());
                } catch (IllegalArgumentException e) {
                    addDoubleWidthString("uuid格式错误，请检查格式或将uuid留空", 8, 0);
                    return;
                }
            }
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

            Config.addCommand(new CustomCommand(uuid, name, command, row, column));
            settingCustomScreen();
        });

        if (editor) addButton("取消", 6, 1, this::editCustomScreen);
        else addButton("取消", 6, 1, this::settingCustomScreen);
    }

    private void editCustomScreen() {
        initNewScreen("自定义命令-编辑");
        addButton("自定义命令-设置", 0, 1, this::settingCustomScreen);

        List<CustomCommand> cmds = Config.getCommands();
        for (int i = 0; i < cmds.size(); i++) {
            CustomCommand cmd = cmds.get(i);
            addButton(cmd.name, cmd.row, cmd.column, () -> {
                editScreen(cmd.uuid, cmd.name, cmd.row + "", cmd.column + "", cmd.command);
            });
        }
    }

    private void editScreen(UUID uuid, String name, String row, String column, String command) {
        editCustomCommand(uuid, name, row + "", column + "", command, true);
        addButton("还原", 6, 2, () -> editScreen(uuid, name, row + "", column + "", command));
        addButton("删除", 6, 3, () -> {
            initNewScreen("删除 " + name);
            addButton("返回", 0, 1, () -> editScreen(uuid, name, row + "", column + "", command));
            addDoubleWidthString("确认要删除" + name + "吗?", 1, 0);
            addButton("是", 3, 0, () -> {
                Config.removeCommand(uuid);
                editCustomScreen();
            });
            addButton("否", 3, 2, () -> editScreen(uuid, name, row + "", column + "", command));
        });
    }

    private int getRowY(int row) {
        return baseY + row * BUTTON_HEIGHT;
    }

    private int getColumnX(int column) {
        return baseX + column * BUTTON_WIDTH;
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
        int x = baseX + column * BUTTON_WIDTH;
        int y = baseY + row * BUTTON_HEIGHT;
        addRenderableWidget(Button.builder(Component.literal(text), b -> onClick.run())
                .bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
    }

    private void addDoubleWidthButton(String text, int row, int column, Runnable onClick) {
        addRenderableWidget(Button.builder(Component.literal(text), b -> onClick.run())
                .bounds(getColumnX(column), getRowY(row), BUTTON_WIDTH * 2, BUTTON_HEIGHT)
                .build());
    }

    private void addString(String text, int row, int column) {
        addRenderableWidget(new StringWidget(
                getColumnX(column),
                getRowY(row),
                BUTTON_WIDTH * 2,
                BUTTON_HEIGHT,
                Component.literal(text),
                Minecraft.getInstance().font
        ).alignCenter());
    }

    private void addDoubleWidthString(String text, int row, int column) {
        addRenderableWidget(new StringWidget(
                getColumnX(column),
                getRowY(row),
                BUTTON_WIDTH * 4,
                BUTTON_HEIGHT,
                Component.literal(text),
                Minecraft.getInstance().font
        ).alignCenter());
    }

    private EditBox addEdit(String text, int row, int column) {
        EditBox editBox = new EditBox(Minecraft.getInstance().font,
                getColumnX(column),
                getRowY(row),
                BUTTON_WIDTH * 3,
                BUTTON_HEIGHT,
                Component.literal(text));
        addRenderableWidget(editBox);
        return editBox;
    }
}
