package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PostDbStore {

    private static final Logger LOG = LoggerFactory.getLogger(PostDbStore.class.getName());

    private final BasicDataSource pool;

    private PostDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(createPost(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method findAll()" , e);
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO post(name, description, visible, city_id, created) VALUES (?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            setStatement(ps, post);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
                LOG.debug("Post added: {}", post.getName());
            }
        } catch (SQLException e) {
            LOG.error("Exception in method add()" , e);
        }
        return post;
    }

    public void update(Post post) {
        boolean flag = false;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(
                    "UPDATE post SET name = ?, description = ?, visible = ?, city_id = ?, created = ? WHERE id = ?")
        ) {
            ps.setInt(6, post.getId());
            setStatement(ps, post);
            ps.execute();
            flag = ps.executeUpdate() > 0;
            if (!flag) {
                LOG.debug("Post : {} was not updated", post.getName());
            } else {
                LOG.debug("Post : {} was updated", post.getName());
            }
        } catch (SQLException e) {
            LOG.error("Exception in method update()" , e);
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createPost(it);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method findById()" , e);
        }
        return null;
    }

    public Post createPost(ResultSet it) throws SQLException {
        return new Post(
                it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                it.getBoolean("visible"),
                new City(it.getInt("city_id"), ""),
                it.getTimestamp("created").toLocalDateTime()
        );
    }

    public void setStatement(PreparedStatement ps, Post post) throws SQLException {
        ps.setString(1, post.getName());
        ps.setString(2, post.getDescription());
        ps.setBoolean(3, post.isVisible());
        ps.setInt(4, post.getCity().getId());
        ps.setTimestamp(5,Timestamp.valueOf(LocalDateTime.now()));
    }
}
