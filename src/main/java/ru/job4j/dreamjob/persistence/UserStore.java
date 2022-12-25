package ru.job4j.dreamjob.persistence;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class UserStore {

    public final static UserStore INST = new UserStore();

    private Map<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public UserStore instOf() {
        return INST;
    }

    public User add(User user) {
        user.setId(id.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public User findById(int id) {
        return users.get(id);
    }

    public void update(User user) {
        users.replace(user.getId(), user);
    }
}
