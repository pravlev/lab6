package com.lev_prav.server.util;

import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.CSVException;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public final class ParserCSV {
    private ParserCSV() {
    }

    public static void parseCollectionToCSV(String fileName, CollectionManager collectionManager) throws
            CSVException {
        try (Writer writer = new FileWriter(fileName)) {
            writer.write("timeCreate," + collectionManager.getTimeCreate() + '\n');
            HeaderColumnNameMappingStrategy<Person> strategy = new HeaderColumnNameMappingStrategyBuilder<Person>().build();
            strategy.setType(Person.class);
            StatefulBeanToCsv<Person> beanToCsv = new StatefulBeanToCsvBuilder<Person>(writer).withMappingStrategy(strategy).build();
            beanToCsv.write(new ArrayList<>(collectionManager.getPersons()));
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            new CSVException("Couldn't save the data. Check if the file is available.");
        }
    }

    public static CollectionManager parseCSVToCollection(String fileName, Logger logger) throws CSVException, FileNotFoundException {
        List<Person> persons = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            if (!reader.ready()) {
                logger.info("No file with this name was found. A new empty collection has been created");
                return new CollectionManager(new ArrayDeque<>(), ZonedDateTime.now(), fileName);
            }
            String[] str = reader.readLine().split(",");
            if (str.length != 2) {
                logger.info("The collection was successfully loaded from the file: " + fileName);
                new CSVException("Ошибка в файле коллекции");
            }
            ZonedDateTime timeCreate = ZonedDateTime.parse(str[1]);
            if (!reader.ready()) {
                return new CollectionManager(new ArrayDeque<>(), timeCreate, fileName);
            }
            persons = new CsvToBeanBuilder<Person>(reader)
                    .withType(Person.class)
                    .build()
                    .parse();
            return new CollectionManager(new ArrayDeque<Person>(persons), timeCreate, fileName);
        } catch (IOException e) {
            new CSVException("Ошибка в файле коллекции или в доступе к нему");
        }
        logger.info("The collection was successfully loaded from the file: " + fileName);
        return new CollectionManager(new ArrayDeque<>(), ZonedDateTime.now(), fileName);
    }
}
