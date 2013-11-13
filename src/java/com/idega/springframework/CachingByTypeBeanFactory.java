package com.idega.springframework;
 
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
 
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
 
/**
 * Caches certain calls to get bean names for type. Profiling showed that significant cpu time was spent on
 * reflection because this was not cached. It does not change, so should be safe to cache.
 */
public class CachingByTypeBeanFactory extends DefaultListableBeanFactory {
    private static Logger log = Logger.getLogger(CachingByTypeBeanFactory.class);
    ConcurrentHashMap<TypeKey, String[]> cachedBeanNamesForType = new ConcurrentHashMap<TypeKey, String[]>();
 
    @Override
    public String[] getBeanNamesForType(Class type) {
        return getBeanNamesForType(type, true, true);
    }
 
    @Override
    public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean allowEagerInit) {
        TypeKey typeKey = new TypeKey(type, includeNonSingletons, allowEagerInit);
        if (cachedBeanNamesForType.containsKey(typeKey)) {
            log.debug("will retrieve from cache: " + typeKey);
            return cachedBeanNamesForType.get(typeKey);
        }
        String[] value = super.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        if (log.isDebugEnabled()) {
            log.debug("will add to cache: " + typeKey + " " + Arrays.asList(value));
        }
        cachedBeanNamesForType.putIfAbsent(typeKey, value);
        return value;
    }
 
    // This is the input parameters, which we memoize.
    // We conservatively cache based on the possible parameters passed in. Assuming that state does not change within the
    // super.getBeanamesForType() call between subsequent requests.
    static class TypeKey {
        Class type;
        boolean includeNonSingletons;
        boolean allowEagerInit;
 
        TypeKey(Class type, boolean includeNonSingletons, boolean allowEagerInit) {
            this.type = type;
            this.includeNonSingletons = includeNonSingletons;
            this.allowEagerInit = allowEagerInit;
        }
 
        @Override
        public String toString() {
            return "TypeKey{" + type + " " + includeNonSingletons + " " + allowEagerInit + "}";
        }
 
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
 
            TypeKey typeKey = (TypeKey) o;
 
            if (allowEagerInit != typeKey.allowEagerInit) return false;
            if (includeNonSingletons != typeKey.includeNonSingletons) return false;
            if (type != null ? !type.equals(typeKey.type) : typeKey.type != null) return false;
 
            return true;
        }
 
        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (includeNonSingletons ? 1 : 0);
            result = 31 * result + (allowEagerInit ? 1 : 0);
            return result;
        }
    }
 
}