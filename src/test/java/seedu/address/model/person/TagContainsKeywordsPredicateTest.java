package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstTags = Collections.singletonList("friend");
        List<String> secondTags = Arrays.asList("friend", "VIP");

        TagContainsKeywordsPredicate firstPredicate = new TagContainsKeywordsPredicate(firstTags);
        TagContainsKeywordsPredicate secondPredicate = new TagContainsKeywordsPredicate(secondTags);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate firstPredicateCopy = new TagContainsKeywordsPredicate(firstTags);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different tags -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personHasAllTags_returnsTrue() {
        // One tag
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(Collections.singletonList("friend"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "VIP").build()));

        // Multiple tags (all must match)
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("friend", "VIP"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "VIP", "client").build()));

        // Order does not matter
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("VIP", "friend"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "VIP", "client").build()));

        // Single tag matches among many
        predicate = new TagContainsKeywordsPredicate(Collections.singletonList("client"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friend", "VIP", "client").build()));
    }

    @Test
    public void test_personDoesNotHaveAllTags_returnsFalse() {
        // Missing one tag
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Arrays.asList("friend", "VIP", "client"));
        assertFalse(predicate.test(new PersonBuilder().withTags("friend", "VIP").build()));

        // No matching tags
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("supplier"));
        assertFalse(predicate.test(new PersonBuilder().withTags("friend", "VIP").build()));

        // Empty predicate
        predicate = new TagContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withTags("friend", "VIP").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> tags = Arrays.asList("friend", "VIP");
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(tags);

        String expected = TagContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + tags + "}";
        assertEquals(expected, predicate.toString());
    }
}
