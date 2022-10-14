package com.lev_prav.server.collectionmanagers;

import com.lev_prav.common.data.Country;
import com.lev_prav.common.data.Person;

import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


public class CollectionManager {
    private ArrayDeque<Person> persons;
    private ZonedDateTime timeCreate;
    private SQLDataManager sqlDataManager;
    private final Lock lock = new ReentrantLock();


    public CollectionManager(ArrayDeque<Person> persons, SQLDataManager sqlDataManager) {
        this.persons = persons;
        this.sqlDataManager = sqlDataManager;
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
        try {
            lock.lock();
            if (isPassportIdExists(person.getPassportID())) {
                return false;
            }
            person.setCreationDate(ZonedDateTime.now());
            Integer id = sqlDataManager.add(person);
            if (id != null) {
                person.setId(id);
                persons.add(person);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Boolean updatePerson(int id, Person newInstance) {
        try {
            lock.lock();
            Person oldInstance = getById(id);
            if (!oldInstance.getOwnerUserName().equals(newInstance.getOwnerUserName())) {
                return false;
            }

            if (persons.stream().filter(p -> p.getPassportID().equals(newInstance.getPassportID())).anyMatch(p -> p.getId() != id)) {
                return false;
            }
            if (!sqlDataManager.update(id, newInstance)) {
                return false;
            }
            oldInstance.setName(newInstance.getName());
            oldInstance.setCoordinates(newInstance.getCoordinates());
            oldInstance.setBirthday(newInstance.getBirthday());
            oldInstance.setHeight(newInstance.getHeight());
            oldInstance.setLocation(newInstance.getLocation());
            oldInstance.setNationality(newInstance.getNationality());
            oldInstance.setPassportID(newInstance.getPassportID());
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeById(int id) {
        try {
            lock.lock();
            if (!sqlDataManager.removeById(id)) {
                return false;
            }
            persons.remove(id);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Integer removeLower(Person pr, String username) {
        try {
            lock.lock();
            AtomicInteger undeletedItems = new AtomicInteger();
            List<Integer> ids = persons.stream().filter(e -> e.compareTo(pr) < 0
                    && e.getOwnerUserName().equals(username)).map(Person::getId).collect(Collectors.toList());
            ids.forEach(k -> {
                if (sqlDataManager.removeById(k)) {
                    persons.remove(k);
                } else {
                    undeletedItems.getAndIncrement();
                }
            });
            return undeletedItems.get();
        } finally {
            lock.unlock();
        }
    }

    public boolean clear(String username) {
        try {
            lock.lock();
            if (!sqlDataManager.deleteAllOwned(username)) {
                return false;
            }
            persons.removeIf(e -> e.getOwnerUserName().equals(username));
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean isId(int id) {
        try {
            lock.lock();
            for (Person i : persons) {
                if (i.getId() == id) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private boolean isPassportIdExists(String passportId) {
        try {
            lock.lock();
            return persons.stream().anyMatch(e -> e.getPassportID().equals(passportId));
        } finally {
            lock.unlock();
        }
    }

    public double averageOfHeight() {
        try {
            lock.lock();
            return persons.stream().mapToDouble(Person::getHeight).average().orElse(0);
        } finally {
            lock.unlock();
        }
    }

    public Map<ZonedDateTime, Long> groupCountingByCreationDate() {
        try {
            lock.lock();
            return persons.stream().collect(Collectors.groupingBy(Person::getCreationDate, Collectors.counting()));
        } finally {
            lock.unlock();
        }
    }

    public List<Person> filerLessThanNationality(Country country) {
        try {
            lock.lock();
            return persons.stream().filter(e -> e != null && e.getNationality().compareTo(country) < 0).collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    public boolean containsId(int id) {
        try {
            lock.lock();
            return persons.stream().anyMatch(v -> v.getId() == id);
        } finally {
            lock.unlock();
        }
    }

    public Person getById(int id) {
        try {
            lock.lock();
            return persons.stream().filter(v -> v.getId() == id).findFirst().get();
        } finally {
            lock.unlock();
        }
    }
}
