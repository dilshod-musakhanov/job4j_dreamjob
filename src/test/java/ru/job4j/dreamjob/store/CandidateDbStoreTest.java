package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.config.JdbcConfiguration;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CandidateDbStoreTest {

    private final CandidateDbStore store = new CandidateDbStore(new JdbcConfiguration().loadPool());

    @AfterEach
    public void cleanTable() throws SQLException {
        BasicDataSource pool = new JdbcConfiguration().loadPool();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("delete from candidate");
        ) {
            ps.execute();
        }
    }

    @Test
    public void whenCreateCandidate() {
        Candidate candidate1 = new Candidate(
                0,
                "Petr",
                "Professional Java Dev",
                new City(1, "Moscow"),
                null,
                LocalDateTime.now()
        );
        store.add(candidate1);
        Candidate candidate2 = store.findById(candidate1.getId());
        assertThat(candidate1.getName(), is(candidate2.getName()));
    }

    @Test
    public void whenFindAllCandidates() {
        Candidate candidate1 = new Candidate(
                0,
                "Petr",
                "Professional Java Dev",
                new City(1, "Moscow"),
                null,
                LocalDateTime.now()
        );
        Candidate candidate2 = new Candidate(
                0,
                "Stas",
                "Professional Java Dev",
                new City(1, "Moscow"),
                null,
                LocalDateTime.now()
        );
        store.add(candidate1);
        store.add(candidate2);
        Collection<Candidate> temp = new ArrayList<>();
        temp.add(candidate1);
        temp.add(candidate2);
        assertThat(store.findAll(), is(temp));
    }
/*
    @Test
    public void whenUpdateCandidate() {
         Candidate candidate1 = new Candidate(
                 0,
                 "Petr",
                 "Professional Java Dev",
                 new City(1, "Moscow"),
                 null,
                 LocalDateTime.now()
         );
         store.add(candidate1);
         int id = candidate1.getId();
         Candidate candidate2 = new Candidate(
                 0,
                 "Stas",
                 "Professional Java Dev",
                 new City(1, "Moscow"),
                 null,
                 LocalDateTime.now()
         );
         store.update(candidate2);
         assertThat(store.findById(id).getName(), is(candidate2.getName()));
     }
*/
}
