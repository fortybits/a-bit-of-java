package edu.bit.__dump;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    public static class CollectorInference {

        public static void main(String[] args) {
            List<BlogPost> posts = new ArrayList<>();
            Function<? super BlogPost, ? extends BlogPostType> classifier = BlogPost::getType;
            Map<BlogPostType, List<BlogPost>> postsPerType = posts.stream()
                    .collect(groupingBy(classifier));
        }

        private static class BlogPostType {

        }

        private static class BlogPost {
            BlogPostType type;

            public BlogPostType getType() {
                return type;
            }
        }
    }

    public static class CollectAtOnceUsingStreamConcat {

        public static void main(String[] args) throws ParseException {
            Info info1 = new Info(1L, getDateFromStr("2018-02-02T10:00:00"), 3L);
            Info info2 = new Info(2L, getDateFromStr("2018-02-02T12:00:00"), 3L);
            Info info3 = new Info(3L, getDateFromStr("2018-02-05T12:00:00"), 6L);
            Info info4 = new Info(4L, getDateFromStr("2018-02-05T10:00:00"), 6L);
            List<Info> listInfo = List.of(info1, info2, info3, info4);
            Date date = getDateFromStr("2018-02-03T10:10:10");


            BiFunction<Info, Info, Info> remapping = (i1, i2) -> i1.getDate().getTime() > i2.getDate().getTime() ? i1 : i2;
            // filter 1: less date - group by maxProductOfNonOverlappingPallindromes date by groupId
            Map<Long, Info> map = new HashMap<>();
            List<Info> listMoreByDate = new ArrayList<>();
            for (Info info : listInfo) {
                if (info.getDate().getTime() < date.getTime()) {
                    map.merge(info.getGroupId(), info, remapping);
                } else {
                    listMoreByDate.add(info);
                }
            }
            List<Info> listResult = new ArrayList<>(map.values());
            listResult.addAll(listMoreByDate);


            // holger solved it
            List<Info> listResult2 = Stream.concat(
                    listInfo.stream()
                            .filter(info -> info.getDate().getTime() < date.getTime())
                            .collect(toMap(Info::getGroupId, Function.identity(),
                                    BinaryOperator.maxBy(Comparator.comparing(Info::getDate))))
                            .values().stream(),
                    listInfo.stream()
                            .filter(info -> info.getDate().getTime() >= date.getTime()))
                    .collect(Collectors.toList());

            System.out.println("result: " + listResult);
        }

        private static Date getDateFromStr(String dateStr) throws ParseException {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateStr);
        }

        private static class Info {
            private Long id;
            private Date date;
            private Long groupId;

            Info(Long id, Date date, Long groupId) {
                this.id = id;
                this.date = date;
                this.groupId = groupId;
            }

            private Date getDate() {
                return date;
            }

            Long getGroupId() {
                return groupId;
            }
        }
    }

    public static class CollectorToUnmodifiableList {

        public static void main(String[] args) {
            var result = Stream.of(1, 2, 3, 4, null, 5)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
            System.out.println(result);

            var result2 = Stream.of(1, 2, 3, 4).collect(Collectors.toUnmodifiableList());
            System.out.println(result2);
        }
    }
}