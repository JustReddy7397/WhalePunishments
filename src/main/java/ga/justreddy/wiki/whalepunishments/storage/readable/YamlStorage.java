package ga.justreddy.wiki.whalepunishments.storage.readable;

import ga.justreddy.wiki.whalepunishments.WhalePunishments;
import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import ga.justreddy.wiki.whalepunishments.storage.Storage;
import ga.justreddy.wiki.whalepunishments.storage.entities.PunishmentEntity;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class YamlStorage extends Storage {

    private final File folder;

    public YamlStorage(WhalePunishments instance) {
        folder = new File(instance.getDataFolder() + "/data/punishments/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @SneakyThrows
    @Override
    public boolean createPunishment(PunishmentEntity punishmentEntity) {
        if (isPunishmentActive(UUID.fromString(punishmentEntity.getUuid()), punishmentEntity.getType(), punishmentEntity.getServer())) {
            return false;
        }

        if (isPunishmentActive(punishmentEntity.getIp(), punishmentEntity.getType(), punishmentEntity.getServer())) {
            return false;
        }
        File file = punishmentEntity.getIp() == null ? getUUIDFile(UUID.
                fromString(punishmentEntity.getUuid())) :
                getIPFile(punishmentEntity.getIp());

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        final String punishmentId = punishmentEntity.getId();
        config.set(punishmentId + ".uuid", punishmentEntity.getUuid());
        config.set(punishmentId + ".ip", punishmentEntity.getIp());
        config.set(punishmentId + ".punisher", punishmentEntity.getPunisher());
        config.set(punishmentId + ".type", punishmentEntity.getType().name());
        config.set(punishmentId + ".reason", punishmentEntity.getReason());
        config.set(punishmentId + ".server", punishmentEntity.getServer());
        config.set(punishmentId + ".timeStamp", punishmentEntity.getTimestamp());
        config.set(punishmentId + ".duration", punishmentEntity.getDuration());
        config.set(punishmentId + ".active", true);
        config.set(punishmentId + ".punishmentId", punishmentId);
        config.set(punishmentId + ".expiredAt", punishmentEntity.getExpireTimestamp());
        config.set(punishmentId + ".removedBy", punishmentEntity.getRemovedBy());
        config.save(file);
        return true;
    }

    @Override
    public boolean isPunishmentActive(UUID uuid, PunishmentType type, String server) {
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPunishmentActive(String ip, PunishmentType type, String server) {
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PunishmentEntity getActivePunishment(UUID uuid, PunishmentType type, String server) {
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                PunishmentEntity entity = new PunishmentEntity();
                entity.setUuid(config.getString(key + ".uuid"));
                entity.setIp(config.getString(key + ".ip"));
                entity.setPunisher(config.getString(key + ".punisher"));
                entity.setType(PunishmentType.valueOf(config.getString(key + ".type")));
                entity.setReason(config.getString(key + ".reason"));
                entity.setServer(config.getString(key + ".server"));
                entity.setTimestamp(config.getLong(key + ".timeStamp"));
                entity.setDuration(config.getLong(key + ".duration"));
                entity.setActive(config.getBoolean(key + ".active"));
                entity.setId(config.getString(key + ".punishmentId"));
                entity.setExpireTimestamp(config.getLong(key + ".expiredAt"));
                entity.setRemovedBy(config.getString(key + ".removedBy"));
                return entity;
            }
        }
        return null;
    }

    @Override
    public PunishmentEntity getActivePunishment(String ip, PunishmentType type, String server) {
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                PunishmentEntity entity = new PunishmentEntity();
                entity.setUuid(config.getString(key + ".uuid"));
                entity.setIp(config.getString(key + ".ip"));
                entity.setPunisher(config.getString(key + ".punisher"));
                entity.setType(PunishmentType.valueOf(config.getString(key + ".type")));
                entity.setReason(config.getString(key + ".reason"));
                entity.setServer(config.getString(key + ".server"));
                entity.setTimestamp(config.getLong(key + ".timeStamp"));
                entity.setDuration(config.getLong(key + ".duration"));
                entity.setActive(config.getBoolean(key + ".active"));
                entity.setId(config.getString(key + ".punishmentId"));
                entity.setExpireTimestamp(config.getLong(key + ".expiredAt"));
                entity.setRemovedBy(config.getString(key + ".removedBy"));
                return entity;
            }
        }
        return null;
    }

    @Override
    public boolean removePunishment(UUID uuid, PunishmentType type, String server) {
        return false;
    }

    @Override
    public boolean removePunishment(String ip, PunishmentType type, String server) {
        return false;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments() {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments() {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments() {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, PunishmentType type) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, PunishmentType type) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, PunishmentType type) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, PunishmentType type) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, PunishmentType type) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, PunishmentType type) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, PunishmentType type, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, PunishmentType type, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, PunishmentType type, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, PunishmentType type, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, PunishmentType type, String server) {
        return null;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, PunishmentType type, String server) {
        return null;
    }

    @Override
    public PunishmentEntity getPunishmentById(String id) {
        return null;
    }

    private File getUUIDFile(UUID uuid) {
        final File file = new File(folder, uuid.toString() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return file;
    }

    private File getIPFile(String ip) {
        final File file = new File(folder, ip + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return file;
    }

}
