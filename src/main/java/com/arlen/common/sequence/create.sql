DROP TABLE IF EXISTS `base_sequence`;
CREATE TABLE `base_sequence` (
  `seq_key` tinyint(4) NOT NULL DEFAULT '0' COMMENT '业务类型，0：product；1：line；2：user',
  `start_id` int(11) NOT NULL DEFAULT '1000000' COMMENT '起始id',
  `step_by` int(11) NOT NULL DEFAULT '10' COMMENT '步长',
  PRIMARY KEY (`seq_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='全局sequence生成表';