package com.lev_prav.client.utility;

import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.ScriptException;

public class SimplePersonFiller<T> {
    private UserIO userIO;

    public SimplePersonFiller(UserIO userIO) {
        this.userIO = userIO;
    }

    public T fill(String message, Reader<T> reader) throws ScriptException {
        T returns;
        while (true) {
            try {
                if (!userIO.isScriptMode()) {
                    userIO.write(message + ": ");
                }
                returns = reader.read();
                break;
            } catch (IllegalValueException e) {
                userIO.writeln(e.getMessage());
                if (userIO.isScriptMode()) {
                    throw new ScriptException();
                }
            }
        }
        return returns;
    }
}
