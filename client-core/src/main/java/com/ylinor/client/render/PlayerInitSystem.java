package com.ylinor.client.render;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.components.AABB;
import com.ylinor.client.physics.components.CollisionState;
import com.ylinor.client.physics.components.Heading;
import com.ylinor.client.physics.components.InputControlledEntity;
import com.ylinor.client.physics.components.Physics;
import com.ylinor.client.physics.components.Position;
import com.ylinor.client.physics.components.Size;
import com.ylinor.client.physics.components.Velocity;
import com.ylinor.client.physics.systems.PhySystem;

/**
 * Initializes the player entity in the world. Used only for debugging.
 * 
 * @author wytrem
 */
public class PlayerInitSystem extends BaseSystem {

	@Wire
	private PhySystem phySystem;
	private int id;

	@Override
	protected void initialize() {
		Archetype playerArchetype = new ArchetypeBuilder().add(RenderViewEntity.class).add(InputControlledEntity.class)
				.add(Heading.class).add(Position.class).add(Velocity.class).add(EyeHeight.class).add(Size.class)
				.add(AABB.class).add(CollisionState.class).add(Physics.class).build(world);

		Entity player = world.createEntity(playerArchetype);
		player.getComponent(EyeHeight.class).eyePadding.y = 1.65f * 0.85f;
		player.getComponent(Size.class).setSize(0.6f, 1.8f);
		id = player.getId();
	}

	@Override
	protected void processSystem() {
		if (id != -1) {
			phySystem.setPosition(id, 0, 260, 0);
			id = -1;
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
}
