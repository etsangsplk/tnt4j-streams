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

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.ClusterResource;
import org.apache.kafka.common.ClusterResourceListener;

/**
 * TODO
 *
 * @version $Revision: 1 $
 */
public class TNTKafkaPInterceptor implements ProducerInterceptor<Object, Object>, ClusterResourceListener {

	private ClusterResource clusterResource;
	private Map<String, ?> configs;
	private InterceptionsManager iManager;

	public TNTKafkaPInterceptor() {
		iManager = InterceptionsManager.getInstance();
		iManager.bindReference(this);
	}

	@Override
	public ProducerRecord<Object, Object> onSend(ProducerRecord<Object, Object> producerRecord) {
		return iManager.send(producerRecord);
	}

	@Override
	public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
		iManager.acknowledge(recordMetadata, e, clusterResource);
	}

	@Override
	public void close() {
		iManager.unbindReference(this);
	}

	@Override
	public void configure(Map<String, ?> configs) {
		this.configs = configs;
	}

	@Override
	public void onUpdate(ClusterResource clusterResource) {
		this.clusterResource = clusterResource;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TNTKafkaPInterceptor{");
		sb.append("clusterResource=").append(clusterResource);
		sb.append(", configs=").append(configs);
		sb.append('}');

		return sb.toString();
	}
}