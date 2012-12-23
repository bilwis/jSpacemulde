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
	
	Box box1, box2;
	
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
    	
    	//Set up boxes (for rendering)
    	box1 = new Box(120.0f, 50.0f, 30.0f, 30.0f, 1.0f, new Image("res/cube.png"), Color.red, world);
    	box2 = new Box(100.0f, 150.0f, 30.0f, 30.0f, 1.0f, new Image("res/cube.png"), Color.blue, world);

    }

    @Override
    public void update(GameContainer container, int delta)
            throws SlickException {
    	input.poll(container.getScreenWidth(), container.getScreenHeight());
    	
    	if (input.isKeyDown(Input.KEY_SPACE))
    	{
    		box1.body.applyLinearImpulse(new Vec2(0.0f, 1000.0f), box1.body.getWorldCenter());
    	}
    	
    	updatePhysics();
    }
    
    public void updatePhysics()
    {
    	world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    	
    	box1.updatePhysics();
    	box2.updatePhysics();
    }

    @Override
    public void render(GameContainer container, Graphics g)
            throws SlickException {
        g.drawImage(box1.image, box1.getPosition().x, box1.getPosition().y, box1.color);
        g.drawImage(box2.image, box2.getPosition().x, box2.getPosition().y, box2.color);
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