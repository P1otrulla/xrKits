package net.osnixer.kits.user;

import java.util.List;

public interface UserRepository {

    void saveUser(User user);

    List<User> loadAllUsers();
}
