package com.expercise.testutils.builder;

import com.expercise.domain.PrioritizedEntity;

public abstract class AbstractPrioritizedEntityBuilder<T extends PrioritizedEntity, B extends AbstractPrioritizedEntityBuilder> extends AbstractEntityBuilder<T, B> {

    private int priority;

    protected abstract T getInstance();

    @Override
    protected T doBuild() {
        T entity = getInstance();
        entity.setPriority(priority);
        return entity;
    }

    public B priority(int priority) {
        this.priority = priority;
        return (B) this;
    }

}
