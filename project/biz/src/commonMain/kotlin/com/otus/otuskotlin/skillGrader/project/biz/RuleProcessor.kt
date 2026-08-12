package com.otus.otuskotlin.skillGrader.project.biz

import com.otus.otuskotlin.skillGrader.project.biz.general.initStatus
import com.otus.otuskotlin.skillGrader.project.biz.general.operation
import com.otus.otuskotlin.skillGrader.project.biz.repo.checkLock
import com.otus.otuskotlin.skillGrader.project.biz.repo.initRepo
import com.otus.otuskotlin.skillGrader.project.biz.repo.prepareResult
import com.otus.otuskotlin.skillGrader.project.biz.repo.repo
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoCreate
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoDelete
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoPrepareCreate
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoDoneRead
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoPrepareDelete
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoPrepareUpdate
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoRead
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoSearch
import com.otus.otuskotlin.skillGrader.project.biz.repo.repoUpdate
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubs
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubCreateSuccess
import com.otus.otuskotlin.skillGrader.project.common.AppContext
import com.otus.otuskotlin.skillGrader.project.common.AppCorSettings
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubDbError
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubDeleteSuccess
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubNoCase
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubReadSuccess
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubSearchSuccess
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubUpdateSuccess
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubValidationBadMinGrammarPercent
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubValidationBadId
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubValidationBadMinLexiconsPercent
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubValidationBadMinListeningPercent
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubValidationBadName
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubValidationBadPriority
import com.otus.otuskotlin.skillGrader.project.biz.stubs.stubValidationBadTimeWindowDays
import com.otus.otuskotlin.skillGrader.project.biz.validation.finishRuleFilterValidation
import com.otus.otuskotlin.skillGrader.project.biz.validation.finishRuleValidation
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateIdNotEmpty
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateIdProperFormat
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateLockNotEmpty
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateLockProperFormat
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateNameNotEmpty
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateSearchStringLength
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateNameHasContent
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateRangeMinGrammarPercent
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateRangeMinLexiconsPercent
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateRangeMinListeningPercent
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateRangePriority
import com.otus.otuskotlin.skillGrader.project.biz.validation.validateRangeTimeWindowDays
import com.otus.otuskotlin.skillGrader.project.biz.validation.validation
import com.otus.otuskotlin.skillGrader.project.common.models.AppCommand
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleId
import com.otus.otuskotlin.skillGrader.project.common.models.AppRuleLock
import rootChain
import worker

class RuleProcessor(val corSettings: AppCorSettings = AppCorSettings.NONE) {

    suspend fun execute(context: AppContext) {
        businessChain.exec(context.also { it.corSettings = corSettings })
    }

    private val businessChain = rootChain<AppContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        operation("Создание объявления", AppCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadName("Имитация ошибки валидации заголовка")
                stubValidationBadMinGrammarPercent("Имитация ошибки валидации параметра minGrammarPercent")
                stubValidationBadMinLexiconsPercent("Имитация ошибки валидации параметра minLexiconsPercent")
                stubValidationBadMinListeningPercent("Имитация ошибки валидации параметра minListeningPercent")
                stubValidationBadTimeWindowDays("Имитация ошибки валидации параметра timeWindowDays")
                stubValidationBadPriority("Имитация ошибки валидации параметра priority")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ruleValidating") { ruleValidating = ruleRequest.deepCopy() }
                worker("Очистка id") { ruleValidating.id = AppRuleId.NONE }
                worker("Очистка названия") { ruleValidating.name = ruleValidating.name.trim() }
                validateNameNotEmpty("Проверка, что название не пусто")
                validateNameHasContent("Проверка символов")
                validateRangeMinGrammarPercent("Проверка диапазона minGrammarPercent")
                validateRangeMinLexiconsPercent("Проверка диапазона minLexiconsPercent")
                validateRangeMinListeningPercent("Проверка диапазона minListeningPercent")
                validateRangeTimeWindowDays("Проверка диапазона timeWindowDays")
                validateRangePriority("Проверка диапазона priority")

                finishRuleValidation("Завершение проверок")
            }
            repo("Логика сохранения") {
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание объявления в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получить объявление", AppCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ruleValidating") { ruleValidating = ruleRequest.deepCopy() }
                worker("Очистка id") { ruleValidating.id = AppRuleId(ruleValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishRuleValidation("Успешное завершение процедуры валидации")
            }
            repo("Логика чтения") {
                repoRead("Чтение правила из БД")
                repoDoneRead("Подготовка ответа для Read")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Изменить объявление", AppCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadName("Имитация ошибки валидации заголовка")
                stubValidationBadMinGrammarPercent("Имитация ошибки валидации параметра minGrammarPercent")
                stubValidationBadMinLexiconsPercent("Имитация ошибки валидации параметра minLexiconsPercent")
                stubValidationBadMinListeningPercent("Имитация ошибки валидации параметра minListeningPercent")
                stubValidationBadTimeWindowDays("Имитация ошибки валидации параметра timeWindowDays")
                stubValidationBadPriority("Имитация ошибки валидации параметра priority")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ruleValidating") { ruleValidating = ruleRequest.deepCopy() }
                worker("Очистка id") { ruleValidating.id = AppRuleId(ruleValidating.id.asString().trim()) }
                worker("Очистка lock") { ruleValidating.lock = AppRuleLock(ruleValidating.lock.asString().trim()) }
                worker("Очистка заголовка") { ruleValidating.name = ruleValidating.name.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateNameNotEmpty("Проверка на непустой заголовок")
                validateNameHasContent("Проверка на наличие содержания в заголовке")
                validateRangeMinGrammarPercent("Проверка диапазона minGrammarPercent")
                validateRangeMinLexiconsPercent("Проверка диапазона minLexiconsPercent")
                validateRangeMinListeningPercent("Проверка диапазона minListeningPercent")
                validateRangeTimeWindowDays("Проверка диапазона timeWindowDays")
                validateRangePriority("Проверка диапазона priority")

                finishRuleValidation("Успешное завершение процедуры валидации")
            }
            repo("Логика сохранения") {
                repoRead("Чтение объявления из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление объявления в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Удалить правило", AppCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в ruleValidating") { ruleValidating = ruleRequest.deepCopy() }
                worker("Очистка id") { ruleValidating.id = AppRuleId(ruleValidating.id.asString().trim()) }
                worker("Очистка lock") { ruleValidating.lock = AppRuleLock(ruleValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                finishRuleValidation("Успешное завершение процедуры валидации")
            }
            repo("Логика удаления") {
                repoRead("Чтение объявления из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление объявления из БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Поиск правил", AppCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в adFilterValidating") { ruleFilterValidating = ruleFilterRequest.deepCopy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishRuleFilterValidation("Успешное завершение процедуры валидации")
            }
            repoSearch("Поиск правила в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}