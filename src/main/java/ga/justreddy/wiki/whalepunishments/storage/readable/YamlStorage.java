package ga.justreddy.wiki.whalepunishments.storage.readable;

import ga.justreddy.wiki.whalepunishments.WhalePunishments;
import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import ga.justreddy.wiki.whalepunishments.storage.Storage;
import ga.justreddy.wiki.whalepunishments.storage.entities.PunishmentEntity;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
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
        if (file == null) return false;
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
        return removePunishment(uuid, type, server, "CONSOLE");
    }

    @SneakyThrows
    @Override
    public boolean removePunishment(UUID uuid, PunishmentType type, String server, String removedBy) {
        if (!isPunishmentActive(uuid, type, server)) return false;

        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                config.set(key + ".active", false);
                config.set(key + ".removedBy", removedBy);
                config.set(key + ".expiredAt", System.currentTimeMillis());
                config.save(file);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removePunishment(String ip, PunishmentType type, String server) {
        return removePunishment(ip, type, server, "CONSOLE");
    }

    @SneakyThrows
    @Override
    public boolean removePunishment(String ip, PunishmentType type, String server, String removedBy) {
        if (!isPunishmentActive(ip, type, server)) return false;

        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                config.set(key + ".active", false);
                config.set(key + ".removedBy", removedBy);
                config.set(key + ".expiredAt", System.currentTimeMillis());
                config.save(file);
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments() {
        Set<PunishmentEntity> entities = new HashSet<>();
        File[] files = folder.listFiles();
        if (files == null) return null;

        for (File file : files) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(false)) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments() {
        Set<PunishmentEntity> entities = new HashSet<>();
        File[] files = folder.listFiles();
        if (files == null) return null;

        for (File file : files) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(false)) {
                if (config.getBoolean(key + ".active")) {
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
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments() {
        Set<PunishmentEntity> entities = new HashSet<>();
        File[] files = folder.listFiles();
        if (files == null) return null;

        for (File file : files) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(false)) {
                if (!config.getBoolean(key + ".active")) {
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
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
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
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
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
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (!config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (!config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, PunishmentType type) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name())) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, PunishmentType type) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name())) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, PunishmentType type) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, PunishmentType type) {
Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, PunishmentType type) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    !config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, PunishmentType type) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    !config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".server"), server)) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".server"), server)) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    !config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    !config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, PunishmentType type, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server)) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, PunishmentType type, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server)) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, PunishmentType type, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, PunishmentType type, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, PunishmentType type, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getUUIDFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".uuid"), uuid.toString()) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    !config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, PunishmentType type, String server) {
        Set<PunishmentEntity> entities = new HashSet<>();
        File file = getIPFile(ip);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            if (Objects.equals(config.getString(key + ".ip"), ip) &&
                    Objects.equals(config.getString(key + ".type"), type.name()) &&
                    Objects.equals(config.getString(key + ".server"), server) &&
                    !config.getBoolean(key + ".active")) {
                entities.add(createPunishmentEntityFromConfig(config));
            }
        }
        return entities;
    }

    @Override
    public PunishmentEntity getPunishmentById(String id) {
        File[] files = folder.listFiles();
        if (files == null) return null;

        for (File file : files) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(false)) {
                if (Objects.equals(config.getString(key + ".punishmentId"), id)) {
                    return createPunishmentEntityFromConfig(config);
                }
            }
        }
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
        if (ip == null) return null;
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

    private PunishmentEntity createPunishmentEntityFromConfig(FileConfiguration configuration) {
        PunishmentEntity entity = new PunishmentEntity();
        entity.setUuid(configuration.getString("uuid"));
        entity.setIp(configuration.getString("ip"));
        entity.setPunisher(configuration.getString("punisher"));
        entity.setType(PunishmentType.valueOf(configuration.getString("type")));
        entity.setReason(configuration.getString("reason"));
        entity.setServer(configuration.getString("server"));
        entity.setTimestamp(configuration.getLong("timeStamp"));
        entity.setDuration(configuration.getLong("duration"));
        entity.setActive(configuration.getBoolean("active"));
        entity.setId(configuration.getString("punishmentId"));
        entity.setExpireTimestamp(configuration.getLong("expiredAt"));
        entity.setRemovedBy(configuration.getString("removedBy"));
        return entity;
    }

}
