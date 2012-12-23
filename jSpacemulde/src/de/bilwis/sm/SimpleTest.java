package de.bilwis.sm;

import org.apache.log4j.BasicConfigurator;
import org.jbox2d.collision.shapes.*;
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
	
	Image rect1, rect2;
	float x1, y1, x2, y2;
	Body b1, b2;
	
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
    	
    	//container.setMinimumLogicUpdateInterval((int)(TIME_STEP));
    	container.setMaximumLogicUpdateInterval((int)(TIME_STEP));
    	
    	//Set up boxes (for rendering)
    	rect1 = new Image("res/cube_red.png");
    	rect2 = new Image("res/cube_blue.png");
    	
    	x1 = 120.0f;
    	y1 = 50.0f;
    	x2 = 100.0f;
    	y2 = 150.0f;
    	
    	//Set up boxes (for physics)
    	BodyDef bdef = new BodyDef();
    	bdef.type = BodyType.DYNAMIC;
    	
    	bdef.position.set(x1, y1);
    	b1 = world.createBody(bdef);
    	
    	bdef.position.set(x2, y2);
    	b2 = world.createBody(bdef);
    	
    	PolygonShape dbox = new PolygonShape();
    	dbox.setAsBox(20.0f, 20.0f);
    	
    	b1.createFixture(dbox, 1.0f);
    	b2.createFixture(dbox, 1.0f);
    }

    @Override
    public void update(GameContainer container, int delta)
            throws SlickException {
    	input.poll(container.getScreenWidth(), container.getScreenHeight());
    	
    	if (input.isKeyDown(Input.KEY_SPACE))
    	{
    		b1.setLinearVelocity(new Vec2(0.0f, 300.0f));
    		logger.info("Changed velocity on Box 1");
    	}
    	
    	updatePhysics();
    }
    
    public void updatePhysics()
    {
    	world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    	
    	x1 = b1.getPosition().x;
    	y1 = b1.getPosition().y;
    	x2 = b2.getPosition().x;
    	y2 = b2.getPosition().y;
    	
    	rect1.setRotation((float) Math.toDegrees(b1.getAngle()));
    	rect2.setRotation((float) Math.toDegrees(b2.getAngle()));
    }

    @Override
    public void render(GameContainer container, Graphics g)
            throws SlickException {
        g.setColor(Color.white);
        g.drawImage(rect1, x1, y1);
        g.drawImage(rect2, x2, y2);
    }

    public static void main(String[] args) {
        try {
        	BasicConfigurator.configure();
        	logger = LoggerFactory.getLogger(SimpleTest.class);
            AppGameContainer app = new AppGameContainer(new SimpleTest());
            app.setTargetFrameRate(60);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}