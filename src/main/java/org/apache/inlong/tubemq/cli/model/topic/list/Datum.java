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
import java.util.List;

@Generated("jsonschema2pojo")
public class Datum implements Serializable {

    @SerializedName("topicName")
    @Expose
    private String topicName;
    @SerializedName("maxMsgSizeInMB")
    @Expose
    private Integer maxMsgSizeInMB;
    @SerializedName("topicInfo")
    @Expose
    private List<TopicInfo> topicInfo;
    @SerializedName("infoCount")
    @Expose
    private Integer infoCount;
    @SerializedName("totalCfgNumPart")
    @Expose
    private Integer totalCfgNumPart;
    @SerializedName("isSrvAcceptPublish")
    @Expose
    private Boolean isSrvAcceptPublish;
    @SerializedName("isSrvAcceptSubscribe")
    @Expose
    private Boolean isSrvAcceptSubscribe;
    @SerializedName("totalRunNumPartCount")
    @Expose
    private Integer totalRunNumPartCount;
    @SerializedName("authData")
    @Expose
    private AuthData authData;
    private final static long serialVersionUID = 3672391246742280137L;

    public String getTopicName() {
        return topicName;
    }

    public Integer getMaxMsgSizeInMB() {
        return maxMsgSizeInMB;
    }

    public List<TopicInfo> getTopicInfo() {
        return topicInfo;
    }

    public Integer getInfoCount() {
        return infoCount;
    }

    public Integer getTotalCfgNumPart() {
        return totalCfgNumPart;
    }

    public Boolean getIsSrvAcceptPublish() {
        return isSrvAcceptPublish;
    }

    public Boolean getIsSrvAcceptSubscribe() {
        return isSrvAcceptSubscribe;
    }

    public Integer getTotalRunNumPartCount() {
        return totalRunNumPartCount;
    }

    public AuthData getAuthData() {
        return authData;
    }

}
