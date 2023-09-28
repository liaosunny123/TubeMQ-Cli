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

import java.util.List;

@Generated("jsonschema2pojo")
public class TopicCommonResult {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("errCode")
    @Expose
    private Integer errCode;
    @SerializedName("errMsg")
    @Expose
    private String errMsg;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("count")
    @Expose
    private Integer count;

    public Boolean getResult() {
        return result;
    }
    public Integer getErrCode() {
        return errCode;
    }
    public String getErrMsg() {
        return errMsg;
    }
    public List<Datum> getData() {
        return data;
    }
    public Integer getCount() {
        return count;
    }
}
