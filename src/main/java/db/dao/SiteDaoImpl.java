package db.dao;

import models.Site;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class SiteDaoImpl implements SiteDao {

    private final Sql2o sql2o;

    public SiteDaoImpl(Sql2o sql2o){
        this.sql2o = sql2o;
    }


    @Override
    public void add(Site site) {
        String sql = "INSERT INTO sites (name, description, engineer_id, created) VALUES (:name, :description, :engineerId, now())"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            int id = (int) con.createQuery(sql, true) //make a new variable
                    .bind(site) //map argument onto the query, so we can use information from it
                    .executeUpdate() //run it all
                    .getKey(); //int id is now row number (row key) //of db
            site.setId(id); //update object to set id now from database
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public List<Site> getAll() {
        return null;
    }


    @Override
    public Site findById(int id) {
        return null;
    }

    @Override
    public void update(int id, String name, String description, int engineerId, String updated) {

    }

    @Override
    public void deleteById(int id, String deleted) {

    }

    @Override
    public void clearAllSites(String deleted) {

    }
}
