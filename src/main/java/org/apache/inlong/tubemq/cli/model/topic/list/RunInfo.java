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

package org.apache.inlong.tubemq.cli.model.topic.list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class RunInfo implements Serializable {

    @SerializedName("acceptPublish")
    @Expose
    private String acceptPublish;
    @SerializedName("acceptSubscribe")
    @Expose
    private String acceptSubscribe;
    @SerializedName("numPartitions")
    @Expose
    private String numPartitions;
    @SerializedName("numTopicStores")
    @Expose
    private String numTopicStores;
    @SerializedName("brokerManageStatus")
    @Expose
    private String brokerManageStatus;
    private final static long serialVersionUID = -2983016102082455760L;

    public String getAcceptPublish() {
        return acceptPublish;
    }

    public String getAcceptSubscribe() {
        return acceptSubscribe;
    }

    public String getNumPartitions() {
        return numPartitions;
    }

    public String getNumTopicStores() {
        return numTopicStores;
    }

    public String getBrokerManageStatus() {
        return brokerManageStatus;
    }
}
