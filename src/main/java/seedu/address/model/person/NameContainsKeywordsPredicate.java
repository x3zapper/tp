package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    /** Maximum Levenshtein distance allowed for fuzzy matching. */
    public static final int FUZZY_MATCH_THRESHOLD = 2;

    private final List<String> keywords;
    private final boolean isStrict;
    private final boolean isFuzzy;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this(keywords, false, false);
    }

    /**
     * Constructs a predicate that matches names using the supplied keywords.
     *
     * @param keywords list of user-provided keywords to test against the name
     * @param isStrict {@code true} for strict full-word matching, {@code false} for
     *                 relaxed partial matching
     */
    public NameContainsKeywordsPredicate(List<String> keywords, boolean isStrict) {
        this(keywords, isStrict, false);
    }

    /**
     * Constructs a predicate that matches names using the supplied keywords.
     *
     * @param keywords list of user-provided keywords to test against the name
     * @param isStrict {@code true} for strict full-word matching, {@code false} for
     *                 relaxed partial matching
     * @param isFuzzy  {@code true} for fuzzy matching using Levenshtein distance,
     *                 {@code false} otherwise
     */
    public NameContainsKeywordsPredicate(List<String> keywords, boolean isStrict, boolean isFuzzy) {
        this.keywords = keywords;
        this.isStrict = isStrict;
        this.isFuzzy = isFuzzy;
    }

    @Override
    public boolean test(Person person) {
        String fullName = person.getName().fullName;
        String lowerCaseName = fullName.toLowerCase();

        if (isFuzzy) {
            return keywords.stream()
                    .map(String::trim)
                    .filter(keyword -> !keyword.isEmpty())
                    .anyMatch(keyword -> fuzzyMatch(fullName, keyword));
        }

        return keywords.stream()
                .map(String::trim)
                .filter(keyword -> !keyword.isEmpty())
                .anyMatch(keyword -> matchKeyword(fullName, lowerCaseName, keyword));
    }

    /**
     * Checks if a keyword matches the name based on the search mode.
     *
     * @param fullName      the full name to check
     * @param lowerCaseName the lowercase version of the full name
     * @param keyword       the keyword to match against
     * @return true if the keyword matches
     */
    private boolean matchKeyword(String fullName, String lowerCaseName, String keyword) {
        return isStrict
                ? StringUtil.containsWordIgnoreCase(fullName, keyword)
                : lowerCaseName.contains(keyword.toLowerCase());
    }

    /**
     * Performs fuzzy matching between the name and keyword using Levenshtein
     * distance.
     * A match is considered if any word in the name has a Levenshtein distance
     * &lt;= {@value #FUZZY_MATCH_THRESHOLD} from the keyword.
     *
     * @param name    the full name to check
     * @param keyword the keyword to match against
     * @return true if a fuzzy match is found
     */
    private boolean fuzzyMatch(String name, String keyword) {
        String[] words = name.toLowerCase().split("\\s+");
        String lowerKeyword = keyword.toLowerCase();

        for (String word : words) {
            int distance = StringUtil.getLevenshteinDistance(word, lowerKeyword);
            if (distance <= FUZZY_MATCH_THRESHOLD) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the minimum Levenshtein distance between any keyword and any word
     * in the name.
     *
     * @param person the person to calculate the distance for
     * @return the minimum distance, or Integer.MAX_VALUE if no keywords are
     *         provided
     */
    public int getMinimumDistance(Person person) {
        String fullName = person.getName().fullName;
        String[] nameWords = fullName.toLowerCase().split("\\s+");

        int minDistance = Integer.MAX_VALUE;

        for (String keyword : keywords) {
            String lowerKeyword = keyword.trim().toLowerCase();
            if (lowerKeyword.isEmpty()) {
                continue;
            }

            for (String nameWord : nameWords) {
                int distance = StringUtil.getLevenshteinDistance(nameWord, lowerKeyword);
                minDistance = Math.min(minDistance, distance);
            }
        }

        return minDistance;
    }

    public boolean isFuzzy() {
        return isFuzzy;
    }

    public boolean isStrict() {
        return isStrict;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords)
                && isStrict == otherNameContainsKeywordsPredicate.isStrict
                && isFuzzy == otherNameContainsKeywordsPredicate.isFuzzy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .add("isStrict", isStrict)
                .add("isFuzzy", isFuzzy)
                .toString();
    }
}
