package de.bitbrain.jpersis;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.google.common.cache.CacheBuilder.newBuilder;

/**
 * Cached implementation of a default mapper
 */
public class CachedDefaultMapper<T extends IdProvider> implements DefaultMapper<T> {

    private DefaultMapper<T> mapper;

    private LoadingCache<Integer, T> cache;

    CachedDefaultMapper(DefaultMapper<T> mapper) {
        this.mapper = mapper;
        cache = newBuilder()
            .maximumSize(1024)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, T>() {
                @Override
                public T load(Integer integer) throws Exception {
                    T element = CachedDefaultMapper.this.mapper.getById(integer);
                    if (element == null) {
                        throw new JPersisException("Data store is not accessible!");
                    }
                    return element;
                }
            });

    }

    /**
     * Invalidates the cache with the data store
     */
    public void invalidate() {
        cache.invalidateAll();
        insert(mapper.getAll());
    }

    @Override
    public void insert(T model) {
        if (cache.asMap().containsKey(model.getId())) {
            mapper.insert(model);
        } else {
            mapper.update(model);
        }
        cache.put(model.getId(), model);
    }

    @Override
    public void insert(Collection<T> models) {
        for (T model : models) {
            insert(model);
        }
    }

    @Override
    public void delete(T model) {
        if (getById(model.getId()) != null) {
            mapper.delete(model);
            cache.invalidate(model.getId());
        }
    }

    @Override
    public void delete(Collection<T> models) {
        for (T model : models) {
            delete(model);
        }
    }

    @Override
    public int count() {
        return Math.round(cache.size());
    }

    @Override
    public Collection<T> getAll() {
        return cache.asMap().values();
    }

    @Override
    public T getById(int id) {
        try {
            return cache.get(id);
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Override
    public void update(T model) {
        insert(model);
    }

    @Override
    public void update(Collection<T> models) {
        for (T model : models) {
            update(model);
        }
    }
}
