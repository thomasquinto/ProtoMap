package com.life360.android.scaffold;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.functions.Function;

/**
 * Maps (converts) entities returned by the Interactor into view model objects used by Presenters
 *
 * @param <From>
 *     the type of input objects provided to the map operation.
 * @param <To>
 *     the type of output objects from map operation.
 */

public abstract class Mapper<From, To> implements Function<From, To> {

    @Override
    public final To apply(From from) {
        return map(from);
    }

    /**
     * Map the provided collection of input objects to an appropriate collection of output objects.
     *
     * @param entities
     *     collection of input objects.
     *
     * @return collection of output objects.
     */
    public Collection<To> map(Collection<From> entities) {
        final Collection<To> result = new ArrayList<>(entities.size());
        for (From from : entities) {
            result.add(map(from));
        }
        return result;
    }

    /**
     * Map the provided input object to an appropriate output object.
     *
     * @param entity
     *     input object
     *
     * @return output object
     */
    public abstract To map(From entity);

}
