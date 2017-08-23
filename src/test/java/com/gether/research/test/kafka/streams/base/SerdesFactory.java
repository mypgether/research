package com.gether.research.test.kafka.streams.base;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class SerdesFactory {
    /**
	 * @param <T> The class should have a constructor without any
	 *        arguments and have setter and getter for every member variable
	 * @param pojoClass POJO class. 
	 * @return Instance of {@link Serde}
	 */
	public static <T> Serde<T> serdFrom(Class<T> pojoClass) {
		return Serdes.serdeFrom(new GenericSerializer<T>(pojoClass), new GenericDeserializer<T>(pojoClass));
	}
}