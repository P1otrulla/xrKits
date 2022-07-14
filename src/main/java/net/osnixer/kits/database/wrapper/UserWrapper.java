package net.osnixer.kits.database.wrapper;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.osnixer.kits.database.persister.MapPersister;
import net.osnixer.kits.user.User;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@DatabaseTable(tableName = "kit-users")
public class UserWrapper {

    @DatabaseField(id = true)
    private UUID uniqueId;

    @DatabaseField(persisterClass = MapPersister.class)
    private Map<String, Instant> kits;

    public UserWrapper(UUID uniqueId, Map<String, Instant> kits) {
        this.uniqueId = uniqueId;
        this.kits = kits;
    }

    public UserWrapper() {

    }

    public static UserWrapper fromUser(User user) {
        return new UserWrapper(user.getUniqueId(), user.getKits());
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Map<String, Instant> getKits() {
        return this.kits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserWrapper that = (UserWrapper) o;

        return this.uniqueId.equals(that.uniqueId)
                && this.kits.equals(that.kits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uniqueId, this.kits);
    }
}
