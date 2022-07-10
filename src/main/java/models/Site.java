package models;

import java.sql.Timestamp;
import java.util.Objects;

public class Site {
    private int id;
    private String name;
    private String description;
    private int engineer_id;
    private Timestamp created;
    private Timestamp updated;
    private String deleted;

    public Site(String name, String description, int engineerId){
        this.name = name;
        this.description = description;
        this.engineer_id = engineerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site)) return false;
        Site site = (Site) o;
        return getId() == site.getId() && engineer_id == site.engineer_id && getName().equals(site.getName()) && getDescription().equals(site.getDescription()) && getCreated().equals(site.getCreated()) && Objects.equals(getUpdated(), site.getUpdated()) && getDeleted().equals(site.getDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), engineer_id, getCreated(), getUpdated(), getDeleted());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getEngineerId() {
        return engineer_id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public String getDeleted(){
        return deleted;
    }
}
