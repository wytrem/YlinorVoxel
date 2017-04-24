package com.ylinor.library.api.terrain.block.state;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.ylinor.library.api.terrain.block.state.props.StateProperty;
import com.ylinor.library.api.terrain.block.type.BlockType;


public class BlockStateFactory {
    private StateProperty<?>[] possibleProperties;
    private BlockType blockType;
    private Map<SortedMap<StateProperty<?>, Object>, BlockState> statesFromProperties;

    public BlockStateFactory(BlockType blockType, StateProperty<?>... possibleProperties) {
        super();
        this.possibleProperties = possibleProperties;
        this.blockType = blockType;
        this.statesFromProperties = new HashMap<>();
        this.buildStatesFromProperties();
    }

    public Collection<BlockState> getPossibleStates() {
        return statesFromProperties.values();
    }

    public StateProperty<?>[] getPossibleProperties() {
        return possibleProperties;
    }

    public Map<SortedMap<StateProperty<?>, Object>, BlockState> getStatesFromProperties() {
        return statesFromProperties;
    }

    public BlockState getStateFromProperties(Map<StateProperty<?>, Object> properties) {
        return statesFromProperties.get(properties);
    }

    public BlockState getOneState() {
        return statesFromProperties.values().iterator().next();
    }

    private void buildStatesFromProperties() {
        buildPossibleValuesSets().stream()
                                 .map(props -> new BlockState(props, this))
                                 .forEach(state -> statesFromProperties.put(state.getProperties(), state));
        statesFromProperties.values().forEach(BlockState::fillLinks);

        if (statesFromProperties.isEmpty()) {
            statesFromProperties.put(new TreeMap<>(), new BlockState(new TreeMap<>(), this));
        }
    }

    private Collection<SortedMap<StateProperty<?>, Object>> buildPossibleValuesSets() {
        Collection<SortedMap<StateProperty<?>, Object>> result = new HashSet<>();

        for (int i = 0; i < possibleProperties.length; i++) {
            result = addPossibleValuesToMaps(result, possibleProperties[i]);
        }

        return result;
    }

    private Collection<SortedMap<StateProperty<?>, Object>> addPossibleValuesToMaps(Collection<SortedMap<StateProperty<?>, Object>> maps, StateProperty<?> property) {
        Collection<SortedMap<StateProperty<?>, Object>> maps2 = new HashSet<>();

        if (maps.isEmpty()) {
            for (Object value : property.possibleValues()) {
                SortedMap<StateProperty<?>, Object> copy = new TreeMap<>();
                copy.put(property, value);
                maps2.add(copy);
            }
        }
        else {
            for (Map<StateProperty<?>, Object> map : maps) {
                for (Object value : property.possibleValues()) {
                    SortedMap<StateProperty<?>, Object> copy = new TreeMap<>(map);
                    copy.put(property, value);
                    maps2.add(copy);
                }
            }
        }

        return maps2;
    }

    public void print() {
        System.out.println("possible = " + Arrays.toString(possibleProperties));
        buildPossibleValuesSets().forEach(BlockStateFactory::prettyPrintValuesSet);
    }

    public static void prettyPrintValuesSet(Map<StateProperty<?>, Object> props) {
        Iterator<Entry<StateProperty<?>, Object>> iterator = props.entrySet()
                                                                  .iterator();
        System.out.println("[");
        while (iterator.hasNext()) {
            Entry<StateProperty<?>, Object> entry = iterator.next();
            System.out.println(entry.getKey()
                                    .name() + " = " + entry.getValue());
        }
        System.out.println("]");
    }

    public BlockType getBlockType() {
        return blockType;
    }
}
