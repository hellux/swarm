package se.liu.ida.noahe116.tddd78.swarm.game;

import java.nio.file.*;
import java.util.logging.*;
import java.util.Arrays;
import java.io.IOException;

/**
 * Save and load game sessions to and from save files.
 * 
 * Save file format specification:
 *  Uses .swrm extension. Is simple binary file with a header and data.
 *
 *  File contents:
 *  -a header with encoded string "SWARM"
 *  -4 byte integer representing the max level of the session
 **/
public final class Sessions {
    private static final Logger LOGGER = Logger.getLogger(Sessions.class.getName());
    
    private static final int BYTESIZE = 8;
    private static final int SIZEOF_INT = 4;

    public static String FILE_EXTENSION = ".swrm";
    public static byte[] HEADER = "SWARM".getBytes();

    public static Game load(String name) throws IOException { 
        byte[] fileContents;

        fileContents = Files.readAllBytes(getPath(name));

        if (hasHeader(fileContents)) {
            int index = HEADER.length;
            int level = byteArraytoInt(
                Arrays.copyOfRange(fileContents, HEADER.length, HEADER.length+SIZEOF_INT)
            );
            if (level < 1) throw new IOException("save file is corrupted");
            return new Game(level, name);
        } else {
            throw new IOException("session file has no header");
        }
        
    }

    public static void save(Game game) throws IOException {
        byte[] fileContents = createContents(game);
        Files.write(getPath(game.getName()), fileContents);
    }

    public static Game create(String name) throws IOException {
        Game game = new Game(1, name);
        Sessions.save(game);
        return game;
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
