package com.lev_prav.client.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.Stack;

public class UserIO {
    private Scanner fin;
    private Stack<ArrayDeque<String>> fileContents;
    private Stack<String> fileNames;
    private BufferedWriter fout;
    private boolean scriptMode;
    private boolean isEnded;

    public UserIO(Scanner fin, Writer fout) {
        this.fin = fin;
        this.fout = new BufferedWriter(fout);
        scriptMode = false;
        isEnded = false;
    }

    public UserIO(Scanner fin, Writer fout, boolean scriptMode) {
        this(fin, fout);
        this.scriptMode = scriptMode;
        isEnded = false;
    }

    public String readline() {
        if (scriptMode) {
            if (!fileContents.empty() && !fileContents.peek().isEmpty()) {
                return fileContents.peek().pop();
            } else {
                finishReadScript();
                return readline();
            }
        }
        if (!fin.hasNextLine() && !isEnded) {
            isEnded = true;
            return "";
        }
        return fin.nextLine();
    }

    public void write(Object s) {
        try {
            fout.write(s.toString());
            fout.flush();
        } catch (IOException e) {
            writeln("Exception while writing to output stream: \n" + e);
        }
    }

    public void writeln(Object s) {
        try {
            fout.write(s.toString());
            fout.newLine();
            fout.flush();
        } catch (IOException e) {
            writeln("Exception while writing to output stream: \n" + e);
        }
    }

    public void writelnColorMessage(Object s, Color color) {
        writeln(color.colorize(s.toString()));
    }

    public void writeColorMessage(Object s, Color color) {
        write(color.colorize(s.toString()));
    }

    public Scanner getFin() {
        return fin;
    }

    public void setFin(Scanner fin) {
        this.fin = fin;
    }

    public BufferedWriter getFout() {
        return fout;
    }

    public void setFout(BufferedWriter fout) {
        this.fout = fout;
    }

    public void setScriptMode(boolean scriptMode) {
        this.scriptMode = scriptMode;
    }

    public boolean isScriptMode() {
        return scriptMode;
    }

    public void startReadScript(String fileName) {
        if (fileContents == null) {
            fileContents = new Stack<>();
            fileNames = new Stack<>();
        }
        if (fileNames.stream().anyMatch(v -> v.equals(fileName))) {
            writeln("File '" + fileName + "' has already been opened");
            return;
        }
        fileContents.push(new ArrayDeque<>());
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                fileContents.peek().add(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            writeln("File '" + fileName + "' not found");
            fileContents.pop();
            return;
        }
        fileNames.push(fileName);

        writeln("Start reading from file " + fileName + " ...");
        scriptMode = true;


    }

    public void finishReadScript() {
        if (!fileContents.empty()) {
            fileContents.pop();
            fileNames.pop();
        }
        if (fileContents.empty()) {
            scriptMode = false;
        }
        writeln("Reading from file was finished");
    }

    public void finishReadAllScript() {
        if (!scriptMode) {
            return;
        }
        if (!fileContents.empty()) {
            fileContents.clear();
            fileNames.clear();
        }
        scriptMode = false;
    }
}
