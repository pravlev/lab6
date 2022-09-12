package com.lev_prav.client.utility;

import com.lev_prav.common.data.Country;
import com.lev_prav.common.exceptions.IllegalValueException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class PersonReader {
    private static final long MAXX = 177L;
    private static final float MINY = -635;
    private final UserIO userIO;

    public PersonReader(UserIO userIO) {
        this.userIO = userIO;
    }

    public String readNotNullString() throws IllegalValueException {
        String string;
        string = userIO.readline();
        if (string.isEmpty()) {
            throw new IllegalValueException("This field cannot be null");
        }
        return string;
    }

    public String readNotEmptyString() throws IllegalValueException {
        String string;
        string = readNotNullString();
        if (string.trim().isEmpty()) {
            throw new IllegalValueException("This field cannot be empty");
        }
        return string;
    }

    public Long readCoordinateX() throws IllegalValueException {
        long x;
        try {
            x = Long.parseLong(userIO.readline());
        } catch (NumberFormatException e) {
            throw new IllegalValueException("Value must be a number");
        }
        if (x > MAXX) {
            throw new IllegalValueException("Value must be less than " + MAXX);
        }
        return x;
    }

    public Float readCoordinateY() throws IllegalValueException {
        float y;
        try {
            y = Float.parseFloat(userIO.readline());
        } catch (NumberFormatException e) {
            throw new IllegalValueException("Value must be a number");
        }
        if (y <= MINY) {
            throw new IllegalValueException("Value must be greater than " + MINY);
        }
        return y;
    }

    public double readHeight() throws IllegalValueException {
        double height;
        try {
            height = Double.parseDouble(userIO.readline());
        } catch (NumberFormatException e) {
            throw new IllegalValueException("Value must be a number");
        }
        double minHeight = 0;
        if (height <= minHeight) {
            throw new IllegalValueException("Value must be greater than " + minHeight);
        }
        return height;
    }

    public LocalDateTime readBirthday() throws IllegalValueException {
        LocalDateTime birthday;
        try {
            birthday = LocalDateTime.parse(userIO.readline());
        } catch (DateTimeParseException e) {
            throw new IllegalValueException("The date must be in the format: 2007-12-03T10:15:30");
        }
        return birthday;
    }

    public String readPassportId() throws IllegalValueException {
        String read = userIO.readline();
        if (read.isEmpty()) {
            return null;
        }
        if (read.trim().isEmpty()) {
            throw new IllegalValueException("The field can be either null or not empty");
        }
        return read;
    }

    public Country readCountry() throws IllegalValueException {
        Country country = null;
        String stringCountry = userIO.readline();
        if (!stringCountry.isEmpty()) {
            try {
                country = Country.valueOf(stringCountry.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException("Chose anything from list");
            }
        }
        return country;
    }

    public Double readLocationX() throws IllegalValueException {
        try {
            return Double.parseDouble(userIO.readline());
        } catch (NumberFormatException e) {
            throw new IllegalValueException("type a number please");
        }
    }

    public Integer readLocationY() throws IllegalValueException {
        try {
            return Integer.parseInt(userIO.readline());
        } catch (NumberFormatException e) {
            throw new IllegalValueException("type a number please");
        }
    }

    public Float readLocationZ() throws IllegalValueException {
        try {
            return Float.parseFloat(userIO.readline());
        } catch (NumberFormatException e) {
            throw new IllegalValueException("type a number please");
        }
    }

}
