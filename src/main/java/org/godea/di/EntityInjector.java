package org.godea.di;

import org.reflections.Reflections;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityInjector {
    public List<Class<?>> inject() {
        Reflections reflections = new Reflections("org.godea.models");
        return new ArrayList<>(reflections.getTypesAnnotatedWith(Entity.class));
    }
}
