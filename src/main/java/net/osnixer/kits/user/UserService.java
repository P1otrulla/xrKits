package net.osnixer.kits.user;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final Map<UUID, User> users = new HashMap<>();

    public void createUser(UUID uniqueId) {
        User user = new User(uniqueId, new HashMap<>());

        this.addUser(user);
    }

    public Optional<User> findUser(UUID uniqueId) {
        return Optional.ofNullable(this.users.get(uniqueId));
    }

    public void addUser(User user) {
        this.users.put(user.getUniqueId(), user);
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(this.users.values());
    }
}
