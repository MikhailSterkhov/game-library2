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
                .mysqlPassword("")
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
        playerDataConnection.createTable(true, "PlayerIds", "`Name` VARCHAR(16) NOT NULL, `Id` INT NOT NULL");


        // запросы

        //1: Выполнение запроса с добавлением или обновлением данных в таблице
        playerDataConnection.execute(true,
                "INSERT INTO `PlayerIds` (`Name`, `Id`) VALUES (?, ?)",
                "ItzStonlex", 69);


        //2: Выполнение запроса с получением ResultSet
        int playerId = playerDataConnection.executeQuery(true, "SELECT * FROM `PlayerIds` WHERE `Name`=?",
                resultSet -> resultSet.next() ? resultSet.getInt("Id") : -1, "ItzStonlex");
    }
}
