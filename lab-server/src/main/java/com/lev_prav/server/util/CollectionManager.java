package com.lev_prav.server.util;

import com.lev_prav.common.data.Country;
import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.CSVException;

import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CollectionManager {
    private ArrayDeque<Person> persons;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private ZonedDateTime timeCreate;
    private String filePath;

    public CollectionManager(ArrayDeque<Person> persons, ZonedDateTime timeCreate, String filePath) {
        this.timeCreate = timeCreate;
        this.persons = persons;
        this.filePath = filePath;
    }

    public int getSize() {
        return persons.size();
    }

    public ArrayDeque<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayDeque<Person> persons) {
        this.persons = persons;
    }

    public ZonedDateTime getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(ZonedDateTime timeCreate) {
        this.timeCreate = timeCreate;
    }

    public boolean addNewPerson(Person person) {
        if (isPassportIdExists(person.getPassportID())) {
            return false;
        }
        person.setId(getMaxId() + 1);
        person.setCreationDate(ZonedDateTime.now());
        persons.add(person);
        return true;
    }

    public Boolean updatePerson(int id, Person person) {
        boolean r = false;

        ArrayList<Person> pr = new ArrayList<>(persons);
        for (int i = 0; i < pr.size(); ++i) {
            if (pr.get(i).getId() == id) {
                if (!person.getPassportID().equals(pr.get(i).getPassportID()) && isPassportIdExists(person.getPassportID())) {
                    return false;
                }
                r = true;
                pr.set(i, person);
                break;
            }
        }
        if(!r)return false;
        person.setId(id);
        persons = new ArrayDeque<>(pr);
        return true;
    }

    public void removeById(int id) {

        ArrayList<Person> pr = new ArrayList<>(persons);
        for (int i = 0; i < pr.size(); ++i) {
            if (pr.get(i).getId() == id) {
                pr.remove(i);
                break;
            }
        }
        persons = new ArrayDeque<>(pr);
    }

    public void removeLower(Person pr) {
        ArrayDeque<Person> prs = new ArrayDeque<>();
        for (Person i : persons) {
            if (i.compareTo(pr) >= 0) {
                prs.add(i);
            }
        }
        persons = prs;
    }

    public void clear() {
        persons.clear();
    }

    public int getMaxId() {
        if (getSize() > 0) {
            return persons.stream().max(Comparator.comparing(Person::getId)).get().getId();
        }
        return 0;
    }

    public boolean isId(int id) {
        for (Person i : persons) {
            if (i.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private boolean isPassportIdExists(String passportId) {
        return persons.stream().anyMatch(e -> e.getPassportID().equals(passportId));
    }

    public double averageOfHeight() {
        return persons.stream().mapToDouble(Person::getHeight).average().orElse(0);
    }

    public Map<ZonedDateTime, Long> groupCountingByCreationDate() {
        return persons.stream().collect(Collectors.groupingBy(Person::getCreationDate, Collectors.counting()));
    }

    public List<Person> filerLessThanNationality(Country country) {
        return persons.stream().filter(e -> e != null && e.getNationality().compareTo(country) < 0).collect(Collectors.toList());
    }

    public void save() throws CSVException {
        ParserCSV.parseCollectionToCSV(filePath, this);
    }
}
