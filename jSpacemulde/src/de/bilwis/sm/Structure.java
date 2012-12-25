package de.bilwis.sm;

import java.util.Hashtable;
import java.util.UUID;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bilwis
 * @version 1.0
 * @since 2012-12-23
 */
public class Structure {
	
	UUID uuid;
	Body body;	
	
	/**
	 * This Hashtable holds all parts of the structure, indexed by their "offset coordinates" (in partSizes)
	 * stored in two shorts bitshifted into an integer.
	 */
	private Hashtable<Integer, Part> parts;
	
	private float x, y;
	private float rotation;
	private int partSize;
	
	Logger logger;
	
	/**
	 * A Structure holds a jBox2D body used for physics simulation and Parts comprised of jBox2D fixtures and images for rendering.
	 * 
	 * @param x The global x coordinate of the origin of the structure.
	 * @param y The global y coordinate of the origin of the structure.
	 * @param partSize The part size (both width and height) this structure accepts.
	 * @param world The jBox2D world object this structure is initialized to.
	 */
	public Structure(float x, float y, int partSize, World world)
	{
		this.x = x;
		this.y = y;
		
		this.partSize = partSize;
		
		//Initialize the jBox2D Body object
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(this.x, this.y);
		body = world.createBody(bodyDef);
		
		//Initialize other variables
		uuid = UUID.randomUUID();
		parts = new Hashtable<Integer, Part>();
		logger = LoggerFactory.getLogger(Structure.class);
	}
	
	/**
	 * Adds an existing part object to the structure at the given offset.
	 * 
	 * @param part The part to add.
	 * @param offsetX The X offset in "part units", i.e. 0,0 is the origin; 1,0 is (partSize*1, 0).
	 * @param offsetY The Y offset in "part units", i.e. 0,0 is the origin; 1,0 is (partSize*1, 0).
	 * @throws Exception Throws an exception if the size of the part is not compatible with the structures partSize.
	 */
	public void addPart(Part part, short offsetX, short offsetY) throws Exception
	{
		if (part.getSize() != partSize)
		{
			throw new Exception("Part size not compatible.");
		}
		
		//The hashtable key of the part in the parts table is an integer
		// into which both coordinates (short) are bitshifted.
		int _coords = (offsetX << 16) | (offsetY & 0xFFFF);
		
		part.parent = this;
		
		//X and Y coordinates need to be flipped to match the image rendering 
		// TODO: God knows why that is, I should do a more thorough analysis.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(partSize/2, partSize/2, new Vec2(-offsetX*partSize, -offsetY*partSize), body.getAngle());
		
		body.createFixture(shape, part.getMass());
		
		part.setRelativePosition(offsetX, offsetY);
		
		parts.put(_coords, part);
	}
	
	/**
	 * Updates the internal and part variables to match the newly calculated position of the jBox2D body. 
	 * @throws Exception
	 */
	public void updatePhysics() throws Exception
	{
		this.x = body.getPosition().x;
		this.y = body.getPosition().y;
		
		rotation = body.getAngle();
		
		for(Part p:parts.values())
		{
			p.updatePhysics(new Vec2(x,y), rotation);
		}
	}
	
	/**
	 * Renders all parts contained in the structure.
	 * 
	 * @param container
	 * @param g
	 * @throws SlickException
	 */
	public void render(GameContainer container, Graphics g)
            throws SlickException {
		
		//Set Graphics global transform to match the Body's rotation.
		g.rotate(x, y, (float) Math.toDegrees(rotation));
		
    	for (Part p:parts.values())
    	{
    		g.drawImage(p.image, p.getPosition().x , p.getPosition().y , p.color);
    	}
		
		g.resetTransform();
    }

}
