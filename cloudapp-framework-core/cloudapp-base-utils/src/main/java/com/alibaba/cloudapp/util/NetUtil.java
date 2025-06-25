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

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetUtil {
    private static String localIp;
    public static String LOCAL_IP = getLocalIp();
    public static String getLocalHostName() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();

            return localHost.getHostName();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static int getProcessId() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        String name = runtimeMxBean.getName();

        return Integer.parseInt(name.split("@")[0]);
    }

    public static String getLocalIp() {
        if (localIp != null) {
            return localIp;
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ipAddress = addresses.nextElement();
                    if (ipAddress.isLoopbackAddress() || ipAddress.isLinkLocalAddress() || ipAddress.isMulticastAddress()) {
                        continue;
                    }
                    localIp = ipAddress.getHostAddress();
                    return localIp;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return localIp;
    }


}
