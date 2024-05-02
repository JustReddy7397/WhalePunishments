package ga.justreddy.wiki.whalepunishments.storage;

import com.j256.ormlite.dao.Dao;
import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import ga.justreddy.wiki.whalepunishments.storage.entities.PunishmentEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

/**
 * @author JustReddy
 */
public abstract class Storage {

    public abstract boolean createPunishment(PunishmentEntity punishmentEntity);

    public abstract boolean isPunishmentActive(UUID uuid, PunishmentType type, String server);

    public abstract boolean isPunishmentActive(String ip, PunishmentType type, String server);

    public abstract PunishmentEntity getActivePunishment(UUID uuid, PunishmentType type, String server);

    public abstract PunishmentEntity getActivePunishment(String ip, PunishmentType type, String server);

    public abstract boolean removePunishment(UUID uuid, PunishmentType type, String server);

    public abstract boolean removePunishment(UUID uuid, PunishmentType type, String server, String removedBy);

    public abstract boolean removePunishment(String ip, PunishmentType type, String server);

    public abstract boolean removePunishment(String ip, PunishmentType type, String server, String removedBy);

    public abstract Set<PunishmentEntity> getAllPunishments();

    public abstract Set<PunishmentEntity> getAllActivePunishments();

    public abstract Set<PunishmentEntity> getAllExpiredPunishments();

    public abstract Set<PunishmentEntity> getAllPunishments(UUID uuid);

    public abstract Set<PunishmentEntity> getAllPunishments(String ip);

    public abstract Set<PunishmentEntity> getAllActivePunishments(UUID uuid);

    public abstract Set<PunishmentEntity> getAllActivePunishments(String ip);

    public abstract Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid);

    public abstract Set<PunishmentEntity> getAllExpiredPunishments(String ip);

    public abstract Set<PunishmentEntity> getAllPunishments(UUID uuid, PunishmentType type);

    public abstract Set<PunishmentEntity> getAllPunishments(String ip, PunishmentType type);

    public abstract Set<PunishmentEntity> getAllActivePunishments(UUID uuid, PunishmentType type);

    public abstract Set<PunishmentEntity> getAllActivePunishments(String ip, PunishmentType type);

    public abstract Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, PunishmentType type);

    public abstract Set<PunishmentEntity> getAllExpiredPunishments(String ip, PunishmentType type);

    public abstract Set<PunishmentEntity> getAllPunishments(UUID uuid, String server);

    public abstract Set<PunishmentEntity> getAllPunishments(String ip, String server);

    public abstract Set<PunishmentEntity> getAllActivePunishments(UUID uuid, String server);

    public abstract Set<PunishmentEntity> getAllActivePunishments(String ip, String server);

    public abstract Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, String server);

    public abstract Set<PunishmentEntity> getAllExpiredPunishments(String ip, String server);

    public abstract Set<PunishmentEntity> getAllPunishments(UUID uuid, PunishmentType type, String server);

    public abstract Set<PunishmentEntity> getAllPunishments(String ip, PunishmentType type, String server);

    public abstract Set<PunishmentEntity> getAllActivePunishments(UUID uuid, PunishmentType type, String server);

    public abstract Set<PunishmentEntity> getAllActivePunishments(String ip, PunishmentType type, String server);

    public abstract Set<PunishmentEntity> getAllExpiredPunishments(UUID uuid, PunishmentType type, String server);

    public abstract Set<PunishmentEntity> getAllExpiredPunishments(String ip, PunishmentType type, String server);

    public abstract PunishmentEntity getPunishmentById(String id);



}
