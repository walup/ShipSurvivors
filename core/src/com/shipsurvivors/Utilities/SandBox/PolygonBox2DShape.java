package com.shipsurvivors.Utilities.SandBox;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.shipsurvivors.Utilities.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEO on 22/12/2017.
 */

/*This class neeeds desperately some cleaning, so i'm going to do that. */


public class PolygonBox2DShape extends Polygon{

    Vector2 circleCenter = new Vector2();
    float circleRadius = 0;

    /*This receives a set of vertices and initialize a Polygon with them.
    * but just if the ammount of vertices is even. */
    public PolygonBox2DShape(float[] vertices) {
        if((vertices.length%2) == 1){System.out.println("Verts Impairs"); return;}

        for (int i = 0; i < vertices.length; i+=0) {
            add(new Vertex(vertices[i++], vertices[i++]));
        }
    }

    /*This receives a Box 2D Shape and sends it to the right method: ConstPolygonBox2DShape if it is a ChainShape, and Const
    * PolygonBox2DShape if it is a Polygon Shape. */

    public PolygonBox2DShape(Shape shape){
        if(shape instanceof ChainShape) this.ConstPolygonBox2DShape((ChainShape)shape);
        else if (shape instanceof PolygonShape) this.ConstPolygonBox2DShape((PolygonShape) shape);
    }

    /**
     * This method dissects a ChainShape into its vertices. If the Shape is looped
     * then you just take one of the points (last one or first one) which is redundant
     * and start adding the vertices. What he does is he stores in an array the coordinates
     * of the vertices, if (x1,y1) is the first vertex of the Shape, (x2,y2) is the second and so
     * forth he stores them like [x1,y1,x2,y2,....] and then he constructs the vertices of this
     * polygon using that array. I don't know why he does this,i think it would have been easier
     * just to construct the vertices directly from the Vector objects of the Polygon, but whatever,
     * i'm going to give him the benefit of the doubt.
     * **/

    public void ConstPolygonBox2DShape(ChainShape shape){
        float[] vertices = null;
            vertices = new float[shape.getVertexCount()*2 - 2];
            for(int i = 0, j = 0; i < shape.getVertexCount() - 1; i++){
                Vector2 vector = new Vector2();
                shape.getVertex(i, vector);
                vertices[j++] = vector.x;
                vertices[j++] = vector.y;
            }

        if((vertices.length%2) == 1){System.out.println("Verts Impairs"); return;}
        for (int i = 0; i < vertices.length; i+=0) {
            add(new Vertex(vertices[i++], vertices[i++]));
        }

    }

    /*Does basically the same as the previous method but for a PolygonShape this time */

    public void ConstPolygonBox2DShape(PolygonShape shape){
        float[] vertices = new float[shape.getVertexCount()*2];
        for(int i = 0, j = 0; i < shape.getVertexCount(); i++){
            Vector2 vector = new Vector2();
            shape.getVertex(i, vector);
            vertices[j++] = vector.x;
            vertices[j++] = vector.y;
        }
        if((vertices.length%2) == 1){System.out.println("Verts Impairs"); return;}
        for (int i = 0; i < vertices.length; i+=0) {
            super.add(new Vertex(vertices[i++], vertices[i++]));
        }
    }

    /*A constructor where they give you an array of points, which are the vertices of the PolygonBox2DShape*/

    public PolygonBox2DShape(float[][] points) {
        super(points);
    }

    /*Returns an array of vertices in the way we´ve been working with them in the previous methods
    * [x1,y1,x2,y2,x3,y3,...]*/
    public float[] vertices(){
        float[] verts = new float[vertices*2];
        Vertex v = first;
        for (int i = 0, j = 0; i < vertices; i++) {
            verts[j++] = v.x;
            verts[j++] = v.y;
            v = v.next;
        }
        return verts;
    }
    /*I honestly don't know what this does. */
    public float[] verticesToLoop(){
        int NbIn = 0;
        List<Float> verts = new ArrayList<Float>();
        Vertex v = first;
        for (int i = 0, j = 0; i < vertices - 1; i++) {
            // Here escape the verts that have a square distance > 0.005f * 0.005f to avoid the b2DistanceSquared(v1,v2) > 0.005f * 0.005f expression
            if(v.equals(first) || (b2SquaredDistance(verts.get(j-2), verts.get(j-1), v.x, v.y) > (0.35f))){
                verts.add(v.x);
                j++;
                verts.add(v.y);
                j++;
            }
            if(circleRadius != 0 && CollisionGeometry.isInCircle(circleCenter.x, circleCenter.y, circleRadius, v.x, v.y)){NbIn ++;}
            v = v.next;
        }

        float[] vertsTab = null;

        if((NbIn == 0 || circleRadius == 0) || (NbIn != (vertices -1))){
            vertsTab = new float[verts.size()];
            for(int i = 0; i < verts.size(); i ++){
                vertsTab[i] = verts.get(i).floatValue();
            }
        }else{
            vertsTab = new float[0];
        }
        return vertsTab;
    }

    /** Calculate the union between two polygons */
    public List<PolygonBox2DShape> unionCS(Polygon poly) {
        return this.clipCS(poly, false, false);
    }

    /** Calculate the intersection between two polygons */
    public List<PolygonBox2DShape> intersectionCS(Polygon poly) {
        return this.clipCS(poly, true, true);
    }

    /** Calculate the difference between two polygons */
    public List<PolygonBox2DShape> differenceCS(Polygon poly) {
        return this.clipCS(poly, false, true);
    }



    /*This just does the clip method of polygon but it will return an array of Polygon2DShapes instead of an
    * array of Polygons.*/
    public List<PolygonBox2DShape> clipCS(Polygon poly, boolean b1, boolean b2){
        List<Polygon> rs = super.clip(poly, b1, b2);
        List<PolygonBox2DShape> rsCS = new ArrayList<PolygonBox2DShape>();
        for(int i = 0; i < rs.size(); i++){
            rsCS.add(new PolygonBox2DShape(rs.get(i).points()));
        }
        return rsCS;
    }

    public void circleContact(Vector2 vec, float radius){
        this.circleCenter = vec;
        this.circleRadius = radius;
    }

    private float b2SquaredDistance(float x1,float y1,float x2,float y2){
        Vector2 vec = new Vector2(x1, y1);
        return vec.dst2(x2, y2);
    }
}