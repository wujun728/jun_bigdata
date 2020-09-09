CREATE TABLE kafka_spark_demo
(
    op_date DATE,
    op_time DATETIME,
    op_source VARCHAR(128),
    op_type VARCHAR(32),
    op_count INT,
    op_total INT
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 DEFAULT COLLATE=utf8_general_ci;