package de.bilwis.sm;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Box {
	
	Image image;
	Body body;
	
	Color color;
	
	private float x, y;
	private float width, height;
	private float rotation;
	
	private float mass;
	
	public Box(float x, float y, float width, float height, float mass, Image img, Color col, World world)
	{
		//Assign variables
		this.x = x;
		this.y = y;
		this.mass = mass;
		this.width = width;
		this.height = height;
		
		//Physics component
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(this.x, this.y);
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(this.height/2, this.width/2);
		
		body.createFixture(shape, this.mass);
		
		//Image component
		this.image = img.getScaledCopy((int)this.width, (int)this.height);
		this.color = col;
	}
	
	public void updatePhysics()
	{
		Vec2 _pos = body.getPosition();
		x = _pos.x;
		y = _pos.y;
		
		float _rot = body.getAngle();
		if (_rot != rotation)
		{
			rotation = _rot;
			image.setRotation((float) Math.toDegrees(rotation));
		}
	}
	
	 public void render(GameContainer container, Graphics g)
	            throws SlickException 
	 { 
		 float x, y;
		 x = body.getPosition().x - width/2;
		 y = body.getPosition().y - height/2;
		 
	     g.drawImage(image, x, y, color);
	 }
	
	public Vec2 getPosition()
	{
		return new Vec2(x,y);
	}
	
	public float getHeight()
	{
		return height;
	}
	
	public float getWidth()
	{
		return width;
	}

}
