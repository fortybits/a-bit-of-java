package edu.bit.functional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class CollectorsUtility {

    record Stake(int customerId, int betOfferI, int stake) {
    }

    record User(String name, String id, String email, List<Integer> lists, int age) {
        User(String email, List<Integer> lists) {
            this(null, null, email, lists, 0);
        }
    }

    record ProductCatalogue(Integer pId, Integer cId) {
    }

    private static List<User> userListWithSameEmailAndMergeLists(List<User> users) {
        return new ArrayList<>(users.stream()
                .collect(Collectors.toMap(User::email, Function.identity(), (user1, user2) -> {
                    List<Integer> l1 = user1.lists();
                    List<Integer> l2 = user2.lists();
                    List<Integer> merge = IntStream.range(0, l1.size())
                            .mapToObj(i -> (l1.get(i) == 0 && l2.get(i) == 0) ? 0 : 1)
                            .collect(Collectors.toList());
                    return new User(user1.email(), merge);
                })).values());
    }


    private static Map<Integer, List<Integer>> collectGroupingByAndMapping(List<ProductCatalogue> productCatalogueList) {
        return productCatalogueList.stream()
                .collect(Collectors.groupingBy(ProductCatalogue::pId,
                        Collectors.mapping(ProductCatalogue::cId, Collectors.toList())));

    }

    private static List<Stake> stakesHighestPerCustomerForParticularStakeLimited(List<Stake> stakes, int maxBetOfferId) {
        return stakes.stream()
                .filter(x -> x.betOfferI() == maxBetOfferId) // retains only objects where their offer is if equal to the supplied offerId
                .collect(toMap(Stake::customerId,    // customer ids do not repeat as you've mentioned.
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingInt(Stake::stake))))  //   gets the highest stake values customer wise.
                .values()
                .stream()
                .collect(groupingBy(Stake::stake)) // particular stake
                .values()
                .stream()
                .flatMap(x -> x.stream().limit(20)) //  and limited to 20 customers for a particular stake.
                .collect(Collectors.toList());
    }
}