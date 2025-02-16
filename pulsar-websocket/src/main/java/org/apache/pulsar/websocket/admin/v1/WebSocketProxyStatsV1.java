/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pulsar.websocket.admin.v1;

import static org.apache.pulsar.common.util.Codec.decode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Collection;
import java.util.Map;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.pulsar.common.naming.TopicName;
import org.apache.pulsar.common.stats.Metrics;
import org.apache.pulsar.websocket.admin.WebSocketProxyStatsBase;
import org.apache.pulsar.websocket.stats.ProxyTopicStat;

@Path("/proxy-stats")
@Api(value = "/proxy", description = "Stats for web-socket proxy", tags = "proxy-stats")
@Produces(MediaType.APPLICATION_JSON)
public class WebSocketProxyStatsV1 extends WebSocketProxyStatsBase {

    @GET
    @Path("/metrics")
    @ApiOperation(value = "Gets the metrics for Monitoring", notes = "Requested should be executed by Monitoring agent"
            + " on each proxy to fetch the metrics", response = Metrics.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 403, message = "Don't have admin permission") })
    public Collection<Metrics> internalGetMetrics() throws Exception {
        return super.internalGetMetrics();
    }

    @GET
    @Path("/{tenant}/{cluster}/{namespace}/{topic}/stats")
    @ApiOperation(value = "Get the stats for the topic.")
    @ApiResponses(value = { @ApiResponse(code = 403, message = "Don't have admin permission"),
            @ApiResponse(code = 404, message = "Topic does not exist") })
    public ProxyTopicStat getStats(@PathParam("tenant") String tenant, @PathParam("cluster") String cluster,
            @PathParam("namespace") String namespace, @PathParam("topic") @Encoded String encodedTopic) {
        return super.internalGetStats(TopicName.get("persistent", tenant, cluster, namespace, decode(encodedTopic)));
    }

    @GET
    @Path("/stats")
    @ApiOperation(value = "Get the stats for the topic.")
    @ApiResponses(value = { @ApiResponse(code = 403, message = "Don't have admin permission") })
    public Map<String, ProxyTopicStat> internalGetProxyStats() {
        return super.internalGetProxyStats();
    }
}
