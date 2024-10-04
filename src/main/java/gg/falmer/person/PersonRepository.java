package gg.falmer.person;

import gg.falmer.person.data.Contact;
import gg.falmer.person.data.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PersonRepository {
    private static final String ADD_PERSONS_QUERY = "INSERT INTO tbl_persons (id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String ADD_CONTACTS_QUERY = "INSERT INTO tbl_contacts (id, description, value, person_id) VALUES (?, ?, ?, ?);";
    private static final String FIND_PERSONS_QUERY = "SELECT * FROM tbl_persons WHERE first_name = ? OR last_name = ?";
    private static final String FIND_CONTACTS_QUERY = "SELECT * FROM tbl_contacts WHERE person_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addPersons(List<Person> persons) {
        for (Person person : persons) {
            jdbcTemplate.update(ADD_PERSONS_QUERY, person.getId(), person.getFirstName(), person.getLastName());
            addContacts(person.getContacts(), person.getId());
        }
    }

    private void addContacts(List<Contact> contacts, int personId) {
        for (Contact contact : contacts) {
            jdbcTemplate.update(ADD_CONTACTS_QUERY, contact.getId(), contact.getDescription(), contact.getValue(), personId);
        }
    }

    public List<Person> findPersonsByText(String text) {
        return jdbcTemplate.query(FIND_PERSONS_QUERY, new Object[]{text, text}, this::mapRowToPerson);
    }

    private Person mapRowToPerson(ResultSet rs, int rowNum) throws SQLException {
        int personId = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");

        List<Contact> contacts = findContactsByPersonId(personId);

        Person person = new Person();
        person.setId(personId);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setContacts(contacts);

        return person;
    }

    private List<Contact> findContactsByPersonId(int personId) {
        return jdbcTemplate.query(FIND_CONTACTS_QUERY, new Object[]{personId}, this::mapRowToContact);
    }

    private Contact mapRowToContact(ResultSet rs, int rowNum) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getInt("id"));
        contact.setDescription(rs.getString("description"));
        contact.setValue(rs.getString("value"));
        return contact;
    }
}
