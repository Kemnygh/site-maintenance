import com.google.gson.Gson;
import db.config.DbConfig;
import db.dao.EngineerDaoImpl;
import db.dao.SiteDaoImpl;
import models.Engineer;
import models.Site;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.sql.Timestamp;
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
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "root");
        EngineerDaoImpl engineerDao = new EngineerDaoImpl(sql2o);
        SiteDaoImpl siteDao = new SiteDaoImpl(sql2o);


        // get: show all engineers
        get("/all-engineers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Engineer> engineers = engineerDao.getAll();
            List<Site> sites = siteDao.getAll();
            model.put("engineers", engineers);
            return new ModelAndView(model, "all-engineers.hbs");
        }, new HandlebarsTemplateEngine());

        // get: show all sites
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Site> sites = siteDao.getAll();
            model.put("sites", sites);
            ArrayList<String> dates = new ArrayList<String>();
            String test = "";
                for(Site site : sites){assert false;
                    test = site.getCreated().toString().substring(0,16);

                    model.put("siteCreated", test);
                    return new ModelAndView(model, "index.hbs");
                }


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
            String location = req.queryParams("location");
            String locationId = req.queryParams("location_id");
            Site newSite = new Site(name, description, engineerId, location, locationId);
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



        // get: show a single site details
        get("/sites/:id/:engineer_id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfSiteToFind = Integer.parseInt(req.params("id"));
            Site foundSite = siteDao.findById(idOfSiteToFind);
            int idOfSiteEngineer = foundSite.getEngineerId();
            Engineer siteEngineer = engineerDao.findById(idOfSiteEngineer);
            model.put("site", foundSite);
            model.put("engineer", siteEngineer);
            model.put("engineers", engineerDao.getAll());
            return new ModelAndView(model, "site-details.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show form to update site details
        get("/site/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("editSite", true);
            Site site = siteDao.findById(Integer.parseInt(req.params("id")));
            model.put("site", site);
            model.put("engineers", engineerDao.getAll()); //refresh list of links for navbar
            return new ModelAndView(model, "site-form.hbs");
        }, new HandlebarsTemplateEngine());



        //post: process form to update site
        post("/sites/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int siteToEditId = Integer.parseInt(req.params("id"));
            String name = req.queryParams("name");
            String description = req.queryParams("description");
            int engineerId = Integer.parseInt(req.queryParams("engineer_id"));
            String location = req.queryParams("location");
            String locationId = req.queryParams("location_id");
            siteDao.update(siteToEditId, name, description, engineerId,location, locationId);
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
            model.put("engineer", foundEngineer);
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

        get("/engineers/delete/all", (req, res) -> {
            engineerDao.clearAllEngineers();
            res.redirect("/");
            return null;
        }, new HandlebarsTemplateEngine());

        //get: search for engineers and sites
        get("/search/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("editEngineer", true);
            String searchString = req.queryParams("search");
            System.out.println(searchString);
            if(searchString.isBlank()){
                model.put("engineers", null);
                model.put("sites", null);
                model.put("no_engineers", null);
                model.put("no_sites", null);
                model.put("message", "The search was blank please enter one or more letters to search!");
            }else {
                List<Engineer> engineers = engineerDao.search(searchString);
                List<Site> sites = siteDao.search(searchString);
                int numberOfEngineers = engineers.size();
                int numberOfSites = sites.size();
                model.put("engineers", engineers);
                model.put("sites", sites);
                model.put("no_engineers", numberOfEngineers);
                model.put("no_sites", numberOfSites);
            }
            return new ModelAndView(model, "search-details.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
