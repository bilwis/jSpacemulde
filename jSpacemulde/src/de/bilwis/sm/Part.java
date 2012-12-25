package de.bilwis.sm;

import java.util.UUID;

import org.jbox2d.common.Vec2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class Part {
	
	UUID uuid;
	Structure parent;
	
	Image image;
	Color color;

	private int size;
	
	/**
	 * These variables store the global coordinates of the top left corner of the part.
	 */
	private float x, y;
	
	/**
	 * These variables store the offset from the structure origin, in "part sizes".
	 */
	private short rel_x, rel_y;	
	
	/**
	 * This vector represents the offset of the top left corner of the part to the center of the body.
	 */
	private Vec2 offsetVector; 
	
	private float rotation;
	
	private float mass;
	
	public int getSize()
	{
		return size;
	}
	
	public float getMass()
	{
		return mass;
	}
	
	public Vec2 getPosition()
	{
		return new Vec2(x,y);
	}
	
	public void setRelativePosition(short offsetX, short offsetY)
	{
		this.rel_x = offsetX;
		this.rel_y = offsetY;
		
		offsetVector = new Vec2((size/2) + (offsetX * size) , (size/2) + (offsetY * size));
	}
	
	public int getCoordIdentifier()
	{
		return (rel_x << 16) | (rel_y & 0xFFFF);
	}
	
	public Part(int size, Image image, Color color, float mass)
	{
		uuid = UUID.randomUUID();
		
		this.size = size;
		this.image = image.getScaledCopy(size, size);
		this.color = color;
		this.mass = mass;
	}
	
	public void updatePhysics(Vec2 parentPos, float parentAngle) throws Exception
	{
		if (parent == null)
			throw new Exception("The part has no parent!");
		
		x = parentPos.x - offsetVector.x;
		y = parentPos.y - offsetVector.y;
		
		if (parentAngle != rotation)
		{
			rotation = parentAngle;
		}
	}

	
}
