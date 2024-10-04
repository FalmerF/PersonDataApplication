package gg.falmer.person.data;

import lombok.Data;

import java.util.List;

@Data
public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private List<Contact> contacts;
}
