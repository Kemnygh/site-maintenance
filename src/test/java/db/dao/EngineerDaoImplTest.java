package db.dao;

import db.config.DbConfig;
import models.Engineer;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.sql.Timestamp;
import java.util.Date;


import static db.config.DbConfig.getTestUrl;
import static org.junit.jupiter.api.Assertions.*;

class EngineerDaoImplTest {
    private static EngineerDaoImpl engineerDao;
    private static Connection conn;
//    @Rule
//    public DatabaseRule database = new DatabaseRule();

    @BeforeAll
    public static void setUp() throws Exception { //changed to static
        String connectionString = "jdbc:postgresql://localhost:5432/site_maintenance_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root");
        engineerDao = new EngineerDaoImpl(sql2o);
        conn = sql2o.open(); // open connection once before this test file is run
    }
    @AfterEach
    public void tearDown() throws Exception { //I have changed
        System.out.println("clearing database");
        engineerDao.clearAllEngineers(); // clear all tasks after every test

    }

    @AfterAll // changed to @AfterClass (run once after all tests in this file completed)
    public static void shutDown() throws Exception { //changed to static and shutDown
        conn.close(); // close connection once after this entire test file is finished
        System.out.println("connection closed");
    }


    @Test
    public void addingEngineerSetsId() throws Exception{
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        int engineerId = engineer.getId();
        assertNotEquals(engineerId, engineer.getId());
    }

    @Test
    public void engineerCreatedDateSetsCorrectly() throws Exception{
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        Timestamp rightNow = new Timestamp(new Date().getTime());
        Timestamp engineerCreateDate = engineerDao.findById(engineer.getId()).getCreated();
        assertNotEquals(engineerCreateDate, rightNow);
    }

    @Test
    public void engineerUpdatedDateSetsCorrectly() throws Exception{
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        Timestamp rightNow = new Timestamp(new Date().getTime());
        Timestamp engineerCreateDate = engineerDao.findById(engineer.getId()).getCreated();
        assertNotEquals(engineerCreateDate, rightNow);
    }




    @Test
    void getAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void clearAllEngineers() {
    }

    @Test
    void deleteAllSitesByEngineer() {
    }

    @Test
    void getAllSitesByEngineer() {
    }

    public Engineer setNewEngineer() { return new Engineer("John", "Doe", "ENG001", "0712345678", "jdoe@engineering.com");}
}