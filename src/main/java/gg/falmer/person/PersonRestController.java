package gg.falmer.person;

import gg.falmer.person.data.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonRestController {
    private final PersonRepository personRepository;

    @PostMapping
    public ResponseEntity<Void> addPersons(@RequestBody List<Person> persons) {
        personRepository.addPersons(persons);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{searchString}")
    public ResponseEntity<List<Person>> getPersonsByText(@PathVariable String searchString) {
        List<Person> persons = personRepository.findPersonsByText(searchString);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }
}
