package tech.theraven.cloudrender.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> {

    private static final Response<?> EMPTY = new Response<>(true, null, null);

    private final boolean success;
    private final Error error;
    private final T data;

    public static <T> Response<T> of(T data) {
        return new Response<>(true, null, data);
    }

    public static <T> Response<T> error(Error error) {
        return new Response<>(false, error, null);
    }

    public static <T> Response<T> error(ErrorType errorType, String description) {
        return Response.error(new Error(errorType, description));
    }

    public static <T> Response<T> empty() {
        @SuppressWarnings("unchecked")
        Response<T> emptyResponse = (Response<T>) EMPTY;
        return emptyResponse;
    }

    public static Response<Void> catching(RunnableException action, Function<Exception, Error> ifFailure) {
        try {
            action.run();
            return Response.empty();
        } catch (Exception t) {
            return Response.error(ifFailure.apply(t));
        }
    }

    public static <T> Response<T> catching(SupplierException<T> action, Function<Exception, Error> ifFailure) {
        try {
            return Response.of(action.get());
        } catch (Exception t) {
            return Response.error(ifFailure.apply(t));
        }
    }

    public static <T> Response<T> catchingResponse(SupplierException<Response<T>> action, Function<Exception, Error> ifFailure) {
        try {
            return action.get();
        } catch (Exception t) {
            return Response.error(ifFailure.apply(t));
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> Response<T> fromOptional(Optional<T> optional, Supplier<Error> ifEmpty) {
        return optional != null && optional.isPresent()
                ? Response.of(optional.get())
                : Response.error(ifEmpty.get());
    }
    @JsonIgnore
    public boolean notSuccessful() {
        return !isSuccess();
    }

    @JsonIgnore
    public Optional<T> getDataOptional() {
        return Optional.ofNullable(data);
    }

    public <K> Response<K> map(Function<T, K> f) {
        return notSuccessful() ? Response.error(error) : Response.of(f.apply(data));
    }

    public <K> Response<K> flatMap(Function<T, Response<K>> f) {
        return notSuccessful() ? Response.error(error) : f.apply(data);
    }

    public Response<T> orElse(Response<T> alternative) {
        return notSuccessful() ? alternative : this;
    }

    public <K> Response<K> andThen(Supplier<Response<K>> next) {
        return notSuccessful() ? Response.error(error) : next.get();
    }

    public Response<T> peek(Consumer<T> consumer) {
        if (isSuccess()) {
            consumer.accept(data);
        }

        return this;
    }

    public Response<T> peekOnError(Consumer<Error> consumer) {
        if (notSuccessful()) {
            consumer.accept(error);
        }

        return this;
    }

    public void forEach(Consumer<T> f) {
        if (isSuccess()) {
            f.accept(data);
        }
    }

    public <X extends Throwable> T orElseThrow(Function<Error, ? extends X> exceptionSupplier) throws X {
        if (isSuccess()) {
            return data;
        } else {
            throw exceptionSupplier.apply(error);
        }
    }

    public Response<T> filter(Predicate<T> predicate, Supplier<Error> ifFiltered) {
        return flatMap(data -> predicate.test(data) ? this : Response.error(ifFiltered.get()));
    }

    @JsonCreator(mode = PROPERTIES)
    private static <T> Response<T> create(@JsonProperty("data") T data, @JsonProperty("error") Error error) {
        return error == null ? Response.of(data) : Response.error(error);
    }

}

