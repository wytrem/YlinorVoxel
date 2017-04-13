package com.ylinor.library.util.ecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EcsTests {
    public static void main(String[] args) {
        World world = new World();
        world.setSystem(new DumpSystem());
        world.setSystem(new DumpSystem2());

        Entity test = world.create();
        world.tick();
        test.set(new TestComp());
        world.tick();
        test.unset(TestComp.class);
        world.tick();
    }
    
    private static final class DumpSystem2 extends IteratingSystem {
        private static final Logger logger = LoggerFactory.getLogger(EcsTests.DumpSystem.class);
        public DumpSystem2() {
            super(Aspect.all(TestComp.class));
        }

        @Override
        protected void process(Entity entity) {
            logger.info("2 : " + entity.get(TestComp.class).toto);
        }
    }
    
    private static final class DumpSystem extends IteratingSystem {
        private static final Logger logger = LoggerFactory.getLogger(EcsTests.DumpSystem.class);
        public DumpSystem() {
            super(Aspect.all());
        }

        @Override
        protected void process(Entity entity) {
            logger.info("1 : " + entity.toString());
        }
    }
    
    private static final class TestComp extends Component {
        public String toto = "aze";

        @Override
        public String toString() {
            return "Toto [toto=" + toto + "]";
        }
    }
}
