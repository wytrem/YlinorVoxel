package com.ylinor.library.util.ecs.test;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ylinor.library.util.ecs.WorldConfiguration;
import com.ylinor.library.util.ecs.component.Component;
import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.BaseSystem;
import com.ylinor.library.util.ecs.system.IteratingSystem;
import com.ylinor.library.util.ecs.World;


public class EcsTests {

    static void testEcs() {
        WorldConfiguration conf = new WorldConfiguration();
        conf.with(PrintSystem.class).with(ComputeSystem.class);
        World world = conf.build();

        Entity test = world.create();
        world.tick();
        test.set(new TestComp());
        world.tick();
        test.unset(TestComp.class);
        world.tick();
        test.delete();
        world.tick();
    }
    
    @Singleton
    public static final class PrintSystem extends IteratingSystem {
        
        @Inject
        ComputeSystem computeSystem;
        
        @Inject
        Truc truc;
        
        public PrintSystem() {
            super(Aspect.all(TestComp.class));
        }

        @Override
        protected void process(Entity entity) {
            System.out.println(entity.get(TestComp.class));
            computeSystem.test(entity.get(TestComp.class));
            System.out.println(truc.prop);
        }
    }
    
    @Singleton
    public static final class ComputeSystem extends BaseSystem {

        @Override
        protected void processSystem() {
            System.out.println("Computing general...");
        }
        
        public void test(TestComp comp) {
            System.out.println("Tested with " + comp);
        }
    }

    public static void main(String[] args) {
        testEcs();
    }
    
    public static class Truc {
        public final String prop = UUID.randomUUID().toString();
    }

    private static final class TestComp extends Component {
        public String toto = "aze";

        @Override
        public String toString() {
            return "Toto [toto=" + toto + "]";
        }
    }
}
