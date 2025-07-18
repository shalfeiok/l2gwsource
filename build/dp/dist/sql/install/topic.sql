DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
`topic_id` int(8) NOT NULL default '0',
`topic_forum_id` int(8) NOT NULL default '0',
`topic_name` varchar(255) character set cp1251 NOT NULL,
`topic_date` decimal(20,0) NOT NULL default '0',
`topic_ownername` varchar(255) NOT NULL default '0',
`topic_ownerid` int(8) NOT NULL default '0',
`topic_type` int(8) NOT NULL default '0',
`topic_reply` int(8) NOT NULL default '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
