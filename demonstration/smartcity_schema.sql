CREATE TABLE "provider" (
    "provider_id" VARCHAR(6) NOT NULL,
    "provider_name" VARCHAR(10) NOT NULL
);

ALTER TABLE "provider" ADD PRIMARY KEY("provider_id");

CREATE TABLE "source"(
    "source_id" VARCHAR(11) NOT NULL
);

ALTER TABLE "source" ADD PRIMARY KEY("source_id");

CREATE TABLE "subject"(
    "subject_id" UUID NOT NULL,
    "provider_id" VARCHAR(6) NOT NULL,
    "source_id" VARCHAR(11) NOT NULL,
    "subject_added" TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

ALTER TABLE "subject" ADD PRIMARY KEY("subject_id");

CREATE TABLE "reading" (
    "reading_id" UUID NOT NULL,
    "subject_id" UUID NOT NULL,
    "reading_kwh" DECIMAL(8,2) NOT NULL,
    "reading_created" TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

ALTER TABLE "reading" ADD PRIMARY KEY("reading_id");

ALTER TABLE "subject" ADD CONSTRAINT "subject_provider_id_foreign" FOREIGN KEY("provider_id") REFERENCES "provider"("provider_id");
ALTER TABLE "subject" ADD CONSTRAINT "subject_source_id_foreign" FOREIGN KEY("source_id") REFERENCES "source"("source_id");

ALTER TABLE "reading" ADD CONSTRAINT "reading_subject_id_foreign" FOREIGN KEY("subject_id") REFERENCES "subject"("subject_id");