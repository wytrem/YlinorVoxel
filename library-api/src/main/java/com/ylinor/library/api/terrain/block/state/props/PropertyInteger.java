package com.ylinor.library.api.terrain.block.state.props;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PropertyInteger extends StateProperty<Integer> {

    protected PropertyInteger(String name, int min, int max) {
        super(name, range(min, max));
    }

    @Override
    public String serialize(Integer value) {
        return value.toString();
    }

    @Override
    public Integer unserialize(String serialized) {
        return Integer.parseInt(serialized);
    }

    public static Collection<Integer> range(int max) {
        return range(0, max, 1);
    }
    
    public static Collection<Integer> range(int min, int max) {
        return range(min, max, 1);
    }
    
    public static Collection<Integer> range(int min, int max, int step) {
        Set<Integer> result = new HashSet<>();
        
        for (int i = min; i < max; i += step) {
            result.add(Integer.valueOf(i));
        }
        
        return result;
    }
    
    public static PropertyInteger create(String name, int min, int max) {
        return new PropertyInteger(name, min, max);
    }
}
