package com.ylinor.library.api.terrain.block.state;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.ylinor.library.api.terrain.block.BlockAttributes;
import com.ylinor.library.api.terrain.block.BlockBehaviour;
import com.ylinor.library.api.terrain.block.state.props.StateProperty;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.Pair;


public class BlockState {
    private final SortedMap<StateProperty<?>, Object> properties;
    private final BlockStateFactory blockStateFactory;
    private final Map<Pair<StateProperty<?>, Object>, BlockState> links;
    private BlockType type;
    private BlockAttributes attributes;
    private BlockBehaviour behaviour;

    public BlockState(SortedMap<StateProperty<?>, Object> properties, BlockStateFactory blockStateFactory) {
        this.properties = new TreeMap<>(properties);
        this.blockStateFactory = blockStateFactory;
        this.links = new HashMap<>();
        this.type = blockStateFactory.getBlockType();
        attributes = new BlockAttributes(type.getDefaultAttributes());
        behaviour = new BlockBehaviour(this);
    }

    public BlockBehaviour getBehaviour() {
        return behaviour;
    }

    public BlockAttributes getAttributes() {
        return attributes;
    }

    public BlockType getBlockType() {
        return type;
    }

    protected void fillLinks() {
        Map<StateProperty<?>, Object> temp = new HashMap<>();

        for (StateProperty<?> linkProperty : blockStateFactory.getPossibleProperties()) {
            for (Object value : linkProperty.possibleValues()) {
                temp.clear();
                temp.putAll(properties);
                temp.put(linkProperty, value);
                Pair<StateProperty<?>, Object> link = new Pair<StateProperty<?>, Object>(linkProperty, value);
                links.put(link, blockStateFactory.getStateFromProperties(temp));
            }
        }
    }

    public SortedMap<StateProperty<?>, Object> getProperties() {
        return properties;
    }

    public String propertiesToString() {
        return properties.entrySet()
                         .stream()
                         .map(entry -> entry.getKey()
                                            .equality(entry.getValue()))
                         .sorted()
                         .collect(Collectors.joining(","));
    }

    @SuppressWarnings("unchecked")
    public <V> V get(StateProperty<V> stateProperty) {
        return (V) properties.get(stateProperty);
    }

    public <V> BlockState with(StateProperty<V> stateProperty, V value) {
        return links.get(new Pair<StateProperty<V>, V>(stateProperty, value));
    }

    @Override
    public String toString() {
        return "BlockState [" + properties + "]";
    }
}
