package sim;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JPanel;

public class Panel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 1000;
	static final int SCREEN_HEIGHT = 1000;
	double realTimeMultiplayer =2678400;
	int fps = 144;
	double precision = 1440; //how many times the calculations are run per day of animation time
	boolean drawOrbits = true;
	int delay = 1000/fps;
	double timeDelta = 86400/precision;
	double pixelSize = 500000000;
	double G = 6.67430 * Math.pow(10, -11);
	Timer timer;
	ArrayList<Point> points = new ArrayList<Point>();
	ArrayList<CelestialBody> CelestialBodies = new ArrayList<CelestialBody>();
	CelestialBody Sun = new CelestialBody((1.98855*Math.pow(10, 30)), 50, pixelSize*SCREEN_WIDTH/2, pixelSize*SCREEN_HEIGHT/2, 0, 0, Color.yellow);
	CelestialBody Earth = new CelestialBody((5.972*Math.pow(10, 24)), 15, (pixelSize*SCREEN_WIDTH/2)+96473452195.0, (pixelSize*SCREEN_HEIGHT/2)+112692778872.0, -22622.5959, 19366.635, Color.blue);
	CelestialBody Mars = new CelestialBody((0.64169*Math.pow(10, 24)), 10, (pixelSize*SCREEN_WIDTH/2)-197750240379.0, (pixelSize*SCREEN_HEIGHT/2)+90544167108.0, -8618.46, -23679.872, Color.orange);
	CelestialBody Venus = new CelestialBody((4.867*Math.pow(10, 24)), 15, (pixelSize*SCREEN_WIDTH/2)+13247287086.0, (pixelSize*SCREEN_HEIGHT/2)+106767740453.0, -34335.526,7285.715, Color.gray);
	CelestialBody Mercury = new CelestialBody((3.285*Math.pow(10, 23)), 10, (pixelSize*SCREEN_WIDTH/2)-62592229393.0, (pixelSize*SCREEN_HEIGHT/2)+31588806454.0, -19797.69, -33717.358, Color.darkGray);
	CelestialBody Jupiter = new CelestialBody((1.898*Math.pow(10, 27)), 20, (pixelSize*SCREEN_WIDTH/2)-472608778069.0, (pixelSize*SCREEN_HEIGHT/2)-575943291703.0, 10520.0, -8619.0, Color.orange);
	
	Panel(){
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		startSimulation();
	}
	public void startSimulation() {
		
		timer = new Timer(delay, this);
		timer.start();
		CelestialBodies.add(Sun);
		CelestialBodies.add(Earth);
		CelestialBodies.add(Mars);
		CelestialBodies.add(Venus);
		CelestialBodies.add(Mercury);
		CelestialBodies.add(Jupiter);
	}
	public void draw(Graphics g, ArrayList<CelestialBody> CelestialBodies) {
		for(int i = 0; i<CelestialBodies.size();i++) {
			g.setColor(CelestialBodies.get(i).getColor());
			g.fillOval((int)(CelestialBodies.get(i).getX()/pixelSize)-(int)CelestialBodies.get(i).getSize()/2, (int)(CelestialBodies.get(i).getY()/pixelSize)-(int)CelestialBodies.get(i).getSize()/2, (int)CelestialBodies.get(i).getSize(), (int)CelestialBodies.get(i).getSize());
		}
	}
	public void paint(Graphics g) {
		super.paintComponent(g);
		if(drawOrbits) {
			for(CelestialBody c : CelestialBodies) {
				if(points.size()>100000) {
					points.remove(0);
				}
				points.add(new Point((int)(c.getX()/pixelSize),(int)(c.getY()/pixelSize)));
			}
			g.setColor(Color.white);
			for(Point p : points) {
				g.drawLine(p.getX(), p.getY(), p.getX(), p.getY());
			}
		}
		draw(g, CelestialBodies);
		for(int i =0;i<(realTimeMultiplayer*precision)/(fps*86400);i++) {
			simulateGravity(CelestialBodies);
		}
			
	}
	public void simulateGravity(ArrayList<CelestialBody> CelestialBodies) {
		ArrayList<CelestialBody> initialValues = new ArrayList<CelestialBody>(CelestialBodies);
		double distance=0;
		double positionDifferenceX;
		double positionDifferenceY;
		double newDistance;
		double newPositionDifferenceX;
		double newPositionDifferenceY;
		double Vx=0;
		double Vy=0;
		double x =0;
		double y =0;
		for(int i = 1; i<CelestialBodies.size(); i++) {
			for(int j = 0; j<CelestialBodies.size(); j++) {
				if(i!=j) {
					positionDifferenceX = initialValues.get(j).getX()-initialValues.get(i).getX();
					positionDifferenceY = initialValues.get(j).getY()-initialValues.get(i).getY();
					distance = Math.sqrt(Math.pow(positionDifferenceX, 2)+Math.pow(positionDifferenceY, 2));
					Vx+=initialValues.get(j).getMass()*positionDifferenceX/Math.pow(distance, 3);
					Vy+=initialValues.get(j).getMass()*positionDifferenceY/Math.pow(distance, 3);
					x+=initialValues.get(j).getMass()*positionDifferenceX/(2*Math.pow(distance, 3));
					y+=initialValues.get(j).getMass()*positionDifferenceY/(2*Math.pow(distance, 3));
				}
			}
			Vx*=G*timeDelta;
			Vy*=G*timeDelta;
			x*=G*timeDelta*timeDelta;
			y*=G*timeDelta*timeDelta;
			
			CelestialBodies.set(i, new CelestialBody(
					CelestialBodies.get(i).getMass(),
					CelestialBodies.get(i).getSize(),
					x+(initialValues.get(i).getVx()*timeDelta)+initialValues.get(i).getX(),
					y+(initialValues.get(i).getVy()*timeDelta)+initialValues.get(i).getY(),
					initialValues.get(i).getVx()+Vx,
					initialValues.get(i).getVy()+Vy,
					CelestialBodies.get(i).getColor()));
			
			Vx=Vy=x=y=0;
		}
		
		for(int i = 0; i<CelestialBodies.size(); i++) {
			for(int j = 0; j<CelestialBodies.size(); j++) {
				if(i!=j) {
					
					positionDifferenceX = initialValues.get(j).getX()-initialValues.get(i).getX();
					positionDifferenceY = initialValues.get(j).getY()-initialValues.get(i).getY();
					distance = Math.sqrt(Math.pow(positionDifferenceX, 2)+Math.pow(positionDifferenceY, 2));
					
					newPositionDifferenceX = CelestialBodies.get(j).getX()-CelestialBodies.get(i).getX();
					newPositionDifferenceY = CelestialBodies.get(j).getY()-CelestialBodies.get(i).getY();
					newDistance = Math.sqrt(Math.pow(newPositionDifferenceX, 2)+Math.pow(newPositionDifferenceY, 2));
					
					Vx+=initialValues.get(j).getMass()*positionDifferenceX/(2*Math.pow(distance, 3));
					Vx+=CelestialBodies.get(j).getMass()*newPositionDifferenceX/(2*Math.pow(newDistance, 3));
					
					Vy+=initialValues.get(j).getMass()*positionDifferenceY/(2*Math.pow(distance, 3));
					Vy+=CelestialBodies.get(j).getMass()*newPositionDifferenceY/(2*Math.pow(newDistance, 3));
					
					x+=initialValues.get(j).getMass()*positionDifferenceX/(4*Math.pow(distance, 3));
					x+=CelestialBodies.get(j).getMass()*newPositionDifferenceX/(4*Math.pow(newDistance, 3));
					
					y+=initialValues.get(j).getMass()*positionDifferenceY/(2*Math.pow(distance, 3));
					y+=CelestialBodies.get(j).getMass()*newPositionDifferenceY/(4*Math.pow(newDistance, 3));
				}
			}
			Vx*=G*timeDelta;
			Vy*=G*timeDelta;
			x*=G*timeDelta*timeDelta;
			y*=G*timeDelta*timeDelta;
			
			CelestialBodies.set(i, new CelestialBody(
					CelestialBodies.get(i).getMass(),
					CelestialBodies.get(i).getSize(),
					x+(initialValues.get(i).getVx()*timeDelta)+initialValues.get(i).getX(),
					y+(initialValues.get(i).getVy()*timeDelta)+initialValues.get(i).getY(),
					initialValues.get(i).getVx()+Vx,
					initialValues.get(i).getVy()+Vy,
					CelestialBodies.get(i).getColor()));
			
			Vx=Vy=x=y=0;
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

}
