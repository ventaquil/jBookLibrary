import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class Directory {
    private String Path;

    public Directory(String path) throws DirectoryException
    {
        Path = path;

        File file = new File(Path);

        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new DirectoryException(path + " is not a directory");
            }
        } else {
            file.mkdir();
        }
    }

    public String[] loadBooks()
    {
        File[] books = new File(Path).listFiles(new FileFilter() {
            /* Get only directories and PDF files */
            @Override
            public boolean accept(File file)
            {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String path = file.getPath();
                    return path.substring(path.lastIndexOf('.') + 1)
                               .toLowerCase()
                               .equals("pdf");
                }
            }
        });

        String[] bookNames;
        String name;
        int i = 0;
        if (jBookLibrary.getDirectory()
                        .equals(jBookLibrary.getBaseDirectory())) {
            bookNames = new String[books.length];
        } else {
            bookNames = new String[books.length + 1];
            bookNames[i++] = "../";
        }
        for (File book: books) {
            bookNames[i++] = BookNames.get(book.getName());
        }

        return sortNames(bookNames);
    }

    public void remove(String fileName)
    {
        File file = new File(Path + "/" + fileName);

        remove(file);
    }

    public static void remove(File file)
    {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file)
                    {
                        return file.isDirectory();
                    }
                });

                if (subFiles.length > 0) {
                    for (File subFile: subFiles) {
                        remove(subFile);
                    }
                }
            } else {
                BookNames.remove(file.getName());

                BookNames.initialize();
            }

            file.delete();
        }
    }

    public void rename(String oldName, String newName)
    {
        File file = new File(Path + "/" + oldName);

        if (file.isDirectory()) {
            file.renameTo(new File(Path + "/" + newName));
        } else {
            BookNames.remove(oldName);
            BookNames.put(oldName, newName);
        }
    }

    private String[] sortNames(String[] names)
    {
        Integer directoriesCount = 0;

        for (String name: names) {
            if (name.indexOf('/') != -1) {
                directoriesCount++;
            }
        }

        String[] directories = new String[directoriesCount],
                 books = new String[names.length - directoriesCount];

        Integer directoriesIndex = 0,
                booksIndex = 0;
        for (String name: names) {
            if (name.indexOf('/') == -1) {
                books[booksIndex++] = name;
            } else {
                directories[directoriesIndex++] = name;
            }
        }

        Arrays.sort(directories);
        Arrays.sort(books);

        Integer namesIndex = 0;
        for (String directory: directories) {
            names[namesIndex++] = directory;
        }
        for (String book: books) {
            names[namesIndex++] = book;
        }

        return names;
    }
};
