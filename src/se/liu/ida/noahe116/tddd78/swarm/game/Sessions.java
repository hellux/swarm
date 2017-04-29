package se.liu.ida.noahe116.tddd78.swarm.game;

import java.nio.file.*;
import java.util.logging.*;
import java.util.Arrays;
import java.io.IOException;

/**
 * Saves and loads game sessions to and from files.
 **/
public final class Sessions {
    private static final Logger LOGGER = Logger.getLogger(Sessions.class.getName());
    
    private static final int BYTESIZE = 8;
    private static final int SIZEOF_INT = 4;

    public static String FILE_EXTENSION = ".swrm";
    public static byte[] HEADER = "SWARM".getBytes();

    @SuppressWarnings("serial")
    public static class NoSuchSessionException extends Exception {
        NoSuchSessionException(String message) {super(message);}
    }

    public static Game load(String name) throws NoSuchSessionException {
        byte[] fileContents;

        try {
            fileContents = Files.readAllBytes(getPath(name));
        } catch (IOException e) {
            throw new NoSuchSessionException("could not read session file");
        }

        if (hasHeader(fileContents)) {
            int index = HEADER.length;
            int level = byteArraytoInt(
                Arrays.copyOfRange(fileContents, HEADER.length, HEADER.length+SIZEOF_INT)
            );
            return new Game(level, name);
        } else {
            throw new NoSuchSessionException("session file has no header");
        }
        
    }

    public static void save(Game game) throws IOException {
        byte[] fileContents = createContents(game);
        Files.write(getPath(game.getName()), fileContents);
    }

    public static Game create(String name) {
        return new Game(1, name);
    }

    private static boolean hasHeader(byte[] fileContents) {
        for (int i = 0; i < HEADER.length; i++) {
            if (fileContents[i] != HEADER[i]) {
                return false;
            }
        }
        return true;
    }

    private static Path getPath(String name) {
        String fileName = name + FILE_EXTENSION;
        return Paths.get(fileName);
    }

    private static byte[] createContents(Game game) {
        byte[] level = intToByteArray(game.getMaxLevel());
        return concatByteArrays(HEADER, level);
    }

    private static byte[] concatByteArrays(byte[]...arrays) {
        int arraySize = 0;
        for (byte[] array : arrays) {
            arraySize += array.length;
        }

        byte[] concatArray = new byte[arraySize];
        int index = 0;

        for (byte[] array : arrays) {
            for (byte b : array) {
                concatArray[index++] = b;
            }
        }

        return concatArray;
    }

    public static int byteArraytoInt(byte[] bytes) {
        int integer = 0;

        for (int i = 0; i < SIZEOF_INT; i++) {
            integer += (int) bytes[i] << i*BYTESIZE;
        }

        return integer;
    }

    public static byte[] intToByteArray(int integer) {
        byte[] bytes = new byte[SIZEOF_INT];

        for (int i = 0; i < SIZEOF_INT; i++) {
            bytes[i] = (byte) (integer >> i*BYTESIZE);
        }

        return bytes;
    }
}
