package com.hedera.mirror.importer.repository;

/*-
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2020 Hedera Hashgraph, LLC
 * ​
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ‍
 */

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

import com.hedera.mirror.importer.IntegrationTest;
import com.hedera.mirror.importer.domain.ApplicationStatusCode;

public class ApplicationStatusRepositoryTest extends IntegrationTest {

    @Resource
    private ApplicationStatusRepository applicationStatusRepository;

    @Test
    void updateStatusValue() {
        String expected = "value1";
        applicationStatusRepository.updateStatusValue(ApplicationStatusCode.LAST_PROCESSED_EVENT_HASH, expected);
        assertThat(applicationStatusRepository.findByStatusCode(ApplicationStatusCode.LAST_PROCESSED_EVENT_HASH))
                .isEqualTo(expected);

        // Check cache invalidation
        expected = "value2";
        applicationStatusRepository.updateStatusValue(ApplicationStatusCode.LAST_PROCESSED_EVENT_HASH, expected);
        assertThat(applicationStatusRepository.findByStatusCode(ApplicationStatusCode.LAST_PROCESSED_EVENT_HASH))
                .isEqualTo(expected);
    }
}
