DROP TABLE IF EXISTS `character_rbp`;
CREATE TABLE `character_rbp` (
`obj_id` int(11) unsigned NOT NULL,
`boss_id` int(11) unsigned NOT NULL,
`points` int(11) NOT NULL default '0',
PRIMARY KEY `pkey` (`obj_id`, `boss_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

