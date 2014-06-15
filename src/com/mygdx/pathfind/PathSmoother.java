package com.mygdx.pathfind;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class PathSmoother {	
	
	public void smoothPath(Vector2 startingPoint, List<Node> path) {
		ArrayList<Point> pathPoints = new ArrayList<Point>();
		
		//int playerSize = 32;
		Vector2[][] portals = buildPortals(path);
		
		Vector2 left = portals[0][0];
		Vector2 right = portals[0][1];
		
		//swap if necessary
		boolean swapped = swapLeftAndRightIfNecessary(left, right, startingPoint);
		
		System.out.println(swapped);
	}
	
	public Vector2[][] buildPortals(List<Node> path) {
		Vector2[][] portals = new Vector2[path.size()-1][1];
		int index = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			Node first = path.get(i);
			Node second = path.get(i + 1);
			Vector2[] portal = getSharedEdge(first.polygon, second.polygon);
			
			portals[index] = portal;
			index++;
		}
		
		return portals;
	}
	
	public boolean swapLeftAndRightIfNecessary(Vector2 left, Vector2 right, Vector2 apex) {
		Vector2 a = new Vector2(left.x - apex.x, left.y - apex.y);
		Vector2 b = new Vector2(right.x - apex.x, right.y - apex.y);
		
		float resultingY = a.y * b.y;
		
		boolean swapped = false;
		if (resultingY < 0) {
			Vector2 temp = left;
			left.x = right.x;
			left.y = right.y;
			right.x = temp.x;
			right.y = temp.y;	
			
			swapped = true;
		}
		
		return swapped;
	}
	
	private Vector2[] getSharedEdge(Polygon p1, Polygon p2) {	
		float[] p2Verts = p2.getTransformedVertices();
		
		Vector2 v1 = new Vector2(p2Verts[0], p2Verts[1]);
		Vector2 v2 = new Vector2(p2Verts[2], p2Verts[3]);
		Vector2 v3 = new Vector2(p2Verts[4], p2Verts[5]);
		
		int index = 0;
		Vector2[] sharedVertices = new Vector2[2];
		
		if (PolygonUtils.triangleContains(v1, p1)) {
			sharedVertices[index] = v1;
			index++;
		}
		if (PolygonUtils.triangleContains(v2, p1)) {
			sharedVertices[index] = v2;
			index++;
		}
		if (PolygonUtils.triangleContains(v3, p1)) {
			sharedVertices[index] = v3;
			index++;
		}
		
		return sharedVertices;
	}
}
