/*
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
package org.apache.sling.jobs;


import org.jetbrains.annotations.NotNull;
import org.osgi.annotation.versioning.ProviderType;

import java.util.Map;
/**
 * A JobBuilder allows users of the JobSystem to modify the properties of the Job and submit it for processing.
 */
@ProviderType
public interface JobBuilder {

    /**
     * Set the optional configuration properties for the job.
     * @param props The properties of the job. All values must be {@code java.io.Serializable}.
     * @return The job builder to continue building.
     */
    @NotNull
    JobBuilder addProperties(@NotNull Map<String, Object> props);

    /**
     * Add the job for processing.
     * @return The job or <code>null</code>
     */
    @NotNull
    Job add();

}
