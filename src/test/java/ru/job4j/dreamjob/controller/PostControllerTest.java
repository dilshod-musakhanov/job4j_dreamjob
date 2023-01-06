package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "Java Job", "Job for juniors", true,
                        new City(1, "Moscow"), LocalDateTime.now()
                ),
                new Post(2, "Java Super Job", "Job for seniors", true,
                        new City(2, "Ivanova"), LocalDateTime.now()
                )
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        HttpSession httpSession = mock(HttpSession.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page  = postController.posts(model, httpSession);
        verify(model).addAttribute("posts", posts);
        assertThat(page).isEqualTo("posts");
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(2, "Java Super Job", "Job for seniors", true,
                new City(2, "Ivanova"), LocalDateTime.now()
        );
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input);
        verify(postService).add(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenFormAddPost() {
        List<City> cities = Arrays.asList(
                new City(1, "Moscow"),
                new City(2, "Ivanova")
        );
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        when(cityService.getAllCities()).thenReturn(cities);
        String page = postController.formAddPost(model, httpSession);
        verify(model).addAttribute("cities", cityService.getAllCities());
        assertThat(page).isEqualTo("addPost");
    }

    @Test
    public void whenFormUpdatePost() {
        Post input = new Post(1, "Java Job", "Job for juniors", true,
                new City(1, "Moscow"), LocalDateTime.now()
        );
        List<City> cities = Arrays.asList(
                new City(1, "Moscow"),
                new City(2, "Ivanova")
        );
        int id = 1;
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(postService.findById(id)).thenReturn(input);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.formUpdatePost(model, id);
        verify(model).addAttribute("post", postService.findById(id));
        verify(model).addAttribute("cities", cityService.getAllCities());
        assertThat(page).isEqualTo("updatePost");
    }

    @Test
    public void whenUpdatePost() {
        Post input = new Post(1, "Java Job", "Job for juniors", true,
                new City(1, "Moscow"), LocalDateTime.now()
        );
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.updatePost(input);
        verify(postService).update(input);
        assertThat(page).isEqualTo("redirect:/posts");
    }
}