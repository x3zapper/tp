package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.transformation.SortedList;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;


public class SortCommandTest {

    private Model model;
    private Model expectedModel;
    private AddressBook sortedAddrBookByNameAsc;
    private AddressBook sortedAddrBookByDateAsc;
    private AddressBook sortedAddrBookByNameDsc;
    private AddressBook sortedAddrBookByDateDsc;

    @BeforeEach
    public void setUp() {
        sortedAddrBookByNameAsc = new AddressBook();
        sortedAddrBookByNameAsc.addPerson(ALICE);
        sortedAddrBookByNameAsc.addPerson(BENSON);
        sortedAddrBookByNameAsc.addPerson(CARL);
        sortedAddrBookByNameAsc.addPerson(DANIEL);
        sortedAddrBookByNameAsc.addPerson(ELLE);
        sortedAddrBookByNameAsc.addPerson(FIONA);

        sortedAddrBookByNameDsc = new AddressBook();
        sortedAddrBookByNameDsc.addPerson(FIONA);
        sortedAddrBookByNameDsc.addPerson(ELLE);
        sortedAddrBookByNameDsc.addPerson(DANIEL);
        sortedAddrBookByNameDsc.addPerson(CARL);
        sortedAddrBookByNameDsc.addPerson(BENSON);
        sortedAddrBookByNameDsc.addPerson(ALICE);

        sortedAddrBookByDateAsc = new AddressBook();
        sortedAddrBookByDateAsc.addPerson(CARL);
        sortedAddrBookByDateAsc.addPerson(ALICE);
        sortedAddrBookByDateAsc.addPerson(BENSON);
        sortedAddrBookByDateAsc.addPerson(DANIEL);
        sortedAddrBookByDateAsc.addPerson(ELLE);
        sortedAddrBookByDateAsc.addPerson(FIONA);

        sortedAddrBookByDateDsc = new AddressBook();
        sortedAddrBookByDateDsc.addPerson(FIONA);
        sortedAddrBookByDateDsc.addPerson(ELLE);
        sortedAddrBookByDateDsc.addPerson(DANIEL);
        sortedAddrBookByDateDsc.addPerson(BENSON);
        sortedAddrBookByDateDsc.addPerson(ALICE);
        sortedAddrBookByDateDsc.addPerson(CARL);

        model = new ModelManager(sortedAddrBookByDateDsc, new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_typicalAddrBookSorted_success() {

        // Commented to show that test cases like these will not work, because the underylying address book is different
        // expectedModel.setAddressBook(sortedAddrBookByNameAsc);
        // assertCommandSuccess(new SortCommand("name"), model, SortCommand.MESSAGE_SUCCESS, expectedModel);

        SortCommand sortCommand = new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING);
        sortCommand.execute(model);
        SortedList<Person> modelSL = new SortedList<Person>(model.getSortedPersonList());
        assertEquals(modelSL, new SortedList<Person>(sortedAddrBookByNameAsc.getPersonList()));

        sortCommand = new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_ASCENDING);
        sortCommand.execute(model);
        modelSL = new SortedList<Person>(model.getSortedPersonList());
        assertEquals(modelSL, new SortedList<Person>(sortedAddrBookByDateAsc.getPersonList()));
    }

    @Test
    public void execute_listIsDoubleSorted_showsSameAsSingleSort() {
        // sort once, will be sorted again when assert command success is called
        model.updateSortComparator(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING);
        model.updateSortComparator(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING);

        // only sorts once
        expectedModel.updateSortComparator(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING);

        assertCommandSuccess(new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING),
                model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_emptyListSorted_showsEmptyList() {
        // empty list
        model.setAddressBook(new AddressBook());
        showNoPerson(model);

        // empty list
        expectedModel.setAddressBook(new AddressBook());
        showNoPerson(expectedModel);

        assertCommandSuccess(new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING),
                model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_emptyFilteredListSorted_showsEmptyList() {
        // empty filtered List
        showNoPerson(model);

        // empty filtered List
        showNoPerson(expectedModel);

        assertCommandSuccess(new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING),
                model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     * Taken from DeleteCommandTest
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);
        assertTrue(model.getSortedPersonList().isEmpty());
    }


}
