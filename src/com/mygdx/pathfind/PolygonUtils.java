package com.mygdx.pathfind;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class PolygonUtils {		
	public static boolean triangleContains(Vector2 contains, Polygon triangle) {
		float x = contains.x;
		float y = contains.y;
		
		float[] tVerts = triangle.getTransformedVertices();
		float x1 = tVerts[0];
		float y1 = tVerts[1];
		
		float x2 = tVerts[2];
		float y2 = tVerts[3];
		
		float x3 = tVerts[4];
		float y3 = tVerts[5];
		
		if ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2)) return true;
		else if ((x >= x1) && (x <= x3) && (y >= y1) && (y <= y3)) return true;
		else if ((x >= x2) && (x <= x3) && (y >= y2) && (y <= y3)) return true;
		else if ((x <= x1) && (x >= x2) && (y <= y1) && (y >= y2)) return true;
		else if ((x <= x1) && (x >= x3) && (y <= y1) && (y >= y3)) return true;
		else if ((x <= x2) && (x >= x3) && (y <= y2) && (y >= y3)) return true;
		else return false;
	}
}
