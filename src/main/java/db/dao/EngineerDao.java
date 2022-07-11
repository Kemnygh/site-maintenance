package db.dao;

import models.Engineer;
import models.Site;

import java.sql.Timestamp;
import java.util.List;

public interface EngineerDao {
    // LIST
    List<Engineer> getAll();
    List<Site> getAllSitesByEngineer(int engineerId);

    // CREATE
    void add(Engineer engineer);

    // READ
    Engineer findById(int id);

    // UPDATE
    void update(int id, String firstName, String lastName, String engNo, String phoneNo, String email);

    // DELETE
    void deleteById(int id);
    void clearAllEngineers();
    void deleteAllSitesByEngineer(int engineerId);

    List<Engineer> search(String engineer);
}
