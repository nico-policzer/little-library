package ui;

import model.Book;
import model.Event;
import model.EventLog;
import model.Library;
import model.Member;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LibraryGUI extends JFrame {
    // A graphical user interface allowing users to interact with various library menu's with buttons.
    // users can borrow books, return books, view the libraries catalogue and even register books!
    // users are also able to load previously saved libraries or create and name new libraries themselves
    // upon quitting, users can decide whether they'd like to save their library.

    public static final String SEARCH_MENU_LABEL = "Select a book below to view or borrow";
    public static final String RETURN_MENU_LABEL = "Click a book to return it";
    public static final String RETURN_MENU_LABEL_NO_BOOKS = "No books to return!";
    public static final String REGISTER_MENU_SUBMIT_BUTTON = "Register Book";
    public static final String REGISTER_MENU_LABEL = "Input the info for the book you'd like to register.";
    public static final String SAVE_MENU_PROMPT = "Would you like to save the state of the library?";
    public static final String LOAD_MENU_PROMPT =
            "Would you like to load from a previously saved library, or create a new one?";
    public static final String LOAD_FROM_SAVE_BUTTON = "Load from a previous library";
    public static final String LOAD_NEW_LIBRARY_BUTTON = "Create a new library with a default set of books";
    public static final String LOAD_FROM_SAVE_MENU_PROMPT = "Please select a library to load";
    public static final String NEW_LIBRARY_MENU_PROMPT = "Please enter the name of your new library.";
    public static final String NEW_LIBRARY_MENU_WARNING =
            "Note: previously saved libraries of the same name will be overwritten";
    public static final String NEW_LIBRARY_MENU_CREATE = "Create new library";
    private static final String RETURN_BUTTON_LABEL = "Return";
    private static final String QUIT_BUTTON_LABEL = "Quit";
    private static final String SEARCH_BUTTON_LABEL = "Search";
    private static final String MENU_TEXT = "Please choose an option below";
    private static final String REGISTER_BOOK_LABEL = "Register a new book";

    private static final File BORROWED_IMAGE = new File("data/images/borrowed.png");
    private static final File RETURNED_IMAGE = new File("data/images/returned.png");

    private JDialog dialog;
    private Library lib;
    private Member user;

    public static void main(String[] args) {
        new LibraryGUI();
    }


    // EFFECT: constructs the main window, starts load process
    public LibraryGUI() {
        super("LibraryGUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUpWindowListener();
        setLayout(new BorderLayout());
        loadMenu();
        setVisible(true);
        pack();
    }

    // MENU METHODS

    // REQUIRES: Library and user are non-null.
    // MODIFIES: this
    // EFFECTS: creates main menu with buttons to search, return or quit.
    private void mainMenu() {
        JLabel titleLabel = new JLabel("Welcome to " + lib.getName() + ". " + "Logged in as: " + user.getName(),
                SwingConstants.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton quitButton = new JButton(QUIT_BUTTON_LABEL);
        JButton returnButton = new JButton(RETURN_BUTTON_LABEL);
        JButton searchButton = new JButton(SEARCH_BUTTON_LABEL);
        JButton registerBookButton = new JButton(REGISTER_BOOK_LABEL);
        buttonPanel.add(searchButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(quitButton);
        searchButton.addActionListener(new MenuClickHandler(MenuClickHandler.SEARCH_BUTTON_FUNCTION));
        returnButton.addActionListener(new MenuClickHandler(MenuClickHandler.RETURN_BUTTON_FUNCTION));
        quitButton.addActionListener(new MenuClickHandler(MenuClickHandler.QUIT_BUTTON_FUNCTION));
        registerBookButton.addActionListener(new MenuClickHandler(MenuClickHandler.REGISTER_BUTTON_FUNCTION));
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(0, 1));

        menuPanel.add(titleLabel);
        menuPanel.add(new JLabel(MENU_TEXT, SwingConstants.CENTER));

        menuPanel.add(buttonPanel);
        menuPanel.add(registerBookButton);
        setContentPane(menuPanel);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        pack();
    }

    // MODIFIES: this
    // EFFECTS: displays search menu as a modal dialog
    private void searchMenu() {
        dialog = new JDialog(this, true);
        JPanel searchMenu = new JPanel();
        searchMenu.setLayout(new BoxLayout(searchMenu, BoxLayout.Y_AXIS));
        searchMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel menuTitle = new JLabel(SEARCH_MENU_LABEL, SwingConstants.CENTER);
        searchMenu.add(menuTitle);
        List<JButton> bookButtonList = booksToButtons();
        for (JButton bookButton : bookButtonList) {
            searchMenu.add(bookButton);
        }

        dialog.setContentPane(searchMenu);
        dialog.pack();
        dialog.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: displays book menu with book information and button to borrow book.
    private void bookMenu(Book book) {
        JPanel bookMenu = new JPanel(new GridLayout(0, 1));
        bookMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel(book.getTitle());
        JLabel author = new JLabel("AUTHOR: " + book.getAuthor());
        JLabel genre = new JLabel("GENRE: " + book.getGenre());
        String bookRating = bookRatingToString(book.getRating());
        JLabel rating = new JLabel("RATING: " + bookRating);
        JButton borrowButton = new JButton("Borrow this book");
        borrowButton.addActionListener(new BookMenuClickHandler(book, BookMenuClickHandler.BORROW_BUTTON_FUNCTION));
        JButton backButton = new JButton("Back to search");
        backButton.addActionListener(new BookMenuClickHandler(book, BookMenuClickHandler.BACK_BUTTON_FUNCTION));
        bookMenu.add(title);
        bookMenu.add(author);
        bookMenu.add(genre);
        bookMenu.add(rating);
        bookMenu.add(borrowButton);
        bookMenu.add(backButton);
        dialog.setContentPane(bookMenu);
        dialog.pack();
    }

    // MODIFIES: this
    // EFFECTS: displays return menu as a modal dialog
    private void returnMenu() {
        dialog = new JDialog(this, true);
        JPanel returnMenu = new JPanel(new GridLayout(0, 1));
        returnMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
        List<JButton> returnButtons = userBooksToReturnButtons();
        JLabel returnMenuLabel = new JLabel(RETURN_MENU_LABEL);
        returnMenu.add(returnMenuLabel);
        for (JButton returnButton : returnButtons) {
            returnMenu.add(returnButton);
        }
        if (returnButtons.isEmpty()) {
            returnMenuLabel.setText(RETURN_MENU_LABEL_NO_BOOKS);
        }
        dialog.setContentPane(returnMenu);
        dialog.pack();
        dialog.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: displays dialog to register a new book
    private void registerBookMenu() {
        dialog = new JDialog(this);
        JPanel registerBookPanel = buildRegisterBookMenu();
        dialog.setContentPane(registerBookPanel);
        dialog.pack();
        dialog.setVisible(true);

    }

    // EFFECTS: returns JPanel for menu to register a new book with title, author and genre text fields + submit button
    private JPanel buildRegisterBookMenu() {
        JPanel registerBookPanel = new JPanel(new GridLayout(0, 1));

        JPanel title = new JPanel(new GridLayout(1, 0));
        JTextField titleInput = new JTextField();
        title.add(new JLabel("Title:"));
        title.add(titleInput);

        JPanel author = new JPanel(new GridLayout(1, 0));
        JTextField authorInput = new JTextField();
        author.add(new JLabel("Author:"));
        author.add(authorInput);

        JPanel genre = new JPanel(new GridLayout(1, 0));
        JTextField genreInput = new JTextField();
        genre.add(new JLabel("Genre:"));
        genre.add(genreInput);

        JButton submitRegister = new JButton(REGISTER_MENU_SUBMIT_BUTTON);
        submitRegister.addActionListener(new RegisterBookClickHandler(titleInput, authorInput, genreInput));
        registerBookPanel.add(new JLabel(REGISTER_MENU_LABEL));
        registerBookPanel.add(title);
        registerBookPanel.add(author);
        registerBookPanel.add(genre);
        registerBookPanel.add(submitRegister);
        registerBookPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return registerBookPanel;
    }

    // MODIFIES: ths
    // EFFECTS: prompts user to load previously saved library, or user a defauly starting library
    private void loadMenu() {
        JPanel loadPanel = new JPanel(new GridLayout(0, 1));
        loadPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel promptLoad = new JLabel(
                LOAD_MENU_PROMPT);
        JButton loadButton = new JButton(LOAD_FROM_SAVE_BUTTON);
        JButton newLibraryButton = new JButton(LOAD_NEW_LIBRARY_BUTTON);
        loadButton.addActionListener(new LoadMenuClickHandler(LoadMenuClickHandler.LOAD_FUNCTION));
        newLibraryButton.addActionListener(new LoadMenuClickHandler(LoadMenuClickHandler.NEW_LIBRARY_FUNCTION));
        loadPanel.add(promptLoad);
        loadPanel.add(loadButton);
        loadPanel.add(newLibraryButton);
        setContentPane(loadPanel);
    }

    // MODIFIES: this
    // EFFECTS: displays all previously saved libraries and allows user to choose which one to load
    private void loadFromSaveMenu() {
        JPanel previousSavesPanel = new JPanel(new GridLayout(0, 1));
        previousSavesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel(LOAD_FROM_SAVE_MENU_PROMPT);
        previousSavesPanel.add(label);
        File userSaves = new File("./data/user");
        String[] contents = userSaves.list();
        ArrayList<String> fileNames = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            fileNames.add(contents[i]);
        }
        for (String fileName : fileNames) {
            JButton loadButton = new JButton(fileName.replaceAll(".json", ""));
            loadButton.addActionListener(new LoadFileClickHandler(fileName));
            previousSavesPanel.add(loadButton);
        }
        this.setContentPane(previousSavesPanel);
        pack();
    }

    // MODIFIES: this
    // EFFECTS: prompts user to save, then quits application
    private void saveMenu() {
        dialog = new JDialog(this, true);
        JPanel savePanel = new JPanel(new GridLayout(0, 1));
        savePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton saveButton = new JButton("Yes");
        saveButton.addActionListener(new SaveMenuClickHandler(SaveMenuClickHandler.SAVE_FUNCTION));
        JButton noSaveButton = new JButton("No");
        noSaveButton.addActionListener(new SaveMenuClickHandler(SaveMenuClickHandler.NO_SAVE_FUNCTION));
        JLabel savePrompt = new JLabel(SAVE_MENU_PROMPT);
        savePanel.add(savePrompt);
        savePanel.add(saveButton);
        savePanel.add(noSaveButton);
        dialog.setContentPane(savePanel);
        dialog.pack();
        dialog.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: prompts user for the name of the new library and creates it
    private void newLibraryMenu() {
        JPanel newLibraryMenu = new JPanel(new GridLayout(0, 1));
        newLibraryMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel(NEW_LIBRARY_MENU_PROMPT);
        JLabel warning = new JLabel(NEW_LIBRARY_MENU_WARNING);
        JTextField input = new JTextField();
        JButton createLibraryButton = new JButton(NEW_LIBRARY_MENU_CREATE);
        createLibraryButton.addActionListener(new NewLibraryClickHandler(input));
        newLibraryMenu.add(label);
        newLibraryMenu.add(warning);
        newLibraryMenu.add(input);
        newLibraryMenu.add(createLibraryButton);
        this.setContentPane(newLibraryMenu);
        pack();
    }

    // HELPERS:

    // EFFECTS: writes library to JSON and saves in data/user/LIBRARYNAME.json, where LIBRARYNAME is the libraries name.
    // Overwrites any previously saved libraries with this name.
    private void saveLibrary() {
        JsonWriter writer = new JsonWriter("./data/user/" + lib.getName() + ".json");
        try {
            writer.open();
            writer.write(lib);
            writer.close();
        } catch (IOException e) {
            System.err.println("ERROR: COULDN'T SAVE");
        }
    }

    // EFFECTS: returns all available books in lib as  list of JButtons displaying title & action listeners to book menu
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


    // EFFECTS: returns string displaying rating or NA if no ratings have been left.
    private String bookRatingToString(int rating) {
        String bookRating;
        if (rating == 0) {
            bookRating = "NA";
        } else {
            bookRating = "" + rating;
        }
        return bookRating;
    }

    // MODIFIES: this
    // EFFECTS: user borrows book from lib, displays affirming dialog with image confirming borrowing of the book
    private void handleBorrow(Book book) {
        lib.borrowBook(book, user);
        affirmDialog(BORROWED_IMAGE, user.getName() + " successfully borrowed " + book.getTitle());
    }

    // EFFECTS: displays dialog of text on top of image
    private void affirmDialog(File image, String text) {
        JDialog affirmDialog = new JDialog(dialog, true);
        JPanel affirmPanel = new JPanel();
        affirmPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        affirmPanel.setLayout(new BoxLayout(affirmPanel, BoxLayout.Y_AXIS));
        JLabel affirmBorrowLabel = new JLabel(text);
        JLabel affirmPic = new JLabel();
        try {
            affirmPic.setIcon(new ImageIcon(ImageIO.read(image)));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        affirmPanel.add(affirmBorrowLabel);
        affirmPanel.add(affirmPic);
        affirmDialog.setContentPane(affirmPanel);
        affirmDialog.pack();
        affirmDialog.setVisible(true);
    }

    // MODIFIES: this, book
    // EFFECTS: returns book to lib, displays affirming return dialog then reloads return menu
    private void handleReturn(Book book) {
        lib.returnBook(book, user);
        affirmDialog(RETURNED_IMAGE, user.getName() + " successfully returned " + book.getTitle());
        dialog.dispose();
        returnMenu();
    }

    // MODIFIES: this
    // EFFECTS: registers new book of name title, author, and genre, then closes return dialog
    private void handleRegisterBook(String title, String author, String genre) {
        lib.registerBook(new Book(title, genre, author));
        dialog.dispose();
    }

    // EFFECTS: adds window listener to this frame to call closeGUI on window close
    private void setUpWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeGUI();
            }
        });
    }

    // EFFECTS: closes GUI and prints current log to console
    private void closeGUI() {
        Iterator<Event> events = EventLog.getInstance().iterator();
        for (Iterator<Event> it = events; it.hasNext(); ) {
            Event event = it.next();
            System.out.println(event.toString());
        }
        dispose();
    }

    // CLICK HANDLERS

    private class RegisterBookClickHandler implements ActionListener {
        // click handler for registering book menu. takes input from texts fields for author, title and genre

        private JTextField titleInput;
        private JTextField authorInput;
        private JTextField genreInput;

        // EFFECTS: creates a new click handler with text fields for author, title and genre
        public RegisterBookClickHandler(JTextField titleInput, JTextField authorInput, JTextField genreInput) {
            this.titleInput = titleInput;
            this.authorInput = authorInput;
            this.genreInput = genreInput;
        }

        @Override
        // MODIFIES: this
        // EFFECTS: if all fields have been filled in,
        // registers new book to lib by getting title, author and genre from text fields.
        public void actionPerformed(ActionEvent e) {
            String title = titleInput.getText();
            String author = authorInput.getText();
            String genre = genreInput.getText();
            if (genre.length() != 0 && author.length() != 0 && title.length() != 0) {
                handleRegisterBook(title, author, genre);
            }
        }
    }

    private class NewLibraryClickHandler implements ActionListener {
        // click handler for menu to create a new library. creates library with name from input on click then goes to
        // main menu
        private JTextField input;

        // EFFECTS: creates new click handler with JTextField for name input
        public NewLibraryClickHandler(JTextField input) {
            this.input = input;
        }

        // MODIFIES: this
        // EFFECTS: sets lib as new library of name from input with default collection of books, starts main menu
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = input.getText();
            if (name.length() != 0) {
                lib = LibraryInitializer.defaultLibrary(name);
                user = lib.getMembers().get(1);
                mainMenu();
            }
        }
    }

    private class LoadFileClickHandler implements ActionListener {
        // a click handler which loads library from a given file on click.
        private String file;

        // EFFECTS: creates a click handler which will load from file file on click.
        public LoadFileClickHandler(String file) {
            this.file = file;
        }

        @Override
        // MODIFIES: this
        // EFFECTS: loads library from file, logs in user.
        public void actionPerformed(ActionEvent e) {
            try {
                JsonReader reader = new JsonReader("./data/user/" + file);
                lib = reader.read();
                user = lib.getMembers().get(1);
            } catch (IOException ex) {
                System.err.println("LIBRARY LOAD FAILURE");
                System.exit(0);
            }
            mainMenu();
        }
    }

    private class LoadMenuClickHandler implements ActionListener {
        // click handler for buttons in load menu. if load = true, button directs to load menu, else directs to
        // menu to create a new library.
        private static final boolean LOAD_FUNCTION = true;
        private static final boolean NEW_LIBRARY_FUNCTION = false;
        private boolean function;

        // EFFECTS: creates a click handler for load menu button with load corresponding to function.
        public LoadMenuClickHandler(boolean function) {
            this.function = function;
        }

        @Override
        // MODIFIES: this
        // EFFECTS: directs to menu to load from a previous save file, or  to menu to create a new lib depending on
        // buttons function
        public void actionPerformed(ActionEvent e) {
            if (function == LOAD_FUNCTION) {
                loadFromSaveMenu();
            } else if (function == NEW_LIBRARY_FUNCTION) {
                newLibraryMenu();
            }


        }
    }

    private class SaveMenuClickHandler implements ActionListener {
        // a click handler for buttons which save and quit from the library.
        private boolean function;
        private static final boolean SAVE_FUNCTION = true;
        private static final boolean NO_SAVE_FUNCTION = false;

        // EFFECTS: creates a click handler for "yes/no" save buttons in save menu.
        // if save button, button will save on click. always ends application
        public SaveMenuClickHandler(boolean function) {
            this.function = function;
        }

        @Override
        // MODIFIES: this
        // EFFECTS: if save button, saves library to file using library name. exits application in all cases.
        public void actionPerformed(ActionEvent e) {
            if (function == SAVE_FUNCTION) {
                saveLibrary();
            }
            closeGUI();
        }
    }

    private class ReturnMenuClickHandler implements ActionListener {
        // button click handler for the books in the return menu.
        private Book book;

        // EFFECTS: creates a click handler to return book.
        public ReturnMenuClickHandler(Book book) {
            this.book = book;
        }

        @Override
        // MODIFIES: this
        // EFFECTS: returns book and closes dialog(returns to main menu).
        public void actionPerformed(ActionEvent e) {
            handleReturn(book);
        }

    }

    private class BookMenuClickHandler implements ActionListener {
        // click
        // handler for the buttons in the book menu.
        private Book book;
        private boolean function;
        private static final boolean BORROW_BUTTON_FUNCTION = true;
        private static final boolean BACK_BUTTON_FUNCTION = false;

        // EFFECTS: creates a click handler for the book menu. depending on function, can be a borrow button or back
        public BookMenuClickHandler(Book book, boolean function) {
            this.book = book;
            this.function = function;
        }

        // MODIFIES: this
        // EFFECTS: if function is borrow; user borrows book, creates JDialog affirming that the book was borrowed.
        // returns to search menu in all cases.
        @Override
        public void actionPerformed(ActionEvent e) {
            if (function == BORROW_BUTTON_FUNCTION) {
                handleBorrow(book);
            }
            dialog.dispose();
            searchMenu();
        }
    }

    private class SearchMenuClickHandler implements ActionListener {
        // a click handler for the books in the search menu.

        private Book book;

        // EFFECTS: creates a click handler for the search menu to direct the user to the book menu for book
        public SearchMenuClickHandler(Book book) {
            this.book = book;
        }

        @Override
        // MODIFIES: this
        // EFFECTS: on button click opens book menu for book
        public void actionPerformed(ActionEvent e) {
            bookMenu(book);
        }
    }

    private class MenuClickHandler implements ActionListener {
        // Click handler for buttons in main menu.
        // Function corresponds to which button it is acting as the action listener for.

        private static final int SEARCH_BUTTON_FUNCTION = 0;
        private static final int QUIT_BUTTON_FUNCTION = 1;
        private static final int RETURN_BUTTON_FUNCTION = 2;
        private static final int REGISTER_BUTTON_FUNCTION = 3;

        private int function;

        // REQUIRES: function is one of SEARCH_BUTTON_FUNCTION, RETURN_BUTTON_FUNCTION or QUIT_BUTTON_FUNCTION
        // EFFECTS: creates a menu click handler for given button
        public MenuClickHandler(int function) {
            this.function = function;
        }

        @Override
        // MODIFIES: this
        // EFFECTS: open menu corresponding to button function
        public void actionPerformed(ActionEvent e) {
            switch (function) {
                case SEARCH_BUTTON_FUNCTION:
                    searchMenu();
                    break;
                case RETURN_BUTTON_FUNCTION:
                    returnMenu();
                    break;
                case QUIT_BUTTON_FUNCTION:
                    saveMenu();
                    break;
                case REGISTER_BUTTON_FUNCTION:
                    registerBookMenu();
                    break;
            }
        }
    }


}
