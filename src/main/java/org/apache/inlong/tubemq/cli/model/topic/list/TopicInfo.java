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
public class TopicInfo implements Serializable {

    @SerializedName("topicName")
    @Expose
    private String topicName;
    @SerializedName("brokerId")
    @Expose
    private Integer brokerId;
    @SerializedName("topicNameId")
    @Expose
    private Integer topicNameId;
    @SerializedName("brokerIp")
    @Expose
    private String brokerIp;
    @SerializedName("brokerPort")
    @Expose
    private Integer brokerPort;
    @SerializedName("topicStatusId")
    @Expose
    private Integer topicStatusId;
    @SerializedName("numTopicStores")
    @Expose
    private Integer numTopicStores;
    @SerializedName("numPartitions")
    @Expose
    private Integer numPartitions;
    @SerializedName("unflushThreshold")
    @Expose
    private Integer unflushThreshold;
    @SerializedName("unflushInterval")
    @Expose
    private Integer unflushInterval;
    @SerializedName("unflushDataHold")
    @Expose
    private Integer unflushDataHold;
    @SerializedName("memCacheMsgSizeInMB")
    @Expose
    private Integer memCacheMsgSizeInMB;
    @SerializedName("memCacheMsgCntInK")
    @Expose
    private Integer memCacheMsgCntInK;
    @SerializedName("memCacheFlushIntvl")
    @Expose
    private Integer memCacheFlushIntvl;
    @SerializedName("acceptPublish")
    @Expose
    private Boolean acceptPublish;
    @SerializedName("acceptSubscribe")
    @Expose
    private Boolean acceptSubscribe;
    @SerializedName("deletePolicy")
    @Expose
    private String deletePolicy;
    @SerializedName("dataStoreType")
    @Expose
    private Integer dataStoreType;
    @SerializedName("dataPath")
    @Expose
    private String dataPath;
    @SerializedName("dataVersionId")
    @Expose
    private Integer dataVersionId;
    @SerializedName("serialId")
    @Expose
    private Long serialId;
    @SerializedName("createUser")
    @Expose
    private String createUser;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    @SerializedName("modifyUser")
    @Expose
    private String modifyUser;
    @SerializedName("modifyDate")
    @Expose
    private String modifyDate;
    @SerializedName("runInfo")
    @Expose
    private RunInfo runInfo;
    private final static long serialVersionUID = 1822911482167301322L;

    public String getTopicName() {
        return topicName;
    }
    public Integer getBrokerId() {
        return brokerId;
    }
    public Integer getTopicNameId() {
        return topicNameId;
    }
    public String getBrokerIp() {
        return brokerIp;
    }
    public Integer getBrokerPort() {
        return brokerPort;
    }
    public Integer getTopicStatusId() {
        return topicStatusId;
    }
    public Integer getNumTopicStores() {
        return numTopicStores;
    }
    public Integer getNumPartitions() {
        return numPartitions;
    }
    public Integer getUnflushThreshold() {
        return unflushThreshold;
    }
    public Integer getUnflushInterval() {
        return unflushInterval;
    }
    public Integer getUnflushDataHold() {
        return unflushDataHold;
    }
    public Integer getMemCacheMsgSizeInMB() {
        return memCacheMsgSizeInMB;
    }
    public Integer getMemCacheMsgCntInK() {
        return memCacheMsgCntInK;
    }
    public Integer getMemCacheFlushIntvl() {
        return memCacheFlushIntvl;
    }
    public Boolean getAcceptPublish() {
        return acceptPublish;
    }
    public Boolean getAcceptSubscribe() {
        return acceptSubscribe;
    }
    public String getDeletePolicy() {
        return deletePolicy;
    }
    public Integer getDataStoreType() {
        return dataStoreType;
    }
    public String getDataPath() {
        return dataPath;
    }
    public Integer getDataVersionId() {
        return dataVersionId;
    }
    public Long getSerialId() {
        return serialId;
    }

    public String getCreateUser() {
        return createUser;
    }
    public String getCreateDate() {
        return createDate;
    }
    public String getModifyUser() {
        return modifyUser;
    }
    public String getModifyDate() {
        return modifyDate;
    }
    public RunInfo getRunInfo() {
        return runInfo;
    }
}
