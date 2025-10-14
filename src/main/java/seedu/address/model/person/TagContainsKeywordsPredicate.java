package seedu.address.model.person;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Tag}s match all of the specified keywords.
 * Matching is case-sensitive.
 */
public class TagContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getTags() {
        return keywords;
    }

    @Override
    public boolean test(Person person) {
        Set<String> personTagNames = person.getTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.toSet());

        // Must contain ALL input tags (case-sensitive)
        return keywords.stream().allMatch(personTagNames::contains);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TagContainsKeywordsPredicate)) {
            return false;
        }
        TagContainsKeywordsPredicate otherPredicate = (TagContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
