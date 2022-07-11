package db.dao;

import models.Engineer;
import models.Site;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.sql.Timestamp;
import java.util.List;

public class EngineerDaoImpl implements EngineerDao{
    private final Sql2o sql2o;

    public EngineerDaoImpl(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public void add(Engineer engineer) {
        String sql = "INSERT INTO engineers (first_name, last_name, eng_no, phone_no, email, created) VALUES (:first_name, :last_name, :eng_no, :phone_no, :email, now())"; //raw sql
        try(Connection con = sql2o.open()){ //try to open a connection
            int id = (int) con.createQuery(sql, true) //make a new variable
                    .bind(engineer) //map argument onto the query, so we can use information from it
                    .executeUpdate() //run it all
                    .getKey(); //int id is now row number (row key) //of db
            engineer.setId(id); //update object to set id now from database
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Engineer> getAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM engineers where deleted = 'FALSE'") //raw sql
//                    .throwOnMappingFailure(false)
                    .executeAndFetch(Engineer.class); //fetch a list
        }
    }

    @Override
    public Engineer findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM engineers WHERE id = :id and deleted = 'FALSE'")
                    .addParameter("id", id) //key/value pair, key must match above
                    .executeAndFetchFirst(Engineer.class); //fetch an individual item
        }
    }

    @Override
    public void update(int id, String firstName, String lastName, String engNo, String phoneNo, String email){
        String sql = "UPDATE engineers SET (first_name, last_name, eng_no, phone_no, email, updated) = (:firstName, :lastName, :engNo, :phoneNo, :email, now()) WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("firstName", firstName)
                    .addParameter("lastName", lastName)
                    .addParameter("lastName", lastName)
                    .addParameter("engNo", engNo)
                    .addParameter("phoneNo", phoneNo)
                    .addParameter("email", email)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "UPDATE engineers SET deleted='TRUE' WHERE id=:id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllEngineers() {
        String sql = "UPDATE engineers SET deleted='TRUE'";
//        String sql = "DELETE FROM engineers";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }


    @Override
    public List<Site> getAllSitesByEngineer(int engineerId) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM sites WHERE engineer_id = :engineerId and deleted = 'FALSE'")
                    .addParameter("engineerId", engineerId)
                    .executeAndFetch(Site.class);
        }
    }

    @Override
    public void deleteAllSitesByEngineer(int engineerId) {
        String sql = "UPDATE sites SET deleted='TRUE' where engineer_id = :engineerId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("engineerId", engineerId)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
    @Override
    public List<Engineer> search(String engineer) {
        String sql = "SELECT * FROM engineers WHERE (lower(first_name) like '%'||:engineer||'%' OR lower(last_name) like '%'||:engineer||'%')";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("engineer", engineer)
                    .executeAndFetch(Engineer.class);
        }
    }
}
