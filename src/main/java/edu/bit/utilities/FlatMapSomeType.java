package edu.bit.utilities;

import java.util.List;
import java.util.Optional;

public class FlatMapSomeType {

    public static void main(String[] args) {

    }

    public static boolean anItemFulfillsCriteria(final SomeType anItem) {
        return true;
    }

    public Optional<String> transform(List<SomeType> aList) {
        return getAnItemFromTheList(aList)
                .filter(FlatMapSomeType::anItemFulfillsCriteria)
                .map(a -> getAStringTypeFieldFromTheItem(a)).orElse(Optional.empty());
    }

    private Optional<SomeType> getAnItemFromTheList(final List<SomeType> aList) {
        return Optional.empty();
    }

    private Optional<String> getAStringTypeFieldFromTheItem(final SomeType anItem) {
        return Optional.empty();
    }

    private static class SomeType {
        String someField;

        public String getSomeField() {
            return someField;
        }
    }
}