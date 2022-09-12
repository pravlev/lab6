package com.lev_prav.client.utility;

import com.lev_prav.common.data.Coordinates;
import com.lev_prav.common.data.Country;
import com.lev_prav.common.data.Location;
import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.ScriptException;

import java.time.LocalDateTime;

public class PersonFiller {
    private final PersonReader reader;
    private final UserIO userIO;

    public PersonFiller(PersonReader reader, UserIO userIO) {
        this.reader = reader;
        this.userIO = userIO;
    }

    public String fillName() throws ScriptException {
        SimplePersonFiller<String> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter name", reader::readNotEmptyString);
    }

    public Long fillX() throws ScriptException {
        SimplePersonFiller<Long> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter x coordinate", reader::readCoordinateX);
    }

    public Float fillY() throws ScriptException {
        SimplePersonFiller<Float> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter y coordinate", reader::readCoordinateY);
    }

    public Coordinates fillCoordinates() throws ScriptException {
        return new Coordinates(fillX(), fillY());
    }

    public double fillHeight() throws ScriptException {
        SimplePersonFiller<Double> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter height", reader::readHeight);
    }

    public LocalDateTime fillBirthday() throws ScriptException {
        SimplePersonFiller<LocalDateTime> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter birthday", reader::readBirthday);
    }

    public String fillPasswordId() throws ScriptException {
        SimplePersonFiller<String> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter passwordId", reader::readPassportId);
    }

    public Country fillNationality() throws ScriptException {
        SimplePersonFiller<Country> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter nationality(UNITED_KINGDOM,GERMANY,SPAIN,INDIA,THAILAND)", reader::readCountry);
    }

    public Double fillLocationX() throws ScriptException {
        SimplePersonFiller<Double> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter x coordinates location", reader::readLocationX);
    }

    public Integer fillLocationY() throws ScriptException {
        SimplePersonFiller<Integer> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter y coordinates location", reader::readLocationY);
    }

    public Float fillLocationZ() throws ScriptException {
        SimplePersonFiller<Float> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter z coordinates location", reader::readLocationZ);
    }

    public String fillLocationName() throws ScriptException {
        SimplePersonFiller<String> filler = new SimplePersonFiller<>(userIO);
        return filler.fill("Enter name location", reader::readNotNullString);
    }

    public Location fillLocation() throws ScriptException {
        return new Location(fillLocationX(), fillLocationY(), fillLocationZ(), fillLocationName());
    }

    public Person fillPerson() throws ScriptException {
        return new Person(fillName(), fillCoordinates(), fillHeight(), fillBirthday(),
                fillPasswordId(), fillNationality(), fillLocation());
    }

}
