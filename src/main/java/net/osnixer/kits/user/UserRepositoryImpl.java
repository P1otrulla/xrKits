package net.osnixer.kits.user;

import net.osnixer.kits.database.DatabaseService;
import net.osnixer.kits.database.wrapper.UserWrapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepositoryImpl implements UserRepository {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final DatabaseService databaseService;

    public UserRepositoryImpl(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void saveUser(User user) {
        if (!user.isChanged()) {
            return;
        }

        this.executor.submit(() -> {
            try {
                this.databaseService.getUsersDao().createOrUpdate(UserWrapper.fromUser(user));

                user.setChanged(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            this.databaseService.getUsersDao().queryForAll().forEach(userWarp -> {
                User user = this.adaptUser(userWarp);

                users.add(user);
            });
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private User adaptUser(UserWrapper wrapper) {
        return new User(wrapper.getUniqueId(), wrapper.getKits());
    }
}
