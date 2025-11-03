---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Customer Relation Book Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
* For the detailed documentation of the `AddressBook-Level3` project, see the [Address Book Product Website](https://se-education.org/addressbook-level3).
* This project is a part of the se-education.org initiative. If you would like to contribute code to this project, see [se-education.org](https://se-education.org/#contributing-to-se-edu) for more info.
* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)

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

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103-F13-3/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103-F13-3/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
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

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103-F13-3/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103-F13-3/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103-F13-3/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

#### Updating the Help Window

The help window displays a list of available commands with usage details. To update the command list:

1. **Locate the FXML file**: Navigate to `src/main/resources/view/HelpWindow.fxml`
2. **Find the command list section**: Look for the `ScrollPane` with `id="help-command-scroll"` containing a `VBox` with `id="help-command-list"`
3. **Add/modify commands**: Each command is represented by a `VBox` with `styleClass="command-section"`. The structure includes:
   - Command title (Label with `styleClass="command-title"`)
   - Command description (one or more Labels with `styleClass="command-description"`)
   - Usage format (Label showing the command syntax)
4. **Alternate styling**: Use `command-section-odd` and `command-section-even` for alternating background colors
5. **Update window settings**: The help window size and position are persisted in `GuiSettings`. Default dimensions are:
   - Width: 600px (minimum: 450px)
   - Height: 500px (minimum: 400px)

Example command entry structure:
```xml
<VBox spacing="4.0" styleClass="command-section, command-section-odd">
  <children>
    <Label styleClass="command-title" text="command_name" ... />
    <Label styleClass="command-description" text="Brief description" ... />
    <Label styleClass="command-description" text="Usage: command_name PARAMS" ... />
  </children>
  <padding>...</padding>
</VBox>
```

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103-F13-3/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

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
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103-F13-3/tp/tree/master/src/main/java/seedu/address/model/Model.java)

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

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103-F13-3/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

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

---

### Filter feature
The filter command allows for the user to filter their contacts based on specified tags (up to 10).

#### Feature Overview
The `filter` command filters and lists all persons in the address book whose tags contain any of the specified keywords.
Tag matching is **case-sensitive**.
For example, entering `filter t/friend` will display only the contacts with the tag “friend”.

#### Implementation
The `FilterCommand` feature is implemented using the **Command design pattern**, similar to other commands in the Logic component.

When the user executes the command:

1. The `FilterCommandParser` parses the user input into individual tag keywords.
2. A `TagContainsKeywordsPredicate` object is created using these keywords.
3. A `FilterCommand` is constructed with this predicate and executed.
4. Inside the `execute(Model model)` method, the model’s `updateFilteredPersonList(predicate)` is called.
5. The UI automatically refreshes to display only persons who match the predicate condition.

#### Main Classes Involved
- `FilterCommand`
- `FilterCommandParser`
- `TagContainsKeywordsPredicate`
- `Model` (interface)
- `ModelManager` (concrete implementation)

#### Key Code Snippet
```java
@Override
public CommandResult execute(Model model) {
    requireNonNull(model);
    model.updateFilteredPersonList(predicate);
    return new CommandResult(
        String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
}
```

#### Class Diagram
The class diagram below illustrates the main classes involved in the `FilterCommand` feature and their relationships.

<puml src="diagrams/FilterClassDiagram.puml" alt="FilterClassDiagram" />

#### Parsing Logic

The `FilterCommandParser` is responsible for parsing the user input for the `filter` command and creating a corresponding `FilterCommand` object.

**Steps of Parsing Logic:**

1. **Trim and validate input**
    - The parser first removes leading and trailing whitespace from the input string.
    - If the input is empty, a `ParseException` is thrown, indicating invalid command format.

2. **Split input into keywords**
    - The remaining string is split by whitespace to obtain individual tag keywords.
    - If more than 10 tags are provided, a `ParseException` is thrown to enforce the tag limit.

3. **Validate each tag**
    - Each keyword is checked against the `Tag.isValidTagName()` method.
    - Invalid tags (non-alphanumeric) trigger a `ParseException` with a helpful error message.

4. **Create Predicate**
    - A `TagContainsKeywordsPredicate` object is instantiated with the validated list of tags.

5. **Return FilterCommand**
    - A new `FilterCommand` is created using the predicate and returned for execution.

#### TagContainsKeywordsPredicate

The `TagContainsKeywordsPredicate` class implements the `Predicate<Person>` interface and is responsible for **testing whether a person contains all of the specified tags**.

**Responsibilities:**

- Encapsulates the list of tag keywords used for filtering.
- Provides a `test(Person person)` method that returns `true` if the person contains **all tags** in the keyword list.
- Supports **case-sensitive matching** for tags.
- Provides a `getTags()` method to retrieve the list of keywords, which can be used for displaying messages to the user.

#### Sequence Diagram

The sequence diagram below illustrates the interactions between the user, logic, parser, command, and model when executing the `filter` command. Using an example of command "filter friend VIP".

<puml src="diagrams/FilterSequenceDiagram.puml" alt="FilterSequenceDiagram" />

---

### Find feature with multiple search modes

The find command allows users to search for contacts by name using three different search modes: relaxed (default), strict, and fuzzy. Each mode provides different matching behavior to suit various search scenarios.

#### Implementation

The find feature is implemented through three main components:

1. **FindCommandParser** - Parses user input and extracts search mode and keywords
2. **FindCommand** - Executes the search based on the parsed predicate
3. **NameContainsKeywordsPredicate** - Implements the matching logic for each mode

<puml src="diagrams/FindParserClassDiagram.puml" alt="FindParserClassDiagram" />

#### Parsing search modes

The `FindCommandParser` supports flexible syntax where the mode flag `s/MODE` can be placed **either** at the beginning (before keywords) **or** at the end (after keywords) of the command, but not both:

* `find s/1 alex` - Mode flag at beginning (before keywords)
* `find alex s/1` - Mode flag at end (after keywords)

**Mode values:**
* `s/0` - Relaxed mode (default): Partial substring matching
* `s/1` - Strict mode: Full word matching only
* `s/2` - Fuzzy mode: Returns up to 5 closest matches using Levenshtein distance

**Parsing logic:**

1. **Mode flag at beginning:** If arguments start with `s/`, `extractSearchModeFromPrefix()` is called:
   - Splits arguments by first whitespace
   - Extracts first mode value (e.g., "1" from "s/1")
   - Remaining text becomes keywords
   - Any subsequent `s/MODE` patterns in the remaining text are treated as keywords (not as mode flags)

2. **Mode flag at end:** Otherwise, `extractSearchModeFromSuffix()` is called:
   - Uses `ArgumentTokenizer` to find all `s/` occurrences
   - If multiple mode flags exist, **last value wins** via `getValue(PREFIX_SEARCH_MODE)`
   - Preamble (text before mode flags) becomes keywords
   - All `s/MODE` patterns except the last one are treated as keywords

**Example parsing scenarios:**

| Command | Mode Used | Keywords | Notes |
|---------|-----------|----------|-------|
| `find alex s/1` | Strict (`s/1`) | `["alex"]` | Mode flag at end: last mode wins |
| `find s/2 alex` | Fuzzy (`s/2`) | `["alex"]` | Mode flag at beginning: uses first mode |
| `find alex s/0 s/1 s/2` | Fuzzy (`s/2`) | `["alex"]` | Mode flags at end: last mode wins, others become keywords |
| `find s/1 alex s/2` | Strict (`s/1`) | `["alex", "s/2"]` | Mode flag at beginning: only first recognized, `s/2` becomes keyword |

#### Search mode behaviors

The `FindCommand#execute()` method checks the predicate mode and delegates to either `FindCommand#executeNormalSearch()` for relaxed and strict modes, or `FindCommand#executeFuzzySearch()` for fuzzy mode.

**Relaxed Mode (s/0 - Default):**

The `NameContainsKeywordsPredicate#test()` performs partial substring matching by checking if the lowercase name contains the lowercase keyword. For example, `find Yeo` matches "Alex Yeoh", "Yeong", or "George" (contains "eo").

**Strict Mode (s/1):**

Uses `StringUtil#containsWordIgnoreCase()` to perform full word matching by splitting the name into words and checking for exact matches. The implementation splits the sentence by whitespace and uses a stream to check if any word matches the keyword (case-insensitive). For example, `find Yeoh s/1` matches "Alex Yeoh" but NOT "Yeohng" or "Yeo".

**Fuzzy Mode (s/2):**

The `FindCommand#executeFuzzySearch()` method ranks all persons by their minimum Levenshtein distance to the keywords using `FindCommand#rankPersonsByDistance()`, which sorts persons by distance in ascending order and limits results to the top 5 matches. The sorted list may not be preserved in the displayed order in the UI.

**Important Note on Multi-Keyword Fuzzy Search:** The fuzzy search compares each word in a person's name individually against each keyword and returns the minimum distance found across all comparisons. This means when using multiple keywords (e.g., `find s/2 Jason Lim`), the search performs an OR operation on individual word matches rather than matching the full phrase. For example, a contact named "Jason" will have distance 0 to the keyword "Jason", and a contact named "Jason Lim A A" will also have distance 0 (matching "Jason" or "Lim" individually). This can lead to unexpected results where shorter names rank equally with longer names that contain the same words. Fuzzy search is most effective with single keywords for typo tolerance.

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

* **Alternative 3 (future improvement):** Smart threshold-based matching instead of fixed limit.
  * Implementation: Use an intelligent algorithm to dynamically determine the similarity threshold and show all matches that meet it, rather than limiting to exactly 5 results. The threshold adapts based on the quality and distribution of matches found.
  * Pros: More intuitive results - shows all close matches without arbitrary cutoffs. Better handles cases with many similar names or very few close matches. Adapts to different search scenarios automatically.
  * Cons: Result count varies, which may be unpredictable for users. More complex algorithm required.

---

### Sort feature
The sort command allows the user to change the sort ordering of the conctact list displayed.
Class diagram:
<puml src="diagrams/SortClassDiagram.puml" alt="SortClassDiagram" />

#### Implementation
Similar to other commands, the Sort feature has a parser and a command, in:
* `SortCommand`
* `SortCommandParser`
Which updates the `ModelManager`'s `sortedPersons` list which is the list that the UI calls upon to show using the `getSortedPersonList()` function of `ModelManager`. 
To clarify, `sortedPersons` is a `SortedList` which the comparator can be updated with `updateSortComparator(Comparator<Person>)` function of `ModelManager`.

It is to note that `SortCommand` only has four types of Comparators defined as static properties
* `COMPARATOR_SORT_PERSONS_BY_NAME_ASCENDING`
* `COMPARATOR_SORT_PERSONS_BY_NAME_DESCENDING`
* `COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_ASCENDING`
* `COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_DESCENDING`
These four comparators are the only comparators that `SortCommandParser` can instantiate a `SortCommand` with.
For example: `new SortCommand(SortCommand.COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_ASCENDING)` and as such the only four comparators that `sortedPersons` can be updated with.

#### Parsing 
`SortCommandParser` has two and only two, mandatory flags that require an accompanying value
* `st/` - short for "sort type" which values can only be 
  * "dateadded" - for sorting by the `dateAdded` property of `Person` which is a `DateAdded` class. Note that the `DateAdded` class uses the `Instant` datatype as the backing value
  * "name" - for sorting by the `name` property of `Person` which is a `Name` class. Note that the `Name` class uses the `String` datatype as the backing value
* `so/` - short for "sort order" which values can only be
  * "asc" - short for ascending order
  * "dsc" - short for descending order

Said flags can be in any order as `SortCommandParser` uses `ArgumentTokenizer` to find occurrences.

**Comparator Details**
It is of note that `COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_DESCENDING` and `COMPARATOR_SORT_PERSONS_BY_NAME_DESCENDING` are just `.reversed()` versions of their ascending versions.
Although user's cannot directly edit a `Person`'s `dateAdded` through the `edit` command, a user can edit the `dateAdded` value through the JSON so that multiple contacts have the same `dateAdded` value.
As this would cause some issues with sorting, `COMPARATOR_SORT_PERSONS_BY_DATE_ADDED_ASCENDING` has a tie-breaker which uses the `Person`'s `name`.
Since CRB currently does not allow for `Person`s of the same `name` (contact list gets reset), this is sufficient.


---

### Command box history feature

This section documents the command-history feature used by `CommandBox`.

#### Feature Overview

The command-history feature provides a terminal-like experience where previously executed commands can be navigated using the Up and Down arrow keys. It supports:
* Browsing older commands with `UP` and newer commands with `DOWN`.
* Preserving the currently typed (partial) input when the user begins browsing history and restoring it when the user navigates back to the most recent position (so the user can continue editing). 
* Preventing duplicate consecutive history entries (the same command repeated in succession). 
* Limiting the history size (e.g. 100 entries) to avoid unbounded memory growth.

Example interaction flow:
* Type `add Alice` and press `Enter` -> command is executed and stored in history. 
* Type `del` (partial), press `UP` -> previous command shown, partial `del` saved. 
* Press `DOWN` until out of history -> `del` restored for continued editing.

#### Implementation

The feature is split into two components:
1. `CommandBox` (UI layer)
  * Handles user keyboard input and UI updates (TextField contents & caret position). 
  * Delegates history navigation to `CommandHistory`. 
  * Uses a `CommandExecutor` functional interface (provided by `MainWindow`) to execute commands.
2. `CommandHistory` (logic layer / helper class)
  * Encapsulates history storage (a `List<String>`), the browsing index, and a saved partial command. 
  * Exposes:
    * `boolean add(String commandText)` - store a new command, trims history and avoids duplicates. 
    * `String getPrevious(String currentInput)` - return older command; save current partial input when browsing starts. 
    * `String getNext(String currentInput)` - return newer command or restore saved partial input when leaving history. 
    * `void reset()` - clear browsing state. 
    * `boolean isEmpty()` - check if history is empty.

Flow when user presses Up/Down arrow keys:
1. `CommandBox` intercepts `KeyEvent` for `UP`/`DOWN`. 
2. `CommandBox` calls `commandHistory.getPrevious(commandTextField.getText())` or `commandHistory.getNext(commandTextField.getText())`. 
3. `CommandHistory` returns the appropriate string. 
4. `CommandBox` sets the `TextField` text and positions the caret at the end.

Flow when user presses Enter:
1. `CommandBox.handleCommandEntered() ` calls `commandHistory.add(commandText)`. 
2. `commandHistory.add()` checks if:
  1. New command is a duplicate of latest entry, if so do not add
  2. History is full, if so remove the oldest entry
3. `commandHistory.add()` internally calls `reset()` to ensure browsing state is cleared.

#### List Access Safety
* `getPrevious` and `getNext` perform empty-history checks first and return safely without indexing.
* `historyIndex` is always maintained within the range `HISTORY_INACTIVE` (−1) or valid list indices 0 to `history.size()-1`.
* Note: The code assumes single-threaded use on the JavaFX Application Thread; if accessed from other threads concurrently, external synchronization is required.

#### Class Diagram
<puml src="diagrams/CommandHistoryClassDiagram.puml" alt="CommandHistoryClassDiagram" />

#### Sequence Diagram - Navigation (Up/Down)
<puml src="diagrams/CommandHistorySequenceDiagram_Navigation.puml" alt="CommandHistoryNavigationSequenceDiagram" />

#### Sequence Diagram - Execute (Enter)
<puml src="diagrams/CommandHistorySequenceDiagram_Execute.puml" alt="CommandHistoryExecuteSequenceDiagram" />

#### Possible Future Development Ideas
* Persistence - Command history can be written to a save file when exiting the application and restored upon next launch

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` - Saves the current address book state in its history.
* `VersionedAddressBook#undo()` - Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` - Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial CustomerRelationBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite - it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone CustomerRelationBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …` command. This is the behavior that most modern desktop applications follow.

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

_To be implemented in the future_


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

| Priority | As a …                                    | I want to …                 | So that I can…                                                        |
|----------|--------------------------------------------|------------------------------|------------------------------------------------------------------------|
| `* * *`  | new user                                   | see usage instructions       | refer to instructions when I forget how to use the App                 |
| `* * *`  | user                                       | add a new person             | store contact details for future reference                             |
| `* * *`  | user                                       | edit contact information     | keep contact details up to date when they change                       |
| `* * *`  | user                                       | delete a person              | remove entries that I no longer need                                   |
| `* * *`  | user                                       | find a person by name        | locate details of persons without having to go through the entire list |
| `* * *`  | user                                       | view all my contacts         | see an overview of all stored contacts                                 |
| `* * *`  | user                                       | filter contacts by tags      | focus on specific groups like clients or vendors                       |
| `* * *`  | user                                       | clear all contacts           | start fresh with a clean address book when needed                      |
| `* * *`  | user                                       | exit the application         | close the app when I'm done using it                                   |
| `* * *`  | user                                       | have my data automatically saved | not lose my contact information when the app closes                    |
| `* * *`  | user                                       | use consistent commands      | interact with the app reliably and predictably                        |
| `* *`    | user                                       | add notes to contacts        | remember important details or context about each person                |
| `* *`    | user                                       | edit notes for contacts      | update or remove notes as circumstances change                         |
| `* *`    | user                                       | search with fuzzy matching   | find contacts even if I misspell their names                           |
| `* *`    | user                                       | search with strict matching  | find only exact name matches when I know the full name                 |
| `* *`    | user                                       | navigate command history     | quickly reuse previous commands without retyping them                  |
| `* *`    | user                                       | sort my contacts by name     | view contacts in alphabetical order for easier browsing                |
| `* *`    | user                                       | sort by date added           | see my newest or oldest contacts first                                 |
| `* *`    | startup founder                            | add custom tags to contacts  | categorize them by type for better organization                        |
| `* *`    | international business owner               | store timezone information   | know what time it is for my contacts in different regions              |
| `* *`    | tech business owner                        | mark contacts as favorites   | quickly access my most important clients or vendors                   |
| `*`      | small business owner                       | backup my contact information | restore my data in case I lose the current information                |

### Use cases

(For all use cases below, the **System** is the CustomerRelationBook and the **Actor** is the `user`, unless specified otherwise)

**Use Case: UC01 - Add a New Contact**

**MSS**

1. User chooses to add a new contact.
2. User enters contact details (e.g., name, phone, email, address).
3. CustomerRelationBook parses the contact details.
4. CustomerRelationBook validates each field details.
5. CustomerRelationBook checks for duplicates.
6. CustomerRelationBook saves the new contact and confirms successful addition.

    Use case ends.

**Extensions**

* 3a. User enters invalid data for any field.

    * 3a1. CustomerRelationBook displays appropriate error message for the fields.
    * 3a2. User re-enters the corrected data.
    * Steps 3a1–3a2 repeat until all data are valid.
    * Use case resumes from step 4.

* 3b. User specified missing field or wrong field:

    * 3b1. CustomerRelationBook displays error.
    * 3b2. CustomerRelationBook displays required field.
    * 3b3. User corrects the command.
    * Steps repeat until all single-valued fields are specified correctly.
    * Use case resumes from step 4.


* 4a. Duplicate contact detected.

    * 4a1. CustomerRelationBook displays a console message indicating that the contact already exists.
    * Use case ends.

**Use case: UC02 - List Contacts**

**MSS**

1. User requests to list all contact.
2. CustomerRelationBook parses the command.
3. CustomerRelationBook validates the command format.
4. CustomerRelationBook retrieves all saved contacts.
5. CustomerRelationBook displays all contacts.
6. CustomerRelationBook displays a console message indicating the number of contacts displayed.

    Use case ends.

**Extensions**

* 5a. No contacts stored in CustomerRelationBook
  * 5a1. CustomerRelationBook will not display any contacts.
  * 5a2. CustomerRelationBook displays a console message indicating CustomerRelationBook is empty.
  * Use case ends.

**Use case: UC03 - Add Tags to Contact**

**MSS**

1. User includes tags when <u>adding a new contact (UC01).</u>
2. CustomerRelationBook parses the tag values.
3. CustomerRelationBook validates the tag values.
4. CustomerRelationBook associates valid tags with the contact.

    Use case ends.

**Extensions**

* 3a. Tag value is invalid
  * 3a1. CustomerRelationBook displays error message and requests for correction.
  * 3a2. User corrects or remove invalid tags.
  * Steps 3a1-3a2 repeat until all tags are valid.
  * Use case resumes from step 4.


**Use case: UC04 - Filter Contacts by Tags**

**MSS**

1. User requests to filter contacts with specified tags.
2. CustomerRelationBook parses the tag values.
3. CustomerRelationBook validates the tag values.
4. CustomerRelationBook retrieves all contacts with the specified tags.
5. CustomerRelationBook <u>lists these filtered contacts (UC02).</u>
6. CustomerRelationBook displays a console message indicating the number of filtered contacts displayed.

    Use case ends.

**Extensions**

* 3a. No tag provided in the command.
  * 3a1. CustomerRelationBook displays an error message.
  * Use case ends.
* 3b. Tag value is invalid.
  * 3b1. CustomerRelationBook will display an error message and request for correction.
  * 3b2. User corrects the tag value.
  * Step 3b1-3b2 repeats until all tags are valid.
  * Use case resumes from step 4.
* 3c. More than 10 tags specified.
  * 3c1. CustomerRelationBook will display an error message and request for correction.
  * 3c2. User corrects the command.
  * Steps 3c1-3c2 repeats until tags are less than 10.
  * Use case resumes from step 4.

* 4a. No contact matches the specified tags.
  * 4a1. CustomerRelationBook will not display any contacts
  * 4a2. CustomerRelationBook will display a console message indicating no contacts found with the specified tags.
  * Use case ends.


**Use case: UC05 - Delete a person**

**MSS**

1.  User requests to list persons
2.  CustomerRelationBook shows a list of persons
3.  User requests to delete a specific person in the list
4.  CustomerRelationBook deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. CustomerRelationBook shows an error message.

      Use case resumes at step 2.


**Use case: UC06 - Edit a Contact**

**MSS**

1. User <u>lists contacts (UC02)</u> or <u>finds contacts (UC08)</u>.
2. CustomerRelationBook displays the list of contacts.
3. User requests to edit a specific contact with new field values.
4. CustomerRelationBook parses the edit command.
5. CustomerRelationBook validates the index and field values.
6. CustomerRelationBook updates the contact with new values.
7. CustomerRelationBook confirms successful update.

    Use case ends.

**Extensions**

* 3a. No index provided.
  * 3a1. CustomerRelationBook displays error message.
  * Use case ends.

* 5a. The given index is invalid.
  * 5a1. CustomerRelationBook displays error message.
  * Use case resumes at step 3.

* 5b. No fields to edit provided.
  * 5b1. CustomerRelationBook displays error message requiring at least one field.
  * Use case resumes at step 3.

* 5c. Field value is invalid.
  * 5c1. CustomerRelationBook displays error message for the specific field.
  * 5c2. User corrects the field value.
  * Steps 5c1-5c2 repeat until all fields are valid.
  * Use case resumes from step 6.


**Use case: UC07 - Clear All Contacts**

**MSS**

1. User requests to clear all contacts.
2. CustomerRelationBook parses the command.
3. CustomerRelationBook removes all contacts.
4. CustomerRelationBook confirms successful clearing.

    Use case ends.


**Use case: UC08 - Find Contacts by Name**

**MSS**

1. User requests to find contacts by name keywords.
2. CustomerRelationBook parses the keywords and search mode.
3. CustomerRelationBook validates the command format.
4. CustomerRelationBook searches for contacts matching the keywords.
5. CustomerRelationBook displays matching contacts.
6. CustomerRelationBook displays a console message indicating the number of contacts found.

    Use case ends.

**Extensions**

* 2a. No keywords provided.
  * 2a1. CustomerRelationBook displays error message.
  * Use case ends.

* 2b. Invalid search mode specified.
  * 2b1. CustomerRelationBook displays error message with valid modes.
  * Use case resumes at step 1.

* 4a. No contacts match the keywords.
  * 4a1. CustomerRelationBook displays empty list.
  * 4a2. CustomerRelationBook displays message indicating no contacts found.
  * Use case ends.


**Use case: UC09 - Sort Contacts**

**MSS**

1. User requests to sort contacts by specified criteria.
2. CustomerRelationBook parses sort type and sort order.
3. CustomerRelationBook validates the sort parameters.
4. CustomerRelationBook sorts the contact list accordingly.
5. CustomerRelationBook displays sorted contacts.
6. CustomerRelationBook confirms successful sorting.

    Use case ends.

**Extensions**

* 2a. Missing sort type or sort order.
  * 2a1. CustomerRelationBook displays error message with required parameters.
  * Use case ends.

* 3a. Invalid sort type specified.
  * 3a1. CustomerRelationBook displays error message with valid sort types (dateadded, name).
  * Use case resumes at step 1.

* 3b. Invalid sort order specified.
  * 3b1. CustomerRelationBook displays error message with valid sort orders (asc, dsc).
  * Use case resumes at step 1.


**Use case: UC10 - Add or Edit Note for Contact**

**MSS**

1. User <u>lists contacts (UC02)</u> or <u>finds contacts (UC08)</u>.
2. CustomerRelationBook displays the list of contacts.
3. User requests to add/edit note for a specific contact.
4. CustomerRelationBook parses the note command.
5. CustomerRelationBook validates the index and note content.
6. CustomerRelationBook updates the contact's note.
7. CustomerRelationBook confirms successful note update.

    Use case ends.

**Extensions**

* 3a. No index provided.
  * 3a1. CustomerRelationBook displays error message.
  * Use case ends.

* 5a. The given index is invalid.
  * 5a1. CustomerRelationBook displays error message.
  * Use case resumes at step 3.

* 5b. Note prefix missing.
  * 5b1. CustomerRelationBook displays error message.
  * Use case resumes at step 3.

* 5c. Empty note content (to delete note).
  * 5c1. CustomerRelationBook removes the contact's note.
  * 5c2. CustomerRelationBook confirms note deletion.
  * Use case ends.


**Use case: UC11 - View Help**

**MSS**

1. User requests to view help.
2. CustomerRelationBook parses the help command.
3. CustomerRelationBook opens help window.
4. Help window displays all available commands with usage details.

    Use case ends.

**Extensions**

* 3a. Help window is already open but minimized.
  * 3a1. User manually restores the minimized help window.
  * Use case resumes at step 4.

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

   2. Double-click the jar file.<br>
      Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.

### Viewing help

1. Opening the help window

   1. Test case: `help`<br>
      Expected: Help window opens showing scrollable list of all commands with usage details.

   2. Test case: Close help window and run `help` again<br>
      Expected: Help window reopens at the same size and position as before.

   3. Test case: Minimize help window and run `help`<br>
      Expected: Help window remains minimized. User must manually restore it.

### Adding a person

1. Adding a new contact

   1. Test case: `add n/John Doe p/98765432 e/johnd@example.com a/123 Street`<br>
      Expected: New contact added to the list. Success message displayed.

   2. Test case: `add n/John Doe p/98765432 e/johnd@example.com a/123 Street`<br>
      Expected: Error message indicating duplicate contact.

   3. Test case: `add n/Jane p/1234567 e/invalid a/456 Road`<br>
      Expected: Error message indicating invalid email format.

   4. Test case: `add n/Bob p/abc e/bob@example.com a/789 Ave`<br>
      Expected: Error message indicating invalid phone number.

   5. Test case: `add p/12345678 e/test@example.com a/Address`<br>
      Expected: Error message indicating missing name field.

### Listing all persons

1. Listing all contacts

   1. Test case: `list`<br>
      Expected: All contacts displayed. Message shows total number of contacts.

   2. Test case: `list extra parameters`<br>
      Expected: All contacts displayed (extra parameters ignored).

### Editing a person

1. Editing a contact while all persons are shown

   1. Prerequisites: List all persons using `list`. Multiple persons in the list.

   2. Test case: `edit 1 p/91234567 e/newemail@example.com`<br>
      Expected: First contact's phone and email updated. Success message displayed.

   3. Test case: `edit 2 t/`<br>
      Expected: Second contact's tags cleared. Success message displayed.

   4. Test case: `edit 1 nt/Important client`<br>
      Expected: First contact's note updated. Success message displayed.

   5. Test case: `edit 0 p/12345678`<br>
      Expected: Error message indicating invalid index.

   6. Test case: `edit 1`<br>
      Expected: Error message indicating at least one field must be provided.

   7. Test case: `edit x p/12345678` (where x exceeds list size)<br>
      Expected: Error message indicating invalid index.

### Deleting a person

1. Deleting a person while all persons are shown

   1. Prerequisites: List all persons using `list`. Multiple persons in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact deleted. Details of deleted contact shown. Timestamp updated.

   3. Test case: `delete 0`<br>
      Expected: Error message indicating invalid index. No person deleted.

   4. Test case: `delete` (no index)<br>
      Expected: Error message indicating invalid command format.

   5. Test case: `delete x` (where x exceeds list size)<br>
      Expected: Error message indicating invalid index.

### Finding persons by name

1. Finding contacts by name

   1. Prerequisites: Have contacts with various names in the address book.

   2. Test case: `find alex`<br>
      Expected: Contacts with "alex" (case-insensitive, partial match) displayed. Count shown.

   3. Test case: `find alex david`<br>
      Expected: Contacts matching "alex" OR "david" displayed.

   4. Test case: `find Yeoh s/1`<br>
      Expected: Only contacts with exact word "Yeoh" displayed (strict mode).

   5. Test case: `find s/2 Alica`<br>
      Expected: Up to 5 closest matches to "Alica" displayed (fuzzy mode, tolerates typos).

   6. Test case: `find`<br>
      Expected: Error message indicating keywords cannot be empty.

   7. Test case: `find s/9 alex`<br>
      Expected: Error message indicating invalid search mode.

### Filtering persons by tags

1. Filtering contacts by tags

   1. Prerequisites: Have contacts with various tags in the address book.

   2. Test case: `filter friends`<br>
      Expected: Only contacts with "friends" tag displayed (case-sensitive). Count shown.

   3. Test case: `filter friends VIP`<br>
      Expected: Only contacts with BOTH "friends" AND "VIP" tags displayed.

   4. Test case: `filter`<br>
      Expected: Error message indicating invalid command format.

   5. Test case: `filter @invalid`<br>
      Expected: Error message indicating invalid tag format (alphanumeric only).

   6. Test case: `filter t1 t2 t3 t4 t5 t6 t7 t8 t9 t10 t11`<br>
      Expected: Error message indicating maximum 10 tags allowed.

### Sorting persons

1. Sorting the contact list

   1. Prerequisites: Have multiple contacts in the address book.

   2. Test case: `sort st/name so/asc`<br>
      Expected: Contacts sorted alphabetically by name (ascending). Success message shown.

   3. Test case: `sort st/name so/dsc`<br>
      Expected: Contacts sorted alphabetically by name (descending).

   4. Test case: `sort st/dateadded so/asc`<br>
      Expected: Contacts sorted by date added (oldest first).

   5. Test case: `sort st/dateadded so/dsc`<br>
      Expected: Contacts sorted by date added (newest first).

   6. Test case: `sort st/name`<br>
      Expected: Error message indicating missing sort order.

   7. Test case: `sort so/asc`<br>
      Expected: Error message indicating missing sort type.

   8. Test case: `sort st/invalid so/asc`<br>
      Expected: Error message indicating invalid sort type.

### Adding or editing notes

1. Managing notes for contacts

   1. Prerequisites: List all persons using `list`. Multiple persons in the list.

   2. Test case: `note 1 nt/Important client, needs follow-up`<br>
      Expected: Note added to first contact. Success message with note content displayed.

   3. Test case: `note 2 nt/`<br>
      Expected: Note removed from second contact. Success message displayed.

   4. Test case: `note 0 nt/Some note`<br>
      Expected: Error message indicating invalid index.

   5. Test case: `note 1`<br>
      Expected: Error message indicating note prefix required.

   6. Test case: `note x nt/Some note` (where x exceeds list size)<br>
      Expected: Error message indicating invalid index.

### Clearing all entries

1. Clearing the address book

   1. Test case: `clear`<br>
      Expected: All contacts removed. Success message displayed. Empty list shown.

   2. Test case: `clear extra parameters`<br>
      Expected: All contacts removed (extra parameters ignored).

### Command history navigation

1. Using arrow keys to navigate command history

   1. Prerequisites: Execute several commands (e.g., `list`, `add n/Test p/12345678 e/test@test.com a/Test St`, `find alex`).

   2. Test case: Press UP arrow key in command box<br>
      Expected: Previous command (`find alex`) appears in command box.

   3. Test case: Press UP arrow key again<br>
      Expected: Command before that (`add...`) appears.

   4. Test case: Press DOWN arrow key<br>
      Expected: More recent command (`find alex`) appears again.

### Saving data

1. Dealing with missing data files

   1. Delete the `data/addressbook.json` file from the application folder.
   2. Launch the application.<br>
      Expected: Application starts with sample data loaded.

2. Dealing with corrupted data files

   1. Open `data/addressbook.json` in a text editor.
   2. Modify the file to make it invalid JSON (e.g., remove a closing brace).
   3. Launch the application.<br>
      Expected: Application starts with empty data, corrupted file discarded.

3. Automatic saving

   1. Add, edit, or delete a contact.
   2. Close the application without using `exit` command.
   3. Relaunch the application.<br>
      Expected: Changes are persisted, data is retained.
