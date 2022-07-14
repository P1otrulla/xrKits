package net.osnixer.kits.user;

import net.osnixer.kits.kit.Kit;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID uniqueId;
    private final Map<String, Instant> kits;

    private boolean changed = false;

    User(UUID uniqueId, Map<String, Instant> kits) {
        this.uniqueId = uniqueId;
        this.kits = kits;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Map<String, Instant> getKits() {
        return this.kits;
    }

    public void markKitUse(Kit kit) {
        Instant now = Instant.now();
        Duration kitTime = kit.getDuration();

        this.kits.put(kit.getName(), now.plus(kitTime));

        this.changed = true;
    }

    public Instant getLastKitUse(Kit kit) {
        return this.kits.get(kit.getName());
    }

    public boolean canUseKit(Kit kit) {
        Instant blockMoment = this.getLastKitUse(kit);

        if (blockMoment == null) {
            return true;
        }

        Instant now = Instant.now();

        if (now.isBefore(blockMoment)) {
            return false;
        }

        this.kits.remove(kit.getName());
        this.changed = true;

        return true;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        return Objects.equals(this.uniqueId, user.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uniqueId);
    }
}
