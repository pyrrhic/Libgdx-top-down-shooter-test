package com.mygdx.pathfind;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class PolygonUtils {			
//	public static boolean triangleContains(Vector2 contains, Polygon triangle) {
//		float x = contains.x;
//		float y = contains.y;
//
//		float[] tVerts = triangle.getTransformedVertices();
//		float x1 = tVerts[0];
//		float y1 = tVerts[1];
//
//		float x2 = tVerts[2];
//		float y2 = tVerts[3];
//
//		float x3 = tVerts[4];
//		float y3 = tVerts[5];
//
//		if ((x >= x1) && (x <= x2) && (x >= x3) &&	
//			(y <= y1) && (y >= y2) && (y >= y3)) 
//				return true;
//		else if ((x <= x1) && (x >= x2) && (x >= x3) &&
//				(y >= y1) && (y >= y2) && (y <= y3))
//				return true;
//		else if ((x >= x1) && (x >= x2) && (x <= x3) &&
//				(y >= y1) && (y <= y2) && (y >= y3))
//				return true;
//		else return false;
//	}
	
	
	public static boolean triangleContains(Vector2 currentPoint, Polygon triangle) {
		float[] tVerts = triangle.getTransformedVertices();

		Vector2 p1 = new Vector2(tVerts[0], tVerts[1]);
		Vector2 p2 = new Vector2(tVerts[2], tVerts[3]);
		Vector2 p3 = new Vector2(tVerts[4], tVerts[5]);
		
		float alpha = ((p2.y - p3.y) * (currentPoint.x - p3.x) + (p3.x - p2.x) * (currentPoint.y - p3.y)) /
		              ((p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y));
		float beta = ((p3.y - p1.y) * (currentPoint.x - p3.x) + (p1.x - p3.x) * (currentPoint.y - p3.y)) /
		             ((p2.y - p3.y) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.y - p3.y));
		float gamma = 1.0f - alpha - beta;
		
		boolean barycentricMethod = (alpha > 0 && beta > 0 && gamma > 0) ? true : false;
		
		if (barycentricMethod) {
			return true;
		}
		else if (isPointBetweenTwoOtherPoints(currentPoint, p1, p2) || 
				isPointBetweenTwoOtherPoints(currentPoint, p1, p3) || 
				isPointBetweenTwoOtherPoints(currentPoint, p2, p3)) {
			return true;
		}
		
		return false;
	}
	
	private static boolean isPointBetweenTwoOtherPoints(Vector2 currentPoint, Vector2 p1, Vector2 p2) {		
		float dxc = currentPoint.x - p1.x;
		float dyc = currentPoint.y - p1.y;
		
		float dx1 = p1.x - p2.x;
		float dy1 = p1.y - p2.y;
		
		float cross = dxc * dy1 - dyc * dx1;
		
		if (cross != 0) {
			return false;	
		}
		else { //checked if it was on vector, now need to check if it's inbetween the two points.
			if (Math.abs(dx1) >= Math.abs(dy1))
				return dx1 < 0 ? 
						p1.x <= currentPoint.x && currentPoint.x <= p2.x :
						p2.x <= currentPoint.x && currentPoint.x <= p1.x;
			else
				return dy1 < 0 ? 
						p1.y <= currentPoint.y && currentPoint.y <= p2.y :
						p2.y <= currentPoint.y && currentPoint.y <= p1.y;
		}
	}
}
