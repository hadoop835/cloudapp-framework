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

package com.alibaba.cloudapp.api.messaging;

import com.alibaba.cloudapp.api.messaging.model.Destination;
import com.alibaba.cloudapp.api.messaging.model.MQMessage;
import com.alibaba.cloudapp.exeption.CloudAppException;

import java.util.concurrent.CompletableFuture;

public interface Producer<UnderlyingProducer, Message> extends AutoCloseable {

	/**
	 * Return the underlying delegating producer directly.
	 *
	 * @return the underlying producer.
	 */
	UnderlyingProducer getDelegatingProducer();

	/**
	 * Send a message to the destination with an encapsulated message.
	 *
	 * @param message the message to send.
	 */
	void send(MQMessage<? extends Message> message) throws CloudAppException;

	/**
	 * Send a message to the destination with a raw message.
	 * @param destination The destination to which the message is sent,
	 * @param message The message to be sent
	 *
	 * @throws CloudAppException exception
	 */
	void send(Destination destination, Message message) throws CloudAppException;

	/**
	 * Send a string message to the topic.
	 *
	 * @param topic		The topic a message send to.
	 * @param message	A string message
	 *
	 * @throws CloudAppException exception
	 */
	void send(String topic, String message) throws CloudAppException;

	/**
	 * Send a string message to the destination.
	 *
	 * @param destination	The destination a message sent to.
	 * @param message		A string message
	 *
	 * @throws CloudAppException exception
	 */
	void send(Destination destination, String message)
			throws CloudAppException;

	/**
	 * Send a message to the destination with an encapsulated message asynchronously.
	 *
	 * @return A future object.
	 *
	 * @param message the message to send.
	 */
	CompletableFuture<?> sendAsync(MQMessage<? extends Message> message)
			throws CloudAppException;

	/**
	 * Send a message to the destination with a raw message asynchronously.
	 *
	 * @param destination  The destination a message sent to.
	 * @param message	   A  message
	 *
	 * @return A future object.
	 *
	 * @throws CloudAppException exception
	 */
	CompletableFuture<?> sendAsync(Destination destination, Message message)
			throws CloudAppException;

	/**
	 * Send a string message to the topic asynchronously.
	 *
	 * @param topic		The topic a message send to.
	 * @param message   A string message
	 *
	 * @return A future object.
	 *
	 * @throws CloudAppException exception
	 */
	CompletableFuture<?> sendAsync(String topic, String message)
			throws CloudAppException;

	/**
	 * Send a string message to the destination asynchronously.
	 *
	 * @param destination  The destination a message sent to.
	 * @param message	   A string message
	 *
	 * @return A future object.
	 *
	 * @throws CloudAppException exception
	 */
	CompletableFuture<?> sendAsync(Destination destination, String message)
			throws CloudAppException;

}
