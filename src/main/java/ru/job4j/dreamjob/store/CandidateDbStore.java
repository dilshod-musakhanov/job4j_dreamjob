package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDbStore {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateDbStore.class.getName());
    private static final String FIND_ALL = "SELECT * FROM candidate";
    private static final String ADD = "INSERT INTO candidate(name, description, city_id, photo, created) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE candidate SET name = ?, description = ?, city_id = ?, photo = ?, created = ? WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM candidate WHERE id = ?";
    private final BasicDataSource pool;

    public CandidateDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL);
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(createCandidate(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method findAll()" , e);
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn  = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
           setStatement(ps, candidate);
           ps.execute();
           try (ResultSet id = ps.getGeneratedKeys()) {
               if (id.next()) {
                   candidate.setId(id.getInt(1));
               }
               LOG.debug("Post added: {}", candidate.getName());
           }
        } catch (SQLException e) {
            LOG.error("Exception in method add()" , e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        boolean flag = false;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(UPDATE)
        ) {
            ps.setInt(6, candidate.getId());
            setStatement(ps, candidate);
            ps.execute();
            flag = ps.executeUpdate() > 0;
            if (!flag) {
                LOG.debug("Post : {} was not updated", candidate.getName());
            } else {
                LOG.debug("Post : {} was updated", candidate.getName());
            }
        } catch (SQLException e) {
            LOG.error("Exception in method update()" , e);
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createCandidate(it);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method findById()" , e);
        }
        return null;
    }

    private Candidate createCandidate(ResultSet it) throws SQLException {
        return new Candidate(
                it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                new City(it.getInt("city_id"), ""),
                it.getBytes("photo"),
                it.getTimestamp("created").toLocalDateTime()
        );
    }

    public void setStatement(PreparedStatement ps, Candidate candidate) throws SQLException {
        ps.setString(1, candidate.getName());
        ps.setString(2, candidate.getDescription());
        ps.setInt(3, candidate.getCity().getId());
        ps.setBytes(4, candidate.getPhoto());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
    }

}
