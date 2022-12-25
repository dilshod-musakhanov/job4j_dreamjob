package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStore {

    private final static Logger LOG = LoggerFactory.getLogger(UserDbStore.class.getName());
    private final static String ADD = "INSERT INTO users(name, email) VALUES (?, ?)";
    private final static String FIND_ALL = "SELECT * FROM users";
    private final static String FIND_BY_ID = "SELECT FROM users WHERE id = ?";
    private final static String UPDATE = "UPDATE users SET name = ?, email = ? WHERE id = ?";
    private final BasicDataSource pool;

    public UserDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        Optional<User> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            setStatement(ps, user);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                    rsl = Optional.of(user);
                }
                LOG.debug("User added: {}", user.getName());
            }
        } catch (SQLException e) {
            LOG.error("Exception in method add()" , e);
        }
        return rsl;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(FIND_ALL);
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(createUser(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method findAll()" , e);
        }
        return users;
    }

    public Optional<User> findById(int id) {
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(FIND_BY_ID);
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.of(createUser(it));
                }
            }
        } catch (SQLException e) {
                LOG.error("Exception in method findById()" , e);
        }
        return Optional.empty();
    }

    public void update(User user) {
        boolean flag = false;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(UPDATE);
        ) {
            ps.setInt(3, user.getId());
            setStatement(ps, user);
            ps.execute();
            flag = ps.executeUpdate() > 0;
            if (!flag) {
                LOG.debug("User : {} was not updated", user.getName());
            } else {
                LOG.debug("User : {} was updated", user.getName());
            }
        } catch (SQLException e) {
            LOG.error("Exception in method update()" , e);
        }
    }

    public void setStatement(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
    }

    public User createUser(ResultSet it) throws SQLException {
        return new User(
                it.getInt("id"),
                it.getString("name"),
                it.getString("email")
        );
    }
}
