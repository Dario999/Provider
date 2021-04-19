package uk.singular.dfs.provider.sandbox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.singular.dfs.provider.sandbox.model.metadata.Sport;
import uk.singular.dfs.provider.sandbox.repository.SportRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
public class SportRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SportRepository sportRepository;


    @Test
    public void whenFindById_thenReturnSport(){
        // given
        Sport sport = new Sport();
        sport.setId(1);
        sport.setName("Basketball");

        // when
        Sport found = sportRepository.findById(sport.getId()).orElse(null);

        // then
        assertThat(found.getName()).isEqualTo(sport.getName());

    }

}
