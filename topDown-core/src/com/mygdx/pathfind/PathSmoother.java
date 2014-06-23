package com.mygdx.pathfind;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class PathSmoother {		
	public void smoothPath(Vector2 startingPoint, List<Node> path) {
		ArrayList<Point> pathPoints = new ArrayList<Point>();
		
		//int playerSize = 32;
		Vector2[][] portals = buildPortals(path);
		
		Vector2 leftEdge = portals[0][0];
		Vector2 rightEdge = portals[0][1];
		
		//swap if necessary
		swapLeftAndRightIfNecessary(leftEdge, rightEdge, startingPoint);
		
		Vector2 leftFunnel = leftEdge;
		Vector2 rightFunnel = rightEdge;
		
		for (int i = 1; i < portals.length; i++) {
			leftEdge = portals[i][0];
			rightEdge = portals[i][1];
			
			swapLeftAndRightIfNecessary(leftEdge, rightEdge, startingPoint);
			
			//check left side
			if(areLeftAndRightBackwards(startingPoint, leftEdge, rightEdge)) {
				//restart
			}
			else if(isEdgeWithinFunnel(leftFunnel, rightFunnel, leftEdge)) {
				leftFunnel = leftEdge; // shrink or maintain the funnel
				//left index ++   keep track for when we restart
			}
			
			//check right side
		}
		
		//some special case handling for end point being in funnel. 
		//http://yushutong.files.wordpress.com/2013/06/screen-shot-2013-06-16-at-12-54-18-pm.png
		//look at http://yushutong.files.wordpress.com/2013/06/screen-shot-2013-06-16-at-12-54-33-pm.png
	}
	
	private boolean isEdgeWithinFunnel(Vector2 leftFunnel, Vector2 rightFunnel, Vector2 edge) {
		/**
		 * line between left and right funnel.
		 * angle of line to new edge
		 * if angle > 90, it's out.
		 */
		// cos(c) = (a2 + b2 - c2) / 2ab
		// C = inverse of cos((a2 + b2 - c2) / 2ab)
		
		double a = distanceBetweenTwoPoints(leftFunnel, rightFunnel);
		double b = distanceBetweenTwoPoints(leftFunnel, edge);
		double c = distanceBetweenTwoPoints(rightFunnel, edge);
		double angleC = Math.acos((a * a + b * b - c * c) / 2 * a * b);
		
		return (angleC <= 90) ? true : false;
	}
	
	private double distanceBetweenTwoPoints(Vector2 a, Vector2 b) {		
		return Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
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
	
	public void drawPortals(OrthographicCamera camera, Vector2[][] portals) {
		ShapeRenderer portalRenderer = new ShapeRenderer();
		portalRenderer.setProjectionMatrix(camera.combined);
		portalRenderer.begin(ShapeType.Line);
		portalRenderer.setColor(Color.RED);
		
		for(int i = 0; i < portals.length; i++) {
			portalRenderer.line(portals[i][0].x, portals[i][0].y, portals[i][1].x, portals[i][1].y);
		}
		
		portalRenderer.end();
		portalRenderer.dispose();
	}
	
	private boolean areLeftAndRightBackwards(Vector2 apex, Vector2 left, Vector2 right) {
		Vector2 a = new Vector2(left.x - apex.x, left.y - apex.y);
		Vector2 b = new Vector2(right.x - apex.x, right.y - apex.y);
		
		float resultingY = a.y * b.y;
		
		return(resultingY <= 0.0f) ? true : false;
	}
	
	private void swapLeftAndRightIfNecessary(Vector2 left, Vector2 right, Vector2 apex) {		
		boolean swapped = areLeftAndRightBackwards(apex, left, right);
		if (swapped) {
			Vector2 temp = left;
			left.x = right.x;
			left.y = right.y;
			right.x = temp.x;
			right.y = temp.y;	
		}
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
