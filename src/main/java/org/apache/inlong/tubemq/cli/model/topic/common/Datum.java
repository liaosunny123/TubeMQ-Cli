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

package org.apache.inlong.tubemq.cli.model.topic.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Datum {

    @SerializedName("brokerId")
    @Expose
    private Integer brokerId;
    @SerializedName("topicName")
    @Expose
    private String topicName;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("errCode")
    @Expose
    private Integer errCode;
    @SerializedName("errInfo")
    @Expose
    private String errInfo;

    public Integer getBrokerId() {
        return brokerId;
    }
    public String getTopicName() {
        return topicName;
    }
    public Boolean getSuccess() {
        return success;
    }
    public Integer getErrCode() {
        return errCode;
    }
    public String getErrInfo() {
        return errInfo;
    }
}
