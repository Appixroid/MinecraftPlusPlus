package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketInput implements Packet<INetHandlerPlayServer>
{
	/** Positive for left strafe, negative for right */
	private float strafeSpeed;
	private float field_192621_b;
	private boolean jumping;
	private boolean sneaking;

	public CPacketInput()
	{
	}

	public CPacketInput(float strafeSpeedIn, float forwardSpeedIn, boolean jumpingIn, boolean sneakingIn)
	{
		this.strafeSpeed = strafeSpeedIn;
		this.field_192621_b = forwardSpeedIn;
		this.jumping = jumpingIn;
		this.sneaking = sneakingIn;
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.strafeSpeed = buf.readFloat();
		this.field_192621_b = buf.readFloat();
		byte b0 = buf.readByte();
		this.jumping = (b0 & 1) > 0;
		this.sneaking = (b0 & 2) > 0;
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeFloat(this.strafeSpeed);
		buf.writeFloat(this.field_192621_b);
		byte b0 = 0;

		if (this.jumping)
		{
			b0 = (byte) (b0 | 1);
		}

		if (this.sneaking)
		{
			b0 = (byte) (b0 | 2);
		}

		buf.writeByte(b0);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	@Override
	public void processPacket(INetHandlerPlayServer handler)
	{
		handler.processInput(this);
	}

	public float getStrafeSpeed()
	{
		return this.strafeSpeed;
	}

	public float func_192620_b()
	{
		return this.field_192621_b;
	}

	public boolean isJumping()
	{
		return this.jumping;
	}

	public boolean isSneaking()
	{
		return this.sneaking;
	}
}
