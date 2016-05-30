public abstract class jBookLibrary {
    private static String directory = "./books";

    public static void addDirectory(String name)
    {
        try {
            new Directory(directory + "/" + name);

            refreshFrame();
        } catch (DirectoryException e) {
            // @TODO error
        }
    }

    public static String getBaseDirectory()
    {
        return "./books";
    }

    public static String getDirectory()
    {
        return directory;
    }

    private static Directory loadDefaultDirectory() throws DirectoryException
    {
        return new Directory(getBaseDirectory());
    }

    public static void main(String[] args)
    {
        BookFrame frame = BookFrame.create();

        BookNames.initialize();

        try {
            Directory defaultDirectory = loadDefaultDirectory();

            frame.showBooks(defaultDirectory.loadBooks());
        } catch (DirectoryException e) {
            // @TODO show error
        }

        frame.pack();
    }

    public static void refreshFrame()
    {
        try {
            Directory directory = new Directory(getDirectory());

            BookFrame.instance()
                     .refreshBooks(directory.loadBooks());
        } catch (DirectoryException e) {
            // @TODO error
        }
    }

    public static void rename(String name, String fileName)
    {
        try {
            new Directory(getDirectory()).rename(BookNames.getFileName(name), fileName);

            BookNames.initialize();

            refreshFrame();
        } catch (DirectoryException e) {
            //@TODO error
        }
    }

    public static void moveTo(String path)
    {
        if ((path.length() >= 3) && (path.substring(0, 3)
                                         .equals("../"))) {
            directory = directory.substring(0, directory.lastIndexOf('/'));
        } else if (path.substring(path.length() - 1)
                       .equals("/")) {
            directory += "/" + path.substring(0, path.length() - 1);
        }

        BookFrame.instance()
                 .updateDirectory();
    }
};
