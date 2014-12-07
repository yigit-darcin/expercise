package com.kodility.service.challenge;

import com.kodility.dao.challenge.SolutionDao;
import com.kodility.domain.challenge.Challenge;
import com.kodility.domain.challenge.Solution;
import com.kodility.domain.user.User;
import com.kodility.enums.ProgrammingLanguage;
import com.kodility.testutils.builder.ChallengeBuilder;
import com.kodility.testutils.builder.SolutionBuilder;
import com.kodility.testutils.builder.UserBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SolutionServiceTest {

    @InjectMocks
    private SolutionService service;

    @Mock
    private SolutionDao solutionDao;

    @Test
    public void shouldSaveSolution() {
        Solution solution = new SolutionBuilder().build();

        service.saveSolution(solution);

        verify(solutionDao).save(solution);
    }

    @Test
    public void shouldFindSolutionChallengeAndUser() {
        User user = new UserBuilder().id(1L).build();
        Challenge challenge = new ChallengeBuilder().id(2L).build();
        Solution solution = new SolutionBuilder().id(3L).challenge(challenge).user(user).build();

        when(solutionDao.findBy(challenge, user, ProgrammingLanguage.Python)).thenReturn(solution);

        Solution foundSolution = service.getSolutionBy(challenge, user, ProgrammingLanguage.Python);

        assertThat(foundSolution, equalTo(solution));
    }

}