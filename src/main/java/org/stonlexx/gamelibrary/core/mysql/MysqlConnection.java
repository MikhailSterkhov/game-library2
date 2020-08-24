package org.stonlexx.gamelibrary.core.mysql;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MysqlConnection {

    private final String mysqlHost, mysqlUsername, mysqlPassword;

    private final int mysqlPort;


    private final Map<String, MysqlDatabaseConnection> databaseConnectionMap                     = new HashMap<>();
    private final MysqlDataSource mysqlDataSource                                                = new MysqlDataSource();



// ================================================================================================================== //

    /**
     * Создать и инициализировать общее подключение
     * к базе данных mysql
     *
     * @param mysqlHost - имя сервера для подключения
     * @param mysqlUsername - имя пользователя для подключения
     * @param mysqlPassword - пароль пользователя для подключения
     * @param mysqlPort - порт сервера для подключения
     */
    public static MysqlConnection createMysqlConnection(@NonNull String mysqlHost,
                                                        @NonNull String mysqlUsername,
                                                        @NonNull String mysqlPassword,

                                                        int mysqlPort) {

        MysqlConnection mysqlConnection = new MysqlConnection(mysqlHost, mysqlUsername, mysqlPassword, mysqlPort);
        MysqlDataSource mysqlDataSource = mysqlConnection.getMysqlDataSource();

        {
            mysqlDataSource.setServerName(mysqlHost);
            mysqlDataSource.setPort(mysqlPort);
            mysqlDataSource.setUser(mysqlUsername);
            mysqlDataSource.setPassword(mysqlPassword);

            mysqlDataSource.setAutoReconnect(true);
            mysqlDataSource.setEncoding("UTF-8");
        }

        return mysqlConnection;
    }

// ================================================================================================================== //


    /**
     * Создать и кешировать подключение к
     * схеме базы данных
     *
     * @param databaseName - имя схемы из базы данных
     */
    public MysqlConnection createConnectionToDatabase(String databaseName) {
        MysqlDatabaseConnection mysqlDatabaseConnection
                = MysqlDatabaseConnection.createDatabaseConnection(databaseName, this);

        databaseConnectionMap.put(databaseName.toLowerCase(), mysqlDatabaseConnection);

        return this;
    }

    /**
     * Получить кешированное подключение к базе данных
     *
     * @param databaseName - имя схемы из базы данных
     */
    public MysqlDatabaseConnection getDatabaseConnection(String databaseName) {
        return databaseConnectionMap.get(databaseName.toLowerCase());
    }

}
