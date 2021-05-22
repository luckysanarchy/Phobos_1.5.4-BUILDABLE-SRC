// 
// Decompiled by Procyon v0.5.36
// 

package me.earth.phobos.features.modules.misc;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.Vec3i;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.command.Command;
import net.minecraft.entity.player.EntityPlayer;
import me.earth.phobos.util.Timer;
import me.earth.phobos.util.TextUtil;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.features.modules.Module;

public class ChatModifier extends Module
{
    public Setting<Suffix> suffix;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    public Setting<Boolean> autoQMain;
    public Setting<Boolean> qNotification;
    public Setting<Integer> qDelay;
    public Setting<TextUtil.Color> timeStamps;
    public Setting<TextUtil.Color> bracket;
    public Setting<Boolean> space;
    public Setting<Boolean> all;
    public Setting<Boolean> shrug;
    public Setting<Boolean> disability;
    private final Timer timer;
    private static ChatModifier INSTANCE;
    
    public ChatModifier() {
        super("Chat", "Modifies your chat", Category.MISC, true, false, false);
        this.suffix = (Setting<Suffix>)this.register(new Setting("Suffix", Suffix.NONE, "Your Suffix."));
        this.clean = (Setting<Boolean>)this.register(new Setting("CleanChat", false, "Cleans your chat"));
        this.infinite = (Setting<Boolean>)this.register(new Setting("Infinite", false, "Makes your chat infinite."));
        this.autoQMain = (Setting<Boolean>)this.register(new Setting("AutoQMain", false, "Spams AutoQMain"));
        this.qNotification = (Setting<Boolean>)this.register(new Setting("QNotification", false, v -> this.autoQMain.getValue()));
        this.qDelay = (Setting<Integer>)this.register(new Setting("QDelay", 9, 1, 90, v -> this.autoQMain.getValue()));
        this.timeStamps = (Setting<TextUtil.Color>)this.register(new Setting("Time", TextUtil.Color.NONE));
        this.bracket = (Setting<TextUtil.Color>)this.register(new Setting("Bracket", TextUtil.Color.WHITE, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.space = (Setting<Boolean>)this.register(new Setting("Space", true, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.all = (Setting<Boolean>)this.register(new Setting("All", false, v -> this.timeStamps.getValue() != TextUtil.Color.NONE));
        this.shrug = (Setting<Boolean>)this.register(new Setting("Shrug", false));
        this.disability = (Setting<Boolean>)this.register(new Setting("Disability", false));
        this.timer = new Timer();
        this.setInstance();
    }
    
    private void setInstance() {
        ChatModifier.INSTANCE = this;
    }
    
    public static ChatModifier getInstance() {
        if (ChatModifier.INSTANCE == null) {
            ChatModifier.INSTANCE = new ChatModifier();
        }
        return ChatModifier.INSTANCE;
    }
    
    @Override
    public void onUpdate() {
        if (this.shrug.getValue()) {
            ChatModifier.mc.player.sendChatMessage(TextUtil.shrug);
            this.shrug.setValue(false);
        }
        if (this.autoQMain.getValue()) {
            if (!this.shouldSendMessage((EntityPlayer)ChatModifier.mc.player)) {
                return;
            }
            if (this.qNotification.getValue()) {
                Command.sendMessage("<AutoQueueMain> Sending message: /queue main");
            }
            ChatModifier.mc.player.sendChatMessage("/queue main");
            this.timer.reset();
        }
    }
}
