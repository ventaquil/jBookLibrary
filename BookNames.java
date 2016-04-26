import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BookNames {
    private static Map<String, String> map;
    private static Map<String, String> files;

    public static void initialize()
    {
        map = new HashMap<String, String>();
        files = new HashMap<String, String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("./names.hash"));

            String line;
            String[] data;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("") && (line.indexOf('/') != -1)) {
                    data = line.split("/");

                    map.put(data[1], data[0]);
                    files.put(data[0], data[1]);
                }
            }
        } catch (IOException e) { }
    }

    public static String get(String name)
    {
        int indexOf = name.lastIndexOf('.');
        if (indexOf != -1) {
            String value = map.get(name.substring(0, indexOf));

            if (value == null) {
                return name;
            } else {
                return value;
            }
        } else {
            return name + "/";
        }
    }

    private static String getModifiedBaseDirectory()
    {
        String directory = jBookLibrary.getBaseDirectory();
        if (!directory.substring(directory.length() - 1).equals("/")) {
            directory += "/";
        }

        return directory;
    }

    private static String getModifiedDirectory()
    {
        String directory = jBookLibrary.getDirectory();
        if (!directory.substring(directory.length() - 1).equals("/")) {
            directory += "/";
        }

        return directory;
    }

    public static File getFile(String name) throws IOException
    {
        return new File(getModifiedDirectory() + getFileName(name));
    }

    public static String getFileName(String name)
    {
        String value = files.get(name);

        if (value == null) {
            return name;
        } else {
            return value + ".pdf";
        }
    }

    public static void put(String fileName, String name)
    {
        File file = new File("./names.hash");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // @TODO error
            }
        }

        fileName = removePdf(fileName);

        try {
            File tempFile = new File("./names-tmp.hash");

            String search = removePdf(fileName);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.write(name + "/" + fileName + System.getProperty("line.separator"));

            writer.close();
            reader.close();

            tempFile.renameTo(file);
        } catch (IOException e) {
            // @TODO error
        }
    }

    public static void remove(String fileName)
    {
        try {
            File file = new File("./names.hash");

            if (file.exists()) {
                File tempFile = new File("./names-tmp.hash");

                String search = removePdf(fileName);

                BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String currentLine;

                while((currentLine = reader.readLine()) != null) {
                    if(!currentLine.trim()
                                .contains(search)){
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                }

                writer.close();
                reader.close();

                tempFile.renameTo(file);
            }
        } catch (IOException e) {
            // @TODO exception
        }
    }

    private static String removePdf(String fileName)
    {
        int lastIndexOf = fileName.lastIndexOf('.') + 1;
        if (lastIndexOf < fileName.length()) {
            if (fileName.substring(lastIndexOf)
                        .equals("pdf")) {
                return fileName.substring(0, fileName.lastIndexOf('.'));
            }
        }

        return fileName;
    }
};
