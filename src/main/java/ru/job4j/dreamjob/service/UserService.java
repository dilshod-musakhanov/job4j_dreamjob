package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.UserDbStore;
import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class UserService {

    private final UserDbStore userDbStore;

    public UserService(UserDbStore userDbStore) {
        this.userDbStore = userDbStore;
    }

    public Optional<User> add(User user) {
        return userDbStore.add(user);
    }

    public Collection<User> findAll() {
        return userDbStore.findAll();
    }

    public Optional<User> findById(int id) {
        return userDbStore.findById(id);
    }

    public void update(User user) {
        userDbStore.update(user);
    }

}
