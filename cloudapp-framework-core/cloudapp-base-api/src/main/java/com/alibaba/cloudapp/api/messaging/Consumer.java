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

import java.time.Duration;
import java.util.Collection;

public interface Consumer<UnderlyingConsumer, Message> extends AutoCloseable {

	/**
	 * Get the underlying delegating consumer.
	 *
	 * @return The actual consumer object that is being delegated.
	 */
	UnderlyingConsumer getDelegatingConsumer();

	/**
	 * Pull one message from the destination.
	 *
	 * @param destination   The destination pulling from
	 *
	 * @return A pulled message.
	 *
	 * @throws CloudAppException exception
	 */
	MQMessage<? extends Message> pull(Destination destination) throws CloudAppException;

	/**
	 * Pull a collection of messages from the destination with a specified count.
	 *
	 * @param destination       The destination pulling from
	 * @param count				The desired message count
	 *
	 * @return A collection of pulled messages.
	 *
	 * @throws CloudAppException exception
	 */
	Collection<MQMessage<? extends Message>> pull(Destination destination, int count)
			throws CloudAppException;


	/**
	 * Pull one raw message from the topic.
	 *
	 * @param topic the topic pulling from
	 *
	 * @return A raw message
	 *
	 * @throws CloudAppException exception
	 */
	Message pull(String topic) throws CloudAppException;

	/**
	 * Pull a collection of message from the topic.
	 *
	 * @param topic         The topic pulling from
	 * @param count			The desired message count
	 *
	 * @return  A collection of pulled raw messages.
	 *
	 * @throws CloudAppException exception
	 */
	Collection<Message> pull(String topic, int count) throws CloudAppException;

	/**
	 * Pull one message from the destination within a time range, will throw a cloudapp
	 * timeout exception if no message delivered within the time range.
	 *
	 * @param destination    The destination pulling from
	 * @param timeout        A timeout will waiting for.
	 *
	 * @return A message
	 *
	 * @throws CloudAppException exception
	 */
	MQMessage<? extends Message> pull(final Destination destination,
									  Duration timeout) throws CloudAppException;

	/**
	 * Pull one raw message from the topic within a time range, will throw a cloudapp
	 * timeout exception if no message delivered within the time range.
	 *
	 * @param topic      The topic pulling from
	 * @param timeout	 A timeout will waiting for.
	 *
	 * @return A raw message.
	 *
	 * @throws CloudAppException exception
	 */
	Message pull(final String topic, Duration timeout) throws CloudAppException;

	/**
	 * Pull a collection of messages from the destination within a time range,
	 * will throw a cloudapp timeout exception if no message delivered within the time range
	 *
	 * @param destination  The destination pulling from
	 * @param count        The desired message count
	 * @param timeout      A timeout will waiting for.
	 *
	 * @return             A collection of messages.
	 *
	 * @throws CloudAppException exception
	 */
	Collection<MQMessage<? extends Message>> pull(final Destination destination, int count,
												  Duration timeout) throws CloudAppException;

	/**
	 * Pull a collection of raw messages from the topic within a time range, will throw a
	 * cloudapp timeout exception if no message delivered within the time range
	 *
	 * @param topic    The topic pulling from
	 * @param count    The desired message count
	 * @param timeout  A timeout will waiting for.
	 *
	 * @return         A collection of raw messages.
	 *
	 * @throws CloudAppException exception
	 */
	Collection<Message> pull(final String topic, int count,
							 Duration timeout) throws CloudAppException;

	/**
	 * Subscribe to a destination with a notifier.
	 *
	 * @param destination the destination to subscribe to
	 * @param notifier 	  the message notifier
	 */
	void subscribe(final Destination destination, final Notifier<Message> notifier);

	/**
	 * Subscribe to a topic with a notifier.
	 *
	 * @param topic     the topic to subscribe to.
	 * @param notifier  the message notifier.
	 */
	void subscribe(String topic, final Notifier<Message> notifier);

	/**
	 * Unsubscribe from a destination
	 *
	 * @param destination the destination to unsubscribe from
	 */
	void unsubscribe(final Destination destination);

	/**
	 * Unsubscribe from a raw topic
	 *
	 * @param topic the topic to unsubscribe from
	 */
	void unsubscribe(String topic);

	/**
	 * Unsubscribe from a destination with the notifier.
	 *
	 * @param destination the destination to unsubscribing from
	 * @param notifier message handler
	 */
	void unsubscribe(final Destination destination, final Notifier<Message> notifier);

	/**
	 * Unsubscribe from a topic with the handler with a notifier.
	 *
	 * @param topic the topic unsubscribing from.
	 * @param notifier the message notifier
	 */
	void unsubscribe(String topic, final Notifier<Message> notifier);

}
