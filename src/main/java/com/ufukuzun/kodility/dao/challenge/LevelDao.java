package com.ufukuzun.kodility.dao.challenge;

import com.ufukuzun.kodility.dao.AbstractHibernateDao;
import com.ufukuzun.kodility.domain.challenge.Level;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LevelDao extends AbstractHibernateDao<Level> {

    protected LevelDao() {
        super(Level.class);
    }

    public List<Level> findAllOrdered() {
        return getCriteria().addOrder(Order.asc("priority")).list();
    }

}