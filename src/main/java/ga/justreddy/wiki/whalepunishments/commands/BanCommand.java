package ga.justreddy.wiki.whalepunishments.commands;


import ga.justreddy.wiki.whalepunishments.WhalePunishments;
import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import ga.justreddy.wiki.whalepunishments.storage.entities.PunishmentEntity;
import ga.justreddy.wiki.whalepunishments.util.TextUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Switch;
import revxrsal.commands.bukkit.annotation.CommandPermission;

/**
 * @author JustReddy
 */
public class BanCommand {

    @Command("ban")
    @CommandPermission("whalepunishments.ban")
    public void ban(ConsoleCommandSender sender, OfflinePlayer target, String time, @Switch("s") boolean silent, @Switch("server") boolean server, @Default("No Reason Specified") String reason) {
        // Ban the player

        if (!sender.hasPermission("whalepunishments.ban")) {
            TextUtil.sendMessage(sender, WhalePunishments.getInstance().getConfig().getString("messages.invalid-permissions"));
        }

        final boolean bungeeMode = WhalePunishments.getInstance().getConfig().getBoolean("settings.bungeecord.enabled");
        final boolean commandsDisabled = WhalePunishments.getInstance().getConfig().getBoolean("settings.bungeecord.disable-commands");

        if (bungeeMode && commandsDisabled) {
            TextUtil.sendMessage(sender, WhalePunishments.getInstance().getConfig()
                    .getString("messages.command-disabled"));
            return;
        }

        if (bungeeMode) return;

        // Ban the player

        Player onlineTarget = target.getPlayer();

        PunishmentEntity entity = new PunishmentEntity();
        entity.setUuid(target.getUniqueId().toString());
        entity.setPunisher(sender instanceof Player ? ((Player) sender).getUniqueId().toString() : "CONSOLE");
        entity.setReason(reason);
        entity.setType(PunishmentType.BAN);
        entity.setDuration(-1L);
        entity.setTimestamp(System.currentTimeMillis());
        entity.setActive(true);
        entity.setIp(null);
        entity.setServer(server ? WhalePunishments.getInstance().getConfig().getString("server") : "global");
        entity.setRemovedBy(null);
        entity.setExpireTimestamp(-1L);
        entity.setId("BAN-" + System.currentTimeMillis());
        if (onlineTarget != null) {
            onlineTarget.kickPlayer(TextUtil.getMessage(entity));
        }

        WhalePunishments.getInstance().getStorage().createPunishment(entity);


    }

}
