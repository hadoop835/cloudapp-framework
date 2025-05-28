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

package io.cloudapp.model;

public class MailUser {
    
    /**
     * sender
     */
    private String from;
    /**
     * reply-to email address
     */
    private String replyTo;
    /**
     * recipients
     */
    private String[] to;
    /**
     * Carbon copy recipient
     */
    private String[] cc;
    /**
     * Blind carbon copy recipient
     */
    private String[] bcc;
    
    public MailUser setFrom(String from) {
        this.from = from;
        return this;
    }
    
    public MailUser setReplyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }
    
    public MailUser setTo(String to) {
        this.to = new String[]{to};
        return this;
    }
    
    public MailUser setTo(String... to) {
        this.to = to;
        return this;
    }
    
    public MailUser setCc(String cc) {
        this.cc = new String[] {cc};
        return this;
    }
    
    public MailUser setCc(String... cc) {
        this.cc = cc;
        return this;
    }
    
    public MailUser setBcc(String bcc) {
        this.bcc = new String[] {bcc};
        return this;
    }
    
    public MailUser setBcc(String... bcc) {
        this.bcc = bcc;
        return this;
    }
    
    public String getFrom() {
        return from;
    }
    
    public String getReplyTo() {
        return replyTo;
    }
    
    public String[] getTo() {
        return to;
    }
    
    public String[] getCc() {
        return cc;
    }
    
    public String[] getBcc() {
        return bcc;
    }
    
}
