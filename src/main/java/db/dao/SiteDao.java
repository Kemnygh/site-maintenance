package db.dao;

import models.Engineer;
import models.Site;

import java.util.List;

public interface SiteDao {
    // LIST
    List<Site> getAll();

    // CREATE
    void add(Site site);


    // READ
    Site findById(int id);

    // UPDATE
    void update(int id,String name, String description, int engineerId, String location, String locationId);

    // DELETE
    void deleteById(int id);
    void clearAllSites();

    List<Site> search(String site);
}
