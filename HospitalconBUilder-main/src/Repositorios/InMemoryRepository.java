package Repositorios;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
public class InMemoryRepository<T>{
    protected Map<Long, T> data = new HashMap<>();
    protected AtomicLong idGenerator = new AtomicLong();

    public T save(T entity) {
        long id = idGenerator.incrementAndGet();
        try {
            String clase;
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);
            clase = entity.getClass().getName();
            System.out.println(clase + " id:   " + id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        data.put(id, entity);
        return entity;
    }
    public Optional<T> genericDelete(Long id) {
        if (!data.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(data.remove(id));
    }

    public List<T> genericFindByField(String fieldName, Object value) {
        List<T> results = new ArrayList<>();
        try {
            for (T entity : data.values()) {
                Method getFieldMethod = entity.getClass().getMethod("get" + capitalize(fieldName));
                Object fieldValue = getFieldMethod.invoke(entity);
                if (fieldValue != null && fieldValue.equals(value)) {
                    results.add(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
    public Collection<T> findAll() {
        return data.values();
    }
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
