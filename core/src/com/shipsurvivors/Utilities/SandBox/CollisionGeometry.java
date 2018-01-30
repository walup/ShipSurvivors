package com.shipsurvivors.Utilities.SandBox;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by SEO on 23/12/2017.
 */

/*Ok, i don't want to be an asshole, but the code for SandBox in general is fucking messy so, let's dissect it, rename
some variables and so on.
* */

public class CollisionGeometry {
    /*This will tell you if a point with coordinates x and y , is inside a circle centered at (circleX,circleY) and with radiuis
* r*/

    public static boolean isInCircle(float circleX, float circleY, float r, float x, float y){
        double d = Math.sqrt((circleX - x)*(circleX - x) + (circleY- y)*(circleY- y));
        return d <= r;

    }

    /*it gives you the difference between two angles, in the range [-180,180]*/

    public static double differenceBetweenAngles(double firstAngle, double secondAngle)
    {
        double difference = secondAngle - firstAngle;
        while (difference < -180) difference += 360;
        while (difference > 180) difference -= 360;
        return difference;
    }


    //Returns the distance between two points as a double
    public static double distanceBetween2Points(Vector2 p1, Vector2 p2){
        return distanceBetween2Points(p1.x, p1.y, p2.x, p2.y);
    }

    //Returns the distance between two points as a float

    public static float distanceBetween2Points(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }


    public static float distanceBetweenPointAndRectangle(float px, float py, float x, float y, float width, float height){
        float dx = Math.max(x - px, px - (x + width));
        float dy = Math.max(y - py, py - (y + height));
        return (float) Math.sqrt(dx*dx + dy*dy);
    }


    //Lets you see if one circle is enclosing another circle.
    public static boolean CircleCircle(Vector2 vec, float r, Vector2 vec2, float r2){
        Circle c = new Circle(vec, r);
        Circle c2 = new Circle(vec2, r2);
        return Intersector.overlaps(c, c2) ;
    }


    public static boolean isPointInTriangle(float px, float py, float ax, float ay, float bx, float by, float cx, float cy){
        return Intersector.isPointInTriangle(px,py,ax,ay,bx,by,cx,cy);
    }

    /*This method approximates a circle with center at (centerX,centerY) using a polygon of num_segments*/

    public static float[] approxCircle(float centerX, float centerY, float r, int num_segments){
        float angle = 2 * MathUtils.PI / num_segments;
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);
        float x = r, y = 0;
        float[] verts = new float[num_segments*2];
        int j= 0;
        for (int i = 0; i < num_segments; i++) {
            float temp = x;
            //Rotation operations, you start at [0,r] and rotate

            x = cos * x - sin * y;
            y = sin * temp + cos * y;

            verts[j] = centerX + x;
            j++;
            verts[j] = centerY + y;
            j++;
        }
        return verts;
    }






    /*This checks if a polygon of vertices verts is contained  within a circle*/
    public static boolean isPolygonInCircle(float[] verts, Vector2 center, float radius) {
        for(int i = 0; i < verts.length; i+=2){
            if(!CollisionGeometry.isInCircle(center.x, center.y, radius, verts[i], verts[i+1])){
                return false;
            }
        }
        return true;
    }


    /*Checks if at least one of the points in a polygon is contained in a circle of given center and radius*/
    public static boolean isPolygonPartiallyInCircle(float[] verts, Vector2 center, float radius) {
        for(int i = 0; i < verts.length; i+=2){
            if(CollisionGeometry.isInCircle(center.x, center.y, radius, verts[i], verts[i+1])){
                return true;
            }
        }
        return false;
    }
}