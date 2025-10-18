package com.gws.crm.core.lookups.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Repository
public class LookupRepositoryUtils {

    /**
     * Retrieves an entity by an attribute (e.g., name) or creates a new one if it does not exist.
     *
     * <p>
     * This method delegates the lookup logic to the provided {@code finder} function
     * (for example, {@code repository::findByName}). If the entity is found, it is returned.
     * Otherwise, a new entity is created and persisted using the {@code creator}.
     * </p>
     *
     * @param repository the JPA repository to persist the entity if needed
     * @param value      the attribute value to search by (e.g., name)
     * @param finder     a function that returns an Optional of the entity if found
     * @param creator    supplier that creates a new entity if not found
     * @param <T>        the entity type
     * @param <R>        the attribute type used for lookup (e.g., String for name)
     * @return the existing or newly created entity
     */
    public static <T, R> T getOrCreateByAttribute(
            JpaRepository<T, ?> repository,
            R value,
            Function<R, Optional<T>> finder,
            Supplier<T> creator
    ) {
        return finder.apply(value)
                .orElseGet(() -> repository.save(creator.get()));
    }

    /**
     * Retrieves an entity by its ID or creates a new one if it does not exist.
     *
     * <p>
     * If the given {@code id} is not null and exists in the repository,
     * the method will return a reference to the existing entity.
     * Otherwise, it will create and persist a new entity using the {@code creator}.
     * </p>
     *
     * @param repository the JPA repository to look up the entity
     * @param id         the entity ID (can be null)
     * @param creator    supplier that creates a new entity if not found
     * @param <T>        the entity type
     * @param <ID>       the ID type of the entity
     * @return the existing or newly created entity
     */
    protected <T, ID> T getOrCreateById(
            JpaRepository<T, ID> repository,
            ID id,
            Supplier<T> creator
    ) {
        if (id != null && repository.existsById(id)) {
            return repository.getReferenceById(id);
        }
        return repository.save(creator.get());
    }
}
