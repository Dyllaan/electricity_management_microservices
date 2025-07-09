CREATE TABLE "reading"(
    "reading_id" UUID NOT NULL DEFAULT gen_random_uuid(),
    "subject_id" UUID NOT NULL,
    "reading_kwh" DECIMAL(8, 2) NOT NULL,
    "reading_added" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE
    "reading" ADD PRIMARY KEY("reading_id");
CREATE TABLE "subject"(
    "subject_id" UUID NOT NULL DEFAULT gen_random_uuid(),
    "source_id" VARCHAR(11) NOT NULL,
    "subject_added" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "customer_id" bigserial NOT NULL
);
ALTER TABLE
    "subject" ADD PRIMARY KEY("subject_id");
CREATE TABLE "source"(
    "source_id" VARCHAR(11) NOT NULL
);
ALTER TABLE
    "source" ADD PRIMARY KEY("source_id");
CREATE TABLE "customer"(
    "customer_id" bigserial NOT NULL,
    "customer_first_name" VARCHAR(30) NOT NULL,
    "customer_last_name" VARCHAR(30) NOT NULL,
    "customer_added" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE
    "customer" ADD PRIMARY KEY("customer_id");
ALTER TABLE
    "reading" ADD CONSTRAINT "reading_subject_id_foreign" FOREIGN KEY("subject_id") REFERENCES "subject"("subject_id");
ALTER TABLE
    "subject" ADD CONSTRAINT "subject_source_id_foreign" FOREIGN KEY("source_id") REFERENCES "source"("source_id");
ALTER TABLE
    "subject" ADD CONSTRAINT "subject_customer_id_foreign" FOREIGN KEY("customer_id") REFERENCES "customer"("customer_id");