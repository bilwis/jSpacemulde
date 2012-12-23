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

public class Structure {
	
	UUID uuid;
	Body body;
	
	private Hashtable<Integer, Part> parts;
	
	private float x, y;
	private float rotation;
	private int partSize;
	
	Logger logger;
	
	public Structure(float x, float y, int partSize, World world)
	{
		this.x = x;
		this.y = y;
		
		this.partSize = partSize;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(this.x, this.y);
		body = world.createBody(bodyDef);
		
		uuid = UUID.randomUUID();
		parts = new Hashtable<Integer, Part>();
		logger = LoggerFactory.getLogger(Structure.class);
	}
	
	public void addPart(Part part, short offsetX, short offsetY) throws Exception
	{
		if (part.getSize() != partSize)
		{
			throw new Exception("Part size not compatible.");
		}
		
		int _coords = (offsetX << 16) | (offsetY & 0xFFFF);
		
		part.parent = this;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(partSize/2, partSize/2, new Vec2(-offsetX*partSize, (offsetY*partSize)), body.getAngle());
		
		body.createFixture(shape, part.getMass());
		
		part.setRelativePosition(offsetX, offsetY);
		
		parts.put(_coords, part);
	}
	
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
	
	public void render(GameContainer container, Graphics g)
            throws SlickException {
		
		g.rotate(x, y, (float) Math.toDegrees(rotation));
		
    	for (Part p:parts.values())
    	{
    		g.drawImage(p.image, p.getPosition().x , p.getPosition().y , p.color);
    	}
		
		g.resetTransform();
		
		/*
		g.setColor(Color.white);
		g.drawOval(x-5, y-5, 10, 10);
		g.setColor(Color.green);
		g.drawOval(body.getWorldCenter().x-5, body.getWorldCenter().y-5, 10, 10);*/
    }

}
