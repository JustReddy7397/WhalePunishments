package ga.justreddy.wiki.whalepunishments.listeners;

import ga.justreddy.wiki.whalepunishments.WhalePunishments;
import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import ga.justreddy.wiki.whalepunishments.storage.entities.PunishmentEntity;
import ga.justreddy.wiki.whalepunishments.util.TextUtil;
import ga.justreddy.wiki.whalepunishments.util.TimeUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
public class PlayerLoginListener implements Listener {

    private final WhalePunishments plugin;

    public PlayerLoginListener(WhalePunishments plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        boolean isPermaBanned = plugin.getStorage().isPunishmentActive(event.getUniqueId(), PunishmentType.BAN, plugin.getConfig().getString("server"));

        boolean isTempBanned = plugin.getStorage().isPunishmentActive(event.getUniqueId(), PunishmentType.TEMPBAN, plugin.getConfig().getString("server"));

        boolean isIpBanned = plugin.getStorage().isPunishmentActive(event.getAddress().getHostAddress(), PunishmentType.IPBAN, plugin.getConfig().getString("server"));


        if (isPermaBanned) {
            var entity = plugin.getStorage().getActivePunishment(event.getUniqueId(), PunishmentType.BAN, plugin.getConfig().getString("server"));
            handlePermaBan(event, entity);
        }

        if (isTempBanned) {
            var entity = plugin.getStorage().getActivePunishment(event.getUniqueId(), PunishmentType.TEMPBAN, plugin.getConfig().getString("server"));
            handleTempBan(event, entity);
        }

        if (isIpBanned) {
            var entity = plugin.getStorage().getActivePunishment(event.getAddress().getHostAddress(), PunishmentType.IPBAN, plugin.getConfig().getString("server"));
            handleIpBan(event, entity);
        }

    }

    private void handlePermaBan(AsyncPlayerPreLoginEvent event, PunishmentEntity entity) {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, String.join("\n", TextUtil.getMessage(entity)));
    }

    private void handleTempBan(AsyncPlayerPreLoginEvent event, PunishmentEntity entity) {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, String.join("\n", TextUtil.getMessage(entity)));
    }

    private void handleIpBan(AsyncPlayerPreLoginEvent event, PunishmentEntity entity) {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, String.join("\n", TextUtil.getMessage(entity)));
    }



}
