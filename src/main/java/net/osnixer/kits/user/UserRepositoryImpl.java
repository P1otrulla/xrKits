package net.osnixer.kits.user;

import net.osnixer.kits.database.DatabaseService;
import net.osnixer.kits.database.wrapper.UserWrapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class UserRepositoryImpl implements UserRepository {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final DatabaseService databaseService;
    private final Logger logger;

    public UserRepositoryImpl(DatabaseService databaseService, Logger logger) {
        this.databaseService = databaseService;
        this.logger = logger;
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
            }
            catch (SQLException e) {
                this.logger.severe("Failed to save user! " + e.getMessage());
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
            this.logger.severe("Failed to load users! " + e.getMessage());
        }

        return users;
    }

    private User adaptUser(UserWrapper wrapper) {
        return new User(wrapper.getUniqueId(), wrapper.getKits());
    }
}
