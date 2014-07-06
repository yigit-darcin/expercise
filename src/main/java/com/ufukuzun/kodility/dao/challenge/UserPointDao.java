package com.ufukuzun.kodility.dao.challenge;

import com.ufukuzun.kodility.dao.AbstractHibernateDao;
import com.ufukuzun.kodility.domain.challenge.Challenge;
import com.ufukuzun.kodility.domain.challenge.UserPoint;
import com.ufukuzun.kodility.domain.user.User;
import com.ufukuzun.kodility.enums.ProgrammingLanguage;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserPointDao extends AbstractHibernateDao<UserPoint> {

    protected UserPointDao() {
        super(UserPoint.class);
    }

    public UserPoint findByChallengeAndUser(Challenge challenge, User user) {
        Map<String, Object> criteriaParams = new HashMap<>();
        criteriaParams.put("challenge", challenge);
        criteriaParams.put("user", user);

        return findOneBy(criteriaParams);
    }

    public long countForPointGivingCriteria(Challenge challenge, User user, ProgrammingLanguage programmingLanguage) {
        Map<String, Object> criteriaParams = new HashMap<>();
        criteriaParams.put("challenge", challenge);
        criteriaParams.put("user", user);
        criteriaParams.put("programmingLanguage", programmingLanguage);

        return countBy(criteriaParams);
    }

    public long getTotalPointsOf(User user) {
        Criteria criteria = getCriteria();
        criteria.add(Restrictions.eq("user", user));
        return sumBy("pointAmount", criteria);
    }

}