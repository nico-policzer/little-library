package ui;

import model.Book;
import model.Library;
import model.Member;
import model.Review;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibraryGUI extends JFrame {

    private JDialog dialog;


    private static String RETURN_BUTTON_LABEL = "Return";
    private static String QUIT_BUTTON_LABEL = "Quit";
    private static String SEARCH_BUTTON_LABEL = "Search";
    private static String MENU_TEXT = "Please choose an option below";

    private static File borrowedImg = new File(
            "/Users/nicopoliczer/Desktop/CPSC 210/project_s3j3k/src/main/ui/borrowed.png");

    private static final int SEARCH_BUTTON_FUNCTION = 0;
    private static final int QUIT_BUTTON_FUNCTION = 1;
    private static final int RETURN_BUTTON_FUNCTION = 2;
    private static final int REGISTER_BUTTON_FUNCTION = 3;


    private Library lib;
    private Member user;


    // EFFECT: contstructs the main window, starts load process
    public LibraryGUI() {
        super("LibraryGUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        loadMenu();
        setVisible(true);
        pack();

    }


    // REQUIRES: Library is non-null.
    // EFFECTS: creates main menu with buttons to search, return or quit.
    private void mainMenu() {
        user = lib.getMembers().get(1);
        JLabel titleLabel = new JLabel("Welcome to " + lib.getName() + ". " + "Logged in as: "  + user.getName(),
                SwingConstants.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton quitButton = new JButton(QUIT_BUTTON_LABEL);
        JButton returnButton = new JButton(RETURN_BUTTON_LABEL);
        JButton searchButton = new JButton(SEARCH_BUTTON_LABEL);
        JButton registerBookButton = new JButton("Register a new book");
        buttonPanel.add(searchButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(quitButton);
        searchButton.addActionListener(new MenuClickHandler(SEARCH_BUTTON_FUNCTION));
        returnButton.addActionListener(new MenuClickHandler(RETURN_BUTTON_FUNCTION));
        quitButton.addActionListener(new MenuClickHandler(QUIT_BUTTON_FUNCTION));
        registerBookButton.addActionListener(new MenuClickHandler(REGISTER_BUTTON_FUNCTION));
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0,1));

        menuPanel.add(titleLabel);
        menuPanel.add(new JLabel(MENU_TEXT, SwingConstants.CENTER));

        menuPanel.add(buttonPanel);
        menuPanel.add(registerBookButton);
        setContentPane(menuPanel);
        pack();
    }

    // EFFECTS: intializes library of name name with books and members
    private Library defaultLibrary(String name) {
        java.util.List<Book> initialBooks = initialzeBooks();
        Library lib = new Library(name, initialBooks);

        Member m1 = new Member("GUI Admin User");
        Member m2 = new Member("H.P Deeves");
        Member m3 = new Member("Nico P");

        lib.registerMember(m1);
        lib.registerMember(m2);
        lib.registerMember(m3);

        lib.borrowBook(initialBooks.get(0), m2);
        lib.returnBook(initialBooks.get(0), m2);
        lib.borrowBook(initialBooks.get(8), m3);
        lib.returnBook(initialBooks.get(8), m3);
        m3.leaveReview(initialBooks.get(8), new Review(initialBooks.get(8), m1,
                2, "Way too convoluted. Characters have four names each, impossible to follow"));
        m2.leaveReview(initialBooks.get(0), new Review(initialBooks.get(0), m2, 5, "Book changed my life."));

        return lib;
    }

    // EFFECTS: initializes the initial catalogue of books
    private java.util.List<Book> initialzeBooks() {
        Book b0 = new Book("The Four Agreements", "Self-Help", "Don Miguel Ruiz");
        Book b1 = new Book("Art of War", "Classic", "Sun Tzu");
        Book b2 = new Book("The Blank Slate", "Science", "Steven Pinker");
        Book b3 = new Book("A tale of Two Cities", "Classic", "Charles Dickens");
        Book b4 = new Book("Violeta: se fue a los cielos", "Foreign Language", "Angel Parra");
        Book b5 = new Book("Stalingrad", "War", "Antony Beevor");
        Book b6 = new Book("The Sun also Rises", "Classic", "Ernest Hemingway");
        Book b7 = new Book("Crime and Punishment", "Classic", "Fyodor Dostoevsky");
        Book b8 = new Book("Brothers Karamazov", "Classic", "Fyodor Dostoevsky");
        Book b9 = new Book("The Lord of the Rings: Return of the King", "Fantasy", "J.R.R Tolkien");
        Book b10 = new Book("The Hobbit", "Fantasy", "J.R.R Tolkien");
        Book b11 = new Book("The Iliad", "Classic", "Homer");
        Book b12 = new Book("Meditations", "Self-Help", "Marcus Aurelius");
        List<Book> initialBooks = new ArrayList<Book>(Arrays.asList(
                b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12));
        return initialBooks;
    }

    // EFFECTS: displays search menu as a modal dialog
    private void handleSearch() {
        dialog = new JDialog(this, true);
        JPanel searchMenu = new JPanel();
        searchMenu.setLayout(new BoxLayout(searchMenu, BoxLayout.Y_AXIS));
        JLabel menuTitle = new JLabel("Select a book below to view or borrow", SwingConstants.CENTER);
        searchMenu.add(menuTitle);
        List<JButton> bookButtonList = booksToButtons();
        for (JButton bookButton: bookButtonList) {
            searchMenu.add(bookButton);
        }

        dialog.setContentPane(searchMenu);
        dialog.pack();
        dialog.setVisible(true);
    }

    // EFFECTS: returns all available books as a list of JButtons displaying title, with action listeners to book menu
    private List<JButton> booksToButtons() {
        List<JButton> buttons = new ArrayList<>();
        for (Book book : lib.getAvailableBooks()) {
            JButton button = new JButton(book.getTitle());
            button.addActionListener(new SearchMenuClickHandler(book));
            button.setOpaque(true);
            buttons.add(button);
        }

        return buttons;
    }


    // EFFECTS: displays book menu with book information and button to borrow book.
    private void handleBookMenu(Book book) {
        JPanel bookMenu = new JPanel(new GridLayout(0,1));
        JLabel title = new JLabel(book.getTitle());
        JLabel author = new JLabel("AUTHOR: " + book.getAuthor());
        JLabel genre = new JLabel("GENRE: " + book.getGenre());
        String bookRating = "";
        if (book.getRating() == 0) {
            bookRating = "NA";
        } else {
            bookRating = ""  + book.getRating();
        }
        JLabel rating = new JLabel("RATING: " + bookRating);
        JButton borrowButton = new JButton("Borrow this book");
        borrowButton.addActionListener(new BookMenuClickHandler(book, 0));
        JButton backButton = new JButton("Back to search");
        backButton.addActionListener(new BookMenuClickHandler(book, 1));
        bookMenu.add(title);
        bookMenu.add(author);
        bookMenu.add(genre);
        bookMenu.add(rating);
        bookMenu.add(borrowButton);
        bookMenu.add(backButton);
        dialog.setContentPane(bookMenu);
        dialog.pack();
    }



    // EFFECTS: writes library to JSON and saves in data/user/LIBRARYNAME.json, where LIBRARYNAME is the libraries name.
    // Overwrites any previously saved librarys with this name.
    private void saveLibrary() {
        JsonWriter writer = new JsonWriter("./data/user/" + lib.getName() + ".json");
        try {
            writer.open();
            writer.write(lib);
            writer.close();
            System.out.println("Saved.");
        } catch (IOException e) {
            System.err.println("ERROR: COULDN'T SAVE");
        }
    }

    // EFFECTS: displays return menu as a modal dialog
    private void handleReturn() {
        dialog = new JDialog(this, true);
        JPanel returnMenu = new JPanel(new GridLayout(0,1));
        List<JButton> returnButtons = userBooksToReturnButtons();
        JLabel label = new JLabel("Click a book to return it");
        returnMenu.add(label);
        for (JButton returnButton: returnButtons) {
            returnMenu.add(returnButton);
        }
        if (returnButtons.isEmpty()) {
            label.setText("No books to return!");
        }
        dialog.setContentPane(returnMenu);
        dialog.pack();
        dialog.setVisible(true);
    }

    // EFFECTS: returns list of JButtons for the books borrowed by user, with action listeners which return the book.
    private List<JButton> userBooksToReturnButtons() {
        List<JButton> buttons = new ArrayList<>();
        for (Book book : user.getBorrowedBooks()) {
            JButton button = new JButton(book.getTitle());
            button.addActionListener(new ReturnMenuClickHandler(book));
            buttons.add(button);
        }
        return buttons;
    }

    // EFFECTS: creates dialog to register a new book
    private void registerBook() {
        dialog = new JDialog(this);
        JPanel registerBookPanel = getRegisterBookPanel();
        dialog.setContentPane(registerBookPanel);
        dialog.pack();
        dialog.setVisible(true);

    }

    // EFFECTS: returns JPanel to register a new book with title, author and genre text fields and a button to submit
    private JPanel getRegisterBookPanel() {
        JPanel registerBookPanel = new JPanel(new GridLayout(0,1));

        JPanel title = new JPanel(new GridLayout(1,0));
        JTextField titleInput = new JTextField();
        title.add(new JLabel("Title:"));
        title.add(titleInput);

        JPanel author = new JPanel(new GridLayout(1,0));
        JTextField authorInput = new JTextField();
        author.add(new JLabel("Author:"));
        author.add(authorInput);


        JPanel genre = new JPanel(new GridLayout(1,0));
        JTextField genreInput = new JTextField();
        genre.add(new JLabel("Genre:"));
        genre.add(genreInput);

        JButton submitRegister = new JButton("Register Book");
        submitRegister.addActionListener(new RegisterBookActionHandler(titleInput, authorInput, genreInput));
        registerBookPanel.add(new JLabel("Input the info for the book you'd like to register."));
        registerBookPanel.add(title);
        registerBookPanel.add(author);
        registerBookPanel.add(genre);
        registerBookPanel.add(submitRegister);
        return registerBookPanel;
    }


    // EFFECTS: prompts user to save, then quits application
    private void handleQuit() {
        dialog = new JDialog(this, true);
        JPanel savePanel = new JPanel(new GridLayout(0,1));
        JButton save = new JButton("Yes");
        save.addActionListener(new SaveMenuClickHandler(true));
        JButton noSave = new JButton("No");
        noSave.addActionListener(new SaveMenuClickHandler(false));
        JLabel savePrompt = new JLabel("Would you like to save the state of the library?");
        savePanel.add(savePrompt);
        savePanel.add(save);
        savePanel.add(noSave);
        dialog.setContentPane(savePanel);
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }

    // EFFECTS: prompts user to load previously saved library, or user a defauly starting library
    private void loadMenu() {
        JPanel loadPanel = new JPanel(new GridLayout(0,1));
        JLabel promptLoad = new JLabel(
                "Would you like to load from a previously saved library, or create a new one?");
        JButton load = new JButton("Load from a previous library");
        JButton defaultLib = new JButton("Create a new library with a default set of books");
        load.addActionListener(new LoadMenuActionHandler(true));
        defaultLib.addActionListener(new LoadMenuActionHandler(false));
        loadPanel.add(promptLoad);
        loadPanel.add(load);
        loadPanel.add(defaultLib);
        this.setContentPane(loadPanel);

    }

    // EFFECTS: displays all previously saved libraries and allows user to choose which one to load
    private void previousSavesMenu() {
        JPanel previousSavesPanel = new JPanel(new GridLayout(0,1));
        JLabel label = new JLabel("Please select a library to load");
        previousSavesPanel.add(label);
        File userSaves = new File("./data/user");
        String[] contents = userSaves.list();
        ArrayList<String> fileNames = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            fileNames.add(contents[i]);
        }
        for (String fileName : fileNames) {
            JButton loadButton = new JButton(fileName.replaceAll(".json", ""));
            loadButton.addActionListener(new LoadFileActionHandler(fileName));
            previousSavesPanel.add(loadButton);
        }
        this.setContentPane(previousSavesPanel);
        pack();
    }

    // EFFECTS: prompts user for the name of the new library and creates it
    private void newLibraryMenu() {
        JPanel newLibraryMenu = new JPanel(new GridLayout(0,1));
        JLabel label = new JLabel("Please enter the name of your new library.");
        JLabel warning = new JLabel("Note: previously saved libraries of the same name will be overwritten");
        JTextField input = new JTextField();
        JButton submit = new JButton("Create new library");
        submit.addActionListener(new NewLibraryActionHandler(input));
        newLibraryMenu.add(label);
        newLibraryMenu.add(warning);
        newLibraryMenu.add(input);
        newLibraryMenu.add(submit);
        this.setContentPane(newLibraryMenu);
        pack();
    }

    class RegisterBookActionHandler implements ActionListener {
        private JTextField titleInput;
        private JTextField authorInput;
        private JTextField genreInput;

        public RegisterBookActionHandler(JTextField titleInput, JTextField authorInput, JTextField genreInput) {
            this.titleInput = titleInput;
            this.authorInput = authorInput;
            this.genreInput = genreInput;

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String title = titleInput.getText();
            String author = authorInput.getText();
            String genre = genreInput.getText();
            if (genre.length() != 0 && author.length() != 0 && title.length() != 0) {
                lib.registerBook(new Book(title, genre, author));
                dialog.dispose();
            }

        }
    }


    class NewLibraryActionHandler implements ActionListener {
        private JTextField input;

        public NewLibraryActionHandler(JTextField input) {
            this.input = input;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = input.getText();
            if (name.length() != 0) {
                System.out.println("POINTER");
                lib = defaultLibrary(name);
                mainMenu();
            }
        }
    }

    class LoadFileActionHandler implements ActionListener {
        // a click handler which loads library from a given file on click.
        private String file;

        // EFFECTS: creates a click handler which will load from file file on click.
        public LoadFileActionHandler(String file) {
            this.file = file;
        }

        @Override
        // EFFECTS: loads library from file, logs in user.
        public void actionPerformed(ActionEvent e) {
            try {
                JsonReader reader = new JsonReader("./data/user/" + file);
                lib = reader.read();
            } catch (IOException ex) {
                System.err.println("LIBRARY LOAD FAILURE");
                System.exit(0);
            }
            mainMenu();
        }
    }

    class LoadMenuActionHandler implements ActionListener {
        // click handler for buttons in load menu. if load = true, button directs to load menu, else directs to
        // menu to create a new library.
        private boolean load;

        // EFFECTS: creates a click handler for load menu button with load corresponding to function.
        public LoadMenuActionHandler(boolean load) {
            this.load = load;
        }

        @Override
        // EFFECTS: if load, directs to menu to load from a previous save file, else directs to menu to create a new lib
        public void actionPerformed(ActionEvent e) {
            if (load) {
                previousSavesMenu();
            } else {
                newLibraryMenu();
            }
        }
    }

    class SaveMenuClickHandler implements ActionListener {
        // a click handler for buttons which save and quit from the library.
        private boolean save;

        // EFFECTS: creates a click handler for "yes/no" save buttons.
        // if true, button will save on click. always ends application
        public SaveMenuClickHandler(boolean save) {
            this.save = save;
        }

        @Override
        // EFFECTS: if save = true, saves library to file using library name. exits application in all cases.
        public void actionPerformed(ActionEvent e) {
            if (save) {
                saveLibrary();
            }
            System.exit(0);
        }
    }

    class ReturnMenuClickHandler implements ActionListener {
        // button click handler for the books in the return menu.
        private Book book;

        // EFFECTS: creates a click handler to return book.
        public ReturnMenuClickHandler(Book book) {
            this.book = book;
        }


        @Override
        // EFFECTS: returns book and closes dialog(returns to main menu).
        public void actionPerformed(ActionEvent e) {
            lib.returnBook(book, user);
            dialog.dispose();
        }
    }

    class BookMenuClickHandler implements ActionListener {
        // click handler for the buttons in the book menu.
        private Book book;
        private int function;

        // REQUIRES: function is in [0,1].
        // EFFECTS: creates a click handler for the book menu. if function = 0, corresponds to book borrow button,
        // if function = 1 , corresponds to back button.
        public BookMenuClickHandler(Book book, int function) {
            this.book = book;
            this.function = function;
        }

        // EFFECTS: if function = 1; user borrows book, creates JDialog affirming that the book was borrowed.
        // returns to search menu in all cases.
        @Override
        public void actionPerformed(ActionEvent e) {
            if (function == 0) {
                lib.borrowBook(book, user);
                JDialog affirmBorrow = new JDialog(dialog, true);
                JPanel affirmPanel = new JPanel();
                affirmPanel.setLayout(new BoxLayout(affirmPanel, BoxLayout.Y_AXIS));
                JLabel affirmBorrowLabel = new JLabel(user.getName() + " successfully borrowed " + book.getTitle());
                JLabel affirmPic = new JLabel();
                try {
                    affirmPic.setIcon(new ImageIcon(ImageIO.read(borrowedImg)));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                affirmPanel.add(affirmBorrowLabel);
                affirmPanel.add(affirmPic);
                affirmBorrow.setContentPane(affirmPanel);
                affirmBorrow.pack();
                affirmBorrow.setVisible(true);
            }
            dialog.dispose();
            handleSearch();
        }
    }

    class SearchMenuClickHandler implements ActionListener {
        // a click handler for the books in the search menu.

        private Book book;

        // EFFECTS: creates a click handler for the search menu to direct the user to the book menu for book
        public SearchMenuClickHandler(Book book) {
            this.book = book;
        }

        @Override
        // EFFECTS: on button click opens book menu
        public void actionPerformed(ActionEvent e) {
            handleBookMenu(book);
        }
    }

    class MenuClickHandler implements ActionListener {
        // Action listener for buttons in main menu.

        private int function;

        // REQUIRES: function is one of SEARCH_BUTTON_FUNCTION, RETURN_BUTTON_FUNCTION or QUIT_BUTTON_FUNCTION
        // EFFECTS: creates a menu click handler for given button
        public MenuClickHandler(int function) {
            this.function = function;
        }

        @Override
        // EFFECTS: depending on function, directs user to next menu or quits on button press.
        public void actionPerformed(ActionEvent e) {
            switch (function) {
                case SEARCH_BUTTON_FUNCTION:
                    handleSearch();
                    break;
                case RETURN_BUTTON_FUNCTION:
                    handleReturn();
                    break;
                case QUIT_BUTTON_FUNCTION:
                    handleQuit();
                    break;
                case REGISTER_BUTTON_FUNCTION:
                    registerBook();
                    break;
            }
        }
    }





}
