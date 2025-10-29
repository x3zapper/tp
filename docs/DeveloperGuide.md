---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Filter feature
The filter command allows for the user to filter their contacts based on specified tags (up to 10).

<puml src="diagrams/FilterClassDiagram.puml" alt="FilterClassDiagram" />

### Find feature with multiple search modes

The find command allows users to search for contacts by name using three different search modes: relaxed (default), strict, and fuzzy. Each mode provides different matching behavior to suit various search scenarios.

#### Implementation

The find feature is implemented through three main components:

1. **FindCommandParser** - Parses user input and extracts search mode and keywords
2. **FindCommand** - Executes the search based on the parsed predicate
3. **NameContainsKeywordsPredicate** - Implements the matching logic for each mode

<puml src="diagrams/FindParserClassDiagram.puml" alt="FindParserClassDiagram" />

#### Parsing search modes

The `FindCommandParser` supports flexible syntax where the mode flag `s/MODE` can be placed either at the beginning or end of the command:

* `find s/1 alex` - Mode flag at beginning
* `find alex s/1` - Mode flag at end

**Mode values:**
* `s/0` - Relaxed mode (default): Partial substring matching
* `s/1` - Strict mode: Full word matching only  
* `s/2` - Fuzzy mode: Returns up to 5 closest matches using Levenshtein distance

**Parsing logic:**

1. If arguments start with `s/`, `extractSearchModeFromPrefix()` is called:
   - Splits arguments by first whitespace
   - Extracts first mode value (e.g., "1" from "s/1")
   - Remaining text becomes keywords
   - Any subsequent `s/X` patterns are treated as keywords

2. Otherwise, `extractSearchModeFromSuffix()` is called:
   - Uses `ArgumentTokenizer` to find all `s/` occurrences
   - If multiple mode flags exist, **last value wins** via `getValue(PREFIX_SEARCH_MODE)`
   - Preamble becomes keywords

**Example parsing scenarios:**

| Command | Mode Used | Keywords | Notes |
|---------|-----------|----------|-------|
| `find alex s/1` | Strict (`s/1`) | `["alex"]` | Last mode wins |
| `find s/2 alex` | Fuzzy (`s/2`) | `["alex"]` | First mode at beginning |
| `find alex s/0 s/1 s/2` | Fuzzy (`s/2`) | `["alex"]` | Last mode wins (not at beginning) |
| `find s/1 alex s/2` | Strict (`s/1`) | `["alex", "s/2"]` | First mode wins, `s/2` becomes keyword |

#### Search mode behaviors

The `FindCommand#execute()` method checks the predicate mode and delegates to either `FindCommand#executeNormalSearch()` for relaxed and strict modes, or `FindCommand#executeFuzzySearch()` for fuzzy mode.

**Relaxed Mode (s/0 - Default):**

The `NameContainsKeywordsPredicate#test()` performs partial substring matching by checking if the lowercase name contains the lowercase keyword. For example, `find Yeo` matches "Alex Yeoh", "Yeong", or "George" (contains "eo").

**Strict Mode (s/1):**

Uses `StringUtil#containsWordIgnoreCase()` to perform full word matching by splitting the name into words and checking for exact matches. The implementation splits the sentence by whitespace and uses a stream to check if any word matches the keyword (case-insensitive). For example, `find Yeoh s/1` matches "Alex Yeoh" but NOT "Yeohng" or "Yeo".

**Fuzzy Mode (s/2):**

The `FindCommand#executeFuzzySearch()` method ranks all persons by their minimum Levenshtein distance to the keywords using `FindCommand#rankPersonsByDistance()`, which sorts persons by distance in ascending order and limits results to the top 5 matches. The sorted list may not be preserved in the displayed order in the UI.

#### Levenshtein Distance Algorithm

The fuzzy search mode uses the Levenshtein distance algorithm to measure similarity between strings. The Levenshtein distance is the minimum number of single-character edits (insertions, deletions, or substitutions) required to change one string into another.

The algorithm is implemented in `StringUtil#getLevenshteinDistance()` using dynamic programming with a 2D matrix approach:

1. A matrix `dp[i][j]` is created where `i` and `j` represent positions in the two strings
2. Base cases are initialized: `dp[i][0] = i` (delete all characters from s1) and `dp[0][j] = j` (insert all characters from s2)
3. For each cell, if characters match, no operation is needed: `dp[i][j] = dp[i-1][j-1]`
4. Otherwise, the minimum cost of three operations is taken: substitution `dp[i-1][j-1]`, deletion `dp[i-1][j]`, or insertion `dp[i][j-1]`, each plus 1
5. The final value `dp[len1][len2]` gives the minimum edit distance

**How fuzzy matching uses Levenshtein distance:**

The `NameContainsKeywordsPredicate#getMinimumDistance()` method calculates the minimum distance for each person by:
1. Splitting the person's name into individual words
2. Comparing each word against all keywords using `StringUtil#getLevenshteinDistance()`
3. Returning the smallest distance found

This distance is then used by `FindCommand#rankPersonsByDistance()` to sort all persons by distance and select the top 5 closest matches using a stream with `Comparator.comparingInt()` and `limit(FUZZY_RESULT_LIMIT)` where `FUZZY_RESULT_LIMIT = 5`.

For example, comparing "Alek" to "Alex" results in a distance of 1 (one substitution: 'k' → 'x'). The matrix computation produces:

|     |   | a | l | e | x |
|-----|---|---|---|---|---|
|     | 0 | 1 | 2 | 3 | 4 |
| a   | 1 | 0 | 1 | 2 | 3 |
| l   | 2 | 1 | 0 | 1 | 2 |
| e   | 3 | 2 | 1 | 0 | 1 |
| k   | 4 | 3 | 2 | 1 | 1 |

The final distance is 1, making "Alek" a close match that would appear in fuzzy search results.

The `NameContainsKeywordsPredicate` defines `FUZZY_MATCH_THRESHOLD = 2` for filtering during the `test()` method, though in fuzzy mode execution, the top 5 closest matches are returned regardless of their distance.

#### Design considerations:

**Aspect: Mode flag position handling:**

* **Alternative 1 (current choice):** Supports both prefix and suffix positions.
  * Pros: Flexible syntax, user-friendly for different typing patterns.
  * Cons: More complex parsing logic, potential ambiguity with multiple flags.

* **Alternative 2:** Require mode flag at fixed position (e.g., always at end).
  * Pros: Simpler parsing, no ambiguity.
  * Cons: Less flexible, may not match user intuition.

**Aspect: Multiple mode flag resolution:**

* **Alternative 1 (current choice):** Last mode wins (when not at beginning), first mode wins (when at beginning).
  * Pros: Predictable behavior, documented in user guide.
  * Cons: May confuse users who accidentally type multiple flags.

* **Alternative 2:** Throw error on multiple mode flags.
  * Pros: Prevents ambiguity, forces correct input.
  * Cons: Less forgiving, requires user to fix command.

**Aspect: Fuzzy search result limit:**

* **Alternative 1 (current choice):** Fixed limit of 5 results.
  * Pros: Prevents overwhelming user with too many results, fast processing.
  * Cons: May miss relevant matches if more than 5 similar names exist.

* **Alternative 2:** Configurable result limit.
  * Pros: User can adjust based on needs.
  * Cons: Added complexity, may impact performance with large limits.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* IT startup founders or small business owners who handle client and vendor communication personally
* has a need to manage and categorize different types of contacts (potential clients, contracted clients, vendors)
* lacks experience in systematic contact management but wants to organize business relationships effectively
* prefer desktop apps over other types for data security and offline access
* can type fast and prefers typing to mouse interactions for efficiency
* is reasonably comfortable using CLI apps

**Value proposition**: helps small business owners organize and categorize their client and vendor contacts more systematically than a typical mouse/GUI driven app, enabling better relationship management without the complexity of enterprise CRM systems


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                 | So that I can…​                                                        |
|----------|--------------------------------------------|------------------------------|------------------------------------------------------------------------|
| `* * *`  | new user                                   | see usage instructions       | refer to instructions when I forget how to use the App                 |
| `* * *`  | user                                       | add a new person             | store contact details for future reference                             |
| `* * *`  | user                                       | delete a person              | remove entries that I no longer need                                   |
| `* * *`  | user                                       | find a person by name        | locate details of persons without having to go through the entire list |
| `* * *`  | user                                       | view all my contacts         | see an overview of all stored contacts                                 |
| `* * *`  | user                                       | filter contacts by tags      | focus on specific groups like clients or vendors                       |
| `* * *`  | user                                       | exit the application         | close the app when I'm done using it                                   |
| `* * *`  | user                                       | have my data automatically saved | not lose my contact information when the app closes                    |
| `* * *`  | user                                       | use consistent commands      | interact with the app reliably and predictably                        |
| `* *`    | startup founder                            | add custom tags to contacts  | categorize them by type for better organization                        |
| `* *`    | tech business owner                        | mark contacts as favorites   | quickly access my most important clients or vendors                   |
| `*`      | normal user                                | sort my contacts             | locate a person easily without scrolling through the full list        |
| `*`      | small business owner                       | backup my contact information | restore my data in case I lose the current information                |

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use Case: UC01 - Add a New Contact**

**MSS**

1. User chooses to add a new contact.
2. User enters contact details (e.g., name, phone, email, address).
3. AddressBook parses the contact details.
4. AddressBook validates each field details.
5. AddressBook checks for duplicates.
6. AddressBook saves the new contact and confirms successful addition.

    Use case ends.

**Extensions**

* 3a. User enters invalid data for any field.

    * 3a1. AddressBook displays appropriate error message for the fields.
    * 3a2. User re-enters the corrected data.
    * Steps 3a1–3a2 repeat until all data are valid.
    * Use case resumes from step 4.

* 3b. User specified missing field or wrong field:

    * 3b1. AddressBook displays error.
    * 3b2. AddressBook displays required field.
    * 3b3. User corrects the command.
    * Steps repeat until all single-valued fields are specified correctly.
    * Use case resumes from step 4.


* 4a. Duplicate contact detected.

    * 4a1. AddressBook displays a console message indicating that the contact already exists.
    * Use case ends.

**Use case: UC02 - List Contacts**

**MSS**

1. User requests to list all contact.
2. AddressBook parses the command.
3. AddressBook validates the command format.
4. AddressBook retrieves all saved contacts.
5. AddressBook displays all contacts.
6. AddressBook displays a console message indicating the number of contacts displayed.

    Use case ends.

**Extensions**

* 5a. No contacts stored in AddressBook
  * 5a1. AddressBook will not display any contacts.
  * 5a2. AddressBook displays a console message indicating AddressBook is empty.
  * Use case ends.

**Use case: UC03 - Add Tags to Contact**

**MSS**

1. User includes tags when <u>adding a new contact (UC01).</u>
2. AddressBook parses the tag values.
3. AddressBook validates the tag values.
4. AddressBook associates valid tags with the contact.

    Use case ends.

**Extensions**

* 3a. Tag value is invalid
  * 3a1. AddressBook displays error message and requests for correction.
  * 3a2. User corrects or remove invalid tags.
  * Steps 3a1-3a2 repeat until all tags are valid.
  * Use case resumes from step 4.


**Use case: UC04 - Filter Contacts by Tags**

**MSS**

1. User requests to filter contacts with specified tags.
2. AddressBook parses the tag values.
3. AddressBook validates the tag values.
4. AddressBook retrieves all contacts with the specified tags.
5. AddressBook <u>lists these filtered contacts (UC02).</u>
6. AddressBook displays a console message indicating the number of filtered contacts displayed.

    Use case ends.

**Extensions**

* 3a. No tag provided in the command.
  * 3a1. AddressBook displays an error message.
  * Use case ends.
* 3b. Tag value is invalid.
  * 3b1. AddressBook will display an error message and request for correction.
  * 3b2. User corrects the tag value.
  * Step 3b1-3b2 repeats until all tags are valid.
  * Use case resumes from step 4.
* 3c. More than 10 tags specified.
  * 3c1. AddressBook will display an error message and request for correction.
  * 3c2. User corrects the command.
  * Steps 3c1-3c2 repeats until tags are less than 10.
  * Use case resumes from step 4.

* 4a. No contact matches the specified tags.
  * 4a1. AddressBook will not display any contacts
  * 4a2. AddressBook will display a console message indicating no contacts found with the specified tags.
  * Use case ends.


**Use case: UC05 - Delete a person**

**MSS**

1.  User requests to list persons
2.  AddressBook shows a list of persons
3.  User requests to delete a specific person in the list
4.  AddressBook deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Backup (save to existing local storage file) and restore (read from existing local storage file with correct data format) operations must complete within 5 seconds for a large (1000) contact list size.
5.  Application startup time should not exceed 10 seconds on a machine with standard business laptop specifications (16GB ram, SSD storage, quad-core CPU)
    In this case, application startup is from running it in bash to having the main UI window appear --> i.e. we do not wait until the all the contacts are shown
6.  Memory usage should not exceed 300MB for normal usage (i.e. Each field should not be > 30 characters, less than 30 contacts) on the maximum dataset size
7.  CRUD operations on **each** contact must be completed within 200ms under normal (0-30) dataset size
8.  Search and filtering operations across a large (1000) contact list size must return results within 300ms
9.  No external connectivity should be required, all features must be available offline

### Glossary

* **CLI**: Command Line Interface - a text-based user interface used to interact with the application
* **Client**: A person or organization that purchases goods or services from the business
* **Contact**: A person whose information is stored in the address book, which can be a client, vendor, or other business relationship
* **CRB**: Customer Relations Book - the name of this application for managing business contacts
* **Filter**: A feature that displays only contacts matching specified tag criteria
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Potential Client**: A contact who may become a client in the future but has not yet made a purchase
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Tag**: A label used to categorize contacts (e.g., "client", "vendor", "VIP")
* **Vendor**: A person or organization that supplies goods or services to the business

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
