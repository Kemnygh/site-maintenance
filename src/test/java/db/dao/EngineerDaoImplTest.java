package db.dao;

import models.Engineer;
import models.Site;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class EngineerDaoImplTest {
    private static EngineerDaoImpl engineerDao;
    private static SiteDaoImpl siteDao;
    private static Connection conn;
//    @Rule
//    public DatabaseRule database = new DatabaseRule();

    @BeforeAll
    public static void setUp() throws Exception { //changed to static
        String connectionString = "jdbc:postgresql://localhost:5432/site_maintenance_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root");
        engineerDao = new EngineerDaoImpl(sql2o);
        siteDao = new SiteDaoImpl(sql2o);
        conn = sql2o.open(); // open connection once before this test file is run
    }

    @AfterEach
    public void tearDown() throws Exception { //I have changed
        System.out.println("clearing database");
//        engineerDao.clearAllEngineers(); // clear all tasks after every test
        clearEngineersDB();
        clearSitesDB();
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
        Engineer foundEngineer = engineerDao.findById(engineerId);
        System.out.println(foundEngineer);
        assertEquals(foundEngineer.getId(), engineer.getId());
    }

    @Test
    public void engineerCreatedDateSetsCorrectly() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        Timestamp rightNow = new Timestamp(new Date().getTime());
        Timestamp engineerCreateDate = engineerDao.findById(engineer.getId()).getCreated();
        assertEquals(dateFormat.format(engineerCreateDate), dateFormat.format(rightNow));
    }

    @Test
    public void engineerUpdatesSuccessfully() throws Exception{
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        engineerDao.update(engineer.getId(), "John", "Doe", "ENGO02", "0712345678", "john.doe@company.com");
        Engineer updatedEngineer = engineerDao.findById(engineer.getId());
        assertNotEquals(updatedEngineer, engineer);
    }

    @Test
    public void engineerUpdatedDateSetsCorrectly() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        engineerDao.update(engineer.getId(), "John", "Doe", "ENGO02", "0712345678", "john.doe@company.com");
        Timestamp rightNow = new Timestamp(new Date().getTime());
        Timestamp engineerUpdateDate = engineerDao.findById(engineer.getId()).getUpdated();
        assertEquals(dateFormat.format(engineerUpdateDate), dateFormat.format(rightNow));
    }

    @Test
    public void existingEngineerCanBeFoundById() throws Exception {
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        Engineer engineerFound = engineerDao.findById(engineer.getId());
        assertEquals(engineer.getId(), engineerFound.getId());
    }

    @Test
    void weCanGetAllEngineers() throws Exception{
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        Engineer anotherEngineer = new Engineer("Jane", "Doe", "ENG00", "0712345678", "jane.doe@engineering.com");
        engineerDao.add(anotherEngineer);
        List<Engineer> allEngineers = engineerDao.getAll();
        assertEquals(allEngineers.size(), 2);
    }

    @Test
    void deleteByIdDeletesCorrectEngineer() throws Exception {
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        engineerDao.deleteById(engineer.getId());
        assertEquals(0, engineerDao.getAll().size());
    }

    @Test
    void clearAllEngineersSetPartialDelete() throws Exception{
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        Engineer anotherEngineer = new Engineer("Jane", "Doe", "ENG00", "0712345678", "jane.doe@engineering.com");
        engineerDao.add(anotherEngineer);
        engineerDao.clearAllEngineers();
        List<Engineer> allEngineers = engineerDao.getAll();
        assertEquals(0,allEngineers.size());
    }

    @Test
    void getAllSitesByEngineerReturnsAllSitesCorrectly() throws Exception {
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        int engineerId = engineer.getId();
        Site site = new Site("Kwanza Site", "first site init", engineerId);
        Site anotherSite = new Site("Pile Site", "second site init", engineerId);
        siteDao.add(site);
        siteDao.add(anotherSite);
        List<Site> allSitesByEngineer = engineerDao.getAllSitesByEngineer(engineerId);
        assertEquals(2, allSitesByEngineer.size());
    }

    @Test
    void deleteAllSitesByEngineer() throws Exception{
        Engineer engineer = setNewEngineer();
        engineerDao.add(engineer);
        int engineerId = engineer.getId();
        Site site = new Site("Kwanza Site", "first site init", engineerId);
        Site anotherSite = new Site("Pile Site", "second site init", engineerId);
        siteDao.add(site);
        siteDao.add(anotherSite);
        engineerDao.deleteAllSitesByEngineer(engineerId);
        List<Site> allSitesByEngineer = engineerDao.getAllSitesByEngineer(engineerId);
        assertEquals(0, allSitesByEngineer.size());
    }


    // Support methods
    public Engineer setNewEngineer() { return new Engineer("John", "Doe", "ENG001", "0712345678", "jdoe@engineering.com");}

    public void clearEngineersDB() {
        String connectionString = "jdbc:postgresql://localhost:5432/site_maintenance_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root");
        String sql = "DELETE FROM engineers";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
    public void clearSitesDB() {
        String connectionString = "jdbc:postgresql://localhost:5432/site_maintenance_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root");
        String sql = "DELETE FROM sites";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
}