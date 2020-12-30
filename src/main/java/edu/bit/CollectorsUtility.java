package edu.bit;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectorsUtility {


    public Map.Entry<Map<Integer, BigDecimal>, List<BigDecimal>> teeingSample(Map<Integer, BigDecimal> data,
                                                                              Function<BigDecimal, BigDecimal> func1,
                                                                              Function<BigDecimal, BigDecimal> func2) {
        return data.entrySet().stream()
                .collect(Collectors.teeing(
                        Collectors.toMap(Map.Entry::getKey, i -> func1.apply(i.getValue())),
                        Collectors.mapping(i -> func1.andThen(func2).apply(i.getValue()), Collectors.toList()),
                        Map::entry));
    }

    // custom implementation of the teeing collector before the API was introduced
    public static <T, A1, A2, R1, R2, R> Collector<T, ?, R> teeing(
            Collector<? super T, A1, R1> downstream1,
            Collector<? super T, A2, R2> downstream2,
            BiFunction<? super R1, ? super R2, R> merger) {

        class Acc {
            A1 acc1 = downstream1.supplier().get();
            A2 acc2 = downstream2.supplier().get();

            void accumulate(T t) {
                downstream1.accumulator().accept(acc1, t);
                downstream2.accumulator().accept(acc2, t);
            }

            Acc combine(Acc other) {
                acc1 = downstream1.combiner().apply(acc1, other.acc1);
                acc2 = downstream2.combiner().apply(acc2, other.acc2);
                return this;
            }

            R applyMerger() {
                R1 r1 = downstream1.finisher().apply(acc1);
                R2 r2 = downstream2.finisher().apply(acc2);
                return merger.apply(r1, r2);
            }
        }

        return Collector.of(Acc::new, Acc::accumulate, Acc::combine, Acc::applyMerger);
    }


    // implementation of a custom collector implemented below
    record Offer(OfferType type, BigDecimal price) {
    }

    public enum OfferType {
        STANDARD, BONUS
    }

    public static Collector<Offer, ?, List<Offer>> minCollector() {
        class Acc {
            private Offer min = null;
            private List<Offer> result = new ArrayList<>();

            private void add(Offer offer) {
                if (offer.type() == OfferType.STANDARD) {
                    if (min == null) {
                        min = offer;
                    } else {
                        min = offer.price()
                                .compareTo(min.price()) > 0 ? min : offer;
                    }
                } else {
                    result.add(offer);
                }
            }

            private Acc combine(Acc another) {
                result.addAll(another.result);
                return this;
            }

            private List<Offer> finisher() {
                result.add(min);
                return result;
            }
        }

        return Collector.of(Acc::new, Acc::add, Acc::combine, Acc::finisher);
    }

    void findingMinimumOffer() {
        List<Offer> offers = Arrays.asList(new Offer(OfferType.STANDARD, BigDecimal.valueOf(10.0)),
                new Offer(OfferType.STANDARD, BigDecimal.valueOf(20.0)),
                new Offer(OfferType.STANDARD, BigDecimal.valueOf(30.0)),
                new Offer(OfferType.BONUS, BigDecimal.valueOf(5.0)),
                new Offer(OfferType.BONUS, BigDecimal.valueOf(5.0)));

        Comparator<Offer> compareLeastPrice = Comparator.comparing(Offer::price);

        List<Offer> some = offers.stream()
                .filter(x -> x.type() != OfferType.STANDARD)
                .collect(Collectors.toList());

        offers.stream()
                .filter(x -> x.type() == OfferType.STANDARD)
                .min(Comparator.comparing(Offer::price))
                .ifPresent(some::add);
    }
}