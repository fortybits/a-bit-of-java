package edu.bit.generics;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Checker<A, B> extends BiFunction<Checker.CheckRequest<A>, Function<A, B>, Checker.CheckResponse<B>> {

    default B execute(A req) {
        CheckRequest<A> chkReq = new CheckRequest<>();
        chkReq.setOperationRequest(req);

        Function<A, B> op = a -> null;

        Checker<A, B> checker = (aCheckRequest, abFunction) -> new CheckResponse<>();

        return checker.apply(chkReq, op).getOperationResponse();
    }

    class CheckResponse<B> {
        private B operationResponse;

        public B getOperationResponse() {
            return operationResponse;
        }

        public void setOperationResponse(B operationResponse) {
            this.operationResponse = operationResponse;
        }
    }

    class CheckRequest<A> {
        private A operationRequest;

        public A getOperationRequest() {
            return operationRequest;
        }

        public void setOperationRequest(A operationRequest) {
            this.operationRequest = operationRequest;
        }
    }
}