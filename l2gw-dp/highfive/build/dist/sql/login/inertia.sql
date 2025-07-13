CREATE TABLE IF NOT EXISTS `character_inertia_credit` (
  `owner_id` INT NOT NULL,
  `credits` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;