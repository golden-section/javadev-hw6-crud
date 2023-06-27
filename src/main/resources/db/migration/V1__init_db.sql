CREATE TYPE IF NOT EXISTS levels AS ENUM ('Trainee', 'Junior', 'Middle', 'Senior');

CREATE TABLE IF NOT EXISTS worker (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(1000) NOT NULL,
    birthday DATE,
    level LEVELS NOT NULL,
    salary NUMERIC,
    CHECK(LENGTH(name) > 1),
    CHECK(EXTRACT(YEAR FROM birthday) > 1900),
    CHECK(salary > 100 AND salary < 100000)
);

CREATE TABLE IF NOT EXISTS client (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(1000) NOT NULL,
    CHECK(LENGTH(name) > 1)
);

CREATE TABLE IF NOT EXISTS project (
    id IDENTITY PRIMARY KEY,
    client_id BIGINT REFERENCES client(id) ON DELETE CASCADE,
    start_date DATE,
    finish_date DATE
);

CREATE TABLE IF NOT EXISTS project_worker (
    project_id BIGINT REFERENCES project(id) ON DELETE CASCADE NOT NULL,
    worker_id BIGINT REFERENCES worker(id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (project_id, worker_id)
);