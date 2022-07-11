import com.google.gson.Gson;
import db.config.DbConfig;
import db.dao.EngineerDaoImpl;
import db.dao.SiteDaoImpl;
import models.Engineer;
import models.Site;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;


import static spark.Spark.*;

public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 5050; //return port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFileLocation("/public");
        String connectionString = "jdbc:postgresql://localhost:5432/site_maintenance";
//        String connectionString = DbConfig.getDbUrl();
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root");
        EngineerDaoImpl engineerDao = new EngineerDaoImpl(sql2o);
        SiteDaoImpl siteDao = new SiteDaoImpl(sql2o);
        Gson gson = new Gson();

        get("/all-sites", "application/json", (req, res) ->{
            List<Site> sites = siteDao.getAll();
            res.status(200);
            res.type("application/json");
            return gson.toJson(sites, Site.class);
        });

        // get: show all sites and all engineers
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Engineer> engineers = engineerDao.getAll();
            List<Site> sites = siteDao.getAll();
            model.put("engineers", engineers);
            model.put("sites", sites);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        // get: show new engineer form
        get("/engineers/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Engineer> engineers = engineerDao.getAll(); //refresh list of links for navbar
            model.put("engineers", engineers);
            return new ModelAndView(model, "engineer-form.hbs"); //new
        }, new HandlebarsTemplateEngine());

        // get: show new site form
        get("/sites/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Engineer> allEngineers = engineerDao.getAll();
            model.put("engineers", allEngineers);
            return new ModelAndView(model, "site-form.hbs");
        }, new HandlebarsTemplateEngine());

        // get: delete all sites
        get("/sites/delete/all", (req, res) -> {
            siteDao.clearAllSites(); //change
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        // get: delete single site
        get("/sites/:id/delete", (req, res) -> {
            int idOfSiteToDelete = Integer.parseInt(req.params("id"));
            siteDao.deleteById(idOfSiteToDelete);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        // post: process new site form
        post("/sites", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String errMsg = "Site already exists";
            String successMsg = "Site created successfully";
            List<Engineer> allEngineers = engineerDao.getAll();
            List<Site> allSites = siteDao.getAll();
            model.put("engineers", allEngineers);
            String name = req.queryParams("name");
            String description = req.queryParams("description");
            int engineerId = Integer.parseInt(req.queryParams("engineer_id"));
            Site newSite = new Site(name, description, engineerId);
            for(Site site : allSites){
                if(site.getName().equals(name) ){
                    model.put("errors", errMsg);
                    model.put("engineers", engineerDao.getAll());
                    return new ModelAndView(model, "site-form.hbs");
                }
            }
            siteDao.add(newSite);
            model.put("success", successMsg);
            model.put("engineers", engineerDao.getAll());
            return new ModelAndView(model, "site-form.hbs");
        }, new HandlebarsTemplateEngine());

        // get: show a single site
        get("/sites/:id/:engineer_id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfSiteToFind = Integer.parseInt(req.params("id"));
            int idOfSiteEngineer = Integer.parseInt(req.params("engineer_id"));
            Site foundSite = siteDao.findById(idOfSiteToFind);
            Engineer siteEngineer = engineerDao.findById(idOfSiteEngineer);
            model.put("site", foundSite);
            model.put("engineer", siteEngineer);
            model.put("engineers", engineerDao.getAll());
            return new ModelAndView(model, "site-details.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show form to update site details
        get("/sites/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Engineer> allEngineers = engineerDao.getAll();
            model.put("engineers", allEngineers);
            Site site = siteDao.findById(Integer.parseInt(req.params("id")));
            model.put("site", site);
            model.put("editSite", true);
            return new ModelAndView(model, "site-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process form to update site
        post("/sites/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int siteToEditId = Integer.parseInt(req.params("id"));
            String name = req.queryParams("name");
            String description = req.queryParams("description");
            int engineerId = Integer.parseInt(req.queryParams("engineer_id"));
            siteDao.update(siteToEditId, name, description, engineerId );
            model.put("updated", "Site updated successfully");
            return new ModelAndView(model, "site-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process new employee form
        post("/engineers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String errMsg = "Engineer already exists";
            String successMsg = "Engineer created successfully";
            List<Engineer> allEngineers = engineerDao.getAll();
            String firstName = req.queryParams("first_name");
            String lastName = req.queryParams("last_name");
            String engId = req.queryParams("eng_no");
            String phoneNo = req.queryParams("phone_no");
            String email = req.queryParams("email");
            Engineer newEngineer = new Engineer(firstName, lastName, engId, phoneNo, email);
            for(Engineer engineer : allEngineers){
                if(engineer.getEng_no().equals(engId) || engineer.getPhone_no().equals(phoneNo)|| engineer.getEmail().equals(email)){
                    model.put("errors", errMsg);
                    model.put("engineers", allEngineers);
                    return new ModelAndView(model, "employee-form.hbs");
                }
            }
            engineerDao.add(newEngineer);
            model.put("success", successMsg);
            model.put("engineers", allEngineers);
            return new ModelAndView(model, "engineer-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show an individual engineer and sites allocated
        get("/engineers/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfEngineerToFind = Integer.parseInt(req.params("id")); //new
            Engineer foundEngineer = engineerDao.findById(idOfEngineerToFind);
            model.put("department", foundEngineer);
            List<Site> allSitesByEngineer = engineerDao.getAllSitesByEngineer(idOfEngineerToFind);
            model.put("sites", allSitesByEngineer);
            model.put("engineers", engineerDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "engineer-details.hbs"); //new
        }, new HandlebarsTemplateEngine());

        //get: show form to update engineer
        get("/engineers/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("editEngineer", true);
            Engineer engineer = engineerDao.findById(Integer.parseInt(req.params("id")));
            model.put("engineer", engineer);
            model.put("engineers", engineerDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "engineer-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process form to update engineer
        post("/engineers/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int engineerToEditId = Integer.parseInt(req.params("id"));
            String firstName = req.queryParams("first_name");
            String lastName = req.queryParams("last_name");
            String engId = req.queryParams("eng_no");
            String phoneNo = req.queryParams("phone_no");
            String email = req.queryParams("email");
            engineerDao.update(engineerToEditId, firstName, lastName, engId, phoneNo, email);
            model.put("updated", "Engineer updated successfully");
            return new ModelAndView(model, "engineer-form.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all departments, positions and all employees
        get("/engineers/:id/delete", (req, res) -> {
            int engineerId = Integer.parseInt(req.params("id"));
            engineerDao.deleteById(engineerId);
//            engineerDao.deleteAllSitesByEngineer(engineerId);
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        get("/delete/engineers/all", (req, res) -> {
            engineerDao.clearAllEngineers();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //post: search for engineers
        post("/search/engineers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String engineer = req.queryParams("engineer");
            List<Engineer> searchedEngineers = engineerDao.search(engineer);
            model.put("searched", searchedEngineers);
            return new ModelAndView(model, "search-results.hbs");
        }, new HandlebarsTemplateEngine());

        //post: search for sites
        post("/search/sites", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String site = req.queryParams("site");
            List<Site> searchedSites = siteDao.search(site);
            model.put("searched", searchedSites);
            return new ModelAndView(model, "search-results.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
