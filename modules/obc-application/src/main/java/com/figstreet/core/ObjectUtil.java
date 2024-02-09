package com.figstreet.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtil implements Cloneable
{
	private static final String LOGGER_NAME = ObjectUtil.class.getPackage().getName() + ".ObjectUtil";

	public static Object deepCopy(Object pObject) throws CloneNotSupportedException
	{
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);)
		{
			oos.writeObject(pObject);
			oos.flush();
			try (ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
					ObjectInputStream ois = new ObjectInputStream(bin);)
			{
				return ois.readObject();
			}
		}
		catch (IOException e)
		{
			Logging.error(LOGGER_NAME, "deepCopy", "IO error copying class", e);
			throw new CloneNotSupportedException();
		}
		catch (ClassNotFoundException e)
		{
			Logging.error(LOGGER_NAME, "deepCopy", "Class Not found.", e);
			throw new CloneNotSupportedException();
		}
	}
}
