/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.alibaba.cloudapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Base64;

/**
 * Utility class for serial numbers, converting objects to base64 encoded strings,
 * and deserializing base64 strings into objects
 */
public class SerialUtil<T> {

    private static final Logger logger = LoggerFactory.getLogger(SerialUtil.class);
    
	public String serializeToBase64(T obj) {
		if(obj == null) {
			return null;
		}

		try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(outputStream)) {

			oos.writeObject(obj);
			oos.flush();

			byte[] bytes = outputStream.toByteArray();

			return Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			logger.error("[serialize error]Serialization exception! serialization object is: {}, Error message: {}",
					obj, e.getMessage());
		}
		return null;
	}

	public T deserializeFromBase64(String base64Code) {
		if(base64Code == null || base64Code.isEmpty()) {
			return null;
		}

		byte[] bytes = Base64.getDecoder().decode(base64Code);

		try(ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(inputStream)) {

			return (T) ois.readObject();
		} catch (Exception e) {
		    logger.error("[deserialize error]Deserialization exception! " +
					"Error message: {}", e.getMessage());
		}

		return null;
	}

}
