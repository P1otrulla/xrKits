package net.osnixer.kits.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariDataSource;
import net.osnixer.kits.config.implementation.PluginConfig;
import net.osnixer.kits.database.wrapper.UserWrapper;

import java.util.UUID;

public class DatabaseService {

    private ConnectionSource connectionSource;
    private Dao<UserWrapper, UUID> usersDao;

    public DatabaseService(PluginConfig config) {
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://" + config.database.host + ":" + config.database.port + "/" + config.database.base + "?useSSL=" + config.database.useSSL);
        dataSource.setUsername(config.database.user);
        dataSource.setPassword(config.database.pass);
        dataSource.setConnectionTimeout(config.database.timeOut);
        dataSource.setMaximumPoolSize(config.database.poolSize);

        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSource.addDataSourceProperty("useServerPrepStmts", true);
        dataSource.addDataSourceProperty("serverTimezone", "GMT");

        try {
            this.connectionSource = new DataSourceConnectionSource(dataSource, dataSource.getJdbcUrl());

            TableUtils.createTableIfNotExists(this.connectionSource, UserWrapper.class);

            this.usersDao = DaoManager.createDao(this.connectionSource, UserWrapper.class);

        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ConnectionSource getConnectionSource() {
        return this.connectionSource;
    }

    public Dao<UserWrapper, UUID> getUsersDao() {
        return this.usersDao;
    }
}