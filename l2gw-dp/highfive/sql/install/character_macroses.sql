DROP TABLE IF EXISTS `character_macroses`;
CREATE TABLE `character_macroses` (
`char_obj_id` INT NOT NULL DEFAULT '0',
`id` INT NOT NULL DEFAULT '0',
`icon` INT,
`name` VARCHAR(40) ,
`descr` VARCHAR(80) ,
`acronym` VARCHAR(4) ,
`commands` VARCHAR(255) ,
PRIMARY KEY(`char_obj_id`,`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;