package com.ylinor.library.api.terrain.block.state;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.ylinor.library.api.terrain.block.state.props.StateProperty;
import com.ylinor.library.api.terrain.block.type.BlockType;

public class BlockStateFactory {
	private StateProperty<?>[] possibleProperties;
	private BlockType blockType;
	private Map<Map<StateProperty<?>, Object>, BlockState> statesFromProperties;

	public BlockStateFactory(BlockType blockType, StateProperty<?>... possibleProperties) {
		super();
		this.possibleProperties = possibleProperties;
		this.blockType = blockType;
		this.statesFromProperties = new HashMap<>();
		this.buildStatesFromProperties();
	}
	
	public StateProperty<?>[] getPossibleProperties() {
		return possibleProperties;
	}
	
	public Map<Map<StateProperty<?>, Object>, BlockState> getStatesFromProperties() {
		return statesFromProperties;
	}
	
	public BlockState getStateFromProperties(Map<StateProperty<?>, Object> properties) {
		return statesFromProperties.get(properties);
	}

	public BlockState getOneState() {
		return statesFromProperties.values().iterator().next();
	}
	
	private void buildStatesFromProperties() {
		buildPossibleValuesSets().stream().map(props -> new BlockState(props, this)).forEach(state -> statesFromProperties.put(state.getProperties(), state));
		statesFromProperties.values().forEach(BlockState::fillLinks);
		
		if (statesFromProperties.isEmpty()) {
			statesFromProperties.put(new HashMap<>(), new BlockState(new HashMap<>(), this));
		}
	}
	
	private Collection<Map<StateProperty<?>, Object>> buildPossibleValuesSets() {
		Collection<Map<StateProperty<?>, Object>> result = new HashSet<>();

		for (int i = 0; i < possibleProperties.length; i++) {
			result = addPossibleValuesToMaps(result, possibleProperties[i]);
		}

		return result;
	}

	private Collection<Map<StateProperty<?>, Object>> addPossibleValuesToMaps(
			Collection<Map<StateProperty<?>, Object>> maps, StateProperty<?> property) {
		Collection<Map<StateProperty<?>, Object>> maps2 = new HashSet<>();

		if (maps.isEmpty()) {
			for (Object value : property.possibleValues()) {
				Map<StateProperty<?>, Object> copy = new HashMap<>();
				copy.put(property, value);
				maps2.add(copy);
			}
		} else {
			for (Map<StateProperty<?>, Object> map : maps) {
				for (Object value : property.possibleValues()) {
					Map<StateProperty<?>, Object> copy = new HashMap<>(map);
					copy.put(property, value);
					maps2.add(copy);
				}
			}
		}

		return maps2;
	}

	public static void prettyPrintValuesSet(Map<StateProperty<?>, Object> props) {
		Iterator<Entry<StateProperty<?>, Object>> iterator = props.entrySet().iterator();
		System.out.println("[");
		while (iterator.hasNext()) {
			Entry<StateProperty<?>, Object> entry = iterator.next();
			System.out.println(entry.getKey().name() + " = " + entry.getValue());
		}
		System.out.println("]");
	}

	public BlockType getBlockType() {
		return blockType;
	}
}
