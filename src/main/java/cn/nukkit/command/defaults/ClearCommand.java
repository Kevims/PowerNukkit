package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

import java.util.StringJoiner;

/**
 * Created by KCodeYT
 */
public class ClearCommand extends VanillaCommand {

    public ClearCommand(String name) {
        super(name, "%nukkit.command.clear.description", "%nukkit.command.clear.usage");
        this.setPermission("nukkit.command.clear.self;"
                + "nukkit.command.clear.other");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length >= 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        if (args.length == 1) {
            if (!sender.hasPermission("nukkit.command.clear.other")) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return true;
            }
            Player player = sender.getServer().getPlayer(args[0]);
            if (player != null) {
                player.getInventory().clearAll();
                Command.broadcastCommandMessage(sender, new TranslationContainer("commands.clear.successful", player.getName()));
            } else if (args[0].equals("@s")) {
                if (!sender.hasPermission("nukkit.command.clear.self")) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                    return true;
                }
                ((Player) sender).getInventory().clearAll();
                sender.sendMessage(new TranslationContainer("commands.kill.successful", sender.getName()));
            } else if (args[0].equals("@a")) {
                if (!sender.hasPermission("nukkit.command.clear.other")) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                    return true;
                }
                for (Level level : Server.getInstance().getLevels().values()) {
                    for (Entity entity : level.getEntities()) {
                        if (entity instanceof Player) {
                            ((Player) entity).getInventory().clearAll();
                        }
                    }
                }
                sender.sendMessage(new TranslationContainer(TextFormat.GOLD + "%commands.clear.all.successful"));
            } else {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.player.notFound"));
            }
            return true;
        }
        if (sender instanceof Player) {
            if (!sender.hasPermission("nukkit.command.clear.self")) {
                sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                return true;
            }
            ((Player) sender).getInventory().clearAll();
            sender.sendMessage(new TranslationContainer("commands.kill.successful", sender.getName()));
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        return true;
    }
}
