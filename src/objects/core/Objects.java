package objects.core;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

class Point3D {
   public int x, y, z;
   public Point3D( int X, int Y, int Z ) {
      x = X;  y = Y;  z = Z;
   }
}

class Edge {
   public int a, b;
   public Edge( int A, int B ) {
      a = A;  b = B;
   }
}

public class Objects extends Applet implements MouseListener, MouseMotionListener, KeyListener {

   int width, height;
   int mx, my;  // the most recently recorded mouse coordinates

   Image backbuffer;
   Graphics backg;

   double azimuth = 0, elevation = 0;

   Point3D[] vertices;
   Edge[] edges;

   public void init() {
	   
	   Random ran = new Random();
	   int num = ran.nextInt(5) - 2;
	   
	   
	   setSize(800, 800);
	   
      width = getSize().width;
      height = getSize().height;

      vertices = new Point3D[11];
      						//    X,  Y,  Z
      vertices[0] = new Point3D( -2, -1, -2 );	// Bottom 
      vertices[1] = new Point3D( -2, -1,  2 );	// Bottom 
      vertices[2] = new Point3D(  0,  2,  0 );	// Top
      vertices[3] = new Point3D(  0,  2,  0 );	// Top
      vertices[4] = new Point3D(  2, -1, -2 );	// Bottom 
      vertices[5] = new Point3D(  2, -1,  2 );	// Bottom 
      vertices[6] = new Point3D(  0,  2,  0 );	// Top
      vertices[7] = new Point3D(  0,  2,  0 );	// Top
      vertices[8] = new Point3D(  0,  3,  0 );	// Top
      vertices[9] = new Point3D(  0,  3,  0 );	// Top
      vertices[10] = new Point3D( 0,  3,  0 );
      
    

      edges = new Edge[16];
      edges[ 0] = new Edge( 0, 1 );
      edges[ 1] = new Edge( 0, 2 );
      edges[ 2] = new Edge( 0, 4 );
      edges[ 3] = new Edge( 1, 3 );
      edges[ 4] = new Edge( 1, 5 );
      edges[ 5] = new Edge( 2, 3 );
      edges[ 6] = new Edge( 2, 6 );
      edges[ 7] = new Edge( 3, 7 );
      edges[ 8] = new Edge( 4, 5 );
      edges[ 9] = new Edge( 4, 6 );
      edges[10] = new Edge( 5, 7 );
      edges[11] = new Edge( 6, 7 );
      edges[12] = new Edge( 7, 8 );
      edges[13] = new Edge( 4, 9 );
      edges[14] = new Edge( 5, 9 );
      
      edges[15] = new Edge( 7, 9);
     

      backbuffer = createImage( width, height );
      backg = backbuffer.getGraphics();
      drawWireframe( backg );

      addMouseListener( this );
      addMouseMotionListener( this );
   }

   void drawWireframe( Graphics g ) {

      // compute coefficients for the projection
      double theta = Math.PI * azimuth / 180.0;
      double phi = Math.PI * elevation / 180.0;
      float cosT = (float)Math.cos( theta ), sinT = (float)Math.sin( theta );
      float cosP = (float)Math.cos( phi ), sinP = (float)Math.sin( phi );
      float cosTcosP = cosT*cosP, cosTsinP = cosT*sinP,
             sinTcosP = sinT*cosP, sinTsinP = sinT*sinP;

      // project vertices onto the 2D viewport
      Point[] points;
      points = new Point[ vertices.length ];
      int j;
      int scaleFactor = width/4;
      float near = 3;  // distance from eye to near plane
      float nearToObj = 2.5f;  // distance from near plane to center of object
      for ( j = 0; j < vertices.length; ++j ) {
         int x0 = vertices[j].x;
         int y0 = vertices[j].y;
         int z0 = vertices[j].z;

         // compute an orthographic projection
         float x1 = cosT*x0 + sinT*z0;
         float y1 = -sinTsinP*x0 + cosP*y0 + cosTsinP*z0;

         // now adjust things to get a perspective projection
         float z1 = cosTcosP*z0 - sinTcosP*x0 - sinP*y0;
         x1 = x1*near/(z1+near+nearToObj);
         y1 = y1*near/(z1+near+nearToObj);

         // the 0.5 is to round off when converting to int
         points[j] = new Point(
            (int)(width/2 + scaleFactor*x1 + 0.5),
            (int)(height/2 - scaleFactor*y1 + 0.5)
         );
      }

      Random ran = new Random();
      // draw the wireframe
      g.setColor( Color.black );
      g.fillRect( 0, 0, width, height );
      g.setColor( new Color(255, 255, 255));
	  for ( j = 0; j < edges.length; ++j ) {
	         g.drawLine(
	            points[ edges[j].a ].x, points[ edges[j].a ].y,
	            points[ edges[j].b ].x, points[ edges[j].b ].y
	         );
	      }
   	  }

   public void mouseEntered( MouseEvent e ) { }
   public void mouseExited( MouseEvent e ) { }
   public void mouseClicked( MouseEvent e ) { }
   public void mousePressed( MouseEvent e ) {
      mx = e.getX();
      my = e.getY();
      e.consume();
   }
   public void mouseReleased( MouseEvent e ) { }
   public void mouseMoved( MouseEvent e ) { }
   public void mouseDragged( MouseEvent e ) {
      // get the latest mouse position
      int new_mx = e.getX();
      int new_my = e.getY();

      // adjust angles according to the distance travelled by the mouse
      // since the last event
      azimuth -= new_mx - mx;
      elevation += new_my - my;

      // update the backbuffer
      drawWireframe( backg );

      // update our data
      mx = new_mx;
      my = new_my;

      repaint();
      e.consume();
   }

   public void update( Graphics g ) {
      g.drawImage( backbuffer, 0, 0, this );
      showStatus("Elev: "+elevation+" deg, Azim: "+azimuth+" deg");
   }

   public void paint( Graphics g ) {
      update( g );
   }

@Override
public void keyPressed(KeyEvent e) {
	if(e.getKeyCode() == KeyEvent.VK_C){
		azimuth = 0; 
		elevation = 0;
		repaint();
	}
}

@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}
}