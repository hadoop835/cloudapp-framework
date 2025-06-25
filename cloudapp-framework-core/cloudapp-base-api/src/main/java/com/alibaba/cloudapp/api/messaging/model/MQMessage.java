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

package com.alibaba.cloudapp.api.messaging.model;

public abstract class MQMessage<Message> {

    /**
     * An application generated message id.
     */
    private String messageID;

    /**
     * The timestamp when the message is sent by a producer.
     */
    private long sentTimestamp;

    /**
     * The timestamp when the message is delivered by a broker.
     */
    private long deliveredTimestamp;

    /**
     * The timestamp when the message if received by a consumer.
     */
    private long receivedTimestamp;

    /**
     * The underlying message body
     */
    private Message messageBody;

    /**
     * The location where the message is sent by a producer.
     */
    private Location sender;

    /**
     * The location where the message is received by a consumer.
     */
    private Location receiver;

    /**
     * The destination this message is sending to, a destination usually
     * including such info: queue, topic, routing key, etc.
     */
    private Destination destination;

    /**
     * The destination will reply to if this message is received.
     */
    private Destination replyTo;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Message getUnderlyingMessage() {
        return messageBody;
    }

    public void setMessageBody(Message messageBody) {
        this.messageBody = messageBody;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Destination getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Destination replyTo) {
        this.replyTo = replyTo;
    }

    public long getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(long sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public long getDeliveredTimestamp() {
        return deliveredTimestamp;
    }

    public void setDeliveredTimestamp(long deliveredTimestamp) {
        this.deliveredTimestamp = deliveredTimestamp;
    }

    public long getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public void setReceivedTimestamp(long receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    public Message getMessageBody() {
        return messageBody;
    }

    public Location getSender() {
        return sender;
    }

    public void setSender(Location sender) {
        this.sender = sender;
    }

    public Location getReceiver() {
        return receiver;
    }

    public void setReceiver(Location receiver) {
        this.receiver = receiver;
    }
}
