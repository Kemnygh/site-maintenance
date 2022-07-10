//package db.dao;
//
//import db.config.DbConfig;
//import org.junit.rules.ExternalResource;
//import org.sql2o.*;
//
//import static db.config.DbConfig.*;
//
//public class DatabaseRule extends ExternalResource {
//    private static EngineerDaoImpl engineerDao;
//    private static Connection conn;
//
////    @Override
////    protected void before() {
////        DbConfig.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/site_maintenance_test", "postgres", "root");
////        engineerDao = new EngineerDaoImpl(DbConfig.sql2o);
////        conn = sql2o.open(); // open connection once before this test file is run
////    }
//
//    @Override
//    protected void after() {
//        System.out.println("deleting db");
//        try(Connection con = DbConfig.sql2o.open()) {
//            String deleteEngineersQuery = "DELETE FROM engineers;";
//            String deleteSitesQuery = "DELETE FROM sites;";
//            con.createQuery(deleteEngineersQuery).executeUpdate();
//            con.createQuery(deleteSitesQuery).executeUpdate();
//        }
//    }
//
//}