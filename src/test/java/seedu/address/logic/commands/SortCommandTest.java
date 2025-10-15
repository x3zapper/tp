package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;


public class SortCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_typicalAddrBookSorted_success() {
        AddressBook unsortedAddrBook = new AddressBook();
        unsortedAddrBook.addPerson(CARL);
        unsortedAddrBook.addPerson(ALICE);
        unsortedAddrBook.addPerson(BENSON);

        model.setAddressBook(unsortedAddrBook);

        AddressBook sortedAddrBook = new AddressBook();
        sortedAddrBook.addPerson(ALICE);
        sortedAddrBook.addPerson(BENSON);
        sortedAddrBook.addPerson(CARL);

        expectedModel.setAddressBook(sortedAddrBook);
        assertCommandSuccess(new SortCommand(), model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsDoubleSorted_showsSameAsSingleSort() {
        // sort once, will be sorted again when assert command success is called
        model.sortFilteredPersonList();

        // only sorts once
        expectedModel.sortFilteredPersonList();

        assertCommandSuccess(new SortCommand(), model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_emptyListSorted_showsEmptyList() {
        // empty list
        model.setAddressBook(new AddressBook());

        // empty list
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new SortCommand(), model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_emptyFilteredListSorted_showsEmptyList() {
        // empty filtered List
        showNoPerson(model);

        // empty filtered List
        showNoPerson(expectedModel);

        assertCommandSuccess(new SortCommand(), model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     * Taken from DeleteCommandTest
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }


}
