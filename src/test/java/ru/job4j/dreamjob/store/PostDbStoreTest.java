package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.config.JdbcConfiguration;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class PostDbStoreTest {

    private final PostDbStore store = new PostDbStore(new JdbcConfiguration().loadPool());

    @AfterEach
    public void cleanTable() throws SQLException {
        BasicDataSource pool = new JdbcConfiguration().loadPool();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("delete from post");
        ) {
            ps.execute();
        }
    }

    @Test
    public void whenCreatePost() {
        Post post = new Post(
                0,
                "Java Job",
                "Web application development",
                true,
                new City(1, "Moscow"),
                LocalDateTime.now()
        );
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenFindAllPosts() {
        Post post = new Post(
                0,
                "Java Job",
                "Web application development",
                true,
                new City(1, "Moscow"),
                LocalDateTime.now()
        );
        Post post2 = new Post(
                1,
                "Middle Job",
                "Web and mobile application development",
                true,
                new City(2, "Tomsk"),
                LocalDateTime.now()
        );
        store.add(post);
        store.add(post2);
        Collection<Post> temp = new ArrayList<>();
        temp.add(post);
        temp.add(post2);
        assertThat(store.findAll(), is(temp));
    }
/*
    @Test
    public void whenUpdatePost() {
        Post post1 = new Post(
                1,
                "Java Job",
                "Web application development",
                true,
                new City(1, "Moscow"),
                LocalDateTime.now()
        );
        store.add(post1);
        int id = post1.getId();
        Post post2 = new Post(
                1,
                "New Java Job",
                "Web application development",
                true,
                new City(1, "Moscow"),
                LocalDateTime.now()
        );
        store.update(post2);
        assertThat(store.findById(id).getName(), is(post2.getName()));
    }
*/
}