package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostDbStore;

import java.util.Collection;

@ThreadSafe
@Service
public class PostService {

    private final PostDbStore postDbStore;
    private final CityService cityService;

    public PostService(PostDbStore postDbStore, CityService cityService) {
        this.postDbStore = postDbStore;
        this.cityService = cityService;
    }

    public Collection<Post> findAll() {
        Collection<Post> result = postDbStore.findAll();
        result.forEach(post -> post.setCity(cityService.findById(post.getCity().getId())));
        return result;
    }

    public void add(Post post) {
        postDbStore.add(post);
    }

    public Post findById(int id) {
        return postDbStore.findById(id);
    }

    public void update(Post post) {
        postDbStore.update(post);
    }
}
