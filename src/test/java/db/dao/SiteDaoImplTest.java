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

class SiteDaoImplTest {
    private static SiteDaoImpl siteDao;
    private static Connection conn;

    @BeforeAll
    public static void setUp() throws Exception { //changed to static
        String connectionString = "jdbc:postgresql://localhost:5432/site_maintenance_test"; // connect to postgres test database
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root");
        siteDao = new SiteDaoImpl(sql2o);
        conn = sql2o.open(); // open connection once before this test file is run
    }

    @AfterEach
    public void tearDown() throws Exception { //I have changed
        System.out.println("clearing database");
        clearSitesDB();
    }

    @AfterAll // changed to @AfterClass (run once after all tests in this file completed)
    public static void shutDown() throws Exception { //changed to static and shutDown
        conn.close(); // close connection once after this entire test file is finished
        System.out.println("connection closed");
    }

    @Test
    void addingSiteSetsId() throws Exception{
        Site site = setNewSite();
        siteDao.add(site);
        int siteId = site.getId();
        Site foundSite = siteDao.findById(siteId);
        assertEquals(foundSite.getId(),siteId);
    }

    @Test
    public void siteCreatedDateSetsCorrectly() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
        Site site = setNewSite();
        siteDao.add(site);
        Timestamp rightNow = new Timestamp(new Date().getTime());
        Timestamp engineerCreateDate = siteDao.findById(site.getId()).getCreated();
        assertEquals(dateFormat.format(engineerCreateDate), dateFormat.format(rightNow));
    }

    @Test
    void weCanGetAllSites() throws Exception{
        Site site = setNewSite();
        siteDao.add(site);
        Site anotherSite = new Site("Pili Site", "Second site init", 2, "Nairobi", "-1.292066, 36.821945");
        siteDao.add(anotherSite);
        List<Site> allSites = siteDao.getAll();
        assertEquals(2,allSites.size());
    }

    @Test
    void existingSiteCanFoundByFindById() throws Exception{
        Site site = setNewSite();
        siteDao.add(site);
        Site siteFound = siteDao.findById(site.getId());
        assertEquals(site.getId(), siteFound.getId());
    }

    @Test
    void updateSiteUpdatesSuccessfully() throws Exception{
        Site site = setNewSite();
        siteDao.add(site);
        int siteId = site.getId();
        siteDao.update(siteId, "Pili Site", "second site init", 1, "Nairobi", "-1.292066, 36.821945");
        Site updatedSite = siteDao.findById(siteId);
        assertNotEquals(updatedSite, site);
    }

    @Test
    void deleteByIdChangesDeletedSuccessfully() throws Exception{
        Site site = setNewSite();
        siteDao.add(site);
        int siteId = site.getId();
        siteDao.deleteById(siteId);
        Site findSite = siteDao.findById(siteId);
        assertNull(findSite);
    }

    @Test
    void clearAllSitesMarksAllSitesAsDeleted() throws Exception{
        Site site = setNewSite();
        siteDao.add(site);
        siteDao.clearAllSites();
        List <Site> foundSites = siteDao.getAll();
        assertEquals(0, foundSites.size());
    }

    @Test
    public void sitesCreatedDateSetsCorrectly() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
        Site site = setNewSite();
        siteDao.add(site);
        Timestamp rightNow = new Timestamp(new Date().getTime());
        Timestamp siteCreateDate = siteDao.findById(site.getId()).getCreated();
        assertEquals(dateFormat.format(siteCreateDate), dateFormat.format(rightNow));
    }

    @Test
    public void sitesUpdatedDateSetsCorrectly() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
        Site site = setNewSite();
        siteDao.add(site);
        int siteId = site.getId();
        siteDao.update(siteId, "Pili Site", "second site init", 1,"Nairobi", "-1.292066, 36.821945");
        Timestamp rightNow = new Timestamp(new Date().getTime());
        Timestamp siteUpdateDate = siteDao.findById(site.getId()).getUpdated();
        assertEquals(dateFormat.format(siteUpdateDate), dateFormat.format(rightNow));
    }

    @Test
    public void siteSearchWorksCorrectly() throws Exception{
        Site site = setNewSite();
        siteDao.add(site);
        Site anotherSite = new Site("Pili Site", "Second site init", 2, "Nairobi", "-1.292066, 36.821945");
        siteDao.add(anotherSite);
        List <Site> foundSites = siteDao.search("site");
        assertEquals(2, foundSites.size());
    }

    // Support methods
    public Site setNewSite() { return new Site("Kwanza Site", "First site init", 1, "Nairobi", "-1.292066, 36.821945");}

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