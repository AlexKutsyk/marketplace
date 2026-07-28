--liquibase formatted SQL

--changeset aKutsyk labels:v0.0.1
CREATE TYPE "rule_grade_type" AS ENUM ('A1', 'A2', 'B1', 'B2', 'C1', 'C2');

CREATE TABLE "rules" (
	"id" text primary key constraint rules_id_length_ctr check (length(id) < 64),
	"name" text constraint rules_name_length_ctr check (length(name) < 128),
    "grade" rule_grade_type not null,
	"min_grammar_percent" smallint constraint rules_min_grammar_percent_max_ctr check (min_grammar_percent >= 0 and min_grammar_percent <= 100),
	"min_lexicons_percent" smallint constraint rules_min_lexicons_percent_max_ctr check (min_lexicons_percent >= 0 and min_lexicons_percent <= 100),
	"min_listening_percent" smallint constraint rules_min_listening_percent_max_ctr check (min_listening_percent >= 0 and min_listening_percent <= 100),
	"time_window_days" smallint constraint rules_time_window_days_max_ctr check (time_window_days >= 0 and time_window_days <= 365),
	"priority" smallint constraint rules_priority_max_ctr check (priority >= 0 and priority <= 100),
	"lock" text not null constraint rules_lock_length_ctr check (length(lock) < 64)
);

CREATE INDEX rules_rule_grade_idx on "rules" ("grade");

--rollback DROP INDEX IF EXISTS rules_rule_grade_idx;
--rollback DROP TABLE IF EXISTS "rules";
--rollback DROP TYPE IF EXISTS "rule_grade_type";