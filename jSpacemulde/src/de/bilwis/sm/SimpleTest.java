package de.bilwis.sm;

import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTest extends BasicGame {
	static float TIME_STEP = 1.0f / 60.0f;
	static int VELOCITY_ITERATIONS = 8;
	static int POSITION_ITERATIONS = 3;
	
	Input input;
	
	Image cube;
	
	Structure box1;
	Structure ship1;
	ArrayList<Structure> structures;
	
	World world;
	
	static Logger logger;
	
    public SimpleTest() {
        super("SimpleTest");
    }
    
    @Override
    public void init(GameContainer container) throws SlickException
    {
    	input = container.getInput();
    	
    	//Set up physics space
    	world = new World(new Vec2(0.0f, 0.0f), true);
    	
    	container.setMaximumLogicUpdateInterval((int)(TIME_STEP));
    	
    	//Load images
    	cube = new Image("res/cube.png");
    	
    	//Set up structures and parts
    	structures = new ArrayList<Structure>();
    	
    	ship1 = new Structure(500.0f, 100.0f, 30, world);
    	
    	ArrayList<Vec2> shipPlan = new ArrayList<Vec2>();
    	
    	//O O O O X
    	//O X O X X
    	//X X X X X
    	//O X O X X
    	//O O O O X
    	
    	shipPlan.add(new Vec2(-2,0));
    	shipPlan.add(new Vec2(-1,-1));
    	shipPlan.add(new Vec2(-1,0));
    	shipPlan.add(new Vec2(-1,1));
    	shipPlan.add(new Vec2(0,0));
    	shipPlan.add(new Vec2(1,-1));
    	shipPlan.add(new Vec2(1,0));
    	shipPlan.add(new Vec2(1,1));
    	shipPlan.add(new Vec2(2,-2));
    	shipPlan.add(new Vec2(2,-1));
    	shipPlan.add(new Vec2(2,0));
    	shipPlan.add(new Vec2(2,1));
    	shipPlan.add(new Vec2(2,2));
    	
    	for (Vec2 v:shipPlan)
    	{
    		short x = (short)v.x;
    		short y = (short)v.y;
    		
    		try{
        		ship1.addPart(
        				new Part(30, cube, new Color(128, ((x+2)*32), ((y+2)*32)), 1.0f),
        				x,
        				y);
    			}
    			catch (Exception ex)
    			{
    				logger.error("Error while creating parts: " + ex.getMessage());
    			}
    	}
    	
    	structures.add(ship1);
    	
    	box1 = new Structure(850.0f, 145.0f, 30, world);
    	
    	try {
			box1.addPart(
					new Part(30, cube, Color.red, 10.0f), 
					(short) 0, 
					(short) 0);
		} catch (Exception e) {
			logger.error("Error while creating parts for Box 1: " + e.getMessage());
		}

    	structures.add(box1);
    }

    @Override
    public void update(GameContainer container, int delta)
            throws SlickException {
    	input.poll(container.getScreenWidth(), container.getScreenHeight());
    	
    	if (input.isKeyDown(Input.KEY_A))
    	{
    		box1.body.applyLinearImpulse(new Vec2(-10000.0f, 0.0f), box1.body.getWorldCenter());
    	}
    	if (input.isKeyDown(Input.KEY_W))
    	{
    		box1.body.applyLinearImpulse(new Vec2(0.0f, -10000.0f), box1.body.getWorldCenter());
    	}
    	if (input.isKeyDown(Input.KEY_S))
    	{
    		box1.body.applyLinearImpulse(new Vec2(0.0f, 10000.0f), box1.body.getWorldCenter());
    	}
    	if (input.isKeyDown(Input.KEY_D))
    	{
    		box1.body.applyLinearImpulse(new Vec2(10000.0f, 0.0f), box1.body.getWorldCenter());
    	}
    	
    	updatePhysics();
    }
    
    public void updatePhysics()
    {
    	world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    	
    	for (Structure s:structures)
    	{
    		try {
				s.updatePhysics();
			} catch (Exception e) {
				logger.error("Error while updating physics: " + e.getMessage());
			}
    	}
    	
    }

    @Override
    public void render(GameContainer container, Graphics g)
            throws SlickException {
    
    	for (Structure s:structures)
    	{
    		s.render(container, g);
    	}
    	
    }

    public static void main(String[] args) {
        try {
        	BasicConfigurator.configure();
        	logger = LoggerFactory.getLogger(SimpleTest.class);
            AppGameContainer app = new AppGameContainer(new SimpleTest());
            app.setTargetFrameRate(60);
            app.setDisplayMode(1280, 720, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}