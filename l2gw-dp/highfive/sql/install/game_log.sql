DROP TABLE IF EXISTS `game_log`;
CREATE TABLE `game_log` (
serv_id tinyint unsigned NOT NULL default '0',
act_time int unsigned NOT NULL default '0',
log_id SMALLINT unsigned NOT NULL default '0',
actor int unsigned NOT NULL default '0',
actor_type varchar(75) NOT NULL default '',
target int unsigned NOT NULL default '0',
target_type varchar(75) NOT NULL default '0',
location_x MEDIUMINT,
location_y MEDIUMINT,
location_z SMALLINT,
etc_str1 varchar(50),
etc_str2 varchar(50),
etc_str3 varchar(50),
etc_num1 int NOT NULL default '0',
etc_num2 int NOT NULL default '0',
etc_num3 int NOT NULL default '0',
etc_num4 int NOT NULL default '0',
etc_num5 int NOT NULL default '0',
etc_num6 int NOT NULL default '0',
etc_num7 int NOT NULL default '0',
etc_num8 int NOT NULL default '0',
etc_num9 int NOT NULL default '0',
etc_num10 int NOT NULL default '0',
STR_actor varchar(50),
STR_actor_account varchar(50),
STR_target varchar(50),
STR_target_account varchar(50),
item_id int unsigned
) ENGINE=MyISAM;