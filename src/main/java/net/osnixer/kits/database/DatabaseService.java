package net.osnixer.kits.database;

import com.google.common.base.Stopwatch;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zaxxer.hikari.HikariDataSource;
import net.osnixer.kits.config.implementation.PluginConfig;
import net.osnixer.kits.database.wrapper.UserWrapper;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DatabaseService {

    private final PluginConfig config;
    private final File dataFolder;
    private final Logger logger;

    private HikariDataSource dataSource;
    private ConnectionSource connectionSource;

    private Dao<UserWrapper, UUID> usersDao;

    public DatabaseService(PluginConfig config, File dataFolder, Logger logger) {
        this.config = config;
        this.dataFolder = dataFolder;
        this.logger = logger;
    }

    public void connect() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();

            DatabaseType databaseType = config.database.type;

            this.dataSource = new HikariDataSource();

            this.dataSource.addDataSourceProperty("cachePrepStmts", true);
            this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
            this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            this.dataSource.addDataSourceProperty("useServerPrepStmts", true);

            this.dataSource.setMaximumPoolSize(this.config.database.poolSize);
            this.dataSource.setUsername(this.config.database.user);
            this.dataSource.setPassword(this.config.database.pass);

            switch (DatabaseType.valueOf(databaseType.toString().toUpperCase())) {
                case MYSQL: {
                    this.dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                    this.dataSource.setJdbcUrl("jdbc:mysql://" + this.config.database.host + ":" + this.config.database.port + "/" + this.config.database.base);

                    break;
                }

                case SQLITE: {
                    this.dataSource.setDriverClassName("org.sqlite.JDBC");
                    this.dataSource.setJdbcUrl("jdbc:sqlite:" + this.dataFolder + "/database.db");

                    break;
                }

                default: {
                    this.logger.warning("Unknown database type: " + databaseType);
                }
            }

            this.connectionSource = new DataSourceConnectionSource(this.dataSource, this.dataSource.getJdbcUrl());

            TableUtils.createTableIfNotExists(this.connectionSource, UserWrapper.class);
            this.usersDao = DaoManager.createDao(this.connectionSource, UserWrapper.class);

            this.logger.info("Loaded database in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        }
        catch (SQLException e) {
            this.logger.warning("Could not connect to database: " + e.getMessage());
        }
    }

    public void close() {
        this.dataSource.close();

        try {
            this.connectionSource.close();
        }
        catch (Exception e) {
            this.logger.warning("Could not close connection: " + e.getMessage());
        }
    }

    public Dao<UserWrapper, UUID> getUsersDao() {
        return this.usersDao;
    }
}