import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleCreateTest
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleDeleteTest
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleReadTest
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleSearchTest
import com.otus.otuskotlin.skillGrader.project.repo.tests.RepoRuleUpdateTest
import com.otus.otuskotlin.skillGrader.repo.common.RuleRepoInitialized
import com.otus.otuskotlin.skillGrader.repo.inmemory.RuleRepoInMemory

class RuleRepoInMemoryCreateTest : RepoRuleCreateTest() {
    override val repo = RuleRepoInitialized(
        RuleRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class RuleRepoInMemoryDeleteTest : RepoRuleDeleteTest() {
    override val repo = RuleRepoInitialized(
        RuleRepoInMemory(),
        initObjects = initObjects,
    )
}

class RuleRepoInMemoryReadTest : RepoRuleReadTest() {
    override val repo = RuleRepoInitialized(
        RuleRepoInMemory(),
        initObjects = initObjects,
    )
}

class RuleRepoInMemorySearchTest : RepoRuleSearchTest() {
    override val repo = RuleRepoInitialized(
        RuleRepoInMemory(),
        initObjects = initObjects,
    )
}

class RuleRepoInMemoryUpdateTest : RepoRuleUpdateTest() {
    override val repo = RuleRepoInitialized(
        RuleRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}