DROP TABLE IF EXISTS `olymp_nobles`;
CREATE TABLE `olymp_nobles` (
  `char_id` int NOT NULL default 0,
  `class_id` int NOT NULL default 0,
  `char_name` varchar(45) NOT NULL default '',
  `points` int NOT NULL default 0,
  `prev_points` int NOT NULL default 0,
  `wins` int NOT NULL default 0,
  `loos` int NOT NULL default 0,
  `cb_matches` int NOT NULL default 0,
  `ncb_matches` int NOT NULL default 0,
  `team_matches` int NOT NULL default 0,
  PRIMARY KEY  (`char_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
