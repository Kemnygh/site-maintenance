package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SiteTest {

    @Test
    public void NewSitesObjectGetsCorrectlyCreated() throws Exception {
        Site site = setNewSite();
        assertNotNull(site);
    }
    @Test
    void SiteInstantiatesWithName() {
        Site site = setNewSite();
        assertEquals("Kwanza Site", site.getName());
    }

    @Test
    void SiteInstantiatesWithDescription() {
        Site site = setNewSite();
        assertEquals("First site init", site.getDescription());
    }

    @Test
    void SiteInstantiatesWithEngineerId() {
        Site site = setNewSite();
        assertEquals(1, site.getEngineerId());
    }

    //Support Method
    public Site setNewSite() { return new Site("Kwanza Site", "First site init", 1);}
}