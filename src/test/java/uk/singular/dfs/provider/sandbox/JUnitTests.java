package uk.singular.dfs.provider.sandbox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.singular.dfs.provider.sandbox.model.metadata.Sport;
import uk.singular.dfs.provider.sandbox.repository.SportRepository;
import uk.singular.dfs.provider.sandbox.services.impl.SportServiceImpl;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class JUnitTests {

    @Mock
    SportRepository repository;

    @InjectMocks
    SportServiceImpl sportsService;

    Sport sport;


    @Before
    public void init(){
        when(repository.save(sport)).thenReturn(sport);
    }

    @Test
    public void testSportSave()  {

        sport = new Sport();
        sport.setName("Football");
        sport.setId(1);
        sport.setCountries(new ArrayList<>());

        //when
        sportsService.addSport(sport);
        //then
        verify(repository, times(1)).save(any());

        Sport savedSport = repository.save(sport);
        assertEquals(sport,savedSport);


    }

}
