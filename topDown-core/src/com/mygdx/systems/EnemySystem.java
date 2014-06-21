package com.mygdx.systems;

import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.components.Enemy;
import com.mygdx.components.MovementPath;
import com.mygdx.components.Physics;
import com.mygdx.components.Position;
import com.mygdx.components.Size;
import com.mygdx.game.AssetManager;
import com.mygdx.game.Map;
import com.mygdx.pathfind.NavMesh;
import com.mygdx.pathfind.Node;
import com.mygdx.pathfind.NodeList;
import com.mygdx.pathfind.PathFinder;

public class EnemySystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Physics> physicsMapper;
	@Mapper ComponentMapper<Position> positionMapper;
	@Mapper ComponentMapper<MovementPath> movementPathMapper;
	@Mapper ComponentMapper<Size> sizeMapper;

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
		moveToPosition(e);
	}
	
	private void moveToPosition(Entity e) {
		MovementPath enemyMovementPath = movementPathMapper.getSafe(e);

		if (enemyMovementPath == null) {
			Node playerNode = navMesh.getNodeEntityIsIn(playerPosition, nodeList);
			
			Position enemyPosition = positionMapper.get(e);
			Node enemyNode = navMesh.getNodeEntityIsIn(enemyPosition, nodeList);

			List<Node> path = pathFinder.findPath(enemyNode, playerNode);
			nodeList.resetParentsAndCosts();
			
			if (path.size() > 0) {
				e.addComponent(new MovementPath(path));	
			}
		}
		else {
			Node targetNode = enemyMovementPath.getCurrentNode();
			if (targetNode != null) {
				Position enemyPosition = positionMapper.get(e);
				if (Math.abs(Math.round(enemyPosition.x) - targetNode.x) < 5 && Math.abs(Math.round(enemyPosition.y) - targetNode.y) < 5) {
					Body body = physicsMapper.get(e).body;
					body.setLinearVelocity(0, 0);
					
					enemyMovementPath.setCurrentNodeToNextNode();
				}
				else {
					Vector2 directionalVelocity = getDirectionalVelocity(new Vector2(enemyPosition.x, enemyPosition.y), new Vector2(targetNode.x, targetNode.y), 100f);
					Body body = physicsMapper.get(e).body;
					body.applyForceToCenter(directionalVelocity.x, directionalVelocity.y, true);
				}
			}
			else {
				e.removeComponent(MovementPath.class);
			}
		}
	}
	
	private Vector2 getDirectionalVelocity(Vector2 start, Vector2 end, float maxVelocity) {				
		// x component of velocity = velocity * cosine of angle
		// y component of velocity = velocity * sine of angle
		
		double angle = getAngleBetweenTwoPositions(start, end);
		
		float velocityX = maxVelocity * (float)Math.cos(angle);
		float velocityY = maxVelocity * (float)Math.sin(angle);
		
		if ((start.x > end.x && velocityX > 0) ||
			(start.x < end.x && velocityX < 0)) {
			velocityX *= -1;
		}
		if ((start.y > end.y && velocityY > 0) ||
			(start.y < end.y && velocityY < 0)) {
			velocityY *= -1;
		}
		
		return new Vector2(velocityX, velocityY);
	}
	
	private double getAngleBetweenTwoPositions(Vector2 point1, Vector2 point2) {
		float xDiff = point1.x - point2.x;
		float yDiff = point1.y - point2.y;

		return Math.atan2(yDiff, xDiff);
	}
}
