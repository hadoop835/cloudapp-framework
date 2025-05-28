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

package io.cloudapp.sequence.demo;

import io.cloudapp.api.sequence.SequenceGenerator;
import io.cloudapp.sequence.service.RedisSequenceGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudappSequenceMultiQueuesApplication implements InitializingBean {

    @Autowired
    @Qualifier("firstQueue")
    private SequenceGenerator firstGenerator;

    @Autowired
    @Qualifier("secondQueue")
    private RedisSequenceGenerator secondGenerator;

    public static void main(String[] args) {
        SpringApplication.run(CloudappSequenceMultiQueuesApplication.class, args);
    }

    @Override
    public void afterPropertiesSet() {

        System.out.println("Generated first queue sequence id: " + firstGenerator.nextId());
        System.out.println("Generated second queue sequence id: " + secondGenerator.nextId());

        long start = System.currentTimeMillis();
        for (int i = 0 ; i < 1000; i++) {
            System.out.println("Loop generating first queue sequence id: " + firstGenerator.nextId());
        }

        System.out.println("Time spent in milliseconds: " + (
                System.currentTimeMillis() - start
        ));

        for (int i = 0 ; i < 1000; i++) {
            System.out.println("Loop generating second queue sequence id: " + secondGenerator.nextId());
        }

        for (int i = 0 ; i < 1000; i++) {
            System.out.println("Loop generating first queue sequence id by second generator: "
                    + secondGenerator.nextId("firstQueue", 1L));
        }
    }
}
