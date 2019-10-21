

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for properties
-- ----------------------------
CREATE TABLE IF NOT EXISTS `property` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据库自增id',
  `application` varchar(255) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `property` longtext,
  `version` int(255) DEFAULT NULL,
  `active` tinyint(4) DEFAULT NULL,
  `created_datetime` DATETIME NULL COMMENT '创建时间',
  `modified_datetime` DATETIME NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
