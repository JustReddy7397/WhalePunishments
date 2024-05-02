package ga.justreddy.wiki.whalepunishments.storage.readable;

import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import ga.justreddy.wiki.whalepunishments.storage.Storage;
import ga.justreddy.wiki.whalepunishments.storage.entities.PunishmentEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class JsonStorage extends Storage {

    private final JavaPlugin plugin;

    public JsonStorage(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean createPunishment(PunishmentEntity punishmentEntity) {
        return false;
    }

    @Override
    public boolean isPunishmentActive(UUID uuid, PunishmentType type, String server) {
        return false;
    }

    @Override
    public boolean isPunishmentActive(String ip, PunishmentType type, String server) {
        return false;
    }

    @Override
    public PunishmentEntity getActivePunishment(UUID uuid, PunishmentType type, String server) {
        return null;
    }

    @Override
    public PunishmentEntity getActivePunishment(String ip, PunishmentType type, String server) {
        return null;
    }

    @Override
    public boolean removePunishment(UUID uuid, PunishmentType type, String server) {
        return false;
    }

    @Override
    public boolean removePunishment(UUID uuid, PunishmentType type, String server, String removedBy) {
        return false;
    }

    @Override
    public boolean removePunishment(String ip, PunishmentType type, String server) {
        return false;
    }

    @Override
    public boolean removePunishment(String ip, PunishmentType type, String server, String removedBy) {
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
}
