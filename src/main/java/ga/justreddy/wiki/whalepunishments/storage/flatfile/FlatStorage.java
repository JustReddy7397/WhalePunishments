package ga.justreddy.wiki.whalepunishments.storage.flatfile;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import ga.justreddy.wiki.whalepunishments.storage.Storage;
import ga.justreddy.wiki.whalepunishments.storage.entities.PunishmentEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

/**
 * @author JustReddy
 */
public class FlatStorage extends Storage {

    private final Dao<PunishmentEntity, String> punishmentDao;

    public FlatStorage(String type, JavaPlugin plugin) {
        try {
            ConnectionSource connectionSource;

            switch (type) {
                case "sqlite" -> {
                    try (ConnectionSource source = new JdbcConnectionSource("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/punishments.db")) {
                        connectionSource = source;
                    } catch (Exception ex) {
                        throw new RuntimeException("Failed to connect to the database", ex);
                    }
                }
                case "h2" -> {
                    try (ConnectionSource source = new JdbcConnectionSource("jdbc:h2:" + plugin.getDataFolder().getAbsolutePath() + "/punishments")) {
                        connectionSource = source;
                    } catch (Exception ex) {
                        throw new RuntimeException("Failed to connect to the database", ex);
                    }
                }
                default -> {
                    throw new IllegalArgumentException("Invalid storage type: " + type);
                }
            }
            TableUtils.createTableIfNotExists(connectionSource, PunishmentEntity.class);
            punishmentDao = DaoManager.createDao(connectionSource, PunishmentEntity.class);
        }catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to the database", ex);
        }
    }

    @Override
    public boolean createPunishment(PunishmentEntity punishmentEntity) {
        if (isPunishmentActive(UUID.fromString(punishmentEntity.getUuid()), punishmentEntity.getType(), punishmentEntity.getServer())) {
            return false;
        }

        if (isPunishmentActive(punishmentEntity.getIp(), punishmentEntity.getType(), punishmentEntity.getServer())) {
            return false;
        }

        try {
            punishmentDao.create(punishmentEntity);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to create punishment", ex);
        }

        return true;
    }

    @Override
    public boolean isPunishmentActive(UUID uuid, PunishmentType type, String server) {

        try {
            List<PunishmentEntity> punishmentEntities;
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            if (!server.equalsIgnoreCase("global")) {
                lookup.put("server", server);
            }
            lookup.put("active", true);
            // This is correct right?
            punishmentEntities =
                    punishmentDao.queryForFieldValues(lookup);
            return !punishmentEntities.isEmpty();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to check if punishment is active", ex);
        }
    }

    @Override
    public boolean isPunishmentActive(String ip, PunishmentType type, String server) {

        try {
            List<PunishmentEntity> punishmentEntities;

            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip == null ? "" : ip);
            lookup.put("type", type);
            if (!server.equalsIgnoreCase("global")) {
                lookup.put("server", server);
            }
            lookup.put("active", true);

            punishmentEntities =
                    punishmentDao.queryForFieldValues(lookup);
            return !punishmentEntities.isEmpty();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to check if punishment is active", ex);
        }
    }

    @Override
    public PunishmentEntity getActivePunishment(UUID uuid, PunishmentType type, String server) {
        if (!isPunishmentActive(uuid, type, server)) return null;

        try {
            List<PunishmentEntity> punishmentEntities;

            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            if (!server.equalsIgnoreCase("global")) {
                lookup.put("server", server);
            }
            lookup.put("active", true);

            punishmentEntities =
                    punishmentDao.queryForFieldValues(lookup);
            return punishmentEntities.get(0);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get active punishment", ex);
        }

    }

    @Override
    public PunishmentEntity getActivePunishment(String ip, PunishmentType type, String server) {
        if (!isPunishmentActive(ip, type, server)) return null;

        try {
            List<PunishmentEntity> punishmentEntities;

            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("type", type);
            if (!server.equalsIgnoreCase("global")) {
                lookup.put("server", server);
            }
            lookup.put("active", true);

            punishmentEntities =
                    punishmentDao.queryForFieldValues(lookup);
            return punishmentEntities.get(0);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get active punishment", ex);
        }
    }

    @Override
    public boolean removePunishment(UUID uuid, PunishmentType type, String server) {
        return removePunishment(uuid, type, server, "CONSOLE");
    }

    @Override
    public boolean removePunishment(UUID uuid, PunishmentType type, String server, String removedBy) {
        if (!isPunishmentActive(uuid, type, server)) return false;

        try {

            List<PunishmentEntity> punishmentEntities;

            Map<String, Object> lookup = new HashMap<>();

            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            if (!server.equalsIgnoreCase("global")) {
                lookup.put("server", server);
            }
            lookup.put("active", true);

            punishmentEntities =
                    punishmentDao.queryForFieldValues(lookup);
            for (PunishmentEntity punishmentEntity : punishmentEntities) {
                punishmentEntity.setActive(false);
                punishmentEntity.setRemovedBy(removedBy);
                punishmentEntity.setExpireTimestamp(System.currentTimeMillis());
                punishmentDao.update(punishmentEntity);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to remove punishment", ex);
        }
        return true;
    }

    @Override
    public boolean removePunishment(String ip, PunishmentType type, String server) {
        return removePunishment(ip, type, server, "CONSOLE");
    }

    @Override
    public boolean removePunishment(String ip, PunishmentType type, String server, String removedBy) {
        if (!isPunishmentActive(ip, type, server)) return false;

        try {

            List<PunishmentEntity> punishmentEntities;

            Map<String, Object> lookup = new HashMap<>();

            lookup.put("ip", ip);
            lookup.put("type", type);
            if (!server.equalsIgnoreCase("global")) {
                lookup.put("server", server);
            }
            lookup.put("active", true);

            punishmentEntities =
                    punishmentDao.queryForFieldValues(lookup);
            for (PunishmentEntity punishmentEntity : punishmentEntities) {
                punishmentEntity.setActive(false);
                punishmentEntity.setRemovedBy(removedBy);
                punishmentEntity.setExpireTimestamp(System.currentTimeMillis());
                punishmentDao.update(punishmentEntity);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to remove punishment", ex);
        }
        return true;    }

    @Override
    public Set<PunishmentEntity> getAllPunishments() {
        try {
            return new HashSet<>(punishmentDao.queryForAll());
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments", ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments() {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments", ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments() {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments", ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments for uuid: " + uuid, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments for ip: " + ip, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments for uuid: " + uuid, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments for ip: " + ip, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments for uuid: " + uuid, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments for ip: " + ip, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, PunishmentType type) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments for uuid: " + uuid + " and type: " + type, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, PunishmentType type) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("type", type);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments for ip: " + ip + " and type: " + type, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, PunishmentType type) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments for uuid: " + uuid + " and type: " + type, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, PunishmentType type) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("type", type);
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments for ip: " + ip + " and type: " + type, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, PunishmentType type) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments for uuid: " + uuid + " and type: " + type, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, PunishmentType type) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("type", type);
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments for ip: " + ip + " and type: " + type, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("server", server);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments for uuid: " + uuid + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("server", server);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments for ip: " + ip + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("server", server);
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments for uuid: " + uuid + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("server", server);
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments for ip: " + ip + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("server", server);
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments for uuid: " + uuid + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("server", server);
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments for ip: " + ip + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(UUID uuid, PunishmentType type, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            lookup.put("server", server);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments for uuid: " + uuid + " and type: " + type + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllPunishments(String ip, PunishmentType type, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("type", type);
            lookup.put("server", server);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all punishments for ip: " + ip + " and type: " + type + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(UUID uuid, PunishmentType type, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            lookup.put("server", server);
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments for uuid: " + uuid + " and type: " + type + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllActivePunishments(String ip, PunishmentType type, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("type", type);
            lookup.put("server", server);
            lookup.put("active", true);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all active punishments for ip: " + ip + " and type: " + type + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, PunishmentType type, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("uuid", uuid.toString());
            lookup.put("type", type);
            lookup.put("server", server);
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments for uuid: " + uuid + " and type: " + type + " and server: " + server, ex);
        }
    }

    @Override
    public Set<PunishmentEntity> getAllExpiredPunishments(String ip, PunishmentType type, String server) {
        try {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("ip", ip);
            lookup.put("type", type);
            lookup.put("server", server);
            lookup.put("active", false);
            return new HashSet<>(punishmentDao.queryForFieldValues(lookup));
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get all expired punishments for ip: " + ip + " and type: " + type + " and server: " + server, ex);
        }
    }

    @Override
    public PunishmentEntity getPunishmentById(String id) {
        try {
            return punishmentDao.queryForId(id);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to get punishment by id: " + id, ex);
        }
    }
}
