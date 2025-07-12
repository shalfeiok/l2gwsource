CREATE TABLE `event_truly_free` (
  `object_id` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  KEY `object_id` (`object_id`),
  UNIQUE KEY `object_id_level` (`object_id`, `level`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
