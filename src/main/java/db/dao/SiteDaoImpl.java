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
        String sql = "SELECT * FROM sites WHERE deleted = 'FALSE'";
        try(Connection con = sql2o.open()){
           return con.createQuery(sql)
                    .executeAndFetch(Site.class);
        }
    }


    @Override
    public Site findById(int id) {
        String sql = "SELECT * FROM sites WHERE id = :id and deleted = 'FALSE'";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Site.class);
        }

    }

    @Override
    public void update(int id, String name, String description, int engineerId) {
        String sql = "UPDATE sites SET (name, description, engineer_id, updated) = (:name, :description, :engineerId, now()) WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("description", description)
                    .addParameter("engineerId", engineerId)
                    .executeUpdate();
        }catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "UPDATE sites SET deleted='TRUE' where id = :id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllSites(String deleted) {
        String sql = "UPDATE sites SET deleted='TRUE'";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .executeUpdate();
        }catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public List<Site> search(String site) {
        String sql = "SELECT * FROM sites WHERE lower(name) like '%'||:site||'%'";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("site", site)
                    .executeAndFetch(Site.class);
        }
    }
}
