package com.ylinor.library.util.ecs.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ylinor.library.util.ecs.component.Component;


public class Aspect {

    private List<Class<? extends Component>> allTypes;
    private List<Class<? extends Component>> oneTypes;
    private List<Class<? extends Component>> excludeTypes;

    public boolean matches(Entity entity) {

        if (entity.componentsTypes()
                  .stream()
                  .anyMatch(excludeTypes::contains)) {
            return false;
        }

        if (!oneTypes.isEmpty() && entity.componentsTypes()
                                         .stream()
                                         .anyMatch(oneTypes::contains)) {
            return true;
        }

        return allTypes.isEmpty() ? true : entity.componentsTypes()
                                                 .containsAll(allTypes);
    }

    @SafeVarargs
    public static final Aspect.Builder all(Class<? extends Component>... classes) {
        return new Builder().all(classes);
    }

    @SafeVarargs
    public static final Aspect.Builder one(Class<? extends Component>... classes) {
        return new Builder().one(classes);
    }

    @SafeVarargs
    public static final Aspect.Builder exclude(Class<? extends Component>... classes) {
        return new Builder().exclude(classes);
    }

    public static final class Builder {
        private List<Class<? extends Component>> allTypes;
        private List<Class<? extends Component>> oneTypes;
        private List<Class<? extends Component>> excludeTypes;

        public Builder() {
            allTypes = new ArrayList<>(2);
            oneTypes = new ArrayList<>(1);
            excludeTypes = new ArrayList<>(1);
        }

        @SafeVarargs
        public final Builder all(Class<? extends Component>... classes) {
            allTypes.addAll(Arrays.asList(classes));
            return this;
        }

        @SafeVarargs
        public final Builder one(Class<? extends Component>... classes) {
            oneTypes.addAll(Arrays.asList(classes));
            return this;
        }

        @SafeVarargs
        public final Builder exclude(Class<? extends Component>... classes) {
            excludeTypes.addAll(Arrays.asList(classes));
            return this;
        }

        public final Aspect build() {
            Aspect aspect = new Aspect();
            aspect.allTypes = allTypes;
            aspect.oneTypes = oneTypes;
            aspect.excludeTypes = excludeTypes;
            return aspect;
        }
    }
}
