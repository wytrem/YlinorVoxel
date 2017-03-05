package com.ylinor.library.api.terrain.block.state;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ylinor.library.api.terrain.block.BlockAttributes;
import com.ylinor.library.api.terrain.block.BlockBehaviour;
import com.ylinor.library.api.terrain.block.state.props.StateProperty;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.Pair;

public class BlockState {
	private final Map<StateProperty<?>, Object> properties;
	private final BlockStateFactory blockStateFactory;
	private final Map<Pair<StateProperty<?>, Object>, BlockState> links;
	private BlockType type;
	private BlockAttributes attributes;
	private BlockBehaviour behaviour;
	
	public BlockState(Map<StateProperty<?>, Object> properties, BlockStateFactory blockStateFactory) {
		this.properties = Collections.unmodifiableMap(properties);
		this.blockStateFactory = blockStateFactory;
		this.links = new HashMap<>();
		this.type = blockStateFactory.getBlockType();
		attributes = new BlockAttributes(type.getBlockMaterial(), type.getBlockMapColor());
		attributes.set(type.getDefaultAttributes());
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
	
	public Map<StateProperty<?>, Object> getProperties() {
		return properties;
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
