package com.mygdx.systems;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.components.Enemy;
import com.mygdx.components.MovementPath;
import com.mygdx.components.Position;
import com.mygdx.game.Map;
import com.mygdx.pathfind.NavMesh;
import com.mygdx.pathfind.Node;
import com.mygdx.pathfind.NodeList;
import com.mygdx.pathfind.PathFinder;
import com.mygdx.pathfind.PolygonUtils;

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
			Node playerNode = getNodeEntityIsIn(player);
			Node enemyNode = getNodeEntityIsIn(e);

			List<Node> path = pathFinder.findPath(enemyNode, playerNode);	
		}
	}

	private Node getNodeEntityIsIn(Entity e) {
		Position position = positionMapper.get(e);
		Node currentNode = null;
		
		Set<Entry<String,Node>> nodes = nodeList.getNodes().entrySet();
		for (Entry<String,Node> cursor : nodes) {
			Polygon polygon = cursor.getValue().polygon;
			
			if (PolygonUtils.triangleContains(new Vector2(position.x, position.y), polygon)) currentNode = cursor.getValue();
			
			if (currentNode != null) break;
		}
		
		return currentNode;
	}
	
	private MovementPath getMovementPath(Node start, Node end) {
		
		return null;
	}
}
