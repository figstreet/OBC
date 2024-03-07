package com.figstreet.core.crypto;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class SecureByteArrayOutputStream extends OutputStream
{
	protected byte[] fBuffer;
	protected int fCount;

	public SecureByteArrayOutputStream()
	{
		this(64);
	}

	public SecureByteArrayOutputStream(int paramInt)
	{
		if (paramInt < 0)
		{
			throw new IllegalArgumentException("Negative initial size: " + paramInt);
		}
		this.fBuffer = new byte[paramInt];
	}

	private void ensureCapacity(int paramInt)
	{
		if (paramInt - this.fBuffer.length > 0)
		{
			grow(paramInt);
		}
	}

	protected void grow(int paramInt)
	{
		int i = this.fBuffer.length;
		int j = i << 1;
		if (j - paramInt < 0)
		{
			j = paramInt;
		}
		if (j - 2147483639 > 0)
		{
			j = hugeCapacity(paramInt);
		}
		byte[] oldBuffer = this.fBuffer;
		this.fBuffer = Arrays.copyOf(oldBuffer, j);
		CryptoUtil.clearArray(oldBuffer);
	}

	private static int hugeCapacity(int paramInt)
	{
		if (paramInt < 0)
		{
			throw new OutOfMemoryError();
		}
		return paramInt > 2147483639 ? Integer.MAX_VALUE : 2147483639;
	}

	@Override
	public synchronized void write(int paramInt)
	{
		ensureCapacity(this.fCount + 1);
		this.fBuffer[this.fCount] = ((byte) paramInt);
		this.fCount += 1;
	}

	@Override
	public synchronized void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
	{
		if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0)
			|| (paramInt1 + paramInt2 - paramArrayOfByte.length > 0))
		{
			throw new IndexOutOfBoundsException();
		}
		ensureCapacity(this.fCount + paramInt2);
		System.arraycopy(paramArrayOfByte, paramInt1, this.fBuffer, this.fCount, paramInt2);
		this.fCount += paramInt2;
	}

	public synchronized void writeTo(OutputStream paramOutputStream) throws IOException
	{
		paramOutputStream.write(this.fBuffer, 0, this.fCount);
	}

	public synchronized void reset()
	{
		this.fCount = 0;
	}

	public synchronized byte[] toByteArray()
	{
		return Arrays.copyOf(this.fBuffer, this.fCount);
	}

	public synchronized int size()
	{
		return this.fCount;
	}

	@Override
	public synchronized String toString()
	{
		return new String(this.fBuffer, 0, this.fCount);
	}

	public synchronized String toString(String paramString) throws UnsupportedEncodingException
	{
		return new String(this.fBuffer, 0, this.fCount, paramString);
	}

	@Override
	public void close()
	{
		CryptoUtil.clearArray(this.fBuffer);
		this.fBuffer = null;
	}
}
