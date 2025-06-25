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
package com.alibaba.cloudapp.messaging.redis.demo.intercepter;

import com.alibaba.cloudapp.api.cache.interceptors.CacheDiskInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.DataType;
import org.springframework.scheduling.annotation.Async;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;

public class LocalCacheDiskInterceptor extends CacheDiskInterceptor {

    private static final Path path = Paths.get("D:\\cache");
    private static final Logger logger = LoggerFactory.getLogger(LocalCacheDiskInterceptor.class);

    @Override
    public void expire(byte[] key, long millis) {
        logger.warn("local filesystem not support expire, current key:{}, millis:{}", key, millis);
    }

    @Override
    public void persist(byte[] key) {
        logger.warn("local filesystem not support expire, current key:{}", key);

    }

    @Override
    public void delete(byte[]... keys) {
        for (byte[] key : keys) {
            String keyString = String.valueOf(getDelegate().getKeySerializer().deserialize(key));
            File file = new File(path.toFile(), keyString);
            file.deleteOnExit();
        }
    }

    @Async
    @Override
    public void notifyChanged(Collection<byte[]> keys) {
        keys.forEach(key -> {
            Object keyObj = getDelegate().getKeySerializer().deserialize(key);

            assert keyObj != null;
            DataType type = getDelegate().type(keyObj);


            Object value = null;

            switch (Objects.requireNonNull(type)) {
                case STRING:
                    value = getDelegate().opsForValue().get(keyObj);
                    break;
                case HASH:
                    value = getDelegate().opsForHash().entries(keyObj);
                    break;
                case LIST:
                    value = getDelegate().opsForList().range(keyObj, 0, -1);
                    break;
                case SET:
                    value = getDelegate().opsForSet().members(keyObj);
                    break;
                case ZSET:
                    value = getDelegate().opsForZSet().range(keyObj, 0, -1);
                    break;
            }

            logger.info("local cache changed, current key:{}, current value:{}", keyObj, value);
            if (value != null) {

                String keyString = String.valueOf(keyObj);
                File file = new File(path.toFile(), keyString);
                byte[] bytes = getDelegate().getValueSerializer().serialize(value);

                logger.info("start save file");
                assert bytes != null;
                try (FileOutputStream out = new FileOutputStream(file);
                     ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                } catch (Exception e) {
                    logger.error("create file error, current key:{}, error:{}", keyString, e.getMessage(), e);
                }
            }
        });
    }


    @Async
    @Override
    public void notifyChanged(byte[]... keys) {
        super.notifyChanged(keys);
    }
}
