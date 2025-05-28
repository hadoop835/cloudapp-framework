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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeRange extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * Start time
     */
    private Date startTime;

    /**
     * End time
     */
    private Date endTime;


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public static class Builder extends
            BaseModel.Builder<DateTimeRange.Builder, DateTimeRange>  {
        public DateTimeRange.Builder startTime(Date startTime) {
            operations.add(args -> args.setStartTime(startTime));
            return this;
        }

        public DateTimeRange.Builder startTime(String startTime) {
            Date s = parse(startTime);
            operations.add(args -> args.setStartTime(s));
            return this;
        }

        public DateTimeRange.Builder endTime(String endTime) {
            Date s = parse(endTime);
            operations.add(args -> args.setEndTime(s));
            return this;
        }

        public DateTimeRange.Builder endTime(Date endTime) {
            operations.add(args -> args.setEndTime(endTime));
            return this;
        }



        @Override
        protected void validate(DateTimeRange bucket) {
            // TODO.
        }

    }

    public static DateTimeRange.Builder builder() {
        return new DateTimeRange.Builder();
    }

    public static DateTimeRange fromString(String startTime, String endTime) {
        return builder().startTime(startTime).endTime(endTime).build();
    }

    public static DateTimeRange fromString(Date startTime, Date endTime) {
        return builder().startTime(startTime).endTime(endTime).build();
    }


    private static Date parse(String startTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(startTime);
        } catch (ParseException e) {
            return null;
        }
    }

}
