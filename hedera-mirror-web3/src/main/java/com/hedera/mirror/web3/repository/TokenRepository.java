package com.hedera.mirror.web3.repository;

/*-
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2023 Hedera Hashgraph, LLC
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

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hedera.mirror.common.domain.token.Token;
import com.hedera.mirror.common.domain.token.TokenId;
import com.hedera.mirror.common.domain.token.TokenTypeEnum;

public interface TokenRepository extends CrudRepository<Token, TokenId> {

    @Query(value = "select name from token where token_id = ?1",
            nativeQuery = true)
    Optional<String> findName(final Long tokenId);

    @Query(value = "select symbol from token where token_id = ?1",
            nativeQuery = true)
    Optional<String> findSymbol(final Long tokenId);

    @Query(value = "select total_supply from token where token_id = ?1",
            nativeQuery = true)
    Optional<Long> findTotalSupply(final Long tokenId);

    @Query(value = "select decimals from token where token_id = ?1",
            nativeQuery = true)
    Optional<Integer> findDecimals(final Long tokenId);

    @Query(value = "select type from token where token_id = ?1",
            nativeQuery = true)
    Optional<TokenTypeEnum> findType(final Long tokenId);

    @Query(value = "select freeze_default from token where token_id = ?1",
            nativeQuery = true)
    boolean findFreezeDefault(final Long tokenId);
}
