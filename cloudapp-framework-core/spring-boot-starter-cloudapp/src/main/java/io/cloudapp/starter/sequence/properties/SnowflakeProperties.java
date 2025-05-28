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

package io.cloudapp.starter.sequence.properties;

public class SnowflakeProperties {
    
    private boolean enabled = true;
    private long workerId = 1;
    
    private long workerIdBits = 5L;
    
    private long sequenceBits = 10L;
    
    public long getWorkerId() {
        return workerId;
    }
    
    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }
    
    public long getWorkerIdBits() {
        return workerIdBits;
    }
    
    public void setWorkerIdBits(long workerIdBits) {
        this.workerIdBits = workerIdBits;
    }
    
    public long getSequenceBits() {
        return sequenceBits;
    }
    
    public void setSequenceBits(long sequenceBits) {
        this.sequenceBits = sequenceBits;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
}