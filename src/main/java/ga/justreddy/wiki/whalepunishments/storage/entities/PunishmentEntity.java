package ga.justreddy.wiki.whalepunishments.storage.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import ga.justreddy.wiki.whalepunishments.enums.PunishmentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author JustReddy
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@DatabaseTable(tableName = "wp_punishments")
public class PunishmentEntity {

    @DatabaseField(columnName = "id", unique = true, id = true)
    private String id;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "punisher", canBeNull = false)
    private String punisher;

    @DatabaseField(columnName = "reason", canBeNull = false)
    private String reason;

    @DatabaseField(columnName = "type", canBeNull = false, dataType = DataType.ENUM_STRING)
    private PunishmentType type;

    @DatabaseField(columnName = "duration", canBeNull = false)
    private long duration;

    @DatabaseField(columnName = "timestamp", canBeNull = false)
    private long timestamp;

    @DatabaseField(columnName = "active", canBeNull = false)
    private boolean active;

    @DatabaseField(columnName = "ip")
    private String ip;

    @DatabaseField(columnName = "server")
    private String server;

    @DatabaseField(columnName = "removed_by")
    private String removedBy;

    @DatabaseField(columnName = "expire_timestamp")
    private long expireTimestamp;


}
