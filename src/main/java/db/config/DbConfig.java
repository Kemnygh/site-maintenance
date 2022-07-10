package db.config;

import org.sql2o.Sql2o;

public class DbConfig {
    private static final String DB_NAME = "site_maintenance";
    private static final String TEST_DB = "site_maintenance_test";
    private static final String DB_HOST = "localhost";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "root";
    private static final int DB_PORT = 5432;
    private static final String DB_URL = "jdbc:postgresql://"+DB_HOST+":"+DB_PORT+"/"+DB_NAME;
    private static final String TEST_URL = "jdbc:postgresql://localhost:5432/site_maintenance_test";

    public static String getDbUrl(){
        return DB_URL;
    }
    public static String getTestUrl(){
        return TEST_URL;
    }
    public static String getDbUser(){
        return DB_USER;
    }
    public static String getDbPass(){
        return DB_PASS;
    }
    public static Sql2o sql2o = new Sql2o(getDbUrl());
}
