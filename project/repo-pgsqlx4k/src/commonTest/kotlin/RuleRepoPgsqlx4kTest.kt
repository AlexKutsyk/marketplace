package com.otus.otuskotlin.skillGrader.repo.pgsqlx4k

import com.benasher44.uuid.uuid4
import com.otus.otuskotlin.skillGrader.libs.sysenv.sysEnv
import com.otus.otuskotlin.skillGrader.project.common.models.AppRule
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleCreateTest
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleDeleteTest
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleReadTest
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleSearchTest
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleUpdateTest
import com.otus.otuskotlin.skillGrader.repo.common.RuleRepoInitialized

/**
 * Создаёт RepoRuleSql с портом из окружения.
 * Платформозависимая реализация — JVM читает System.getProperty, native — getenv.
 */
fun testRepo(
    initObjects: List<AppRule>,
    randomUuid: () -> String = { uuid4().toString() },
): RuleRepoInitialized {
    val port = sysEnv("postgresPort")?.toInt() ?: 5432
    val repo = RepoRuleSql(
        SqlProperties(
            host = "localhost",
            port = port,
            user = "postgres",
            password = "skill-grader-pass",
        ),
        randomUuid = randomUuid,
    )
    repo.clear()
    return RuleRepoInitialized(repo, initObjects)
}

class RuleRepoPgsqlx4kCreateTest : RepoRuleCreateTest() {
    override val repo = testRepo(initObjects, randomUuid = { uuidNew.asString() })
}

class RuleRepoPgsqlx4kDeleteTest : RepoRuleDeleteTest() {
    override val repo = testRepo(initObjects, randomUuid = { uuid4().toString() })
}

class RuleRepoPgsqlx4kReadTest : RepoRuleReadTest() {
    override val repo = testRepo(initObjects, randomUuid = { uuid4().toString() })
}

class RuleRepoPgsqlx4kSearchTest : RepoRuleSearchTest() {
    override val repo = testRepo(initObjects, randomUuid = { uuid4().toString() })
}

class RuleRepoPgsqlx4kUpdateTest : RepoRuleUpdateTest() {
    override val repo = testRepo(initObjects, randomUuid = { lockNew.asString() })
}