package com.ddd.polllpoll.config

import com.ddd.polllpoll.domain.PollpollDomains
import com.ddd.polllpoll.repository.PollpollRepositories
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EntityScan(basePackageClasses = [PollpollDomains::class])
@EnableJpaRepositories(basePackageClasses = [PollpollRepositories::class])
class PollpollJpaConfig {
}
