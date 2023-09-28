/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.inlong.tubemq.cli.cmd;

import org.apache.inlong.tubemq.client.config.ConsumerConfig;
import org.apache.inlong.tubemq.client.config.TubeClientConfig;
import org.apache.inlong.tubemq.client.consumer.ConsumePosition;
import org.apache.inlong.tubemq.client.consumer.PullMessageConsumer;
import org.apache.inlong.tubemq.client.exception.TubeClientException;
import org.apache.inlong.tubemq.client.factory.MessageSessionFactory;
import org.apache.inlong.tubemq.client.factory.TubeSingleSessionFactory;
import org.apache.inlong.tubemq.client.producer.MessageProducer;

public class TubeMQSingleFactory {

    private final String _masterAddr;

    MessageSessionFactory factory;

    private TubeMQSingleFactory(String masterAddr) throws TubeClientException {
        _masterAddr = masterAddr;
        TubeClientConfig clientConfig = new TubeClientConfig(masterAddr);
        factory = new TubeSingleSessionFactory(clientConfig);
    }

    @lombok.Getter
    private static TubeMQSingleFactory instance = null;

    public static synchronized TubeMQSingleFactory getInstance(String masterAddr) throws TubeClientException {
        if (masterAddr.isEmpty()) {
            return null;
        }
        if (instance == null) {
            instance = new TubeMQSingleFactory(masterAddr);
        }
        return instance;
    }

    public MessageProducer createProducer() throws TubeClientException {
        return factory.createProducer();
    }

    public PullMessageConsumer createPullConsumer(String topic) throws TubeClientException {
        ConsumerConfig consumerConfig = new ConsumerConfig(_masterAddr, topic);
        consumerConfig.setConsumePosition(ConsumePosition.CONSUMER_FROM_LATEST_OFFSET);
        return factory.createPullConsumer(consumerConfig);
    }

    public String getMasterAddr() {
        return _masterAddr;
    }

    public boolean isRegistered() {
        return !_masterAddr.isEmpty();
    }
}
