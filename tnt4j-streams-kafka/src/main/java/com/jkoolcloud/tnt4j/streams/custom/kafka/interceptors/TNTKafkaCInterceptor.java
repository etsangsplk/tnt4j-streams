/*
 * Copyright 2014-2017 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jkoolcloud.tnt4j.streams.custom.kafka.interceptors;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.ClusterResource;
import org.apache.kafka.common.ClusterResourceListener;
import org.apache.kafka.common.TopicPartition;

/**
 * TODO
 *
 * @version $Revision: 1 $
 */
public class TNTKafkaCInterceptor implements ConsumerInterceptor<Object, Object>, ClusterResourceListener {

	private ClusterResource clusterResource;
	private Map<String, ?> configs;
	private String groupId = "";

	private InterceptionsManager iManager;

	public TNTKafkaCInterceptor() {
		iManager = InterceptionsManager.getInstance();
		iManager.bindReference(this);
	}

	@Override
	public ConsumerRecords<Object, Object> onConsume(ConsumerRecords<Object, Object> consumerRecords) {
		return iManager.consume(consumerRecords, clusterResource);
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {
		iManager.commit(map);
	}

	@Override
	public void close() {
		iManager.unbindReference(this);
	}

	@Override
	public void configure(Map<String, ?> configs) {
		this.configs = configs;
		Object groupIdValue = configs.get("group.id");
		if (groupIdValue != null && groupIdValue instanceof String) {
			this.groupId = (String) groupIdValue;
		}
	}

	@Override
	public void onUpdate(ClusterResource clusterResource) {
		this.clusterResource = clusterResource;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TNTKafkaCInterceptor{");
		sb.append("clusterResource=").append(clusterResource);
		sb.append(", configs=").append(configs);
		sb.append(", groupId='").append(groupId).append('\'');
		sb.append('}');

		return sb.toString();
	}
}