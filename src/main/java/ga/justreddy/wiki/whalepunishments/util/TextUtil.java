package ga.justreddy.wiki.whalepunishments.util;
import ga.justreddy.wiki.whalepunishments.WhalePunishments;
import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import ga.justreddy.wiki.whalepunishments.storage.entities.PunishmentEntity;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author JustReddy
 */
public class TextUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static void sendMessage(Player player, String format) {
        Audience audience = WhalePunishments.getInstance().getAdventure().player(player);
        audience.sendMessage(MINI_MESSAGE.deserialize(format));
    }

    public static String convertText(String text) {
        return ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(MINI_MESSAGE.deserialize(text)));
    }

    public static String getPunishmentType(PunishmentType type, long time) {
        switch (type) {
            case BAN, MUTE -> {
                return WhalePunishments.getInstance().getConfig().getString("messages.punishment-type-strings.permanent");
            }
            case TEMPBAN, TEMPMUTE -> {
                return WhalePunishments.getInstance().getConfig().getString("messages.punishment-type-strings.temporary");
            }
            case IPBAN, IPMUTE -> {
                return time == -1L ? WhalePunishments.getInstance().getConfig().getString("messages.punishment-type-strings.ip-permanent") : WhalePunishments.getInstance().getConfig().getString("messages.punishment-type-strings.ip-temporary");
            }
            case WARN -> {
                return WhalePunishments.getInstance().getConfig().getString("messages.punishment-type-strings.warn");
            }
            default -> {
                return "Unknown";
            }
        }
    }

    public static String getPunishmentType(PunishmentType type) {
        return getPunishmentType(type, -1L);
    }

    public static String getMessage(PunishmentEntity entity) {
        final List<String> banMessage = WhalePunishments.getInstance().getConfig().getStringList("messages.formats.ban");
        StringBuilder messages = new StringBuilder();
        for (String message : banMessage) {
            message = message.replaceAll("<type>", TextUtil.getPunishmentType(entity.getType(), entity.getDuration()));
            message = message.replaceAll("<reason>", entity.getReason());
            message = message.replaceAll("<banid>", entity.getId());
            message = message.replaceAll("<server>", entity.getServer());
            message = message.replaceAll("<time>", TimeUtil.getDurationString(entity.getDuration()));
            messages.append(convertText(message)).append("\n");
        }
        return messages.toString();
    }

}
