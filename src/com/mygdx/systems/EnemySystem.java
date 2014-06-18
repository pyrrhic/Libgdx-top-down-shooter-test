package com.mygdx.systems;

import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.mygdx.components.Enemy;
import com.mygdx.components.MovementPath;
import com.mygdx.components.Position;
import com.mygdx.game.Map;
import com.mygdx.pathfind.NavMesh;
import com.mygdx.pathfind.Node;
import com.mygdx.pathfind.NodeList;
import com.mygdx.pathfind.PathFinder;

public class EnemySystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> positionMapper;
	@Mapper ComponentMapper<MovementPath> movementPathMapper;

	private Entity player;
	private Position playerPosition;

	private Map map;
	private NavMesh navMesh;
	private PathFinder pathFinder;

	private NodeList nodeList;

	@SuppressWarnings("unchecked")
	public EnemySystem(Map map) {
		super(Aspect.getAspectForAll(Enemy.class));

		this.map = map;
		navMesh = new NavMesh();
		pathFinder = new PathFinder();

		nodeList = navMesh.buildNavMesh(map.getTiledMap());
	}

	@Override
	protected void begin() {
		player = world.getManager(TagManager.class).getEntity("PLAYER");
		playerPosition = positionMapper.get(player);
	}

	@Override
	protected void process(Entity e) {
		MovementPath enemyMovementPath = movementPathMapper.getSafe(e);

		if (enemyMovementPath == null) {
			Position playerPosition = positionMapper.get(player);
			Node playerNode = navMesh.getNodeEntityIsIn(playerPosition);
			
			Position enemyPosition = positionMapper.get(e);
			Node enemyNode = navMesh.getNodeEntityIsIn(enemyPosition);

			List<Node> path = pathFinder.findPath(enemyNode, playerNode);
			e.addComponent(new MovementPath(path));
		}
		else {
			
		}
	}
}
