package models;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EngineerTest {

    @Test
    public void NewEngineerObjectGetsCorrectlyCreated() throws Exception {
        Engineer engineer = setNewEngineer();
        assertNotNull(engineer);
    }

    @Test
    void EngineerInstantiatesWithFirstName() throws Exception  {
        Engineer engineer = setNewEngineer();
        assertEquals("John", engineer.getFirst_name());
    }

    @Test
    void EngineerInstantiatesWithLastName() throws Exception  {
        Engineer engineer = setNewEngineer();
        assertEquals("Doe", engineer.getLast_name());
    }

    @Test
    void EngineerInstantiatesWithEngineerID() throws Exception {
        Engineer engineer = setNewEngineer();
        assertEquals("ENG001", engineer.getEng_no());
    }

    @Test
    void EngineerInstantiatesWithPhone() throws Exception {
        Engineer engineer = setNewEngineer();
        assertEquals("0712345678", engineer.getPhone_no());
    }

    @Test
    void EngineerInstantiatesWithEmail() throws Exception {
        Engineer engineer = setNewEngineer();
        assertEquals("jdoe@engineering.com", engineer.getEmail());
    }


    // Support methods
    public Engineer setNewEngineer() { return new Engineer("John", "Doe", "ENG001", "0712345678", "jdoe@engineering.com");}
}