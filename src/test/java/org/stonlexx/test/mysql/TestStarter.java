package org.stonlexx.test.mysql;

import org.stonlexx.gamelibrary.core.mysql.MysqlConnection;
import org.stonlexx.gamelibrary.core.mysql.MysqlConnectionBuilder;
import org.stonlexx.gamelibrary.core.mysql.MysqlDatabaseConnection;

public class TestStarter {

    public static void main(String[] args) {

        //создание подключения может происходить двумя способами
        MysqlConnection mysqlConnection;

        //1: Factory method
        mysqlConnection
                = MysqlConnection.createMysqlConnection("localhost", "root", "password", 3306)

                .createConnectionToDatabase("player_data")
                .createConnectionToDatabase("permissions")
                .createConnectionToDatabase("economy");

        //2: Call new builder
        mysqlConnection
                = MysqlConnectionBuilder.newMysqlBuilder()

                .mysqlHost("localhost")
                .mysqlUsername("root")
                .mysqlPassword("password")
                .mysqlPort(3306)

                .databaseConnect("player_data")
                .databaseConnect("permissions")
                .databaseConnect("economy")

                .buildConnection();

        //получение обычного подключения к самой базе данных
        MysqlDatabaseConnection databaseStandardConnection = mysqlConnection.getStandardMysqlConnection();

        //есть возможность вручную создавать схемы баз данных,
        // одновременно кешируя и получая соединение к ним
        MysqlDatabaseConnection testDatabaseConnection = mysqlConnection.createDatabaseScheme("TestDatabase", true);


        //получение подключения к схеме базы данных
        MysqlDatabaseConnection playerDataConnection = mysqlConnection.getDatabaseConnection("player_data");
        playerDataConnection.createTable(true, "PlayerIds", "`Name` VARCHAR(16) NOT NULL PRIMARY KEY, `Id` INT NOT NULL");


        // запросы

        //1: Выполнение запроса с добавлением или обновлением данных в таблице
        playerDataConnection.execute(true,
                "INSERT INTO `PlayerIds` (`Name`, `Id`) VALUES (?, ?)",
                "ItzStonlex", 1);


        //2: Выполнение запроса с получением ResultSet

        // при [asyncQuery: true] метод ничего не будет возвращать из-за
        //  того, что задача выполняется в другом потоке
        int playerId = playerDataConnection.executeQuery(false, "SELECT * FROM `PlayerIds` WHERE `Name`=?",
                resultSet -> resultSet.next() ? resultSet.getInt("Id") : -1, "ItzStonlex");
    }
}
