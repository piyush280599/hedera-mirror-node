package com.hedera.mirror.importer.exception;

/*-
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2022 Hedera Hashgraph, LLC
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

@SuppressWarnings("java:S110")
public class StreamFileReaderException extends ImporterException {

    private static final long serialVersionUID = 2533328395713171797L;

    public StreamFileReaderException(Throwable throwable) {
        super(throwable);
    }

    public StreamFileReaderException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
