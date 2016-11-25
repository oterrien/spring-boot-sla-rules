package com.test.rule;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.function.BiPredicate;
import java.util.function.Function;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Parameter<T extends Comparable> {

    private final String fieldName;

    private final Value<T> value;

    private final boolean isKey;

    public static final Parameter of(String fieldName, String value, String clause, String type, boolean isKey){
        return new Parameter(fieldName, new Value(value, Clause.valueOf(clause), Type.valueOf(type)), isKey);
    }

    @Data
    public static class Value<T extends Comparable> {

        private final T value;

        private final Clause clause;

        private Value(String value, Clause clause, Type type){
            this.value = (T) type.apply(value);
            this.clause = clause;
        }
    }

    enum Clause implements BiPredicate<Comparable, Comparable> {

        EQUALS((o1, o2) -> o1.equals(o2));

        private BiPredicate<Comparable, Comparable> comparator;

        Clause(BiPredicate<Comparable, Comparable> comparator){
            this.comparator = comparator;
        }

        @Override
        public boolean test(Comparable o1, Comparable o2) {
            return comparator.test(o1, o2);
        }
    }

    enum Type implements Function<String, Object> {

        STRING(o -> (String)o);

        private Function<String, Object> transformer;

        Type( Function<String, Object> transformer){
            this.transformer = transformer;
        }

        @Override
        public Object apply(String s) {
            return transformer.apply(s);
        }
    }

}
