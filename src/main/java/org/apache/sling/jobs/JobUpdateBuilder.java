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
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import java.util.Map;
/**
 * Create a job update ading properties and building.
 */
@ProviderType
public interface JobUpdateBuilder {

    @NotNull
    JobUpdateBuilder command(@NotNull JobUpdate.JobUpdateCommand command);

    @NotNull
    JobUpdate build();

    /**
     * Set a property to update.
     * @param name the name of the property
     * @param value the value of the property which may be null. To remove the property set the value to JobUpdate.JobPropertyAction.REMOVE.
     * @return this JobBuilder instance.
     */
    @NotNull
    JobUpdateBuilder put(@NotNull String name, @Nullable Object value);

    @NotNull
    JobUpdateBuilder putAll(@NotNull Map<String, Object> properties);
}
