package edu.bit;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GenericsUtility {

    private static <E> void addToMatrix(List<? extends E> list, List<List<? extends E>> matrix) {
        matrix.add(list);
    }

    public static void main(String[] args) {
        Attempt attempt = new Attempt();
        attempt.toArrayGeneralized();

        // generic implementation of list<list<? extends E>>
        List<List<? extends Number>> matrix = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        addToMatrix(list, matrix);

        EntityCloneService<? extends AbstractNamedEntityBase> service = new EntityCloneService<AbstractNamedEntityBase>() {
            @Override
            public AbstractNamedEntityBase getByUuid(UUID uuid) {
                return new AbstractNamedEntityBase();
            }

            @Override
            public <S> S getCloneAndSave(S existingEntity) {
                return null;
            }
        };
        AbstractNamedEntityBase entity = service.getByUuid(UUID.randomUUID()); // THIS WORKS because it up casting.
        service.getCloneAndSave(entity);    // now how do I pass entity object such that

        // this won't work
//        Comparator<A<String, Integer>> c = wrap(Integer::compare).thenComparing(wrap(Integer::compare));
        // but this would
        Comparator<A<String, Integer>> comparator = wrap(Integer::compare);
        comparator = comparator.thenComparing(wrap(Integer::compare));

    }

    private static <F, S> Comparator<A<F, S>> wrap(Comparator<S> c) {
        return (L, R) -> c.compare(L.getS(), R.getS());
    }

    private static void f(int ordinal) {
        System.out.println("f");
    }

    public void genericsWithCollections() {
        // interesting generics
        SortedSet<int[]> all = new TreeSet<>((a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(a[1], b[1]);
            } else {
                return Integer.compare(a[0], b[0]);
            }
        });
    }

    // example of more generics implementation witth enums and intersection allowed
    private <T extends Enum<T> & InterfaceA> void moreGenerics(Class<T> type) {
        Map<String, Class<T>> filter = new HashMap<>();
        filter.put("a", type);
        importSettingViaEnum(filter.get("a"));
    }

    private <T extends Enum<T> & InterfaceA> void importSettingViaEnum(Class<? extends T> clazz) {
        for (T elem : clazz.getEnumConstants()) {
            f(elem.ordinal());
            elem.foo();
        }
    }

    // generic utility for sorting any Map by keys and then by values
    public <T, K extends Comparable<K>> void sortAMapByKeyThenValues(Map<T, List<K>> yourMap) {
        Map<T, List<K>> sortedByKey = new TreeMap<>(yourMap);
        sortedByKey.values().forEach(Collections::sort);
    }

    public enum EnumA implements InterfaceA {
        RED();

        public Object[] foo() {
            System.out.println("g");
            return null;
        }
    }

    public enum EnumB implements InterfaceA {
        OAK();

        public Object[] foo() {
            System.out.println("g");
            return null;
        }
    }


    public interface BaseLookup<Id extends Serializable, T extends BaseEntity<Id>> {
        T findById(Id id);
    }

    public interface EntityCloneService<T extends AbstractNamedEntityBase> {
        T getByUuid(UUID uuid);

        <S> S getCloneAndSave(S existingEntity);
    }

    interface A<F, S> {
        F getF();

        S getS();
    }

    public interface Checker<A, B> extends BiFunction<Checker.CheckRequest<A>, Function<A, B>, Checker.CheckResponse<B>> {

        default B execute(A req) {
            CheckRequest<A> chkReq = new CheckRequest<>(req);
            Function<A, B> op = a -> null;
            Checker<A, B> checker = (aCheckRequest, abFunction) -> new CheckResponse<>();
            return checker.apply(chkReq, op).operationResponse();
        }

        record CheckResponse<B>(B operationResponse) {
            public CheckResponse() {
                this(null);
            }
        }

        record CheckRequest<A>(A operationRequest) {
        }
    }

    public interface InterfaceA {
        Object[] foo();
    }

    public static class Helper {
        static <T> T[] CollectionToArray(Collection<T> collection) {
            return (T[]) collection.toArray();
        }

        static <T> T[] ListToArray(List<T> list, T[] a) {
            return list.toArray(a); // Error
        }
    }

    public static class Attempt {
        void toArrayGeneralized() {
            List<String> list = new ArrayList<>();
            Set<String> set = new HashSet<>();
            String[] arrayFromList = Helper.CollectionToArray(list);
            String[] arrayFromSet = Helper.CollectionToArray(set);
            String[] array = Helper.ListToArray(list, new String[0]);
        }
    }

    static class AbstractNamedEntityBase {
    }

    // interface of type BaseEntity
    public class BaseEntity<I extends Serializable> {
        I id;

        public BaseEntity() {
        }

        public I getId() {
            return id;
        }

        public void setId(I id) {
            this.id = id;
        }

        // also has hashCode() and equals() methods to be based on id
    }
}