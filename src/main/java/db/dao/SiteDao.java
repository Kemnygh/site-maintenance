package db.dao;

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
    void update(int id,String name, String description, int engineerId, String updated);

    // DELETE
    void deleteById(int id, String deleted);
    void clearAllSites(String deleted);
}
