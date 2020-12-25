package edu.bit.generics;

import java.util.function.BiFunction;
import java.util.function.Function;

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