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
	
	private float x, y; 		//TOP LEFT CORNER
	
	private short rel_x, rel_y;	//NOT IN COORDINATES, BUT IN "PART SIZES"!
	
	private Vec2 offsetVector; 	//Offset of the TOP LEFT CORNER OF THE PART
								// relative to the CENTER OF THE BODY
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
		
		//Rotate offset vector
		/*if (parentAngle != 0.0f)
		{
			offsetVector.x = (float) (((x - parentPos.x) * Math.cos(parentAngle)) - ((parentPos.y - y) * Math.sin(parentAngle)) + parentPos.x);
			offsetVector.y = (float) (((parentPos.y - y) * Math.cos(parentAngle)) - ((x - parentPos.x) * Math.sin(parentAngle)) + parentPos.y);
		}*/
		
		x = parentPos.x - offsetVector.x;
		y = parentPos.y - offsetVector.y;
		
		if (parentAngle != rotation)
		{
			rotation = parentAngle;
			
			//image.setCenterOfRotation(-offsetVector.x, -offsetVector.y);
			//image.setRotation((float)Math.toDegrees(rotation));
		}
	}

	
}
